package model

// EventoModel.kt
data class EventoModel(
    val eventoId: String,
    val lugar: String,
    val fecha: String,
    val hora: String,
    val tipo: String,
    val estudiantes: List<EstudianteAsistenteModel>
)

data class EstudianteAsistenteModel(
    val personaId: String,
    val institucion: String,
    val numeroAsiento: Int
)
