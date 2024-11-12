package cr.ac.utn.movil

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import identities.Evento
import model.EventoModel

class eve_AddActivity : AppCompatActivity() {

    private lateinit var etEventName: EditText
    private lateinit var etEventLocation: EditText
    private lateinit var etEventDate: EditText
    private lateinit var etEventTime: EditText
    private lateinit var spEventType: Spinner
    private lateinit var btnSaveEvent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eve_add)

        // Inicialización de vistas
        etEventName = findViewById(R.id.etEventName)
        etEventLocation = findViewById(R.id.etEventLocation)
        etEventDate = findViewById(R.id.etEventDate)
        etEventTime = findViewById(R.id.etEventTime)
        spEventType = findViewById(R.id.spEventType)
        btnSaveEvent = findViewById(R.id.btnSaveEvent)

        // Configuración del Spinner (lista desplegable)
        val eventTypes = arrayOf("Conferencia", "Concierto", "Seminario", "Taller", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEventType.adapter = adapter

        // Configuración del botón guardar
        btnSaveEvent.setOnClickListener {
            saveEvent()
        }
    }

    private fun saveEvent() {
        try {
            // Recoger datos del formulario
            val eventName = etEventName.text.toString()
            val eventLocation = etEventLocation.text.toString()
            val eventDate = etEventDate.text.toString()
            val eventTime = etEventTime.text.toString()
            val eventType = spEventType.selectedItem.toString()

            // Validación de datos
            if (eventName.isEmpty() || eventLocation.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_LONG).show()
                return
            }

            // Creación de un nuevo evento
//            val event = Event(
//                name = eventName,
//                location = eventLocation,
//                date = eventDate,
//                time = eventTime,
//                type = eventType
//            )

            // Guardar el evento en el modelo
//            EventoModel.AddEvento(event)
            Toast.makeText(this, "Evento guardado exitosamente.", Toast.LENGTH_LONG).show()

            // Limpieza del formulario
            clearForm()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar el evento: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearForm() {
        etEventName.text.clear()
        etEventLocation.text.clear()
        etEventDate.text.clear()
        etEventTime.text.clear()
        spEventType.setSelection(0)
    }
}
