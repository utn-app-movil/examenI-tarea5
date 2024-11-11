package cr.ac.utn.appmovil.interfaces

import identities.Identifier

interface IDBManager {
    fun add (obj: Unit)
    fun update (obj: Unit)
    fun remove (id: String)
    fun getAll(): List<Identifier>
    fun getByid(id: String): Identifier?
    fun getByFullDescription(fullDescription: String): Identifier?
}