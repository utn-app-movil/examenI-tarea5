package cr.ac.utn.movil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import identities.tcam_TypeChange

class TransactionAdapter(private val context: Context, private val transactions: List<tcam_TypeChange>) : BaseAdapter() {

    override fun getCount(): Int {
        return transactions.size
    }

    override fun getItem(position: Int): Any {
        return transactions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_tcam_custom_list, parent, false)

        val transaction = getItem(position) as tcam_TypeChange

        val txtFullName = view.findViewById<TextView>(R.id.tcam_txtFullName)
        val txtExchangeRate = view.findViewById<TextView>(R.id.tcam_ExchangeRate)
        val txtTotalAmount = view.findViewById<TextView>(R.id.tcam_txtAmountToPay)
        val txtDateTime = view.findViewById<TextView>(R.id.tcam_txtDateTime)

        txtFullName.text = transaction.fullName
        txtExchangeRate.text = "Tipo de cambio: ${transaction.valueChange}"
        txtTotalAmount.text = "Monto total: ${transaction.totalAmount}"
        txtDateTime.text = transaction.dateTime.toString() // Formatea según sea necesario

        return view
    }
}
