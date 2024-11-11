package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.bib_Reservation
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class bib_CRUD : AppCompatActivity() {

    private lateinit var txtStudentName: EditText
    private lateinit var txtBookCode: EditText
    private lateinit var txtBookName: EditText
    private lateinit var txtReservationDate: EditText
    private lateinit var txtReturnDate: EditText
    private lateinit var txtLibraryLocation: EditText
    private lateinit var btnSaveReservation: Button
    private var isEditionMode: Boolean = false
    private var reservationId: String? = null

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bib_crud)

        txtStudentName = findViewById(R.id.bib_student_name)
        txtBookCode = findViewById(R.id.bib_book_code)
        txtBookName = findViewById(R.id.bib_book_name)
        txtReservationDate = findViewById(R.id.bib_reservation_date)
        txtReturnDate = findViewById(R.id.bib_return_date)
        txtLibraryLocation = findViewById(R.id.bib_library_location)
        btnSaveReservation = findViewById(R.id.bib_btn_save_reservation)

        btnSaveReservation.setOnClickListener {
            saveReservation()
        }

        setupDatePickers()

        reservationId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (reservationId != null) {
            loadReservation(reservationId!!)
            isEditionMode = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crud_menu, menu)
        menu?.findItem(R.id.mnu_delete)?.isEnabled = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveReservation()
                true
            }
            R.id.mnu_delete -> {
                deleteReservation()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveReservation() {
        try {
            val reservation = bib_Reservation(
                id = reservationId ?: UUID.randomUUID().toString(),
                studentName = txtStudentName.text.toString(),
                bookCode = txtBookCode.text.toString(),
                bookName = txtBookName.text.toString(),
                reservationDate = dateFormat.parse(txtReservationDate.text.toString()) ?: Date(),
                returnDate = dateFormat.parse(txtReturnDate.text.toString()) ?: Date(),
                libraryLocation = txtLibraryLocation.text.toString()
            )

            if (validateData(reservation)) {
                if (!isEditionMode) {
                    if (isDuplicate(reservation)) {
                        Toast.makeText(this, getString(R.string.reservation_duplicate), Toast.LENGTH_LONG).show()
                        return
                    }
                    MemoryManager.add(reservation)
                    Toast.makeText(this, getString(R.string.reservation_saved), Toast.LENGTH_LONG).show()
                } else {
                    MemoryManager.update(reservation)
                    Toast.makeText(this, getString(R.string.reservation_updated), Toast.LENGTH_LONG).show()
                }
                finish()
            }
        } catch (e: ParseException) {
            Toast.makeText(this, getString(R.string.invalid_date_format), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateData(reservation: bib_Reservation): Boolean {
        return when {
            reservation.StudentName.isBlank() -> {
                txtStudentName.error = getString(R.string.error_student_name)
                false
            }
            reservation.BookCode.isBlank() -> {
                txtBookCode.error = getString(R.string.error_book_code)
                false
            }
            reservation.BookName.isBlank() -> {
                txtBookName.error = getString(R.string.error_book_name)
                false
            }
            reservation.LibraryLocation.isBlank() -> {
                txtLibraryLocation.error = getString(R.string.error_library_location)
                false
            }
            reservation.ReservationDate.after(reservation.ReturnDate) -> {
                txtReturnDate.error = getString(R.string.error_return_date)
                false
            }
            else -> true
        }
    }

    private fun isDuplicate(reservation: bib_Reservation): Boolean {
        val allReservations = MemoryManager.getAll().filterIsInstance<bib_Reservation>()
        return allReservations.any {
            it.StudentName == reservation.StudentName &&
                    it.BookCode == reservation.BookCode &&
                    it.ReservationDate == reservation.ReservationDate
        }
    }

    private fun deleteReservation() {
        reservationId?.let {
            MemoryManager.remove(it)
            Toast.makeText(this, getString(R.string.reservation_deleted), Toast.LENGTH_LONG).show()
            finish()
        } ?: Toast.makeText(this, getString(R.string.reservation_not_found), Toast.LENGTH_LONG).show()
    }

    private fun loadReservation(id: String) {
        val reservation = MemoryManager.getByid(id) as? bib_Reservation
        if (reservation != null) {
            txtStudentName.setText(reservation.StudentName)
            txtBookCode.setText(reservation.BookCode)
            txtBookName.setText(reservation.BookName)
            txtReservationDate.setText(dateFormat.format(reservation.ReservationDate))
            txtReturnDate.setText(dateFormat.format(reservation.ReturnDate))
            txtLibraryLocation.setText(reservation.LibraryLocation)
            isEditionMode = true
            btnSaveReservation.text = getString(R.string.update_button_text)
        } else {
            Toast.makeText(this, getString(R.string.reservation_not_found), Toast.LENGTH_LONG).show()
        }
    }

    private fun setupDatePickers() {
        txtReservationDate.setOnClickListener {
            showDatePickerDialog(txtReservationDate)
        }
        txtReturnDate.setOnClickListener {
            showDatePickerDialog(txtReturnDate)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}
