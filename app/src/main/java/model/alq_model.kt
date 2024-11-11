package model

import android.util.Log
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.movil.alq_AddClientActivity

class alq_model {
    private val dbManager: IDBManager = MemoryManager

    fun addReservation(reservation: alq_AddClientActivity) {
        if (.isEmpty()) {
            = java.util.UUID.randomUUID().toString()
        }
        dbManager.add()
    }


    fun updateReservation(: alq_AddClientActivity) {
        dbManager.update()
    }

    fun removeReservation(id: String) {
        dbManager.remove(id)
    }

    fun getReservations(): List<alq_AddClientActivity> {
        return dbManager.getAll().filterIsInstance<alq_AddClientActivity>()
    }

    fun getReservation(id: String): alq_AddClientActivity? {
        val reservation = dbManager.getByid(id) as? alq_AddClientActivity
        Log.d("bib_ReservationModel", "getting id: $id - result: $reservation")
        return reservation
    }
}

}