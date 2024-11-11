package identities

import java.nio.DoubleBuffer
import java.util.Date

class tcam_TypeChange(
    id: String,
    private var _fullName: String,
    private var _valueChange: Double,
    private var _valueAmount: Double,
    private var _totalAmount: Double,
    private var _dateTime: Date
) : Identifier() {

    // Inicializa el ID en el constructor primario usando la propiedad heredada `Id`
    init {
        this.Id = id
    }

    // Implementación de la propiedad abstracta FullDescription
    override val FullDescription: String
        get() = "Transaction for $_fullName on $_dateTime with value change rate: $_valueChange"

    // Getters y Setters
    var fullName: String
        get() = this._fullName
        set(value) {
            this._fullName = value
        }

    var valueChange: Double
        get() = this._valueChange
        set(value) {
            this._valueChange = value
        }

    var valueAmount: Double
        get() = this._valueAmount
        set(value) {
            this._valueAmount = value
        }

    var totalAmount: Double
        get() = this._totalAmount
        set(value) {
            this._totalAmount = value
        }

    var dateTime: Date
        get() = this._dateTime
        set(value) {
            this._dateTime = value
        }
}
