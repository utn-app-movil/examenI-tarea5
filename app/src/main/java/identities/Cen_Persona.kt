package identities

class Cen_Persona {
    private var _id: String =""
    private var _name: String =""
    private var _lastName: String=""
    private var _phone: Int = 0
    private var _email: String=""
    private var _address: String=""
    private var _province: String=""
    private var _canton: String=""
    private var _distrits: String=""


    constructor()

    constructor(id: String, name: String, lastName: String, phone: Int, email: String, address: String,
    province:String, canton:String, distrits: String ){
        this._id= id
        this._name= name
        this._lastName = lastName
        this._phone=phone
        this._email=email
        this._address= address
        this._province= province
        this._canton= canton
        this._distrits=distrits
    }

    var id: String
        get() = this._id
        set(value) {this._id = value}

    var name: String
        get() = this._name
        set(value) {this._name = value}

    var lastName: String
        get() = this._lastName
        set(value) {this._lastName = value}

    val fullName get() = this._name + " " + this._lastName

    var phone: Int
        get() = this._phone
        set(value) {this._phone = value}

    var email: String
        get() = this._email
        set(value) {this._email = value}

    var address: String
        get() = this._address
        set(value) {this._address = value}

    var province: String
        get() = this._province
        set(value) {this._province = value}

    var canton: String
        get() = this._canton
        set(value) {this._canton = value}

    var distrits: String
        get() = this._distrits
        set(value) {this._distrits = value}


}