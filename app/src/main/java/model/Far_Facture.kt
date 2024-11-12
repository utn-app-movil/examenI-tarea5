package model

import cr.ac.utn.appmovil.identities.Persona
import identities.Identifier
import identities.far_Medicine
import java.io.Serializable

class far_Facture(
    val person: Persona,
    val listMedicine: MutableList<far_Medicine> = mutableListOf()
) : Identifier(), Serializable {

    override val FullDescription: String
        get() = "Factura para ${person.Name} con ${listMedicine.size} medicamentos."

}
