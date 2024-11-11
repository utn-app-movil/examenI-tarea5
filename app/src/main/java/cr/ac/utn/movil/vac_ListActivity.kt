package cr.ac.utn.movil

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util
import identities.vac_VaccinationRecord

class vac_ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: vac_VaccinationAdapter
    private var records = mutableListOf<vac_VaccinationRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vac_list)

        recyclerView = findViewById(R.id.lstVaccinationRecords)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadVaccinationRecords()
        adapter = vac_VaccinationAdapter(records) { record ->
            util.openActivity(this, vac_FormActivity::class.java, EXTRA_MESSAGE_ID, record.Id)
        }
        recyclerView.adapter = adapter
    }

    private fun loadVaccinationRecords() {
        records = MemoryManager.getAll().filterIsInstance<vac_VaccinationRecord>().toMutableList()
    }

    override fun onResume() {
        super.onResume()
        loadVaccinationRecords()
        adapter.notifyDataSetChanged()
    }
}
