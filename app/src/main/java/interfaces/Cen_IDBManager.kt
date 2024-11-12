package interfaces

import identities.Cen_Persona

interface Cen_IDBManager {
    fun add (cen_obje: Cen_Persona)
    fun update (cen_obje: Cen_Persona)
    fun remove (id: String)
    fun getAll (): List<Cen_Persona>
    fun getById (id: String): Cen_Persona?
    fun getByFullName(fullName: String): Cen_Persona?
}