package cr.ac.utn.movil

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util

class sin_MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sin_main)

        val btnViewSinpes: Button = findViewById(R.id.sin_btn_view_sinpes)
        val btnAddSinpe: Button = findViewById(R.id.sin_btn_add_sinpe)

        btnViewSinpes.setOnClickListener {
            util.openActivity(this, sin_ListaActivity::class.java, EXTRA_MESSAGE_ID, "")
        }

        btnAddSinpe.setOnClickListener {
            util.openActivity(this, sin_CRUD::class.java, EXTRA_MESSAGE_ID, "")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sin_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_addSinpe -> {
                util.openActivity(this, sin_CRUD::class.java, EXTRA_MESSAGE_ID, "")
                true
            }
            R.id.mnu_viewSinpeList -> {
                util.openActivity(this, sin_ListaActivity::class.java, EXTRA_MESSAGE_ID, "")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
