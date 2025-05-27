package com.catolicapps.smartrosario
import java.util.Calendar

enum class TipoMisterio(val nombre: String) {
    GOZOSOS("Gozosos"),
    DOLOROSOS("Dolorosos"),
    GLORIOSOS("Gloriosos"),
    LUMINOSOS("Luminosos")
}

fun obtenerMisteriosDelDia(): TipoMisterio {
    return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY, Calendar.SATURDAY -> TipoMisterio.GOZOSOS
        Calendar.TUESDAY, Calendar.FRIDAY -> TipoMisterio.DOLOROSOS
        Calendar.WEDNESDAY, Calendar.SUNDAY -> TipoMisterio.GLORIOSOS
        Calendar.THURSDAY -> TipoMisterio.LUMINOSOS
        else -> TipoMisterio.GOZOSOS // Valor por defecto
    }
}