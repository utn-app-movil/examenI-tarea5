package cr.ac.utn.appmovil.identities


import identities.Identifier

class _invProduct(
    id: String,
    var productCode: String,
    var productName: String,
    var quantity: Int,
    var date: String,
    var supplierName: String,
    var unitCost: Double,
    var imagePath: String?
) : Identifier() {

    init {
        this.Id = id
    }

    override val FullDescription: String
        get() = "$productName - $productCode"
}
