package cr.ac.utn.movil

import Adapter.listAdapter
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import identities.nav_container
import model.nav_model
import cr.ac.utn.appmovil.util.util

class nav_custom_list : AppCompatActivity() {
    lateinit var contaMod: nav_model
    lateinit var listCustom: List<nav_container>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_custom_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        contaMod= nav_model(this)
        val lstCustomList = findViewById<ListView>(R.id.nav_ListCust)
        listCustom = contaMod.getContainerAll().filterIsInstance<nav_container>()


        val adapter = listAdapter(this, R.layout.nav_item_list, listCustom)
        lstCustomList.adapter= adapter

            lstCustomList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val containerNum = listCustom[position].containerNum
                util.openActivity(
                    this,
                    nav_Container_control::class.java,
                    EXTRA_MESSAGE_CONTACT_ID,
                    containerNum
                )
            }

    }
}