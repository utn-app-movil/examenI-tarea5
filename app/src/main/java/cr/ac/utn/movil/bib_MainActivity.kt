package cr.ac.utn.appmovil

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.movil.R
import cr.ac.utn.movil.bib_CRUD
import cr.ac.utn.movil.bib_Lista

class bib_MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bib_main)

        val btnViewReservations: Button = findViewById(R.id.bib_btn_view_reservations)
        val btnAddReservation: Button = findViewById(R.id.bib_btn_add_reservation)

        btnViewReservations.setOnClickListener {
            util.openActivity(this, bib_Lista::class.java, EXTRA_MESSAGE_ID, null)
        }

        btnAddReservation.setOnClickListener {
            util.openActivity(this, bib_CRUD::class.java, EXTRA_MESSAGE_ID, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_add -> {
                util.openActivity(this, bib_CRUD::class.java, EXTRA_MESSAGE_ID, null)
                true
            }
            R.id.mnu_view -> {
                util.openActivity(this, bib_Lista::class.java, EXTRA_MESSAGE_ID, null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
