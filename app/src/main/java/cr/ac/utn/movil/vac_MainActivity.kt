package cr.ac.utn.movil

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID

import cr.ac.utn.appmovil.util.util

class vac_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vac_main)

        val btnViewRecords: Button = findViewById(R.id.vac_btnViewRecords)
        val btnAddRecord: Button = findViewById(R.id.vac_btnAddRecord)

        btnViewRecords.setOnClickListener {
            util.openActivity(this, vac_ListActivity::class.java, EXTRA_MESSAGE_ID, "")
        }

        btnAddRecord.setOnClickListener {

            util.openActivity(this, vac_FormActivity::class.java)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vac_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_add_vaccination_record -> {
                util.openActivity(this, vac_FormActivity::class.java)
                true
            }
            R.id.mnu_vaccination_record_list -> {
                util.openActivity(this, vac_ListActivity::class.java, EXTRA_MESSAGE_ID, "")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
