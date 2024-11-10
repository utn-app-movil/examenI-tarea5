package model

import android.util.Log
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.interfaces.IDBManager
import identities.vac_VaccinationRecord

class vac_VaccinationModel {
    private val dbManager: IDBManager = MemoryManager

    fun addVaccinationRecord(record: vac_VaccinationRecord) {
        if (record.Id.isEmpty()) {
            record.Id = java.util.UUID.randomUUID().toString()
        }
        dbManager.add(record)
    }

    fun updateVaccinationRecord(record: vac_VaccinationRecord) {
        dbManager.update(record)
    }

    fun removeVaccinationRecord(id: String) {
        dbManager.remove(id)
    }

    fun getVaccinationRecordById(id: String): vac_VaccinationRecord? {
        val record = dbManager.getByid(id) as? vac_VaccinationRecord
        Log.d("vac_VaccinationModel", "Getting id: $id - Result: $record")
        return record
    }
}
