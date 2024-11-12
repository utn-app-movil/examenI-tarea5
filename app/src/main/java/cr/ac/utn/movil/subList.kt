package cr.ac.utn.movil

import adapters.subAdapter
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util
import identities.sub_datas
import model.sub_model

class subList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var submodel: sub_model
        lateinit var contactList: List<sub_datas>
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sub_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        submodel = sub_model(this)
        val lstCustomList = findViewById<ListView>(R.id.subListLyout)
        contactList = submodel.getAll()

        val adapter =subAdapter(this, R.layout.subcustom, contactList)
        lstCustomList.adapter = adapter

        lstCustomList.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val idIdentifier = contactList[position].Id
            util.openActivity(this, sub_main::class.java, EXTRA_MESSAGE_ID, idIdentifier)
        }
    }
}