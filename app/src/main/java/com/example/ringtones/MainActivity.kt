package com.example.ringtones

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val splashHandler = Handler(Looper.getMainLooper())
    private var splashRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTitulo = findViewById<TextView>(R.id.tvTituloRingtones)
        iniciarAnimacionPulso(tvTitulo)

        // Espera 3.5 segundos (3500 ms) y pasa a MainActivity2
        splashRunnable = Runnable {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }
        splashHandler.postDelayed(splashRunnable!!, 3500L)
    }

    private fun iniciarAnimacionPulso(vista: View) {
        val scaleX = ObjectAnimator.ofFloat(vista, View.SCALE_X, 1f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(vista, View.SCALE_Y, 1f, 1.2f, 1f)

        scaleX.repeatCount = 2
        scaleY.repeatCount = 2
        scaleX.duration = 1100L
        scaleY.duration = 1100L
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashRunnable?.let { splashHandler.removeCallbacks(it) }
    }
}