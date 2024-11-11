package model

import android.content.Context
import android.provider.ContactsContract.Contacts
import androidx.activity.result.contract.ActivityResultContracts
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.Persona
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.movil.R
import identities.Identifier
import identities.far_Medicine

class far_factureModel {
    private val dbManager: IDBManager = MemoryManager
    private lateinit var _context: Context
    fun far_addFacture(facture: Identifier){
        dbManager.add(facture)
    }
    constructor(context: Context){
        _context = context
    }
    constructor()
    fun getAll():List<far_Facture> {
        return dbManager.getAll().filterIsInstance<far_Facture>()
    }

    fun delete(id: String){
        dbManager.remove(id)
    }

    fun far_update(facture: Identifier){
        dbManager.update(facture)
    }

    fun getFullinfoByID(id: String): far_Facture?{
        val result = dbManager.getAll().filterIsInstance<far_Facture>()
        return  result.find { it.Id == id }
    }
}