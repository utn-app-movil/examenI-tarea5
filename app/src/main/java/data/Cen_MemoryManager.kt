package data

import cr.ac.utn.appmovil.data.MemoryManager
import identities.Cen_Persona
import identities.Identifier
import interfaces.Cen_IDBManager

object Cen_MemoryManager: Cen_IDBManager {
    private var Cen_objectList = mutableListOf<Cen_Persona>()

    override fun add(cen_obje: Cen_Persona) {
        Cen_objectList.add(cen_obje)
    }

    override fun update(cen_obje: Cen_Persona) {
       remove(cen_obje.id)
        Cen_objectList.add(cen_obje)
    }

    override fun remove(id: String) {
        Cen_objectList.removeIf{it.id.trim() == id.trim()}
    }

    fun remove (cen_obje: Cen_Persona){
        Cen_objectList.remove(cen_obje)
    }


    override fun getAll(): List<Cen_Persona> = Cen_objectList.toList()



    override fun getById(id: String): Cen_Persona? {
        try {
            var result = Cen_MemoryManager.Cen_objectList.filter { (it.id) == id }
            return if(!result.any()) null else result[0]
        }catch (e: Exception){
            throw e
        }
    }

    override fun getByFullName(fullName: String): Cen_Persona? {
        try {
            var result = Cen_objectList.filter { (it.fullName) == fullName }
            return if(!result.any()) null else result[0]
        }catch (e: Exception){
            throw e
        }
    }
    }
