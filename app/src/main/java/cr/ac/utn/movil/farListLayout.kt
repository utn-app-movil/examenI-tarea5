package cr.ac.utn.movil

import Adapter.far_adapter
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util
import identities.Identifier
import model.far_factureModel

class farListLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var model: far_factureModel
        lateinit var patientList: List<Identifier>
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.far_list_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayourt)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        model = far_factureModel(this)
        val lstCustomList = findViewById<ListView>(R.id.lst_customContactList)
        patientList = model.getAll()

        val adapter =far_adapter(this, R.layout.far_list_custom, patientList)
        lstCustomList.adapter = adapter

        lstCustomList.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val ids = patientList[position].Id
            util.openActivity(this, far_adding_patient::class.java, EXTRA_MESSAGE_ID, ids)
        }
    }
}