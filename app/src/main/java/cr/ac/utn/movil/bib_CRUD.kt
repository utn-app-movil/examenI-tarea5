package cr.ac.utn.movil

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cr.ac.utn.appmovil.identities.bib_Reservation
import cr.ac.utn.appmovil.model.bib_ReservationModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import java.io.File
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
    private lateinit var imgReservationPhoto: ImageView
    private lateinit var btnSelectPhoto: Button
    private var isEditionMode: Boolean = false
    private var reservationId: String? = null

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val reservationModel = bib_ReservationModel()

    private var photoUri: Uri? = null
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

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
        imgReservationPhoto = findViewById(R.id.bib_reservation_image)
        btnSelectPhoto = findViewById(R.id.bib_btn_select_photo)

        btnSaveReservation.setOnClickListener {
            saveReservation()
        }

        btnSelectPhoto.setOnClickListener {
            checkPermissionsAndProceed()
        }

        initializeActivityResultLaunchers()
        setupDatePickers()

        reservationId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (!reservationId.isNullOrEmpty()) {
            loadReservation(reservationId!!)
            isEditionMode = true
        }
    }

    private fun initializeActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = photoUri
            if (success && uri != null) {
                imgReservationPhoto.setImageURI(uri)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                photoUri = uri
                imgReservationPhoto.setImageURI(uri)
            }
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var allPermissionsGranted = true

            permissions.entries.forEach { permission ->
                val isGranted = permission.value
                if (!isGranted) {
                    allPermissionsGranted = false
                    Toast.makeText(this, getString(R.string.permission_denied, permission.key), Toast.LENGTH_SHORT).show()
                }
            }

            if (allPermissionsGranted) {
                showImageSelectionDialog()
            } else {
                Toast.makeText(this, getString(R.string.permissions_required), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionsAndProceed() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
            Log.d("Permissions", getString(R.string.camera_permission_not_granted))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
                Log.d("Permissions", getString(R.string.read_media_permission_not_granted))
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                Log.d("Permissions", getString(R.string.read_storage_permission_not_granted))
            }
        }

        if (permissions.isNotEmpty()) {
            Log.d("Permissions", getString(R.string.requesting_permissions, permissions.toString()))
            requestPermissionLauncher.launch(permissions.toTypedArray())
        } else {
            Log.d("Permissions", getString(R.string.all_permissions_granted))
            showImageSelectionDialog()
        }
    }


    private fun showImageSelectionDialog() {
        val options = arrayOf(getString(R.string.select_from_gallery), getString(R.string.take_photo))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_option))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> selectImageFromGallery()
                1 -> takePhotoWithCamera()
            }
        }
        builder.show()
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        galleryLauncher.launch("image/*")
    }

    private fun takePhotoWithCamera() {
        val photoFile = createImageFile()
        val uri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )
        photoUri = uri
        cameraLauncher.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timestamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bib_crud_menu, menu)
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
            val id = reservationId ?: UUID.randomUUID().toString()
            val reservation = bib_Reservation(
                id = id,
                studentName = txtStudentName.text.toString(),
                bookCode = txtBookCode.text.toString(),
                bookName = txtBookName.text.toString(),
                reservationDate = dateFormat.parse(txtReservationDate.text.toString()) ?: Date(),
                returnDate = dateFormat.parse(txtReturnDate.text.toString()) ?: Date(),
                libraryLocation = txtLibraryLocation.text.toString(),
                photoUri = photoUri?.toString() ?: ""
            )

            if (validateData(reservation)) {
                if (!isEditionMode) {
                    if (isDuplicate(reservation)) {
                        Toast.makeText(this, getString(R.string.reservation_duplicate), Toast.LENGTH_LONG).show()
                        return
                    }
                    reservationModel.addReservation(reservation)
                    Toast.makeText(this, getString(R.string.reservation_saved), Toast.LENGTH_LONG).show()
                } else {
                    reservationModel.updateReservation(reservation)
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
        val allReservations = reservationModel.getReservations()
        return allReservations.any {
            it.StudentName == reservation.StudentName &&
                    it.BookCode == reservation.BookCode &&
                    it.ReservationDate == reservation.ReservationDate
        }
    }

    private fun deleteReservation() {
        reservationId?.let {
            reservationModel.removeReservation(it)
            Toast.makeText(this, getString(R.string.reservation_deleted), Toast.LENGTH_LONG).show()
            finish()
        } ?: Toast.makeText(this, getString(R.string.reservation_not_found), Toast.LENGTH_LONG).show()
    }

    private fun loadReservation(id: String) {
        Log.d("bib_CRUD", getString(R.string.loading_reservation, id))
        val reservation = reservationModel.getReservation(id)
        if (reservation != null) {
            txtStudentName.setText(reservation.StudentName)
            txtBookCode.setText(reservation.BookCode)
            txtBookName.setText(reservation.BookName)
            txtReservationDate.setText(dateFormat.format(reservation.ReservationDate))
            txtReturnDate.setText(dateFormat.format(reservation.ReturnDate))
            txtLibraryLocation.setText(reservation.LibraryLocation)
            if (reservation.PhotoUri.isNotEmpty()) {
                val uri = Uri.parse(reservation.PhotoUri)
                photoUri = uri
                imgReservationPhoto.setImageURI(uri)
            }
            isEditionMode = true
            btnSaveReservation.text = getString(R.string.update_button_text)
        } else {
            Log.d("bib_CRUD", getString(R.string.reservation_not_found2, id))
            Toast.makeText(this, getString(R.string.reservation_not_found2, id), Toast.LENGTH_LONG).show()
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
