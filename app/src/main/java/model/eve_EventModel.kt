package cr.ac.utn.appmovil.model

import android.content.Context
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.eve_Event
import cr.ac.utn.appmovil.interfaces.IDBManager

/**
 * Modelo para gestionar eventos culturales, interactuando con MemoryManager.
 */
class eve_EventModel(context: Context) {
    private val dbManager: IDBManager = MemoryManager

    /**
     * Agrega un nuevo evento cultural a la base de datos en memoria.
     */
    fun addEvent(event: eve_Event) {
        dbManager.add(event)
    }

    /**
     * Actualiza un evento cultural existente.
     */
    fun updateEvent(event: eve_Event) {
        dbManager.update(event)
    }

    /**
     * Elimina un evento cultural según su ID.
     */
    fun removeEvent(id: String) {
        dbManager.remove(id)
    }

    /**
     * Obtiene todos los eventos culturales almacenados.
     */
    fun getEvents(): List<eve_Event> {
        return dbManager.getAll().filterIsInstance<eve_Event>()
    }

    /**
     * Obtiene un evento cultural por su ID.
     */
    fun getEventById(id: String): eve_Event? {
        return dbManager.getByid(id) as? eve_Event
    }

    /**
     * Busca un evento cultural por su descripción completa.
     */
    fun getEventByFullDescription(fullDescription: String): eve_Event? {
        return dbManager.getByFullDescription(fullDescription) as? eve_Event
    }
}
