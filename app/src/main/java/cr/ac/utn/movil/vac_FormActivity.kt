package cr.ac.utn.movil

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import identities.vac_VaccinationRecord
import model.vac_VaccinationModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class vac_FormActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtCountry: EditText
    private lateinit var txtVaccineType: EditText
    private lateinit var txtVaccinationDate: EditText
    private lateinit var txtVaccinationTime: EditText
    private lateinit var btnSaveVaccination: Button
    private lateinit var imgPhoto: ImageView
    private lateinit var btnSelectPhoto: Button

    private var isEditionMode: Boolean = false
    private var vaccinationId: String? = null
    private var photoUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private val vaccinationModel = vac_VaccinationModel()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es", "CR"))
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale("es", "CR"))

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var actionAfterPermissionGranted: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vac_form)

        // Inicializar componentes de UI
        txtName = findViewById(R.id.vac_name)
        txtLastName = findViewById(R.id.vac_last_name)
        txtPhone = findViewById(R.id.vac_phone)
        txtEmail = findViewById(R.id.vac_email)
        txtAddress = findViewById(R.id.vac_address)
        txtCountry = findViewById(R.id.vac_country)
        txtVaccineType = findViewById(R.id.vac_vaccine_type)
        txtVaccinationDate = findViewById(R.id.vac_vaccination_date)
        txtVaccinationTime = findViewById(R.id.vac_vaccination_time)
        imgPhoto = findViewById(R.id.vac_imgPhoto)
        btnSelectPhoto = findViewById(R.id.vac_btnSelectPhoto)
        btnSaveVaccination = findViewById(R.id.vac_btn_save_vaccination)

        btnSelectPhoto.setOnClickListener {
            showImageSelectionDialog()
        }

        btnSaveVaccination.setOnClickListener {
            saveVaccinationRecord()
        }

        initializeActivityResultLaunchers()
        setupDateAndTimePickers()

        vaccinationId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (!vaccinationId.isNullOrEmpty()) {
            loadVaccinationRecord(vaccinationId!!)
            isEditionMode = true
        }
    }

    private fun initializeActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imgPhoto.setImageURI(photoUri)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                photoUri = uri
                imgPhoto.setImageURI(uri)
            }
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                actionAfterPermissionGranted?.invoke()
            } else {
                Toast.makeText(this, getString(R.string.vac_permissions_required), Toast.LENGTH_SHORT).show()
            }
            actionAfterPermissionGranted = null
        }
    }

    private fun showImageSelectionDialog() {
        val options = arrayOf(getString(R.string.vac_select_from_gallery), getString(R.string.vac_take_photo))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.vac_choose_option))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkPermissionsAndProceedForGallery()
                1 -> checkPermissionsAndProceedForCamera()
            }
        }
        builder.show()
    }

    private fun checkPermissionsAndProceedForGallery() {
        val permissionsNeeded = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        if (permissionsNeeded.isNotEmpty()) {
            actionAfterPermissionGranted = ::selectImageFromGallery
            requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            selectImageFromGallery()
        }
    }

    private fun checkPermissionsAndProceedForCamera() {
        val permissionsNeeded = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (permissionsNeeded.isNotEmpty()) {
            actionAfterPermissionGranted = ::takePhotoWithCamera
            requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            takePhotoWithCamera()
        }
    }

    private fun selectImageFromGallery() {
        galleryLauncher.launch(arrayOf("image/*"))
    }

    private fun takePhotoWithCamera() {
        try {
            val photoFile = createImageFile()
            val uri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.provider",
                photoFile
            )
            photoUri = uri
            cameraLauncher.launch(uri)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.vac_error_creating_file), Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun saveVaccinationRecord() {
        try {
            val id = vaccinationId ?: UUID.randomUUID().toString()
            val vaccinationRecord = vac_VaccinationRecord(
                id = id,
                name = txtName.text.toString(),
                lastName = txtLastName.text.toString(),
                phone = txtPhone.text.toString().toIntOrNull() ?: 0,
                email = txtEmail.text.toString(),
                address = txtAddress.text.toString(),
                country = txtCountry.text.toString(),
                vaccineType = txtVaccineType.text.toString(),
                vaccinationDate = dateFormat.parse(txtVaccinationDate.text.toString()) ?: Date(),
                vaccinationTime = txtVaccinationTime.text.toString(),
                photoUri = photoUri?.toString() ?: ""
            )
            if (validateData(vaccinationRecord)) {
                if (isEditionMode) {
                    vaccinationModel.updateVaccinationRecord(vaccinationRecord)
                    Toast.makeText(this, getString(R.string.vac_vaccination_updated), Toast.LENGTH_LONG).show()
                } else {
                    vaccinationModel.addVaccinationRecord(vaccinationRecord)
                    Toast.makeText(this, getString(R.string.vac_vaccination_saved), Toast.LENGTH_LONG).show()
                }
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message ?: getString(R.string.vac_error_generic), Toast.LENGTH_LONG).show()
        }
    }

    private fun validateData(record: vac_VaccinationRecord): Boolean {
        var isValid = true
        val missingFields = mutableListOf<String>()

        if (record.Name.isBlank()) {
            txtName.error = getString(R.string.vac_error_name)
            missingFields.add(getString(R.string.vac_name_hint))
            isValid = false
        }
        if (record.LastName.isBlank()) {
            txtLastName.error = getString(R.string.vac_error_last_name)
            missingFields.add(getString(R.string.vac_last_name_hint))
            isValid = false
        }
        if (record.Email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(record.Email).matches()) {
            txtEmail.error = getString(R.string.vac_error_invalid_email)
            isValid = false
        }
        val phoneText = txtPhone.text.toString()
        if (phoneText.isBlank() || !phoneText.matches(Regex("\\d{8,10}"))) {
            txtPhone.error = getString(R.string.vac_error_invalid_phone)
            isValid = false
        }
        if (record.Address.isBlank()) {
            txtAddress.error = getString(R.string.vac_error_address)
            missingFields.add(getString(R.string.vac_address_hint))
            isValid = false
        }
        if (record.Country.isBlank()) {
            txtCountry.error = getString(R.string.vac_error_country)
            missingFields.add(getString(R.string.vac_country_hint))
            isValid = false
        }
        if (record.VaccineType.isBlank()) {
            txtVaccineType.error = getString(R.string.vac_error_vaccine_type)
            missingFields.add(getString(R.string.vac_vaccine_type_hint))
            isValid = false
        }
        if (txtVaccinationDate.text.isBlank()) {
            txtVaccinationDate.error = getString(R.string.vac_error_vaccination_date)
            missingFields.add(getString(R.string.vac_vaccination_date_hint))
            isValid = false
        }
        if (txtVaccinationTime.text.isBlank()) {
            txtVaccinationTime.error = getString(R.string.vac_error_vaccination_time)
            missingFields.add(getString(R.string.vac_vaccination_time_hint))
            isValid = false
        }
        if (!isValid) {
            val message = if (missingFields.isNotEmpty()) {
                getString(R.string.vac_error_missing_fields, missingFields.joinToString(", "))
            } else {
                getString(R.string.vac_error_invalid_input)
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
        return isValid
    }

    private fun loadVaccinationRecord(id: String) {
        val record = vaccinationModel.getVaccinationRecordById(id)
        if (record != null) {
            txtName.setText(record.Name)
            txtLastName.setText(record.LastName)
            txtPhone.setText(record.Phone.toString())
            txtEmail.setText(record.Email)
            txtAddress.setText(record.Address)
            txtCountry.setText(record.Country)
            txtVaccineType.setText(record.VaccineType)
            txtVaccinationDate.setText(dateFormat.format(record.VaccinationDate))
            txtVaccinationTime.setText(record.VaccinationTime)
            if (record.PhotoUri.isNotEmpty()) {
                val uri = Uri.parse(record.PhotoUri)
                photoUri = uri
                imgPhoto.setImageURI(uri)
            }
            isEditionMode = true
            btnSaveVaccination.text = getString(R.string.vac_update_button_text)
        } else {
            Toast.makeText(this, getString(R.string.vac_vaccination_not_found), Toast.LENGTH_LONG).show()
        }
    }

    private fun setupDateAndTimePickers() {
        txtVaccinationDate.setOnClickListener {
            showDatePickerDialog(txtVaccinationDate)
        }
        txtVaccinationTime.setOnClickListener {
            showTimePickerDialog(txtVaccinationTime)
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

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                editText.setText(timeFormat.format(selectedTime.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vac_crud_menu, menu)
        menu?.findItem(R.id.mnu_delete)?.isEnabled = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveVaccinationRecord()
                true
            }
            R.id.mnu_delete -> {
                deleteVaccinationRecord()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteVaccinationRecord() {
        vaccinationId?.let {
            vaccinationModel.removeVaccinationRecord(it)
            Toast.makeText(this, getString(R.string.vac_vaccination_deleted), Toast.LENGTH_LONG).show()
            finish()
        } ?: Toast.makeText(this, getString(R.string.vac_vaccination_not_found), Toast.LENGTH_LONG).show()
    }
}
