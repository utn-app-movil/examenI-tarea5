package cr.ac.utn.appmovil.model

import android.util.Log
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.bib_Reservation
import cr.ac.utn.appmovil.interfaces.IDBManager

class bib_ReservationModel {
    private val dbManager: IDBManager = MemoryManager

    fun addReservation(reservation: bib_Reservation) {
        if (reservation.Id.isEmpty()) {
            reservation.Id = java.util.UUID.randomUUID().toString()
        }
        dbManager.add(reservation)
    }


    fun updateReservation(reservation: bib_Reservation) {
        dbManager.update(reservation)
    }

    fun removeReservation(id: String) {
        dbManager.remove(id)
    }

    fun getReservations(): List<bib_Reservation> {
        return dbManager.getAll().filterIsInstance<bib_Reservation>()
    }

    fun getReservation(id: String): bib_Reservation? {
        val reservation = dbManager.getByid(id) as? bib_Reservation
        Log.d("bib_ReservationModel", "getting id: $id - result: $reservation")
        return reservation
    }
}
