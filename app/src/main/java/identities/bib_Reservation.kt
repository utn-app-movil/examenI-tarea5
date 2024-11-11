package cr.ac.utn.appmovil.identities

import identities.Identifier
import java.util.Date

class bib_Reservation : Identifier {
    var StudentName: String = ""
    var BookCode: String = ""
    var BookName: String = ""
    var ReservationDate: Date = Date()
    var ReturnDate: Date = Date()
    var LibraryLocation: String = ""

    constructor(
        id: String,
        studentName: String,
        bookCode: String,
        bookName: String,
        reservationDate: Date,
        returnDate: Date,
        libraryLocation: String
    ) {
        this.Id = id
        this.StudentName = studentName
        this.BookCode = bookCode
        this.BookName = bookName
        this.ReservationDate = reservationDate
        this.ReturnDate = returnDate
        this.LibraryLocation = libraryLocation
    }

    override val FullDescription: String
        get() = "$StudentName reserved $BookName on $ReservationDate"
}
