package cr.ac.utn.movil

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cr.ac.utn.appmovil.model.sin_model
import cr.ac.utn.appmovil.identities.sin_Sinpe
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class sin_CRUD : AppCompatActivity() {

    private lateinit var txtOriginPerson: EditText
    private lateinit var txtPhoneNumber: EditText
    private lateinit var txtDestinationName: EditText
    private lateinit var txtAmount: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtDateTime: EditText
    private lateinit var btnSaveSinpe: Button
    private lateinit var sin_imgPhoto: ImageView
    private lateinit var btnSelectFromGallery: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var sinpeId: String? = null
    private var sin_photoUri: Uri? = null
    private lateinit var sin_currentPhotoPath: String
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val model = sin_model()

    companion object {
        private const val sin_REQUEST_CAMERA_PERMISSION = 100
        private const val sin_REQUEST_STORAGE_PERMISSION = 101
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sin_crud)

        txtOriginPerson = findViewById(R.id.sin_origin_person)
        txtPhoneNumber = findViewById(R.id.sin_phone_number)
        txtDestinationName = findViewById(R.id.sin_destination_name)
        txtAmount = findViewById(R.id.sin_amount)
        txtDescription = findViewById(R.id.sin_description)
        txtDateTime = findViewById(R.id.sin_date_time)
        btnSaveSinpe = findViewById(R.id.sin_btn_save_sinpe)
        sin_imgPhoto = findViewById(R.id.sin_image_view)
        btnSelectFromGallery = findViewById(R.id.btn_select_from_gallery)
        btnTakePhoto = findViewById(R.id.sin_btn_select_photo)

        setupActivityResultLaunchers()

        btnSelectFromGallery.setOnClickListener { openGalleryWithoutPermission() }
        btnTakePhoto.setOnClickListener { openCamera() }
        btnSaveSinpe.setOnClickListener { saveSinpe() }

        setupDatePicker()

        sinpeId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        sinpeId?.let { loadSinpe(it) }
    }

    private fun setupActivityResultLaunchers() {
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                sin_photoUri = uri
                sin_imgPhoto.setImageURI(uri)
                Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                sin_imgPhoto.setImageURI(sin_photoUri)
                Toast.makeText(this, "Foto capturada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se capturó la foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGalleryWithoutPermission() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                val photoFile: File? = createImageFile()
                if (photoFile != null) {
                    val photoUri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.provider",
                        photoFile
                    )
                    sin_photoUri = photoUri
                    cameraLauncher.launch(photoUri)
                } else {
                    Toast.makeText(this, "No se pudo crear el archivo para la foto", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), sin_REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                sin_currentPhotoPath = absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            sin_REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveSinpe() {
        try {
            val sinpe = sin_Sinpe(
                id = sinpeId ?: UUID.randomUUID().toString(),
                originPerson = txtOriginPerson.text.toString().trim(),
                phoneNumber = txtPhoneNumber.text.toString().trim(),
                destinationName = txtDestinationName.text.toString().trim(),
                amount = txtAmount.text.toString().toDoubleOrNull() ?: 0.0,
                description = txtDescription.text.toString().trim(),
                dateTime = dateFormat.parse(txtDateTime.text.toString()) ?: Date(),
                photoUri = sin_photoUri?.toString()
            )

            if (validateData(sinpe)) {
                if (sinpeId != null) {
                    model.updateSinpeTransaction(sinpe)
                    Toast.makeText(this, getString(R.string.sin_sinpe_updated), Toast.LENGTH_LONG).show()
                } else {
                    model.addSinpeTransaction(sinpe)
                    Toast.makeText(this, getString(R.string.sin_sinpe_saved), Toast.LENGTH_LONG).show()
                }
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateData(sinpe: sin_Sinpe): Boolean {
        return when {
            sinpe.sin_originPerson.isBlank() -> {
                txtOriginPerson.error = getString(R.string.sin_error_origin_person_empty)
                false
            }
            sinpe.sin_phoneNumber.isBlank() -> {
                txtPhoneNumber.error = getString(R.string.sin_error_phone_number)
                false
            }
            sinpe.sin_destinationName.isBlank() -> {
                txtDestinationName.error = getString(R.string.sin_error_destination_name)
                false
            }
            sinpe.sin_amount <= 0 -> {
                txtAmount.error = getString(R.string.sin_error_amount)
                false
            }
            txtDateTime.text.toString().isBlank() -> {
                txtDateTime.error = getString(R.string.sin_error_date_time)
                false
            }
            else -> true
        }
    }

    private fun loadSinpe(id: String) {
        val sinpe = model.getSinpeTransactionById(id)
        sinpe?.let {
            txtOriginPerson.setText(it.sin_originPerson)
            txtPhoneNumber.setText(it.sin_phoneNumber)
            txtDestinationName.setText(it.sin_destinationName)
            txtAmount.setText(it.sin_amount.toString())
            txtDescription.setText(it.sin_description)
            txtDateTime.setText(dateFormat.format(it.sin_dateTime))
            sin_photoUri = it.sin_photoUri?.let { Uri.parse(it) }
            sin_photoUri?.let { sin_imgPhoto.setImageURI(it) }
        }
    }

    private fun deleteSinpe() {
        sinpeId?.let {
            model.removeSinpeTransaction(it)
            Toast.makeText(this, getString(R.string.sin_sinpe_deleted), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sin_crud_menu, menu)
        menu?.findItem(R.id.mnu_delete)?.isEnabled = sinpeId != null
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveSinpe()
                true
            }
            R.id.mnu_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Método para mostrar un diálogo de confirmación antes de eliminar
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.sin_confirm_delete))
            .setMessage(getString(R.string.sin_delete_message))
            .setPositiveButton(getString(R.string.sin_delete)) { _, _ ->
                deleteSinpe()
            }
            .setNegativeButton(getString(R.string.sin_cancel), null)
            .show()
    }

    private fun setupDatePicker() {
        txtDateTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    txtDateTime.setText(dateFormat.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }
    }
}
