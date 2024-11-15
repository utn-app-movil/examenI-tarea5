package model

import android.content.Context
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.movil.R
import identities.vul_Person

class vul_PersonModel(context: Context) {
    private var dbManager: IDBManager = MemoryManager
    private val _context: Context = context

    fun addPerson(person: vul_Person) {
        dbManager.add(person)
    }

    fun getPersons(): List<vul_Person> {
        return dbManager.getAll().filterIsInstance<vul_Person>()
    }

    fun getPerson(id: String): vul_Person? {
        val result = dbManager.getByid(id) as? vul_Person
        if (result == null) {
            val message = _context.getString(R.string.vul_PersonNotFound)
            throw Exception(message)
        }
        return result
    }

    fun removePerson(id: String) {
        val result = dbManager.getByid(id) as? vul_Person
        if (result == null) {
            val message = _context.getString(R.string.vul_PersonNotFound)
            throw Exception(message)
        }
        dbManager.remove(id)
    }

    fun updatePerson(person: vul_Person) {
        dbManager.update(person)
    }

    fun getPersonByFullDescription(fullDescription: String): vul_Person? {
        val result = dbManager.getByFullDescription(fullDescription) as? vul_Person
        if (result == null) {
            val message = _context.getString(R.string.vul_PersonNotFound)
            throw Exception(message)
        }
        return result
    }
}
