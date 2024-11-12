package cr.ac.utn.movil

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.appmovil.identities.Candidate

class CandidateAdapter(context: Context, private val candidates: List<Candidate>) :
    ArrayAdapter<Candidate>(context, 0, candidates) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.rec_item_candidate, parent, false)

        // Obtener elementos del layout
        val candidatePhoto = view.findViewById<ImageView>(R.id.candidate_photo)
        val candidateName = view.findViewById<TextView>(R.id.candidate_name)
        val candidateRole = view.findViewById<TextView>(R.id.candidate_role)

        // Obtener el candidato actual
        val candidate = candidates[position]

        // Asignar los datos del candidato a los elementos de la vista
        candidateName.text = "${candidate.name} ${candidate.lastName}"
        candidateRole.text = candidate.role

        // Cargar la foto si existe, si no usar el placeholder
        val photoPath = candidate.photoPath
        if (photoPath != null && photoPath.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(photoPath)
            candidatePhoto.setImageBitmap(bitmap)
        } else {
            candidatePhoto.setImageResource(R.drawable.ic_placeholder_image)
        }

        // Al hacer clic, abrir la actividad de detalles
        view.setOnClickListener {
            val intent = Intent(context, rec_DetailActivity::class.java).apply {
                putExtra("name", candidate.name)
                putExtra("lastName", candidate.lastName)
                putExtra("phone", candidate.phone)
                putExtra("email", candidate.email)
                putExtra("address", candidate.address)
                putExtra("country", candidate.country)
                putExtra("role", candidate.role)
                putExtra("photoPath", candidate.photoPath)
            }
            context.startActivity(intent)
        }

        return view
    }
}
