package cr.ac.utn.movil

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.view.View
import android.widget.Button

class eve_MainActivity: AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eve_main)

        //Llamados de pantalla
        val btnadd: Button = findViewById<Button>(R.id.btnAdd)
        btnadd.setOnClickListener(View.OnClickListener { view ->
            openActivity(eve_AddActivity::class.java)

        })

        val btnview: Button = findViewById<Button>(R.id.btnView)
        btnview.setOnClickListener(View.OnClickListener { view ->
            openActivity(eve_ViewActivity::class.java)
        })


        // Inicializando los botones
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val btnView: Button = findViewById(R.id.btnView)

        // Configuración del botón btnAdd para ir a Eve_AddActivity
        btnAdd.setOnClickListener(View.OnClickListener {
            openActivity(eve_AddActivity::class.java)
        })

        // Configuración del botón btnView para ir a Eve_ViewActivity
        btnView.setOnClickListener(View.OnClickListener {
            openActivity(eve_ViewActivity::class.java)
        })
    }

    // Función para abrir cualquier actividad
    fun openActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

}
