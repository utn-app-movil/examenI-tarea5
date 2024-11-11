package cr.ac.utn.movil

import android.app.Activity
import android.app.Dialog
import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.Persona
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import identities.Identifier
import model.far_Facture
import identities.far_Medicine
import model.far_factureModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class far_adding_patient : AppCompatActivity() {
    private var temporaryListOfMedicine: MutableList<far_Medicine> = mutableListOf()
    private var temporaryListOfMedicineS: String = ""
    private var totalPrice: Int = 0


    private lateinit var txtPrice: EditText
    private lateinit var txtQuantity: EditText
    private lateinit var txtName: EditText

    private lateinit var txtIDPatient: EditText
    private lateinit var txtNamePatient: EditText
    private lateinit var txtLNamePatient: EditText
    private lateinit var txtPhonePatient: EditText
    private lateinit var txtEmailPatient: EditText
    private lateinit var txtAddressPatient: EditText
    private lateinit var txtCountryPatient: EditText
    private lateinit var menuItemDelete: MenuItem
    private lateinit var btnAdding: Button
    private var far_info: String? = ""
    private lateinit var modelFarma: far_factureModel
    private var isEditionMode: Boolean = false
    private var imageUri: String = ""
    private lateinit var far_image: ImageView


    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private var currentPhotoPath: String? = null
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_far_adding_patient)
        far_image = findViewById(R.id.far_img_profile)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CAMERA_PERMISSION
            )
        }

        modelFarma = far_factureModel()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtPrice = findViewById(R.id.far_price)
        txtQuantity = findViewById(R.id.far_quantityMedicine)
        txtName = findViewById(R.id.far_nameMedicine)

        txtNamePatient = findViewById<EditText>(R.id.far_name_inp)
        txtIDPatient = findViewById<EditText>(R.id.far_inpt_id)
        txtLNamePatient = findViewById<EditText>(R.id.far_last_name)
        txtPhonePatient = findViewById<EditText>(R.id.far_phone_inp)
        txtEmailPatient = findViewById<EditText>(R.id.far_email_inp)
        txtAddressPatient = findViewById<EditText>(R.id.far_inp_address)
        txtCountryPatient = findViewById<EditText>(R.id.far_Country_inp)
        far_image = findViewById<ImageView>(R.id.far_img_profile)
        btnAdding = findViewById<Button>(R.id.far_btn_add)
        btnAdding.setOnClickListener(View.OnClickListener { view ->
            far_addMedicine()
        })

        val imageV: ImageView = findViewById(R.id.far_img_profile)
        imageV.setOnClickListener{
            showImageDialog()
        }

        far_info = intent.getStringExtra(EXTRA_MESSAGE_ID)

        if (far_info != null && far_info != "") loadContact(far_info.toString())

    }

    private fun showImageDialog() {
        val options = arrayOf(getString(R.string.far_takePhoto), getString(R.string.far_Gallery))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.far_selectAnOption))
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_GALLERY)
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "cr.ac.utn.movil.provider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageUri = currentPhotoPath.toString()
                findViewById<ImageView>(R.id.far_img_profile).setImageURI(Uri.parse(imageUri))
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                val selectedImageUri: Uri? = data?.data
                findViewById<ImageView>(R.id.far_img_profile).setImageURI(selectedImageUri)
                
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.far_menu_crud, menu)
        menuItemDelete =menu!!.findItem(R.id.far_btn_delete)
        if (!isEditionMode) {
            menuItemDelete.setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.add_crud_menu -> {
                far_addFactureDone()
                return true
            }
            R.id.far_moreDetails -> {
                far_moreDetails()
                return true
            }
            R.id.far_btn_delete -> {
                far_DeleteFacture()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun far_DeleteFacture(){
        modelFarma.delete(far_info.toString())
        far_cleanUserFields()
        far_cleanMedicineFields()
        txtIDPatient.isEnabled = true
        btnAdding.isEnabled = true
        menuItemDelete.setVisible(false)
        isEditionMode = false
        temporaryListOfMedicineS = ""
    }

    private fun far_moreDetails(){
        val dialogBuilder = AlertDialog.Builder(this)
        var totalPrice: Int = 0

        dialogBuilder.setMessage(temporaryListOfMedicineS.toString())
            .setCancelable(false)
            .setNegativeButton(getString(R.string.far_ok_option), DialogInterface.OnClickListener{
                dialog, id -> dialog.cancel()
            })
        val alert =dialogBuilder.create()
        alert.setTitle(getString(R.string.far_show_medicines))
        alert.show()
    }

    private fun far_cleanUserFields(){
        txtIDPatient.setText("")
        txtNamePatient.setText("")
        txtLNamePatient.setText("")
        txtPhonePatient.setText("")
        txtEmailPatient.setText("")
        txtAddressPatient.setText("")
        txtCountryPatient.setText("")
        far_image.setImageURI(Uri.parse(""))
    }

    private fun far_cleanMedicineFields(){
        txtPrice.setText("")
        txtQuantity.setText("")
        txtName.setText("")
    }



    private fun far_addFactureDone (){
        try {
            val patient = Persona()

            patient.Id = txtIDPatient.text.toString()
            patient.Name = txtNamePatient.text.toString()
            patient.LastName = txtLNamePatient.text.toString()
            patient.Email = txtEmailPatient.text.toString()
            patient.Phone = txtPhonePatient.text.toString()?.toInt()!!
            patient.Address = txtAddressPatient.text.toString()
            patient.Country = txtCountryPatient.text.toString()

            if (txtIDPatient.text.isEmpty() && txtName.text.isEmpty() &&
                txtName.text.isEmpty() && txtEmailPatient.text.isEmpty() &&
                txtPhonePatient.text.isEmpty() && txtAddressPatient.text.isEmpty() &&
                txtCountryPatient.text.isEmpty()){
                return  Toast.makeText(this, getString(R.string.far_txt_failed),
                    Toast.LENGTH_LONG).show()
            }
            val farModel = far_factureModel()
            if (!isEditionMode){
                val numFacture = far_Facture.far_searchNumberFact(1)
                temporaryListOfMedicineS += "Total: ${totalPrice.toString()}"
                val Prescription = far_Facture(patient, temporaryListOfMedicineS, numFacture,
                    Uri.parse(imageUri)).apply {
                    Id = numFacture.toString()
                }
                farModel.far_addFacture(Prescription)
                temporaryListOfMedicineS = ""
                far_cleanUserFields()
            } else {
                val ids = far_info!!.toInt()
                val Prescription = far_Facture(patient, temporaryListOfMedicineS, ids, Uri.parse(imageUri)).apply {
                    Id = far_info.toString()
                }
                farModel.far_update(Prescription)
                Toast.makeText(this, getString(R.string.far_success_update), Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, getString(R.string.far_success_add), Toast.LENGTH_SHORT).show()

        }catch (err: Exception){
            Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun far_addMedicine(){
        if (txtName.text.isEmpty() && txtPrice.text.isEmpty()
            && txtQuantity.text.isEmpty()){
            return Toast.makeText(this, getString(R.string.far_txt_failed), Toast.LENGTH_LONG).show()
        }

        temporaryListOfMedicineS += "${getString(R.string.far_medicine_txt)}: ${ txtName.text.toString()}\n"
        temporaryListOfMedicineS += "${getString(R.string.far_price_Input)}: ${txtPrice.text.toString()}\n"
        temporaryListOfMedicineS += "${getString(R.string.far_quantity_input)}: ${txtQuantity.text.toString()}\n"
        totalPrice += txtPrice.text.toString().toInt() * txtQuantity.text.toString().toInt()
        far_cleanMedicineFields()
    }

    private fun loadContact(id: String){
        try {
            val patient = modelFarma.getFullinfoByID(id.toString())
            txtIDPatient.setText(patient!!.person.Id)
            txtNamePatient.setText(patient.person.Name)
            txtLNamePatient.setText(patient.person.LastName)
            txtPhonePatient.setText(patient.person.Phone.toString())
            txtEmailPatient.setText(patient.person.Email)
            txtAddressPatient.setText(patient.person.Address)
            txtCountryPatient.setText(patient.person.Country)
            far_image.setImageURI(patient.photoProfile)
            isEditionMode = true
            txtIDPatient.isEnabled = false
            btnAdding.isEnabled = false
            temporaryListOfMedicineS = patient.listMedicine

        }   catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}