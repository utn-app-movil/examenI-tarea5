package cr.ac.utn.movil

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import identities.cap_Training
import model.cap_TrainingModel

class cap_AddActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtCountry: EditText
    private lateinit var txtCourseCode: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtHours: EditText
    private lateinit var txtDate: EditText
    private lateinit var imgPhoto: ImageView
    private lateinit var btnAddPhoto: Button
    private lateinit var rowImgPhoto: TableRow

    private lateinit var trainingModel: cap_TrainingModel
    private var isEditionMode: Boolean = false
    private lateinit var menuItemDelete: MenuItem

    private var selectedPhotoBitmap: Bitmap? = null // Cambiar a Bitmap
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cap_add)

        // Inicializa los elementos de la interfaz
        txtId = findViewById(R.id.cap_txtId)
        txtName = findViewById(R.id.cap_txtName)
        txtLastName = findViewById(R.id.cap_txtLastName)
        txtPhone = findViewById(R.id.cap_txtPhone)
        txtEmail = findViewById(R.id.cap_txtEmail)
        txtAddress = findViewById(R.id.cap_txtAddress)
        txtCountry = findViewById(R.id.cap_txtCountry)
        txtCourseCode = findViewById(R.id.cap_txtCourseCode)
        txtDescription = findViewById(R.id.cap_txtDescription)
        txtHours = findViewById(R.id.cap_txtHours)
        txtDate = findViewById(R.id.cap_txtDate)
        imgPhoto = findViewById(R.id.cap_imgPhoto)
        btnAddPhoto = findViewById(R.id.cap_btnAddPhoto)
        rowImgPhoto = findViewById(R.id.cap_rowImgPhoto)

        // Oculta la fila de la imagen al inicio
        rowImgPhoto.visibility = View.GONE

        trainingModel = cap_TrainingModel(this)

        // Configura los lanzadores para manejar los resultados de las actividades
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imgPhoto.setImageBitmap(imageBitmap)
                selectedPhotoBitmap = imageBitmap // Guarda el Bitmap
                rowImgPhoto.visibility = View.VISIBLE
            } else {
                Toast.makeText(this,R.string.cap_msgErrorCaptureImg, Toast.LENGTH_SHORT).show()
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                imgPhoto.setImageBitmap(bitmap)
                selectedPhotoBitmap = bitmap // Guarda el Bitmap
                rowImgPhoto.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.cap_msgErrorPickImg, Toast.LENGTH_SHORT).show()
            }
        }

        btnAddPhoto.setOnClickListener {
            val options = arrayOf(getString(R.string.cap_TakePhoto), getString(R.string.cap_ChooseGallery))
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.cap_AddPhoto2))
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        if (checkAndRequestCameraPermission()) {
                            openCamera()
                        }
                    }
                    1 -> openGallery()
                }
            }
            builder.show()
        }

        val trainingId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (trainingId != null && trainingId.isNotEmpty()) loadTraining(trainingId)
    }

    private fun checkAndRequestCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, R.string.cap_msgCameraPermissionDenied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(this, R.string.cap_msgCameraAppAvailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(pickPhotoIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cap_crud_menu, menu)
        menuItemDelete = menu!!.findItem(R.id.cap_mnu_delete)
        menuItemDelete.isVisible = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cap_mnu_save -> {
                saveTraining()
                true
            }
            R.id.cap_mnu_delete -> {
                deleteTraining()
                true
            }
            R.id.cap_mnu_cancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveTraining() {
        try {
            val training = cap_Training(
                id = txtId.text.toString(),
                _name = txtName.text.toString(),
                _lastName = txtLastName.text.toString(),
                _phone = txtPhone.text.toString().toIntOrNull() ?: 0,
                _email = txtEmail.text.toString(),
                _address = txtAddress.text.toString(),
                _country = txtCountry.text.toString(),
                _courseCode = txtCourseCode.text.toString(),
                _description = txtDescription.text.toString(),
                _hours = txtHours.text.toString().toIntOrNull() ?: 0,
                _dateTime = txtDate.text.toString(),
                photoBitmap = selectedPhotoBitmap // Guardar el Bitmap
            )

            if (dataValidation(training)) {
                if (!isEditionMode)
                    trainingModel.addTraining(training)
                else
                    trainingModel.updateTraining(training)

                cleanScreen()
                Toast.makeText(this, R.string.cap_msgSave, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.cap_msgMissingData, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(training: cap_Training): Boolean {
        return training.Id.isNotEmpty() &&
                training.name.isNotEmpty() &&
                training.lastName.isNotEmpty() &&
                training.phone > 0 &&
                training.address.isNotEmpty() &&
                training.email.isNotEmpty() &&
                training.country.isNotEmpty() &&
                training.courseCode.isNotEmpty() &&
                training.description.isNotEmpty() &&
                training.hours > 0 &&
                training.dateTime.isNotEmpty()
    }

    private fun deleteTraining() {
        trainingModel.removeTraining(txtId.text.toString())
        Toast.makeText(this, R.string.cap_msgDelete, Toast.LENGTH_LONG).show()
        cleanScreen()
    }

    private fun cleanScreen() {
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtCountry.setText("")
        txtCourseCode.setText("")
        txtDescription.setText("")
        txtHours.setText("")
        txtDate.setText("")
        imgPhoto.setImageResource(0)
        rowImgPhoto.visibility = View.GONE
        selectedPhotoBitmap = null // Limpia el Bitmap
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadTraining(id: String) {
        try {
            val training = trainingModel.getTraining(id) as cap_Training
            txtId.setText(training.Id)
            txtName.setText(training.name)
            txtLastName.setText(training.lastName)
            txtPhone.setText(training.phone.toString())
            txtEmail.setText(training.email)
            txtAddress.setText(training.address)
            txtCountry.setText(training.country)
            txtCourseCode.setText(training.courseCode)
            txtDescription.setText(training.description)
            txtHours.setText(training.hours.toString())
            txtDate.setText(training.dateTime)
            training.photoBitmap?.let {
                imgPhoto.setImageBitmap(it)
                rowImgPhoto.visibility = View.VISIBLE
            } ?: imgPhoto.setImageResource(R.drawable.cap_img) // Imagen de marcador de posición

            isEditionMode = true
            txtId.isEnabled = false
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
