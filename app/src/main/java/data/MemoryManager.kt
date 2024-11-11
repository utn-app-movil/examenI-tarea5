package cr.ac.utn.appmovil.data

import cr.ac.utn.appmovil.interfaces.IDBManager
import identities.Identifier

object MemoryManager: IDBManager {
    private var objectList = mutableListOf<Identifier>()

    override fun add (obj: Unit){
        objectList.add(obj)
    }
    override fun update (obj: Unit){
        remove(obj.javaClass)
        objectList.add(obj)

    }

    private fun remove(javaClass: Class<Unit>) {
        TODO("Not yet implemented")
    }

    override fun remove (id: String){
        objectList.removeIf { it.Id.trim() == id.trim() }
    }

    fun remove (obj: Identifier){
        objectList.remove(obj)
    }

    override fun getAll(): List<Identifier> = objectList.toList()

    override fun getByid(id: String): Identifier? {
        try {
            var result = objectList.filter { (it.Id) == id }
            return if(!result.any()) null else result[0]
        }catch (e: Exception){
            throw e
        }
    }

    override fun getByFullDescription(fullDescription: String): Identifier? {
        try {
            var result = objectList.filter { (it.FullDescription) == fullDescription }
            return if(!result.any()) null else result[0]
        }catch (e: Exception){
            throw e
        }
    }
}

private fun <E> MutableList<E>.add(element: Unit) {
    TODO("Not yet implemented")
}
