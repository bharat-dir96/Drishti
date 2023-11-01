

package org.tensorflow.lite.examples.objectdetection

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.objectdetection.databinding.ActivityMainBinding
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.tensorflow.lite.examples.objectdetection.fragments.MyAdapter

/**
 * Main entry point into our app. This app follows the single-activity pattern, and all
 * functionality is implemented in the form of fragments.
 */

class MainActivity : AppCompatActivity() {

    var tabTitle = arrayOf("Explore", "Read", "Find")

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        var pager = findViewById<ViewPager2>(R.id.viewPager2)
        var tl = findViewById<TabLayout>(R.id.tabLayout)
        pager.adapter = MyAdapter(supportFragmentManager, lifecycle)

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> window.decorView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.Tab1Background))
                    1 -> window.decorView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.Tab2Background))
                    2 -> window.decorView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.Tab3Background))
                }
            }
        })

        TabLayoutMediator(tl, pager) {
            tab, position ->
                tab.text = tabTitle[position]
        }.attach()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}