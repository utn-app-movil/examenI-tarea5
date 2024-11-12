package cr.ac.utn.movil

import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.movil.R
import model.vul_PersonModel

class vul_PersonListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vul_activity_person_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val personModel = vul_PersonModel(this)
        val lstPerson = findViewById<ListView>(R.id.lstPersonList)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            personModel.getPersons())

        lstPerson.adapter = adapter

        lstPerson.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            val itemValue = lstPerson.getItemAtPosition(position) as String
            util.openActivity(this, vul_PersonActivity::class.java
                , EXTRA_MESSAGE_ID, itemValue)
        }
    }
}