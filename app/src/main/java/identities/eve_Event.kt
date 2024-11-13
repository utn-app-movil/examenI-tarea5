package cr.ac.utn.appmovil.identities

import android.graphics.Bitmap
import identities.Identifier
import java.util.Date

/**
 * Representa un evento cultural con información detallada, incluyendo la persona asociada.
 */
class eve_Event(
    id: String,
    var institution: String,
    var eventLocation: String,
    var eventDateTime: Date,
    var seatNumber: Int,
    var eventType: String,
    var person: Persona, // Vincula el evento con la información de la persona (estudiante) que asiste
    var photoBitmap: Bitmap? = null // Foto opcional del evento
) : Identifier() {

    init {
        this.Id = id
    }

    /**
     * Descripción completa del evento, incluyendo el nombre de la persona que asiste.
     */
    override val FullDescription: String
        get() = "$eventType - $eventLocation ($institution) - Asistente: ${person.FullDescription}"
}
