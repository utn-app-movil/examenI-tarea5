package identities

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

data class ConContribution(
    override var Id: String,
    var con_name: String,
    var con_totalcontributions: Int,
    var con_day: Int,
    var con_year: Int
) : Identifier(), Parcelable {
    val con_date: LocalDate = LocalDate.of(con_year, 11, con_day)

    init {
        require(con_totalcontributions >= 0) { "Total de contribuciones no puede ser negativo." }
        require(con_day in 1..31) { "El día debe estar entre 1 y 31." }
        require(con_year > 0) { "El año debe ser positivo." }
    }

    override val FullDescription: String
        get() = "Contribución ID: $Id, Nombre: $con_name, Total: $con_totalcontributions, Fecha: ${formattedDate()}"

    fun formattedDate(): String {
        return "${con_date.dayOfMonth}/${con_date.monthValue}/${con_date.year}"
    }

    // Implementación de Parcelable
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Id)
        parcel.writeString(con_name)
        parcel.writeInt(con_totalcontributions)
        parcel.writeInt(con_day)
        parcel.writeInt(con_year)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConContribution> {
        override fun createFromParcel(parcel: Parcel): ConContribution {
            return ConContribution(parcel)
        }

        override fun newArray(size: Int): Array<ConContribution?> {
            return arrayOfNulls(size)
        }
    }
}
