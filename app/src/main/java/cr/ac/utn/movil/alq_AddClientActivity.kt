package cr.ac.utn.movil

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class alq_AddClientActivity : AppCompatActivity() {
    private val alq_tipoVehiculo: Any
    private val alq_apellidoCliente: Any
    private val aql_nombreCliente: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alq_add_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

            private lateinit var binding: alq_AddClientActivity
            private val alquilerDao = AppDatabase.getDatabase(this).clienteDao()

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = alq_AddClientActivity.inflate(layoutInflater)
                setContentView(binding.root)

                binding.guardarAlquilerBtn.setOnClickListener { guardarAlquiler() }
                binding.cancelarBtn.setOnClickListener { finish() }
            }

            private fun guardarAlquiler() {
                val nombre = binding.aql_nombreCliente.text.toString()
                val apellido = binding.alq_apellidoCliente.text.toString()
                val tipoVehiculo = binding.alq_tipoVehiculo.selectedItem.toString()
                val placa = binding.alq_placaVehiculo.text.toString()
                val kilometraje = binding.alq_kilometraje.text.toString().toIntOrNull()
                val licencia = binding.alq_licenciaConductor.text.toString()

                // Validar campos
                if (nombre.isBlank() || apellido.isBlank() || placa.isBlank() || kilometraje == null || licencia.isBlank()) {
                    Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                // Crear objeto cliente
                val cliente = Client(
                    id = 0, // Room generará un ID automáticamente
                    nombre = nombre,
                    apellido = apellido,
                    tipoVehiculo = tipoVehiculo,
                    placa = placa,
                    kilometraje = kilometraje,
                    licencia = licencia
                )

                // Insertar en la base de datos en un hilo de fondo
                CoroutineScope(Dispatchers.IO).launch {
                    if (alquilerDao.obtenerPorPlaca(placa) != null) {
                        runOnUiThread {
                            Toast.makeText(
                                this@,
                                "La placa ya está registrada",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        alquilerDao.insert(cliente)
                        runOnUiThread {
                            Toast.makeText(
                                this@AddAlquilerActivity,
                                "Alquiler guardado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish() // Cerrar la actividad después de guardar
                        }
                    }
                }
            }
        }
    }

