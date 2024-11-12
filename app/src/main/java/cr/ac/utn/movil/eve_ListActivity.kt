package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import identities.Evento
import model.EventoModel

class Eve_ListActivity : AppCompatActivity() {

    private lateinit var rvEvents: RecyclerView
    private lateinit var btnAddEvent: Button
    private lateinit var adapter: EventAdapter
    private var events = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eve_list)

        rvEvents = findViewById(R.id.rvEvents)
        btnAddEvent = findViewById(R.id.btnAddEvent)

        // Configurar RecyclerView
        rvEvents.layoutManager = LinearLayoutManager(this)
        adapter = EventAdapter(events, onEditClick = { editEvent(it) }, onDeleteClick = { deleteEvent(it) })
        rvEvents.adapter = adapter

        btnAddEvent.setOnClickListener {
            val intent = Intent(this, eve_AddActivity::class.java)
            startActivity(intent)
        }

        loadEvents()
    }

    private fun loadEvents() {
        // Simular carga desde una base de datos
        events.clear()
        events.addAll(EventoModel.getAllEvents()) // Reemplazar con la lógica de tu base de datos
        adapter.notifyDataSetChanged()
    }

    private fun editEvent(event: Evento) {
        val intent = Intent(this, eve_AddActivity::class.java)
        intent.putExtra("EVENT_ID", event.id)
        startActivity(intent)
    }

    private fun deleteEvent(event: Evento) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Evento")
            .setMessage("¿Estás seguro de que deseas eliminar el evento '${event.id}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                EventoModel.deleteEvent(event.id) // Lógica para eliminar el evento
                loadEvents()
                Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadEvents() // Recargar los eventos al volver
    }
}
