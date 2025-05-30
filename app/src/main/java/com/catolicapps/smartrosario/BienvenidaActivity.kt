package com.catolicapps.smartrosario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

import com.catolicapps.smartrosario.TipoMisterio

class BienvenidaActivity : AppCompatActivity() {

    private lateinit var tipoMisterioDelDia: TipoMisterio
    private lateinit var tipoSeleccionado: TipoMisterio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val titulo = findViewById<TextView>(R.id.tituloDia)
        val descripcion = findViewById<TextView>(R.id.descripcionMisterio)
        val spinner = findViewById<Spinner>(R.id.spinnerTipoMisterio)
        val boton = findViewById<Button>(R.id.btnComenzarRosario)

        tipoMisterioDelDia = obtenerMisteriosDelDia()
        tipoSeleccionado = tipoMisterioDelDia

        titulo.text = "Misterios del día"
        descripcion.text = "Hoy se rezan los Misterios ${tipoMisterioDelDia.nombre}, pero puede seleccionar otros a continuación."

        val opciones = TipoMisterio.values().map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinner.adapter = adapter
        spinner.setSelection(tipoMisterioDelDia.ordinal)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                tipoSeleccionado = TipoMisterio.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        boton.setOnClickListener {
            val intent = Intent(this, RosarioActivity::class.java)
            intent.putExtra("tipoMisterio", tipoSeleccionado.name)
            startActivity(intent)
        }
    }
}
