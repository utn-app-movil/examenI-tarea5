package identities

abstract class Identifier {
    abstract val lic_medicalReportCodesss: Any
    private var _id: String =""

    var Id: String
        get() = this._id
        set(value) {this._id = value}

    abstract val FullDescription: String
}