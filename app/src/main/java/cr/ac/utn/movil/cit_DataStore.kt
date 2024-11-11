package cr.ac.utn.movil

import cr.ac.utn.appmovil.model.cit_Citizen


object cit_DataStore {

    val citRegisterList = mutableListOf<cit_Citizen>()


    fun addCitizen(citizen: cit_Citizen) {
        citRegisterList.add(citizen)
    }


    fun modifyCitizen(index: Int, updatedCitizen: cit_Citizen) {
        if (index in citRegisterList.indices) {
            citRegisterList[index] = updatedCitizen
        }
    }

    fun deleteCitizen(index: Int) {
        if (index in citRegisterList.indices) {
            citRegisterList.removeAt(index)
        }
    }

    fun getCitizenById(id: String): cit_Citizen? {
        return citRegisterList.find { it.cit_Identification == id }
    }
}
