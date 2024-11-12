package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.movil.R
import cr.ac.utn.appmovil.identities._invProduct

class ProductAdapter(private val products: List<_invProduct>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: _invProduct) {
            itemView.findViewById<TextView>(R.id.inv_productName).text = product.productName
            itemView.findViewById<TextView>(R.id.inv_quantity).text = "quantity: ${product.quantity}"
            itemView.findViewById<TextView>(R.id.inv_supplierName).text = "supplier: ${product.supplierName}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
