package cr.ac.utn.movil

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import model.nav_model
import cr.ac.utn.appmovil.util.util
const val EXTRA_MESSAGE_CONTACT_ID = "cr.ac.utn.appmovil.nav_ContainerNum"

class nav_list : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val model = nav_model(this)
        val lstcContain = findViewById<ListView>(R.id.listContainers)

        val containerList = model.getContainerAll()


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, containerList.map { it.Id })

        lstcContain.adapter = adapter

        lstcContain.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val itemValue = containerList[position]
                util.openActivity(
                    this,
                    nav_Container_control::class.java,
                    EXTRA_MESSAGE_CONTACT_ID,
                    itemValue.Id
                )
            }
    }
}
