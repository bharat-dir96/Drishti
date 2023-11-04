
package org.tensorflow.lite.examples.objectdetection

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.gpu.support.TfLiteGpu
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.detector.Detection
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector
import android.speech.tts.TextToSpeech
import java.util.Locale
import android.os.Vibrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking





class ObjectDetectorHelper(
    var threshold: Float = 0.6f,  // Threshold for filtering detection results
    var numThreads: Int = 1,       // Number of threads for detection
    var maxResults: Int = 1,       // Maximum number of detection results
    var currentDelegate: Int = 0,  // Current hardware delegate (CPU, GPU, NNAPI)
    var currentModel: Int = 0,    // Current object detection model
    val context: Context,          // Android application context

    val objectDetectorListener: DetectorListener,

    private var results: MutableList<Detection>? = null,

    val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

) {

    private val TAG = "ObjectDetectionHelper"


    // For this example this needs to be a var so it can be reset on changes. If the ObjectDetector
    // will not change, a lazy val would be preferable.
    private var objectDetector: ObjectDetector? = null
    private var gpuSupported = false

    //    private val handler = Handler()
    private var tts: TextToSpeech? = null


    init {
        // Initialization block
        speakText("इस ऐप में आपका स्वागत है। अब मैं स्कैनिंग शुरू करूंगी")
        // Check if GPU delegate is available and initialize TensorFlow Lite
        TfLiteGpu.isGpuDelegateAvailable(context).onSuccessTask { gpuAvailable: Boolean ->
            val optionsBuilder =
                TfLiteInitializationOptions.builder()
            if (gpuAvailable) {
                optionsBuilder.setEnableGpuDelegateSupport(true)
            }
            TfLiteVision.initialize(context, optionsBuilder.build())
        }.addOnSuccessListener {
            // Callback when initialization is successful
            objectDetectorListener.onInitialized()


        }.addOnFailureListener {
            // Callback when an error occurs during initialization
            objectDetectorListener.onError(
                "TfLiteVision failed to initialize: "
                        + it.message
            )
        }
    }

    init {
        // Initialize Text-to-Speech
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
//                val result = tts?.setLanguage(Locale.ENGLISH)
                val result = tts?.setLanguage(Locale("hi", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    objectDetectorListener.onError("Language not supported or missing data for TTS")
                }
            } else {
                objectDetectorListener.onError("TTS initialization failed")
            }
        })
    }

    fun clearObjectDetector() {
        // Method to clear the object detector instance

        objectDetector = null
    }

    // Initialize the object detector using current settings on the
    // thread that is using it. CPU and NNAPI delegates can be used with detectors
    // that are created on the main thread and used on a background thread, but
    // the GPU delegate needs to be used on the thread that initialized the detector
    fun setupObjectDetector() {
        if (!TfLiteVision.isInitialized()) {
            Log.e(TAG, "setupObjectDetector: TfLiteVision is not initialized yet")
            return
        }

        // Create the base options for the detector using specifies max results and score threshold
        val optionsBuilder =
            ObjectDetector.ObjectDetectorOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)

        // Set general detection options, including number of used threads
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

        // Use the specified hardware for running the model. Default to CPU
        when (currentDelegate) {
            DELEGATE_CPU -> {
                // Default
            }

            DELEGATE_GPU -> {
                if (gpuSupported) {
                    baseOptionsBuilder.useGpu()
                } else {
                    objectDetectorListener.onError("GPU is not supported on this device")
                }
            }

            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())
        // Determine the model to be used based on the currentModel value

        val modelName =
            when (currentModel) {
                MODEL_MOBILENETV1 -> "mobilenetv1.tflite"
                MODEL_EFFICIENTDETV0 -> "efficientdet-lite0.tflite"
                MODEL_EFFICIENTDETV1 -> "efficientdet-lite1.tflite"
                MODEL_EFFICIENTDETV2 -> "efficientdet-lite2.tflite"
                else -> "efficientdet-lite2.tflite"
            }

        try {
            // Create the object detector from the specified model and options
            objectDetector =
                ObjectDetector.createFromFileAndOptions(context, modelName, optionsBuilder.build())

        } catch (e: Exception) {
            objectDetectorListener.onError(
                "Object detector failed to initialize. See error logs for details"
            )
            Log.e(TAG, "TFLite failed to load model with error: " + e.message)
        }
    }

