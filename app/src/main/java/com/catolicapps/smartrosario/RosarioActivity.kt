    package com.catolicapps.smartrosario

    import android.os.Bundle
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import android.content.Intent
    import androidx.core.content.ContextCompat
    import android.widget.Button

    import com.catolicapps.smartrosario.TipoMisterio

    class RosarioActivity : AppCompatActivity() {

        private lateinit var imageView: ImageView
        private lateinit var textView: TextView

        private lateinit var tipoMisterioDelDia: TipoMisterio  // ⬅️ Esta línea es clave
        private var misterios: List<String> = listOf()
        private var textosMisterios: List<String> = listOf()
        private var misterioActual = 0
        private var cuentaAveMaria = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_rosario)

            imageView = findViewById(R.id.imageMisterio)
            textView = findViewById(R.id.textMisterio)
            val btnAvanzar = findViewById<Button>(R.id.btnAvanzar)
            val btnContarAveMaria = findViewById<Button>(R.id.btnContarAveMaria)
            val textContadorAveMaria = findViewById<TextView>(R.id.textContadorAveMaria)
            val textMensajes = findViewById<TextView>(R.id.textMensajes)

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
                if(cuentaAveMaria < 10) {
                    textMensajes.text = ""
                    cuentaAveMaria++
                    textContadorAveMaria.text = "Avemarías: $cuentaAveMaria"
                }
                else {
                    textMensajes.text = "Misterio Completado"
                }
            }

            // Si quieres avanzar con botones volumen, añade aquí también el receiver o lógica.
            val intent = Intent(this, RosarioService::class.java)
            intent.putExtra("pantallaApagada", true)
            intent.putExtra("encenderEntreMisterios", true)
            ContextCompat.startForegroundService(this, intent)
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
        }

        fun avanzarMisterio() {
            misterioActual = (misterioActual + 1) % misterios.size
            mostrarMisterio(misterioActual)
        }
    }
