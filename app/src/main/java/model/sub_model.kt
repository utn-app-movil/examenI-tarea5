package model

import android.content.Context
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.interfaces.IDBManager
import identities.Identifier
import identities.sub_datas

class sub_model {
    private val dbManager: IDBManager = MemoryManager
    private lateinit var _context: Context
    fun sub_add(facture: Identifier){
        dbManager.add(facture)
    }

    fun getAll():List<sub_datas> {
        return dbManager.getAll().filterIsInstance<sub_datas>()
    }
    constructor(context: Context){
        _context = context
    }
    constructor()
}