package Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.movil.R
import identities.nav_container

class listAdapter(private val context: Context, private var resource: Int, private var datasource: List<nav_container>):
    ArrayAdapter<nav_container>(context, resource, datasource){

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Inflar
        val rowView = inflater.inflate(R.layout.nav_item_list, parent, false)


        val nav_ContainerNum = rowView.findViewById<TextView>(R.id.item_txtContainerNum)
        val nav_Person = rowView.findViewById<TextView>(R.id.item_txtNamePer)
        val nav_day = rowView.findViewById<TextView>(R.id.item_txtDay)
        val nav_Temp = rowView.findViewById<TextView>(R.id.item_txtTemp)
        val nav_Weight = rowView.findViewById<TextView>(R.id.item_txtWeight)
        val nav_Product = rowView.findViewById<TextView>(R.id.item_txtProduct)
        val nav_spinner = rowView.findViewById<TextView>(R.id.item_txtSpinner)
        val nav_imgPhoto = rowView.findViewById<ImageView>(R.id.imgitem_foto)

        val container = datasource[position] as nav_container


        nav_ContainerNum.text = container.containerNum
        nav_Person.text = container.namePer.toString()
        nav_Temp.text = container.weight.toString()
        nav_Product.text = container.product.toString()
        nav_Weight.text = container.weight.toString()
        nav_day.text = container.day.toString()
        nav_spinner.text = container.spinner.toString()


        val imgUri = Uri.parse(container.imageUri)
        nav_imgPhoto.setImageURI(imgUri)
        return rowView
    }

}