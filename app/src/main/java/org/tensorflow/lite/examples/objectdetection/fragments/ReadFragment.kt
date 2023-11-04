package org.tensorflow.lite.examples.objectdetection.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.tensorflow.lite.examples.objectdetection.R
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReadFragment : Fragment(), OnInitListener{

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var textToSpeech: TextToSpeech
    private var isTTSInitialized = false

    private lateinit var textViewFinder: PreviewView


    private val imageAnalyzer: ImageAnalysis.Analyzer = TextAnalysis()
    private var imageAnalysis = ImageAnalysis.Builder()
        .setImageQueueDepth(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_read, container, false)

        val btnTextR = view.findViewById<ImageButton>(R.id.btnTextR)

        textViewFinder = view.findViewById(R.id.textViewFinder)

        btnTextR.setOnClickListener {
            startRecognising()
        }

        askCameraPermission()
        startCamera()

        cameraExecutor = Executors.newSingleThreadExecutor()

        return view

    }

    companion object {
        private const val TAG = "CameraXBasic"
        const val CAMERA_PERM_CODE = 422
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERM_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Error")
                    .setMessage("Camera Permission not provided")
                    .setPositiveButton("OK") { _, _ ->  }
                    .setCancelable(false)
                    .show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(textViewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US) // Set the desired language (e.g., US English)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "TextToSpeech initialization failed")
            } else {
                isTTSInitialized = true
            }
        } else {
            Log.e(TAG, "TextToSpeech initialization failed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
        cameraExecutor.shutdown()
    }


    private fun startRecognising() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(requireContext()),
            imageAnalyzer
        )

        if (isTTSInitialized) {
            val textToSpeak = textDone.toString()
            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            // Handle TTS not initialized
            Log.e(TAG, "TextToSpeech is not initialized.")
        }
    }

}


