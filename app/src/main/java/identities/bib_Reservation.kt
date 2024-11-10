package cr.ac.utn.appmovil.identities

import identities.Identifier
import java.util.Date

class bib_Reservation(
    id: String = java.util.UUID.randomUUID().toString(),
    studentName: String,
    bookCode: String,
    bookName: String,
    reservationDate: Date,
    returnDate: Date,
    libraryLocation: String,
    photoUri: String
) : Identifier() {

    init {
        this.Id = id
    }

    var StudentName: String = studentName
    var BookCode: String = bookCode
    var BookName: String = bookName
    var ReservationDate: Date = reservationDate
    var ReturnDate: Date = returnDate
    var LibraryLocation: String = libraryLocation
    var PhotoUri: String = photoUri

    override val FullDescription: String
        get() = "$StudentName reserved $BookName on $ReservationDate"
}
