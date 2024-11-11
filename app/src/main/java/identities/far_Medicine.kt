package identities

import identities.Identifier

class far_Medicine: Identifier {
    private var _name: String = ""
    private var _price: Int = 0
    private var _quantity: Int = 0

    constructor()

    constructor(name: String, price: Int, quantity: Int) {
        this._name = name
        this._price = price
        this._quantity = quantity
    }

    override val FullDescription: String
        get() = this._name + " " + this._quantity + " " + this.price

    var name: String
        get() = this._name
        set(value) {this._name = value}

    var price: Int
        get() = this._price
        set(value) {this._price = value}

    var quantity: Int
        get() = this._quantity
        set(value) {this._quantity = value}
}