package com.catolicapps.smartrosario

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat

class RosarioService : Service() {

    private lateinit var vibrator: Vibrator
    private var pantallaApagada = false
    private var encenderPantalla = false
    private var misterioActual = 0
    private var cuentaActual = 1

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        pantallaApagada = intent?.getBooleanExtra("pantallaApagada", false) ?: false
        encenderPantalla = intent?.getBooleanExtra("encenderEntreMisterios", false) ?: false

        startForeground(1, createNotification())

        // Aquí puedes usar MediaSession o InputManager para detectar botones físicos

        return START_STICKY
    }

    private fun avanzarRosario() {
        cuentaActual++
        if (cuentaActual > 10) {
            cuentaActual = 1
            misterioActual++
            vibrarLargo()

            if (pantallaApagada && encenderPantalla) {
                encenderPantallaTemporal()
            }
        } else {
            vibrarCorto()
        }
    }

    private fun retrocederRosario() {
        cuentaActual = maxOf(1, cuentaActual - 1)
        vibrarCorto()
    }

    private fun vibrarCorto() {
        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun vibrarLargo() {
        vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun encenderPantallaTemporal() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "rosario:WakeLock"
        )
        wakeLock.acquire(3000) // Enciende pantalla por 3 segundos
    }

    private fun createNotification(): Notification {
        val channelId = "rosario_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Rosario en segundo plano", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Rezando el Rosario")
            .setContentText("Usa los botones de volumen para avanzar.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
