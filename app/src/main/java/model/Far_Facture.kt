package model

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.Persona
import identities.Identifier
import identities.far_Medicine
import java.io.Serializable

class far_Facture(
    val person: Persona,
    val listMedicine: String,
    val numF: Int,
    val photoProfile: Uri
) : Identifier(), Serializable{

    override val FullDescription: String
        get() = "Facture for ${person.Name} with ${listMedicine} medicines and the " +
                "facture number is ${numF}"

    companion object {
        fun far_searchNumberFact(numFactures: Int): Int{
            var currentNumber = numFactures
            val list: List<Identifier> = MemoryManager.getAll()

            while (list.any { it is far_Facture && it.numF == currentNumber }) {
                currentNumber++
            }
            return currentNumber
        }
    }

}
