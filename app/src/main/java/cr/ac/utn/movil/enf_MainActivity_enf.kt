package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class enf_MainActivity_enf : AppCompatActivity() {

    private lateinit var enf_btn_AddP: Button
    private lateinit var enf_btn_ViewP: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enf_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        enf_btn_AddP.setOnClickListener {
            val intent = Intent(this, activity_enf_main_enf::class.java)
            startActivity(intent)
        }

        enf_btn_ViewP.setOnClickListener {
            val intent = Intent(this, activity_enf_view::class.java)
            startActivity(intent)
        }
    }
}

class AgregarPacienteActivity : AppCompatActivity() {

    private lateinit var et_id: EditText
    private lateinit var et_name: EditText
    private lateinit var et_surname: EditText
    private lateinit var enf_btn_save: Button
    private lateinit var enf_btn_back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enf_nurse)

        et_id = findViewById(R.id.et_id)
        et_name = findViewById(R.id.et_name)
        et_surname = findViewById(R.id.et_surname)
        enf_btn_save = findViewById(R.id.enf_btn_save)
        enf_btn_back = findViewById(R.id.enf_btn_back)

        enf_btn_save.setOnClickListener {
            val id = et_id.text.toString()
            val nombre = et_name.text.toString()
            val apellido = et_surname.text.toString()
            guardarPaciente(id, nombre, apellido)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        enf_btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun guardarPaciente(id: String, nombre: String, apellido: String) {

    }
}

class VerPacientesActivity : AppCompatActivity() {
    private lateinit var enf_btn_add_patient: Button
    private lateinit var enf_btn_back2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enf_view)

        enf_btn_add_patient = findViewById(R.id.enf_btn_add_patient)
        enf_btn_back2 = findViewById(R.id.enf_btn_back2)


        enf_btn_add_patient.setOnClickListener {
            val intent = Intent(this, AgregarPacienteActivity::class.java)
            startActivity(intent)
        }

        enf_btn_back2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}


