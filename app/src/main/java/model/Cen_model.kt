package model

import android.content.Context
import cr.ac.utn.movil.R
import identities.Cen_Persona
import interfaces.Cen_IDBManager
import data.Cen_MemoryManager
class Cen_model {
    private var Cen_manager: Cen_IDBManager= Cen_MemoryManager
    private lateinit var _context: Context

    constructor(context: Context){
        _context= context
    }

    fun addContact (Cen_perso: Cen_Persona){
        Cen_manager.add(Cen_perso)
    }

    fun getContacts() = Cen_manager.getAll()

    fun getContact(id: String): Cen_Persona?{
        var result = Cen_manager.getById(id)
        if (result == null){
            val message =_context.getString(R.string.Cen_msg_notFound)
        }
        return result
    }

    fun getContactNames(): List<String>{
        val names = mutableListOf<String>()
        Cen_manager.getAll().forEach { i-> names.add(i.fullName) }
        return names.toList()
    }

    fun removeContact(id: String){
        val result = Cen_manager.getById(id)
        if (result == null){
            val message = _context.getString(R.string.Cen_msg_notFound)
            throw Exception(message)
        }
        Cen_manager.remove(id)
    }

    fun updateContact(contact: Cen_Persona){
        Cen_manager.update(contact)
    }

    fun getContactByFullName(fullName: String): Cen_Persona {
        var result = Cen_manager.getByFullName(fullName)
        if (result == null){
            val message = _context.getString(R.string.Cen_msg_notFound)
            throw Exception(message)
        }
        return result
    }
}