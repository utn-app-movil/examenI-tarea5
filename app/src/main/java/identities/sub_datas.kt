package identities
import cr.ac.utn.appmovil.identities.Persona
class sub_datas : Identifier {
    private lateinit var _persona: Persona
    private lateinit var _hour: String
    private lateinit var _date: String
    private lateinit var _amount: String
    private lateinit var _code: String
    private lateinit var _description: String
    private lateinit var _adjuted: String
    private lateinit var _uri: String

    constructor(
        persona: Persona,
        hour: String,
        date: String,
        amount: String,
        code: String,
        description: String,
        adjuted: String,
        uri: String
    ) {
        this._persona = persona
        this._hour = hour
        this._date = date
        this._amount = amount
        this._code = code
        this._description = description
        this._adjuted = adjuted
        this._uri = uri
    }

    constructor()

    var persona: Persona
        get() = this._persona
        set(value) { this._persona = value }

    var hour: String
        get() = this._hour
        set(value) { this._hour = value }

    var uri: String
        get() = this._uri
        set(value) { this._uri = value }

    var date: String
        get() = this._date
        set(value) { this._date = value }

    var amount: String
        get() = this._amount
        set(value) { this._amount = value }

    var code: String
        get() = this._code
        set(value) { this._code = value }

    var description: String
        get() = this._description
        set(value) { this._description = value }

    var adjuted: String
        get() = this._adjuted
        set(value) { this._adjuted = value }

    override val FullDescription: String
        get() = "${_persona.Name} and more"
}
