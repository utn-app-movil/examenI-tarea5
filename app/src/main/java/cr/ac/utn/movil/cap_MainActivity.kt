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

class cap_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cap_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para abrir cap_AddActivity
        val btnLlamarCapAddActivity: Button = findViewById<Button>(R.id.cap_btnAddTraining)
        btnLlamarCapAddActivity.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, cap_AddActivity::class.java)
        })

        // Botón para abrir cap_ListActivity
        val btnLlamarCapListActivity: Button = findViewById<Button>(R.id.cap_btnListTraning)
        btnLlamarCapListActivity.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, cap_ListActivity::class.java)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cap_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cap_mnu_AddTraining -> {
                util.openActivity(this, cap_AddActivity::class.java)
                true
            }
            R.id.cap_mnu_ListTraining-> {
                util.openActivity(this, cap_ListActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}