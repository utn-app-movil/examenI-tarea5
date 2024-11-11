package cr.ac.utn.movil

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import identities.Empleado

class EmpleadoActivity : AppCompatActivity() {

    private val listaEmpleados = mutableListOf<Empleado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nom_pantalla1)

        val agregarButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        agregarButton.setOnClickListener {
            agregarEmpleado()
        }
    }

    private fun agregarEmpleado() {
        val nombre = findViewById<EditText>(R.id.NombreEmpleado).text.toString()
        val numero = findViewById<EditText>(R.id.NumeroEmpleado).text.toString().toIntOrNull() ?: 0
        val puesto = findViewById<EditText>(R.id.PuestoEmpleado).text.toString()
        val salario = findViewById<EditText>(R.id.SalarioEmpleado).text.toString().toDoubleOrNull() ?: 0.0
        val iban = findViewById<EditText>(R.id.IbanEmpleado).text.toString()
        val nombreBanco = findViewById<EditText>(R.id.NombreBancoEmpleado).text.toString()


        if (nombre.isBlank() || puesto.isBlank() || numero == 0 || salario == 0.0 || iban.isBlank() || nombreBanco.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            return
        }


        val empleado = Empleado(nombre, numero.toString(), puesto, salario, iban, nombreBanco)
        listaEmpleados.add(empleado)


        Toast.makeText(this, "Empleado agregado: $nombre", Toast.LENGTH_SHORT).show()
    }
}


