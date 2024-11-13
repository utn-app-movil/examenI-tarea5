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
import cr.ac.utn.appmovil.identities.eve_Event
import cr.ac.utn.appmovil.identities.Persona
import cr.ac.utn.appmovil.model.eve_EventModel
import java.util.*

class eve_AddActivity : AppCompatActivity() {

    // Campos de evento
    private lateinit var txtId: EditText
    private lateinit var txtInstitution: EditText
    private lateinit var txtEventLocation: EditText
    private lateinit var txtDateTime: EditText
    private lateinit var txtSeatNumber: EditText
    private lateinit var spnEventType: Spinner

    // Campos de persona (asistente)
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtCountry: EditText

    // Campos de foto
    private lateinit var imgPhoto: ImageView
    private lateinit var btnAddPhoto: Button
    private lateinit var rowImgPhoto: TableRow

    private lateinit var eventModel: eve_EventModel
    private var isEditionMode: Boolean = false
    private lateinit var menuItemDelete: MenuItem

    private var selectedPhotoBitmap: Bitmap? = null
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eve_add)

        // Inicializa los elementos de la interfaz de usuario
        txtId = findViewById(R.id.eve_txtId)
        txtInstitution = findViewById(R.id.eve_txtInstitution)
        txtEventLocation = findViewById(R.id.eve_txtEventLocation)
        txtDateTime = findViewById(R.id.eve_txtDateTime)
        txtSeatNumber = findViewById(R.id.eve_txtSeatNumber)
        spnEventType = findViewById(R.id.eve_spnEventType)

        // Inicializa los campos de persona
        txtName = findViewById(R.id.eve_txtName)
        txtLastName = findViewById(R.id.eve_txtLastName)
        txtPhone = findViewById(R.id.eve_txtPhone)
        txtEmail = findViewById(R.id.eve_txtEmail)
        txtAddress = findViewById(R.id.eve_txtAddress)
        txtCountry = findViewById(R.id.eve_txtCountry)

        // Configura opciones para el Spinner de tipo de evento
        val eventTypes = arrayOf("Teatro", "Cine", "Concierto")
        spnEventType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, eventTypes)

        // Configuración de la imagen
        imgPhoto = findViewById(R.id.eve_imgPhoto)
        btnAddPhoto = findViewById(R.id.eve_btnAddPhoto)
        rowImgPhoto = findViewById(R.id.eve_rowImgPhoto)
        rowImgPhoto.visibility = View.GONE

        eventModel = eve_EventModel(this)

        // Configuración de la cámara y galería para la imagen
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imgPhoto.setImageBitmap(imageBitmap)
                selectedPhotoBitmap = imageBitmap
                rowImgPhoto.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.eve_msgErrorCaptureImg, Toast.LENGTH_SHORT).show()
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                imgPhoto.setImageBitmap(bitmap)
                selectedPhotoBitmap = bitmap
                rowImgPhoto.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.eve_msgErrorPickImg, Toast.LENGTH_SHORT).show()
            }
        }

        btnAddPhoto.setOnClickListener {
            val options = arrayOf(getString(R.string.eve_TakePhoto), getString(R.string.eve_ChooseGallery))
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.eve_AddPhoto2))
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

        val eventId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (eventId != null && eventId.isNotEmpty()) loadEvent(eventId)
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
                Toast.makeText(this, R.string.eve_msgCameraPermissionDenied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(this, R.string.eve_msgCameraAppAvailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(pickPhotoIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.eve_crud_menu, menu)
        menuItemDelete = menu!!.findItem(R.id.eve_mnu_delete)
        menuItemDelete.isVisible = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eve_mnu_save -> {
                saveEvent()
                true
            }
            R.id.eve_mnu_delete -> {
                deleteEvent()
                true
            }
            R.id.eve_mnu_cancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveEvent() {
        val person = Persona(
            id = txtId.text.toString(),
            name = txtName.text.toString(),
            lastName = txtLastName.text.toString(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString(),
            address = txtAddress.text.toString(),
            country = txtCountry.text.toString()
        )

        val event = eve_Event(
            id = txtId.text.toString(),
            institution = txtInstitution.text.toString(),
            eventLocation = txtEventLocation.text.toString(),
            eventDateTime = Date(),
            seatNumber = txtSeatNumber.text.toString().toIntOrNull() ?: 0,
            eventType = spnEventType.selectedItem.toString(),
            person = person
        )

        if (validateData(event)) {
            if (!isEditionMode) {
                eventModel.addEvent(event)
            } else {
                eventModel.updateEvent(event)
            }
            cleanScreen()
            Toast.makeText(this, R.string.eve_msgSave, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, R.string.eve_msgMissingData, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateData(event: eve_Event): Boolean {
        return event.Id.isNotEmpty() &&
                event.institution.isNotEmpty() &&
                event.eventLocation.isNotEmpty() &&
                event.seatNumber > 0
    }

    private fun deleteEvent() {
        eventModel.removeEvent(txtId.text.toString())
        Toast.makeText(this, R.string.eve_msgDelete, Toast.LENGTH_LONG).show()
        cleanScreen()
    }

    private fun cleanScreen() {
        txtId.setText("")
        txtInstitution.setText("")
        txtEventLocation.setText("")
        txtDateTime.setText("")
        txtSeatNumber.setText("")
        spnEventType.setSelection(0)

        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtCountry.setText("")

        imgPhoto.setImageResource(0)
        rowImgPhoto.visibility = View.GONE
        selectedPhotoBitmap = null
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadEvent(id: String) {
        try {
            val event = eventModel.getEventById(id) as eve_Event
            txtId.setText(event.Id)
            txtInstitution.setText(event.institution)
            txtEventLocation.setText(event.eventLocation)
            txtDateTime.setText(event.eventDateTime.toString())
            txtSeatNumber.setText(event.seatNumber.toString())
            spnEventType.setSelection((spnEventType.adapter as ArrayAdapter<String>).getPosition(event.eventType))

            txtName.setText(event.person.Name)
            txtLastName.setText(event.person.LastName)
            txtPhone.setText(event.person.Phone.toString())
            txtEmail.setText(event.person.Email)
            txtAddress.setText(event.person.Address)
            txtCountry.setText(event.person.Country)

            event.photoBitmap?.let {
                imgPhoto.setImageBitmap(it)
                rowImgPhoto.visibility = View.VISIBLE
            } ?: imgPhoto.setImageResource(R.drawable.ic_launcher_foreground)


            isEditionMode = true
            txtId.isEnabled = false

        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}