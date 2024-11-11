package interfaces

import identities.Evento

// EventoDbManager.kt
class EventoDbManager : EventoCRUD {

    private val eventos = mutableListOf<Evento>()

    override fun crearEvento(evento: Evento): Boolean {
        return if (eventos.any { it.id == evento.id }) {
            false // Ya existe un evento con este ID
        } else {
            eventos.add(evento)
            true
        }
    }

    override fun obtenerEventoPorId(id: String): Evento? {
        return eventos.find { it.id == id }
    }

    override fun listarEventos(): List<Evento> {
        return eventos.toList()
    }

    override fun actualizarEvento(id: String, evento: Evento): Boolean {
        val index = eventos.indexOfFirst { it.id == id }
        return if (index >= 0) {
            eventos[index] = evento
            true
        } else {
            false
        }
    }

    override fun eliminarEvento(id: String): Boolean {
        return eventos.removeIf { it.id == id }
    }
}
