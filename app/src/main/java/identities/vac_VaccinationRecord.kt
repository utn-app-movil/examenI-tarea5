package identities

import java.util.Date

class vac_VaccinationRecord(
    id: String = java.util.UUID.randomUUID().toString(),
    name: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,
    country: String,
    vaccineType: String,
    vaccinationDate: Date,
    vaccinationTime: String,
    photoUri: String
) : Identifier() {

    init {
        Id = id
    }

    var Name: String = name
    var LastName: String = lastName
    var Phone: Int = phone
    var Email: String = email
    var Address: String = address
    var Country: String = country
    var VaccineType: String = vaccineType
    var VaccinationDate: Date = vaccinationDate
    var VaccinationTime: String = vaccinationTime
    var PhotoUri: String = photoUri

    override val FullDescription: String
        get() = "$Name $LastName - Vacuna: $VaccineType el $VaccinationDate a las $VaccinationTime"
}
