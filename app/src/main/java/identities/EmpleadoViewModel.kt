package identities
import androidx.lifecycle.ViewModel



class EmpleadoViewModel : ViewModel() {


        private val listaEmpleados = mutableListOf<Empleado>()


        fun obtenerEmpleado(): Empleado {

            return Empleado("Juan Pérez", "12345", "Gerente", 50000.0, "ES123456789", "Banco ABC")
        }


        fun actualizarEmpleado(empleado: Empleado) {

            val index = listaEmpleados.indexOfFirst { it.numero == empleado.numero }
            if (index >= 0) {

                listaEmpleados[index] = empleado
            } else {
                listaEmpleados.add(empleado)
            }
        }


        fun eliminarEmpleado(numeroEmpleado: String) {
            listaEmpleados.removeAll { it.numero == numeroEmpleado }
        }


        fun obtenerListaEmpleados(): List<Empleado> {
            return listaEmpleados
        }

    companion object
}

