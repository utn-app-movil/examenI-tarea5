package model

import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.Persona
import cr.ac.utn.appmovil.interfaces.IDBManager
import identities.Identifier

class far_factureModel {
    private val dbManager: IDBManager = MemoryManager
    fun far_addFacture(facture: Identifier){
        dbManager.add(facture)
    }
}