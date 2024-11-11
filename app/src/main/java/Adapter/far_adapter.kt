package Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cr.ac.utn.movil.R
import identities.Identifier
import model.far_Facture

class far_adapter(private val context: Context,
                  private val resource: Int,
                  private val datasource: List<Identifier>
):
    ArrayAdapter<Identifier>(context, resource, datasource){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView (position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.far_list_custom, parent, false)
        val far_name = rowView.findViewById(R.id.far_name_patient) as TextView
        val far_Lname = rowView.findViewById(R.id.far_lastName_patient) as TextView
        val far_facture = rowView.findViewById(R.id.far_numfacture_patient) as TextView
        val lbImage = rowView.findViewById(R.id.far_image_view) as ImageView

        val facture = datasource[position] as far_Facture
        far_name.text = facture.person.Name
        far_Lname.text = facture.person.LastName
        far_facture.text = "N° " + facture.numF.toString()
        lbImage.setImageURI(facture.photoProfile)


        return rowView
    }
}