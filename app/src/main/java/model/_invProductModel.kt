package cr.ac.utn.appmovil.model

import identities.Identifier

import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities._invProduct


class _invProductModel {
    private val dbManager = MemoryManager

    fun addProduct(product: _invProduct): Boolean {
        if (dbManager.getByid(product.Id) != null) {
            return false
        }
        dbManager.add(product)
        return true
    }

    fun updateProduct(product: _invProduct) {
        dbManager.update(product)
    }

    fun deleteProduct(id: String) {
        dbManager.remove(id)
    }

    fun getProduct(id: String): _invProduct? {
        return dbManager.getByid(id) as? _invProduct
    }

    fun getAllProducts(): List<_invProduct> {
        return dbManager.getAll().filterIsInstance<_invProduct>()
    }
}