package cr.ac.utn.movil

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cr.ac.utn.appmovil.identities.Candidate
import cr.ac.utn.appmovil.model.CandidateModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class rec_RegisterActivity : AppCompatActivity() {

    private lateinit var imageViewPhoto: ImageView
    private lateinit var buttonChoosePhoto: Button
    private var photoPath: String? = null // Ruta del archivo de la foto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_register)

        val nameEditText = findViewById<EditText>(R.id.rec_edit_name)
        val lastNameEditText = findViewById<EditText>(R.id.rec_edit_last_name)
        val phoneEditText = findViewById<EditText>(R.id.rec_edit_phone)
        val emailEditText = findViewById<EditText>(R.id.rec_edit_email)
        val addressEditText = findViewById<EditText>(R.id.rec_edit_address)
        val countryEditText = findViewById<EditText>(R.id.rec_edit_country)
        val rolesSpinner = findViewById<Spinner>(R.id.rec_spinner_roles)
        val submitButton = findViewById<Button>(R.id.rec_button_submit)
        imageViewPhoto = findViewById(R.id.image_view_photo)
        buttonChoosePhoto = findViewById(R.id.button_choose_photo)

        buttonChoosePhoto.setOnClickListener {
            showImageOptionDialog()
        }

        val roles = listOf(
            getString(R.string.rec_role_software),
            getString(R.string.rec_role_infrastructure),
            getString(R.string.rec_role_networks),
            getString(R.string.rec_role_security),
            getString(R.string.rec_role_qa)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rolesSpinner.adapter = adapter

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()
            val role = rolesSpinner.selectedItem.toString()

            if (name.isEmpty() || lastName.isEmpty() || address.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val candidate = Candidate(
                id = System.currentTimeMillis().toString(),
                name = name,
                lastName = lastName,
                phone = phone.toInt(),
                email = email,
                address = address,
                country = country,
                role = role,
                photoPath = photoPath
            )
            CandidateModel.addCandidate(candidate)
            Toast.makeText(this, getString(R.string.rec_data_saved), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showImageOptionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Choose Photo")
            .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> selectPhotoFromGallery()
                }
            }
            .show()
    }

    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            photoPath = saveImageToInternalStorage(it)
            imageViewPhoto.setImageBitmap(it)
        }
    }

    private val galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            photoPath = saveImageToInternalStorage(bitmap)
            imageViewPhoto.setImageBitmap(bitmap)
        }
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            cameraActivityResultLauncher.launch(null)
        }
    }

    private fun selectPhotoFromGallery() {
        galleryActivityResultLauncher.launch("image/*")
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "IMG_$timeStamp.jpg"
        val file = File(filesDir, fileName)

        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }
}
