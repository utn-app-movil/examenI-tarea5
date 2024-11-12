package cr.ac.utn.movil

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.util

class eve_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eve_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para abrir eve_AddActivity
        val btnLlamarEveAddActivity: Button = findViewById<Button>(R.id.eve_btnAddEvent)
        btnLlamarEveAddActivity.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, eve_AddActivity::class.java)
        })

        // Botón para abrir eve_ListActivity
        val btnLlamarEveListActivity: Button = findViewById<Button>(R.id.eve_btnListEvent)
        btnLlamarEveListActivity.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, eve_ListActivity::class.java)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.eve_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eve_mnu_AddEvent -> {
                util.openActivity(this, eve_AddActivity::class.java)
                true
            }
            R.id.eve_mnu_ListEvent-> {
                util.openActivity(this, eve_ListActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}