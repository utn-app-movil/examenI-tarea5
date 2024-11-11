package Adapter

import identities.Cen_Persona
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.movil.R


class Cen_Adapter(private  val context: Context, private val resource: Int, private val  dataSource: List<Cen_Persona>)
    :ArrayAdapter<Cen_Persona>(context,resource,dataSource){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as  LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var view = inflater.inflate(R.layout.activity_cen_custom_menu, parent,false)
                var viewid = view.findViewById(R.id.Cen_CustomLID) as TextView
                var viewname = view.findViewById(R.id.Cen_CustomLName) as TextView
                var viewlastname = view.findViewById(R.id.Cen_CustomLILastName) as TextView
                var viewPhone = view.findViewById(R.id.Cen_CustomLIPhone) as TextView
                var viewEmail = view.findViewById(R.id.Cen_CustomLiEmail) as TextView
                var viewAddres = view.findViewById(R.id.Cen_CustomLIAdress) as TextView
                var viewProvince = view.findViewById(R.id.Cen_CustomProvince) as TextView
                var viewCanton = view.findViewById(R.id.Cen_CustomCanton) as TextView
                var viewDistrits = view.findViewById(R.id.Cen_CustomDistrits) as TextView
                var viewimage = view.findViewById(R.id.imageView3) as ImageView



        val Census = dataSource[position] as Cen_Persona
        viewid.text = Census.id
        viewname.text = Census.name
        viewlastname.text = Census.lastName
        viewPhone.text = Census.phone.toString()
        viewEmail.text = Census.email
        viewAddres.text = Census.address
        viewProvince.text = Census.province
        viewCanton.text = Census.canton
        viewDistrits.text = Census.distrits
        return view

    }
}   

