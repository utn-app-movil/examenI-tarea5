package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.movil.R
import cr.ac.utn.appmovil.identities._invProduct
import kotlinx.android.synthetic.main.activity_inventory.view.inv_productName
import kotlinx.android.synthetic.main.activity_inventory.view.inv_quantity
import kotlinx.android.synthetic.main.activity_inventory.view.inv_supplierName
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(private val products: List<_invProduct>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: _invProduct) {
            itemView.inv_productName.text = product.productName
           itemView.inv_quantity.text = "quantity: ${product.quantity}"
            itemView.inv_supplierName.text = "supplier: ${product.supplierName}"

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
