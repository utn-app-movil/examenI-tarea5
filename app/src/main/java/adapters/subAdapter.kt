package adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.movil.R
import identities.Identifier
import identities.sub_datas

class subAdapter(private val context: Context,
                 private val resource: Int,
                 private val datasource: List<Identifier>):
    ArrayAdapter<Identifier>(context, resource, datasource){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return datasource.size
    }
    override fun getView (position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.subcustom, parent, false)
        val subname = rowView.findViewById(R.id.sub_name_txt) as TextView
        val sublnam = rowView.findViewById(R.id.sub_lst_name_txt) as TextView
        val subsell = rowView.findViewById(R.id.sub_pointerselled) as TextView
        val image = rowView.findViewById(R.id.subprofileresee) as ImageView

        val subUser = datasource[position] as sub_datas
        subname.text = subUser.persona.Name
        sublnam.text = subUser.persona.LastName
        subsell.text = subUser.adjuted
        image.setImageURI(Uri.parse(subUser.uri))


        return rowView
    }
}