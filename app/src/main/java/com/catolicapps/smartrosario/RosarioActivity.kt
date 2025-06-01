    package com.catolicapps.smartrosario

    import android.os.Bundle
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Vibrator
    import android.widget.Button
    import android.content.Context
    import android.view.KeyEvent
    import android.os.Build
    import android.os.VibrationEffect
    import android.content.Intent
    import android.os.PowerManager
    import android.view.View

    import com.catolicapps.smartrosario.TipoMisterio

    class RosarioActivity : AppCompatActivity() {

        private lateinit var imageView: ImageView
        private lateinit var textView: TextView
        private lateinit var vibrator: Vibrator
        private lateinit var wakeLock: PowerManager.WakeLock

        private lateinit var tipoMisterioDelDia: TipoMisterio  // ⬅️ Esta línea es clave
        private var misterios: List<String> = listOf()
        private var textosMisterios: List<String> = listOf()
        private var misterioActual = 0
        private var cuentaAveMaria = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_rosario)

            // Código para activar el wakelock
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SmartRosario::RosarioWakeLock")
            wakeLock.acquire()

            imageView = findViewById(R.id.imageMisterio)
            textView = findViewById(R.id.textMisterio)
            val btnAvanzar = findViewById<Button>(R.id.btnAvanzar)
            val btnContarAveMaria = findViewById<Button>(R.id.btnContarAveMaria)
            val textContadorAveMaria = findViewById<TextView>(R.id.textContadorAveMaria)
            val textMensajes = findViewById<TextView>(R.id.textMensajes)
            val btnApagarPantalla = findViewById<Button>(R.id.btnApagarPantalla)
            //val overlayOscuro = findViewById<View>(R.id.overlayOscuro)

            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            // CORREGIR!!!!!!
            tipoMisterioDelDia = obtenerMisteriosDelDia()
            cargarMisterios(tipoMisterioDelDia)
            mostrarMisterio(misterioActual)

            btnAvanzar.setOnClickListener {
                if(cuentaAveMaria == 10) {
                    textMensajes.text = ""
                    avanzarMisterio()
                    cuentaAveMaria = 0
                    textContadorAveMaria.text = "Avemarías: $cuentaAveMaria"
                }
                else {
                    textMensajes.text = "Aún no se han completado 10 avemarías."
                }
            }

            btnContarAveMaria.setOnClickListener {
                if(cuentaAveMaria < 9) {
                    textMensajes.text = ""
                    cuentaAveMaria++
                    textContadorAveMaria.text = "Avemarías: $cuentaAveMaria"
                    vibrarCorto()
                }
                else {
                    cuentaAveMaria=10
                    textContadorAveMaria.text = "Avemarías: $cuentaAveMaria"
                    textMensajes.text = "Misterio Completado"
                    vibrarLargo()
                }
            }

            btnApagarPantalla.setOnClickListener {
                /*
                // Baja el brillo de la pantalla al mínimo
                val lp = window.attributes
                lp.screenBrightness = 0.01f  // 1% de brillo
                window.attributes = lp
                */

                // Mantener pantalla encencida
                window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                // Llamada a la actividad que contiene unicamente un fondo negro a pantalla completa
                val intent = Intent(this, ApagarPantallaActivity::class.java)
                startActivity(intent)
            }
        }

        override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                val textMensajes = findViewById<TextView>(R.id.textMensajes)
                val textContadorAveMaria = findViewById<TextView>(R.id.textContadorAveMaria)
                //val overlayOscuro = findViewById<View>(R.id.overlayOscuro)
                if(cuentaAveMaria < 9) {
                    textMensajes.text = ""
                    cuentaAveMaria++
                    textContadorAveMaria.text = "Avemarías: $cuentaAveMaria"
                    vibrarCorto()
                }
                else {
                    cuentaAveMaria=10
                    textContadorAveMaria.text = "Avemarías: $cuentaAveMaria"
                    textMensajes.text = "Misterio Completado"
                    vibrarLargo()
                    // Brillo de la pantalla al 50%
                    val lp = window.attributes
                    lp.screenBrightness = 0.50f  // 50% de brillo
                    window.attributes = lp
                    //overlayOscuro.visibility = View.INVISIBLE
                }
                return true  // Consumimos el evento
            }
            return super.onKeyDown(keyCode, event)
        }

        override fun onDestroy() {
            super.onDestroy()
            if (::wakeLock.isInitialized && wakeLock.isHeld) {
                wakeLock.release()
            }
        }

        fun cargarMisterios(tipo: TipoMisterio) {
            when (tipo) {
                TipoMisterio.GOZOSOS -> {
                    misterios = listOf(
                        "misterio_gozoso_1", "misterio_gozoso_2", "misterio_gozoso_3",
                        "misterio_gozoso_4", "misterio_gozoso_5"
                    )
                    textosMisterios = listOf(
                        "1º Gozoso: La Anunciación",
                        "2º Gozoso: La Visitación",
                        "3º Gozoso: El Nacimiento de Jesús",
                        "4º Gozoso: La Presentación de Jesús en el Templo",
                        "5º Gozoso: El Niño perdido y hallado en el Templo"
                    )
                }

                TipoMisterio.DOLOROSOS -> {
                    misterios = listOf(
                        "misterio_doloroso_1", "misterio_doloroso_2", "misterio_doloroso_3",
                        "misterio_doloroso_4", "misterio_doloroso_5"
                    )
                    textosMisterios = listOf(
                        "1º Doloroso: La Oración en el Huerto",
                        "2º Doloroso: La Flagelación del Señor",
                        "3º Doloroso: La Coronación de Espinas",
                        "4º Doloroso: Jesús con la Cruz a Cuestas",
                        "5º Doloroso: La Crucifixión y Muerte de Jesús"
                    )
                }

                TipoMisterio.GLORIOSOS -> {
                    misterios = listOf(
                        "misterio_glorioso_1", "misterio_glorioso_2", "misterio_glorioso_3",
                        "misterio_glorioso_4", "misterio_glorioso_5"
                    )
                    textosMisterios = listOf(
                        "1º Glorioso: La Resurrección del Señor",
                        "2º Glorioso: La Ascensión del Señor",
                        "3º Glorioso: La Venida del Espíritu Santo",
                        "4º Glorioso: La Asunción de la Virgen María",
                        "5º Glorioso: La Coronación de María Santísima como Reina del Cielo"
                    )
                }

                TipoMisterio.LUMINOSOS -> {
                    misterios = listOf(
                        "misterio_luminoso_1", "misterio_luminoso_2", "misterio_luminoso_3",
                        "misterio_luminoso_4", "misterio_luminoso_5"
                    )
                    textosMisterios = listOf(
                        "1º Luminoso: El Bautismo de Jesús en el Jordán",
                        "2º Luminoso: Las Bodas de Caná",
                        "3º Luminoso: El Anuncio del Reino de Dios",
                        "4º Luminoso: La Transfiguración del Señor",
                        "5º Luminoso: La Institución de la Eucaristía"
                    )
                }

            }
        }

        fun mostrarMisterio(index: Int) {
            if (index in misterios.indices) {
                val imageRes = resources.getIdentifier(misterios[index], "drawable", packageName)
                imageView.setImageResource(imageRes)
                textView.text = textosMisterios[index]
            }

            val misterioNombre = misterios[misterioActual]
            val obra = ObrasArteRepository.obrasArte[misterioNombre]

            if (obra != null) {
                val intent = Intent(this, MisterioImagenActivity::class.java)
                intent.putExtra("imagenResId", obra.imagenResId)
                intent.putExtra("textoObra", "${obra.titulo} - ${obra.autor}")
                startActivity(intent)
            }

            // Permitir que la pantalla pueda entrar en suspensión nuevamente
            window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        fun avanzarMisterio() {
            misterioActual = (misterioActual + 1) % misterios.size
            mostrarMisterio(misterioActual)

            // Brillo de la pantalla al 50%
            val lp = window.attributes
            lp.screenBrightness = 0.50f  // 50% de brillo
            window.attributes = lp
        }

        private fun vibrarCorto() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(100)
            }
        }

        private fun vibrarLargo() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(400)
            }
        }

    }
