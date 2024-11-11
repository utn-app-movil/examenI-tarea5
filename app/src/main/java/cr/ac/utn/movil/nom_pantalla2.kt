package cr.ac.utn.movil

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import identities.Empleado
import identities.EmpleadoViewModel

class Nom_Pantalla2Activity : AppCompatActivity() {

    private lateinit var nombreEmpleado: EditText
    private lateinit var numeroEmpleado: EditText
    private lateinit var puestoEmpleado: EditText
    private lateinit var salarioEmpleado: EditText
    private lateinit var ibanEmpleado: EditText
    private lateinit var nombreBancoEmpleado: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nom_pantalla2)


        nombreEmpleado = findViewById(R.id.NombreEmpleado)
        numeroEmpleado = findViewById(R.id.NumeroEmpleado)
        puestoEmpleado = findViewById(R.id.PuestoEmpleado)
        salarioEmpleado = findViewById(R.id.SalarioEmpleado)
        ibanEmpleado = findViewById(R.id.IbanEmpleado)
        nombreBancoEmpleado = findViewById(R.id.NombreBancoEmpleado)

        empleadoViewModel = ViewModelProvider(this).get(EmpleadoViewModel::class.java)


        cargarDatosEmpleado()
    }


    private fun cargarDatosEmpleado() {



        val empleado = EmpleadoActivity.obtenerEmpleado()
        nombreEmpleado.setText(empleado.nombre)
        numeroEmpleado.setText(empleado.numero)
        puestoEmpleado.setText(empleado.puesto)
        salarioEmpleado.setText(empleado.salario.toString())
        ibanEmpleado.setText(empleado.iban)
        nombreBancoEmpleado.setText(empleado.banco)
    }


    fun onEditClick() {
        val nombre = nombreEmpleado.text.toString()
        val numero = numeroEmpleado.text.toString()
        val puesto = puestoEmpleado.text.toString()
        val salario = salarioEmpleado.text.toString().toDoubleOrNull() ?: 0.0
        val iban = ibanEmpleado.text.toString()
        val banco = nombreBancoEmpleado.text.toString()


        if (nombre.isNotEmpty() && numero.isNotEmpty()) {
            val empleado = Empleado.Empleado(nombre, numero, puesto, salario, iban, banco)
            EmpleadoViewModel.actualizarEmpleado(empleado)

            Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }


    fun onDeleteClick() {
        val numero = numeroEmpleado.text.toString()

        if (numero.isNotEmpty()) {
            EmpleadoViewModel.eliminarEmpleado(numero)
            Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Por favor ingrese un número de empleado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun limpiarCampos() {
        nombreEmpleado.text.clear()
        numeroEmpleado.text.clear()
        puestoEmpleado.text.clear()
        salarioEmpleado.text.clear()
        ibanEmpleado.text.clear()
        nombreBancoEmpleado.text.clear()
    }
}

private fun EmpleadoViewModel.Companion.actualizarEmpleado(empleado: Empleado.Empleado) {

}

private fun EmpleadoViewModel.Companion.eliminarEmpleado(numero: String) {

}
