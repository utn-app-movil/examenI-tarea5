package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.utn.appmovil.vuelos.vul_Person
import cr.ac.utn.movil.R

class vul_PersonListAdapter(private val context: Context, private val resource: Int, private val datasource: List<vul_Person>): ArrayAdapter<vul_Person>(context, resource, datasource){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.vul_person_item_list, parent, false)
        val  lbPhone = rowView.findViewById(R.id.lbItemPhone) as TextView
        val  lbFullname = rowView.findViewById(R.id.lbItemFullName) as TextView
        val  lbEmail = rowView.findViewById(R.id.lbItemEmail) as TextView

        val contact = datasource[position] as vul_Person
        lbPhone.text = contact.Phone.toString()
        lbFullname.text = contact.FullDescription
        lbEmail.text = contact.Email

        return rowView
    }
}