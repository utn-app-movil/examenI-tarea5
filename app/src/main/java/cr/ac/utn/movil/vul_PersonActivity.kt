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
import identities.vul_Person
import model.vul_PersonModel

class vul_PersonActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtCountry: EditText
    private lateinit var txtDesntinationCountry: EditText
    private lateinit var txtFlightnumber: EditText
    private lateinit var txtFlighdate: EditText
    private lateinit var imgPhoto: ImageView
    private lateinit var btnAddPhoto: Button
    private lateinit var rowImgPhoto: TableRow

    private lateinit var personModel: vul_PersonModel
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
        setContentView(R.layout.vul_activity_person)

        txtId = findViewById(R.id.vul_txtPersonID)
        txtName = findViewById(R.id.vul_txtPersonName)
        txtLastName = findViewById(R.id.vul_txtPersonLastName)
        txtPhone = findViewById(R.id.vul_txtPersonPhone)
        txtEmail = findViewById(R.id.vul_txtPersonEmail)
        txtAddress = findViewById(R.id.vul_txtPersonAddress)
        txtCountry = findViewById(R.id.vul_txtPersonCountry)
        txtDesntinationCountry = findViewById(R.id.vul_txtPersonDestinatioCountry)
        txtFlightnumber = findViewById(R.id.vul_txtPersonFlightNumber)
        txtFlighdate = findViewById(R.id.vul_txtPersonFlightDate)
        imgPhoto = findViewById(R.id.vul_imgPhoto)
        btnAddPhoto = findViewById(R.id.vul_btnAddPhoto1)
        rowImgPhoto = findViewById(R.id.vul_rowImgPhoto)

        rowImgPhoto.visibility = View.GONE

        personModel = vul_PersonModel(this)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imgPhoto.setImageBitmap(imageBitmap)
                selectedPhotoBitmap = imageBitmap
                rowImgPhoto.visibility = View.VISIBLE
            } else {
                Toast.makeText(this,R.string.vul_PhotoError, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, R.string.vul_PhotoError, Toast.LENGTH_SHORT).show()
            }
        }

        btnAddPhoto.setOnClickListener {
            val options = arrayOf(getString(R.string.vul_TakePhoto), getString(R.string.vul_Gallery))
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.vul_TakePhoto))
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

        val personId = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (personId != null && personId.isNotEmpty()) loadPerson(personId)
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
            Toast.makeText(this, R.string.vul_NotCamera, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(pickPhotoIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.vul_crud_menu, menu)
        menuItemDelete = menu!!.findItem(R.id.mnu_delete)
        menuItemDelete.isVisible = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                savePerson()
                true
            }
            R.id.cap_mnu_delete -> {
                deletePerson()
                true
            }
            R.id.cap_mnu_cancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun savePerson() {
        try {
            val person = vul_Person(
                id = txtId.text.toString(),
                _name = txtName.text.toString(),
                _lastName = txtLastName.text.toString(),
                _phone = txtPhone.text.toString().toIntOrNull() ?: 0,
                _email = txtEmail.text.toString(),
                _address = txtAddress.text.toString(),
                _country = txtCountry.text.toString(),
                _desntinationCountry = txtDesntinationCountry.text.toString(),
                _flightnumber = txtFlightnumber.text.toString(),
                _flighdate = txtFlighdate.text.toString(),
                photoBitmap = selectedPhotoBitmap
            )

            if (dataValidation(person)) {
                if (!isEditionMode)
                    personModel.addPerson(person)
                else
                    personModel.updatePerson(person)

                cleanScreen()
                Toast.makeText(this, R.string.vul_PersonSaved, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.vul_FieldsRequired, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(person: vul_Person): Boolean {
        return person.Id.isNotEmpty() &&
                person.name.isNotEmpty() &&
                person.lastName.isNotEmpty() &&
                person.phone > 0 &&
                person.address.isNotEmpty() &&
                person.email.isNotEmpty() &&
                person.country.isNotEmpty() &&
                person.desntinationCountry.isNotEmpty() &&
                person.flightnumber.isNotEmpty() &&
                person.flighdate.isNotEmpty()
    }

    private fun deletePerson() {
        personModel.removePerson(txtId.text.toString())
        Toast.makeText(this, R.string.vul_DeleteDIalog, Toast.LENGTH_LONG).show()
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
        txtDesntinationCountry.setText("")
        txtFlightnumber.setText("")
        txtFlighdate.setText("")
        imgPhoto.setImageResource(0)
        rowImgPhoto.visibility = View.GONE
        selectedPhotoBitmap = null
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadPerson(id: String) {
        try {
            val person = personModel.getPerson(id) as vul_Person
            txtId.setText(person.Id)
            txtName.setText(person.name)
            txtLastName.setText(person.lastName)
            txtPhone.setText(person.phone.toString())
            txtEmail.setText(person.email)
            txtAddress.setText(person.address)
            txtCountry.setText(person.country)
            txtDesntinationCountry.setText(person.desntinationCountry)
            txtFlightnumber.setText(person.flightnumber)
            txtFlighdate.setText(person.flighdate)
            person.photoBitmap?.let {
                imgPhoto.setImageBitmap(it)
                rowImgPhoto.visibility = View.VISIBLE
            } ?: imgPhoto.setImageResource(R.drawable.cap_img)

            isEditionMode = true
            txtId.isEnabled = false
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
