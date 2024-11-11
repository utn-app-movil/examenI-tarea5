package model

import android.content.Context
import cr.ac.utn.appmovil.data.MemoryManager

import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.movil.R
import identities.Identifier
import identities.nav_Container


class nav_model(context: Context) {


    private val dbManager: IDBManager = MemoryManager
    private val _context: Context = context

    fun addContainer(container: nav_Container){
        dbManager.add(container)

    }

    fun updateContainer(container: nav_Container){
        dbManager.update(container)
    }

    fun removeContainer(id: String){
        val result = dbManager.getByid(id)
        if (result == null) {
            val message = _context.getString(R.string.containerNotF)
            throw Exception(message)
        }
        dbManager.remove(id)
    }

    fun getContainerAll()= dbManager.getAll()

    fun getContainers(id: String): Identifier? {
        val result = dbManager.getByid(id)
        if (result == null) {
            val message = _context.getString(R.string.containerNotF)
            throw Exception(message)
        }
        return result
    }

   /* // Método isDuplicate para verificar duplicados por ContainerNum o product
    fun isDuplicate(container: nav_Container): Boolean {
        return dbManager.getAll().any {
            it._ContainerNum == container._ContainerNum ||
                    it._product.equals(container._product, ignoreCase = true)
        }
    }*/





}