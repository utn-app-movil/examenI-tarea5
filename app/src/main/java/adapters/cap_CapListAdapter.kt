package adapters

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
import identities.cap_Training

class cap_CapListAdapter(
    context: Context,
    resource: Int,
    private val datasource: List<cap_Training>
) : ArrayAdapter<cap_Training>(context, resource, datasource) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: inflater.inflate(R.layout.cap_training_item_list, parent, false)

        // Obtén las referencias de los TextViews en el layout
        val lbFullName = rowView.findViewById<TextView>(R.id.cap_lbItemFullName)
        val lbCourseCode = rowView.findViewById<TextView>(R.id.cap_lbItemCourseCode)
        val lbDateTime = rowView.findViewById<TextView>(R.id.cap_lbItemDate)
        val imgPhoto = rowView.findViewById<ImageView>(R.id.cap_imgItemPhoto)

        // Obtiene el objeto `cap_Capacitacion` en la posición actual
        val training = datasource[position]

        // Asigna los valores de `fullName`, `courseCode`, y `dateTime`
        lbFullName.text = training.fullName
        lbCourseCode.text = "Code: ${training.courseCode}"
        lbDateTime.text = "Date: ${training.dateTime}"

        // Maneja la carga de la foto con `Bitmap`
        training.photoBitmap?.let {
            imgPhoto.setImageBitmap(it)
        } ?: imgPhoto.setImageResource(R.drawable.cap_img) // Imagen de marcador de posición

        return rowView
    }
}