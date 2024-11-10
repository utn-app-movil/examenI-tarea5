package model

import android.content.Context
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.movil.R
import identities.cap_Training

class cap_TrainingModel(context: Context) {
    private var dbManager: IDBManager = MemoryManager
    private val _context: Context = context

    fun addTraining(training: cap_Training) {
        dbManager.add(training)
    }

    fun getTraining(): List<cap_Training> {
        return dbManager.getAll().filterIsInstance<cap_Training>()
    }

    fun getTraining(id: String): cap_Training? {
        val result = dbManager.getByid(id) as? cap_Training
        if (result == null) {
            val message = _context.getString(R.string.cap_msgTrainingNotFound)
            throw Exception(message)
        }
        return result
    }

    fun removeTraining(id: String) {
        val result = dbManager.getByid(id) as? cap_Training
        if (result == null) {
            val message = _context.getString(R.string.cap_msgTrainingNotFound)
            throw Exception(message)
        }
        dbManager.remove(id)
    }

    fun updateTraining(training: cap_Training) {
        dbManager.update(training)
    }

    fun getTrainingByFullDescription(fullDescription: String): cap_Training? {
        val result = dbManager.getByFullDescription(fullDescription) as? cap_Training
        if (result == null) {
            val message = _context.getString(R.string.cap_msgTrainingNotFound)
            throw Exception(message)
        }
        return result
    }
}
