package cr.ac.utn.movil

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.model.ema_EmailModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util

class ema_listEmail : AppCompatActivity() {
    private lateinit var emailModel: ema_EmailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ema_list_email)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailModel = ema_EmailModel()
        val lstEmail = findViewById<ListView>(R.id.lstEmaillist)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            emailModel.getEmails().map { it.Id }) // Suponiendo que quieres mostrar solo los IDs

        lstEmail.adapter = adapter

        // Configurar el listener de clics
        lstEmail.setOnItemClickListener { parent, view, position, id ->
            val selectedEmailId = emailModel.getEmails()[position].Id // Obtener el ID del correo seleccionado
            util.openActivity(this, ema_mailDetailActivity::class.java, EXTRA_MESSAGE_ID, selectedEmailId)
        }
    }
}
