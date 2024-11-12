package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class rec_MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_main)

        val registerButton = findViewById<Button>(R.id.rec_button_register)
        val listButton = findViewById<Button>(R.id.rec_button_list)

        registerButton.setOnClickListener {
            val intent = Intent(this, rec_RegisterActivity::class.java)
            startActivity(intent)
        }

        listButton.setOnClickListener {
            val intent = Intent(this, rec_ListActivity::class.java) // Asegúrate de que apunta a RecListActivity
            startActivity(intent)
        }
    }
}