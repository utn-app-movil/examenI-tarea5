package identities

import android.graphics.Bitmap

class vul_Person(
    id: String,
    private var _name: String = "",
    private var _lastName: String = "",
    private var _phone: Int = 0,
    private var _email: String = "",
    private var _address: String = "",
    private var _country: String = "",
    private var _desntinationCountry: String = "",
    private var _flightnumber: String = "",
    private var _flighdate: String = "",
    var photoBitmap: Bitmap? = null // Cambiar de URI a Bitmap
) : Identifier() {

    init {
        this.Id = id
    }

    override val FullDescription: String
        get() = "$_desntinationCountry - $_flightnumber ($_flighdate date)"

    var name: String
        get() = this._name
        set(value) { this._name = value }

    var lastName: String
        get() = this._lastName
        set(value) { this._lastName = value }

    val fullName: String
        get() = "$_name $_lastName"

    var phone: Int
        get() = this._phone
        set(value) { this._phone = value }

    var email: String
        get() = this._email
        set(value) { this._email = value }

    var address: String
        get() = this._address
        set(value) { this._address = value }

    var country: String
        get() = this._country
        set(value) { this._country = value }

    var desntinationCountry: String
        get() = this._desntinationCountry
        set(value) { this._desntinationCountry = value }

    var flightnumber: String
        get() = this._flightnumber
        set(value) { this._flightnumber = value }

    var flighdate: String
        get() = this._flighdate
        set(value) { this._flighdate = value }
}
