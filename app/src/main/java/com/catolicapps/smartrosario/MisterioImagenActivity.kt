package com.catolicapps.smartrosario

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent

class MisterioImagenActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_misterio_imagen)

        val imageResId = intent.getIntExtra("imagenResId", R.drawable.placeholder)  // Imagen de respaldo
        val textoObra = intent.getStringExtra("textoObra") ?: "Sin título"

        val imageView = findViewById<ImageView>(R.id.imageMisterio)
        val textoObraView = findViewById<TextView>(R.id.textoObra)

        if (imageResId != -1) {
            imageView.setImageResource(imageResId)
        }

        textoObraView.text = textoObra

        // Detector de gestos
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                finish() // o lo que quieras hacer
                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}
