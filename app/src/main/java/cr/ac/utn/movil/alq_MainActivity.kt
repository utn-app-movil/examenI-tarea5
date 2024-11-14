package cr.ac.utn.movil

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class alq_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alq_main)
        package com.example.rentafacil
        // Navegar a la pantalla de lista de clientes
        binding.verListaClientesBtn.setOnClickListener {
            startActivity(Intent(this, alq_ClientListActivity::class.java))
                }
        // Navegar a la pantalla de añadir nuevo alquiler
        binding.anadirNuevoAlquilerBtn.setOnClickListener {
            startActivity(Intent(this, alq_AddClientActivity::class.java))
                }
            }
        }







