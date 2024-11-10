package cr.ac.utn.appmovil.vuelos

import cr.ac.utn.appmovil.identities.Persona

class vul_Person : Persona {
    var vul_destinationCountry: String = ""
    var vul_flightNumber: String = ""
    var vul_flightDate: String = ""
    var vul_flightTime: String = ""

    constructor() : super()

    constructor(
        id: String,
        name: String,
        lastName: String,
        phone: Int,
        email: String,
        address: String,
        country: String,
        destinationCountry: String,
        flightNumber: String,
        flightDate: String,
        flightTime: String
    ) : super(id, name, lastName, phone, email, address, country) {
        this.vul_destinationCountry = destinationCountry
        this.vul_flightNumber = flightNumber
        this.vul_flightDate = flightDate
        this.vul_flightTime = flightTime
    }

    val FlightDescription: String
        get() = "Flight: $vul_flightNumber from $Country to $vul_destinationCountry on $vul_flightDate at $vul_flightTime"
}
