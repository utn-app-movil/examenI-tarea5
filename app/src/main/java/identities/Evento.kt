package identities

import cr.ac.utn.appmovil.identities.Persona

// Evento.kt
data class Evento(
    val id: String, // Identificador único del evento
    val lugar: String,
    val fecha: String, // YYYY-MM-DD
    val hora: String,  // HH:mm:ss
    val tipo: String, // Teatro, Cine, Concierto
    val estudiantesAsistentes: List<EstudianteAsistente>
)

// EstudianteAsistente.kt
data class EstudianteAsistente(
    val persona: Persona,  // Relación con la entidad Persona
    val institucion: String,
    val numeroAsiento: Int
)
