package cr.ac.utn.appmovil.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import cr.ac.utn.appmovil.identities.eve_Event
import cr.ac.utn.movil.R

class eve_ListAdapter(
    private val context: Context,
    private val resource: Int,
    private val events: List<eve_Event>
) : ArrayAdapter<eve_Event>(context, resource, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        // Obtén el evento actual
        val event = events[position]

        // Vincula los elementos de la interfaz
        val imgEventPhoto = view.findViewById<ImageView>(R.id.imgEventPhoto)
        val txtEventType = view.findViewById<TextView>(R.id.txtEventType)
        val txtInstitution = view.findViewById<TextView>(R.id.txtInstitution)
        val txtDateTime = view.findViewById<TextView>(R.id.txtDateTime)

        // Configura los datos
        txtEventType.text = event.eventType
        txtInstitution.text = event.institution
        txtDateTime.text = event.eventDateTime.toString()

        // Si el evento tiene foto, mostrarla; si no, usar un placeholder
        event.photoBitmap?.let {
            imgEventPhoto.setImageBitmap(it)
        } ?: imgEventPhoto.setImageResource(R.drawable.ic_launcher_foreground)

        return view
    }
}
