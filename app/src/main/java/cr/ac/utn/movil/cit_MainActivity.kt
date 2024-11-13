package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View

class cit_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cit_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.cit_btnRegister).setOnClickListener {
            // Iniciar la actividad cit_RegisterInformation al hacer clic
            val intent = Intent(this, cit_RegisterInformation::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.cit_btnModifyInformation).setOnClickListener {
            // Iniciar la actividad cit_ModifyInformation al hacer clic
            val intent = Intent(this, cit_ModifyInformation::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.cit_btnDeleteInformation).setOnClickListener {
            // Iniciar la actividad cit_DeleteInformation al hacer clic
            val intent = Intent(this, cit_DeleteInformation::class.java)
            startActivity(intent)
        }
    }
}
