package interfaces

import identities.Evento

// EventoCRUD.kt
interface EventoCRUD {
    fun crearEvento(evento: Evento): Boolean
    fun obtenerEventoPorId(id: String): Evento?
    fun listarEventos(): List<Evento>
    fun actualizarEvento(id: String, evento: Evento): Boolean
    fun eliminarEvento(id: String): Boolean
}