//    In this code, we use a coroutine to create a structured flow for detection.
//    After speaking and vibrating for each detection, we introduce a delay using delay() to control the pace of the process.
//    This ensures that each object is identified, and the app is ready for the next frame after speaking is done.


    fun detect(image: Bitmap, imageRotation: Int) {
        // Initialize a coroutine scope for this detection
        runBlocking {


            launch(Dispatchers.Default) {

                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if (!TfLiteVision.isInitialized()) {
                    Log.e(TAG, "detect: TfLiteVision is not initialized yet")
                    return@launch
                }

                if (objectDetector == null) {
                    setupObjectDetector()
                }

                // Inference time is the difference between the system time at the start and finish of the process
                var inferenceTime = SystemClock.uptimeMillis()

                val imageProcessor =
                    ImageProcessor.Builder().add(Rot90Op(-imageRotation / 90)).build()
                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

                val results = objectDetector?.detect(tensorImage)


                for (detection in results!!) {
                    for (category in detection.categories) {
                        val categoryLabel = category.label
                        val displayMessage = "Detected: $categoryLabel "

                        // Main Function Should Implement here for enabling speak for the specific objects.


                        // Speak the category label
                        speakText("यहाँ  एक  " + categoryLabel + " है.")


                        // Vibrate the phone for 500 milliseconds (0.5 seconds) after speaking
                        val vibrationDuration = 50
                        vibrator.vibrate(vibrationDuration.toLong())

                        // Delay for a few seconds before processing the next detection
                        delay(2000)
                    }
                }

                // Calculate the inference time
                inferenceTime = SystemClock.uptimeMillis() - inferenceTime

                // Callback with the detection results, inference time, and image dimensions
                objectDetectorListener.onResults(
                    results,
                    inferenceTime,
                    tensorImage.height,
                    tensorImage.width
                )
            }
        }
    }

    fun detect2(image: Bitmap, imageRotation: Int, labelsToDetect: Set<String>) {
        // Initialize a coroutine scope for this detection
        runBlocking {


            launch(Dispatchers.Default) {

                if (!TfLiteVision.isInitialized()) {
                    Log.e(TAG, "detect: TfLiteVision is not initialized yet")
                    return@launch
                }

                if (objectDetector == null) {
                    setupObjectDetector()
                }

                // Inference time is the difference between the system time at the start and finish of the process
                var inferenceTime = SystemClock.uptimeMillis()

                val imageProcessor =
                    ImageProcessor.Builder().add(Rot90Op(-imageRotation / 90)).build()
                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

                val results = objectDetector?.detect(tensorImage)

                for (detection in results!!) {
                    for (category in detection.categories) {
                        val categoryLabel = category.label
                        val displayMessage = "Detected: $categoryLabel "
                        if (labelsToDetect.contains(categoryLabel)) {
                            // Speak the category label
                            speakText("यहाँ  एक  $categoryLabel है.")
                        }

                        // Vibrate the phone for 500 milliseconds (0.5 seconds) after speaking
                        val vibrationDuration = 50
                        vibrator.vibrate(vibrationDuration.toLong())

                        // Delay for a few seconds before processing the next detection.
                        delay(2000)
                    }
                }


                // Calculate the inference time
                inferenceTime = SystemClock.uptimeMillis() - inferenceTime

                // Callback with the detection results, inference time, and image dimensions
                objectDetectorListener.onResults(
                    results,
                    inferenceTime,
                    tensorImage.height,
                    tensorImage.width
                )
            }
        }
    }


//    fun detect(image: Bitmap, imageRotation: Int) {
//        // Obtain a reference to the Vibrator service
//        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//
//
//
//
//        if (!TfLiteVision.isInitialized()) {
//            Log.e(TAG, "detect: TfLiteVision is not initialized yet")
//            return
//        }
//
//        if (objectDetector == null) {
//            setupObjectDetector()
//        }
//
//        // Inference time is the difference between the system time at the start and finish of the
//        // process
//        var inferenceTime = SystemClock.uptimeMillis()
//
//        // Create preprocessor for the image.
//        // See https://www.tensorflow.org/lite/inference_with_metadata/
//        //            lite_support#imageprocessor_architecture
//        val imageProcessor = ImageProcessor.Builder().add(Rot90Op(-imageRotation / 90)).build()
//
//        // Preprocess the image and convert it into a TensorImage for detection.
//        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))
//
//        // Perform object detection using the object detector
//
//        val results = objectDetector?.detect(tensorImage)
//
////        val category = results
////        // Iterate through the list
//
//        for (detection in results!!) {
//            // Iterate through the categories of each detection
//            for (category in detection.categories) {
//                // Display the category
//                val categoryLabel = category.label
//                val displayMessage = "Detected: $categoryLabel "
////                Log.d("ObjectDetection", displayMessage)
//
//
//                // Speak the category label
//                speakText("यहाँ  एक  "+categoryLabel+" है.")
//
//                // Vibrate the phone for 500 milliseconds (0.5 seconds) after speaking
//                val vibrationDuration = 50
//                vibrator.vibrate(vibrationDuration.toLong())
//                Thread.sleep(2000)
//
//            }
//        }
//
//        // Calculate the inference time
//        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
//
//        // Callback with the detection results, inference time, and image dimensions
//        objectDetectorListener.onResults(
//          results,
//          inferenceTime,
//          tensorImage.height,
//          tensorImage.width)
//    }



    fun speakText(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
//        Log.d("speech", "I am speaking")
    }

    interface DetectorListener {
        fun onInitialized()  // Callback when the object detector is initialized
        fun onError(error: String)  // Callback when an error occurs
        fun onResults(
          results: MutableList<Detection>?,
          inferenceTime: Long,
          imageHeight: Int,
          imageWidth: Int
        ) // Callback when detection results are available
    }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
        const val MODEL_MOBILENETV1 = 0
        const val MODEL_EFFICIENTDETV0 = 1
        const val MODEL_EFFICIENTDETV1 = 2
        const val MODEL_EFFICIENTDETV2 = 3
    }
}
