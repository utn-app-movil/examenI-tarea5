package cr.ac.utn.movil

import Adapters.vul_PersonListAdapter
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import model.vul_PersonModel
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.movil.R
import identities.vul_Person

class vul_CustomListActivity : AppCompatActivity() {
    lateinit var personModel: vul_PersonModel
    lateinit var personList: List<vul_Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vul_activity_custom_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        personModel = vul_PersonModel(this)
        val lstCustomList = findViewById<ListView>(R.id.lstCustomContactList)
        personList = personModel.getPersons()
        val adapter = vul_PersonListAdapter(this, R.layout.vul_person_item_list, personList)
        lstCustomList.adapter = adapter

        lstCustomList.onItemClickListener = AdapterView.OnItemClickListener{ adapter, view, position, id ->
            val fullName = personList[position].FullDescription
            util.openActivity(this, vul_PersonActivity::class.java, EXTRA_MESSAGE_ID, fullName)
        }
    }


}