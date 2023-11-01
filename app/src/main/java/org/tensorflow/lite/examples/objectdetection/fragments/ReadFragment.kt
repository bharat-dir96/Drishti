package org.tensorflow.lite.examples.objectdetection.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import org.tensorflow.lite.examples.objectdetection.databinding.FragmentReadBinding
import java.util.Locale


class ReadFragment : Fragment(), OnInitListener {

    // update 5 initializing variables
    private lateinit var binding: FragmentReadBinding
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {         // update 6 - (binding the data according to buttons pressed by user.)
            captureImage.setOnClickListener {
                takeImage()
                textView.text = ""
            }
            detectTextImageBtn.setOnClickListener {
                processImage()
            }
        }

        // Initialize the TextToSpeech engine
        textToSpeech = TextToSpeech(requireContext(), this)
    }

    // update 7 - (Defining functions for takeImage() and onActivityResult() )
    private fun takeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (_: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val extras: Bundle? = data?.extras

            imageBitmap = extras?.get("data") as Bitmap

            if (imageBitmap != null) {
                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
    }


    // update 7 - (Defining functions for processImage() )
    private fun processImage() {
        if (imageBitmap != null) {
            val image = imageBitmap?.let {
                InputImage.fromBitmap(it, 0)
            }

            image?.let {
                recognizer.process(it)
                    .addOnSuccessListener { visionText ->

                        binding.textView.text = visionText.text
                        val textToRead = visionText.text
                        textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Text recognition failed.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        else {
            Toast.makeText(requireContext(), "Please select a photo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language for text-to-speech (you can change this to your preferred language)
            val locale = Locale.getDefault()
            val result = textToSpeech.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle the case where the language is not available or supported
                Toast.makeText(requireContext(), "Text-to-speech is not supported in this language.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where text-to-speech initialization failed
            Toast.makeText(requireContext(), "Text-to-speech initialization failed.", Toast.LENGTH_SHORT).show()
        }
    }
}