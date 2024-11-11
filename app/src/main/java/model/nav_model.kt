package model

import android.content.Context
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.movil.R
import identities.Identifier
import identities.nav_container

class nav_model (context: Context){
    private val dbManager: IDBManager = MemoryManager
    private val _context: Context = context

    fun addContainer(container: nav_container){
        dbManager.add(container)

    }

    fun updateContainer(container: nav_container){
        dbManager.update(container)
    }

    fun removeContainer(id: String){
        val result = dbManager.getByid(id)
        if (result == null) {
            val message = _context.getString(R.string.msgErr)
            throw Exception(message)
        }
        dbManager.remove(id)
    }

    fun getContainerAll()= dbManager.getAll()

    fun getContainers(id: String): Identifier? {
        val result = dbManager.getByid(id)
        if (result == null) {
            val message = _context.getString(R.string.msgErr)
            throw Exception(message)
        }
        return result
    }
    fun isDuplicate(container: nav_container): Boolean {
        return dbManager.getAll().any {
            it.Id == container.Id || (it as? nav_container)?.containerNum.equals(container.containerNum, ignoreCase = true)
        }
    }
}