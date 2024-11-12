package cr.ac.utn.movil

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util

class bib_MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bib_main)

        val btnViewReservations: Button = findViewById(R.id.bib_btn_view_reservations)
        val btnAddReservation: Button = findViewById(R.id.bib_btn_add_reservation)

        btnViewReservations.setOnClickListener {
            util.openActivity(this, bib_Lista::class.java, EXTRA_MESSAGE_ID, "")
        }

        btnAddReservation.setOnClickListener {
            util.openActivity(this, bib_CRUD::class.java, EXTRA_MESSAGE_ID, "")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bib_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_add -> {
                util.openActivity(this, bib_CRUD::class.java)
                true
            }
            R.id.mnu_view -> {
                util.openActivity(this, bib_Lista::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
