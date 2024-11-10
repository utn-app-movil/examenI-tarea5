package identities

import android.graphics.Bitmap

/**
 * cap_Capacitacion: Clase que representa una capacitación.
 * Hereda de Identifier para ser compatible con MemoryManager.
 */
class cap_Training(
    id: String,
    private var _name: String = "",
    private var _lastName: String = "",
    private var _phone: Int = 0,
    private var _email: String = "",
    private var _address: String = "",
    private var _country: String = "",
    private var _courseCode: String = "",
    private var _description: String = "",
    private var _hours: Int = 0,
    private var _dateTime: String = "",
    var photoBitmap: Bitmap? = null // Cambiar de URI a Bitmap
) : Identifier() {

    init {
        this.Id = id
    }

    // Implementación de la propiedad requerida por Identifier
    override val FullDescription: String
        get() = "$_courseCode - $_description ($_hours hrs)"

    // Propiedades de Persona con getter y setter
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

    // Propiedades de Capacitación con métodos getter y setter
    var courseCode: String
        get() = this._courseCode
        set(value) { this._courseCode = value }

    var description: String
        get() = this._description
        set(value) { this._description = value }

    var hours: Int
        get() = this._hours
        set(value) { this._hours = value }

    var dateTime: String
        get() = this._dateTime
        set(value) { this._dateTime = value }
}
