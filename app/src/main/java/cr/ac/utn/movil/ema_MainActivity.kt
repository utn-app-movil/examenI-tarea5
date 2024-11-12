package cr.ac.utn.movil

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ema_MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ema_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAddEmail: Button = findViewById<Button>(R.id.ema_btnmainadd)
        btnAddEmail.setOnClickListener(View.OnClickListener { view ->

            val intentAddEmail = Intent(this, ema_EmailActivity::class.java)
            startActivity(intentAddEmail)

        })

        val btnviewEmail: Button = findViewById<Button>(R.id.ema_btnmainview)
        btnviewEmail.setOnClickListener(View.OnClickListener { view ->

            val intentViewEmail = Intent(this, ema_listEmail::class.java)
            startActivity(intentViewEmail)

        })


    }



}