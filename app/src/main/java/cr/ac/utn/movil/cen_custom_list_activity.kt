package cr.ac.utn.movil

import Adapter.Cen_Adapter
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import identities.Cen_Persona
import model.Cen_model
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID

class cen_custom_list_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        lateinit var Cen_model: Cen_model
        lateinit var cencontact : List<Cen_Persona>



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cen_custom_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cen_model = Cen_model(this)
        val Contactlistview =  findViewById<ListView>(R.id.Contactlistwiew)
        cencontact = cen_model.getContacts()


        val adapter = Cen_Adapter(this, R.layout.activity_cen_custom_list,cencontact)
        Contactlistview.adapter = adapter
        Contactlistview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> val Cname = cencontact[position].fullName
                util.openActivity(this, cen_add_persona_activity::class.java, EXTRA_MESSAGE_ID,Cname)

        }
    }
}

