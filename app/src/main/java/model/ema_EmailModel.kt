package cr.ac.utn.appmovil.model

import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.ema_Email
import cr.ac.utn.appmovil.interfaces.IDBManager

class ema_EmailModel {
    private val dbManager: IDBManager = MemoryManager

    // Agregar un nuevo correo electrónico
    fun addEmail(email: ema_Email) {
        dbManager.add(email)
    }

    // Actualizar un correo electrónico existente
    fun updateEmail(email: ema_Email) {
        dbManager.update(email)
    }

    // Eliminar un correo electrónico por ID
    fun removeEmail(id: String) {
        dbManager.remove(id)
    }

    // Obtener todos los correos electrónicos
    fun getEmails(): List<ema_Email> {
        val emails = mutableListOf<String>()
        dbManager.getAll().forEach{ i-> emails.add(i.Id)}
        return dbManager.getAll().filterIsInstance<ema_Email>()
    }

    // Obtener un correo electrónico por ID
    fun getEmail(id: String): ema_Email? {
        return dbManager.getByid(id) as? ema_Email
    }



    // Obtener correos electrónicos por fecha de envío mayor a la actual
    fun getEmailsAfterDate(date: String): List<ema_Email> {
        return getEmails().filter { it.SendDate > date }
    }
}
