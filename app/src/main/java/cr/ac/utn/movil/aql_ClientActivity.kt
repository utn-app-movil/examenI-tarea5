package cr.ac.utn.movil

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class aql_ClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aql_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


            package com.example.rentafacil

            import android.view.LayoutInflater
                    import android.view.ViewGroup
                    import androidx.recyclerview.widget.DiffUtil
                    import androidx.recyclerview.widget.ListAdapter
                    import androidx.recyclerview.widget.RecyclerView
                    import com.example.rentafacil.databinding.ItemClienteBinding

            class ClienteAdapter(
                private val onAction: (Cliente, String) -> Unit
            ) : ListAdapter<Cliente, ClienteAdapter.ClienteViewHolder>(ClienteDiffCallback()) {

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
                    val binding = ItemClienteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    return ClienteViewHolder(binding)
                }

                override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
                    holder.bind(getItem(position), onAction)
                }

                class ClienteViewHolder(private val binding: ItemClienteBinding) : RecyclerView.ViewHolder(binding.root) {
                    fun bind(cliente: Cliente, onAction: (Cliente, String) -> Unit) {
                        binding.nombreClienteTv.text = "${cliente.nombre} ${cliente.apellido}"
                        binding.tipoVehiculoTv.text = cliente.tipoVehiculo
                        binding.placaTv.text = cliente.placa

                        binding.editarBtn.setOnClickListener { onAction(cliente, "editar") }
                        binding.eliminarBtn.setOnClickListener { onAction(cliente, "eliminar") }
                    }
                }
            }

            class ClienteDiffCallback : DiffUtil.ItemCallback<Cliente>() {
                override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente): Boolean = oldItem.id == newItem.id
                override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente): Boolean = oldItem == newItem
            }

        }
    }
}