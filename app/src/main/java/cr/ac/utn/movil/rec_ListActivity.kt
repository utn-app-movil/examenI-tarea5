package cr.ac.utn.movil

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.CandidateModel


class rec_ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_list)

        val listView = findViewById<ListView>(R.id.candidate_list_view)

        // Obtener lista de candidatos del modelo
        val candidates = CandidateModel.getCandidates()

        // Configurar adaptador personalizado
        val adapter = CandidateAdapter(this, candidates)
        listView.adapter = adapter
    }
}
