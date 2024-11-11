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
const val EXTRA_MESSAGE_CONTACT_ID = "com.blopix.myapplication.contactId"
class nav_contList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_cont_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nav_model = nav_model(this)
        val lstContact = findViewById<ListView>(R.id.lstContactList)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, nav_model.getContainerAll())

        lstContact.adapter = adapter
        lstContact.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val itemValue = lstContact.getItemAtPosition(position) as String
                util.openActivity(
                    this,
                    nav_Control_Container::class.java,
                    EXTRA_MESSAGE_CONTACT_ID,
                    itemValue
                )
            }
    }
}