package identities

abstract class Identifier {
    private var _id: String =""

    open var Id: String
        get() = this._id
        set(value) {this._id = value}

    abstract val FullDescription: String
}