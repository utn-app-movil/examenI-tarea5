package cr.ac.utn.movil

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.adapters.eve_ListAdapter
import cr.ac.utn.appmovil.model.eve_EventModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util

class eve_ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eve_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa el modelo de eventos
        val eventModel = eve_EventModel(this)
        val lstCustomList = findViewById<ListView>(R.id.eve_lstCustomList)
        val eventList = eventModel.getEvents()

        // Configura el adaptador personalizado
        val adapter = eve_ListAdapter(this, R.layout.eve_event_custom_list, eventList)
        lstCustomList.adapter = adapter

        // Configura el listener para clics en los elementos de la lista
        lstCustomList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val eventId = eventList[position].Id
            util.openActivity(this, eve_AddActivity::class.java, EXTRA_MESSAGE_ID, eventId)
        }

    }
}