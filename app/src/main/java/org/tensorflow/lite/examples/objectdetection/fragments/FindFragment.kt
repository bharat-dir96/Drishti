package org.tensorflow.lite.examples.objectdetection.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import org.tensorflow.lite.examples.objectdetection.R
import org.tensorflow.lite.examples.objectdetection.ObjectDetectorHelper
import java.io.InputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FindFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = "FindFragment"

    private lateinit var objectDetectorHelper: ObjectDetectorHelper

    // Define a variable to hold electronicItemLabels
    private var electronicItemLabels: Set<String> = emptySet()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_find, container, false)

        // Attach click listeners to your ImageButtons
        val personLayout = view.findViewById<LinearLayout>(R.id.personLayout)
        val btnChair = view.findViewById<ImageButton>(R.id.btnChair)
        val btnBottle = view.findViewById<ImageButton>(R.id.btnBottle)
        val btnDesktop = view.findViewById<ImageButton>(R.id.btnDesktop)
        val btnTable = view.findViewById<ImageButton>(R.id.btnTable)

        personLayout.setOnClickListener {
            val txtPerson = personLayout.findViewById<TextView>(R.id.txtPerson)

            txtPerson.setTextColor(Color.parseColor("#FF0000"))
        }
        btnChair.setOnClickListener {

        }
        btnBottle.setOnClickListener {

        }
        btnDesktop.setOnClickListener {

        }
        btnTable.setOnClickListener {

        }

        return view

    }

    // Define a function to load a Bitmap from a resource ID
    private fun loadBitmapFromResource(resourceId: Int): Bitmap {
        val inputStream: InputStream = resources.openRawResource(resourceId)
        return BitmapFactory.decodeStream(inputStream)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}