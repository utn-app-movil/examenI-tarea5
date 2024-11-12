package cr.ac.utn.movil

import android.Manifest
import android.app.Activity
import android.app.Person
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import cr.ac.utn.appmovil.identities.Persona
import identities.sub_datas
import model.sub_model
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

private lateinit var sub_IMAGE_VIEW: ImageView
private var URI_IMG: String = ""




class sub_main : AppCompatActivity() {
    //date auction
    private lateinit var txtHour: EditText
    private lateinit var txtdate: EditText
    private lateinit var txtamount: EditText
    private lateinit var txtselledQ: EditText
    private lateinit var txtCode: EditText
    private lateinit var txtDesc: EditText


    //data user
    private lateinit var txtNameUsr: EditText
    private lateinit var txtLNameUsr: EditText
    private lateinit var txtPhoneUsr: EditText
    private lateinit var txtEmailUsr: EditText
    private lateinit var txtAddrssUsr: EditText
    private lateinit var txtCountryUsr: EditText


    //data temporal
    private lateinit var subHo: String
    private lateinit var subda: String
    private lateinit var subam: String
    private lateinit var subse: String
    private lateinit var subco: String
    private lateinit var subde: String


    private val PHOTO_PERMISSION = 1
    private val GALLERY_PERMISSION = 2
    private var photoPath: String? = null
    private var PathPhoto: String? = null
    companion object {
        private const val CAMERAPERMISIION = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sub_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtHour = findViewById<EditText>(R.id.sub_hour)
        txtdate = findViewById<EditText>(R.id.sub_date)
        txtamount = findViewById<EditText>(R.id.sub_amount)
        txtselledQ = findViewById<EditText>(R.id.sub_adjunt)
        txtCode = findViewById<EditText>(R.id.sub_code)
        txtDesc = findViewById<EditText>(R.id.sub_desc)
        txtNameUsr = findViewById<EditText>(R.id.sub_name_insert)
        txtLNameUsr = findViewById<EditText>(R.id.sub_Lname_insert)
        txtPhoneUsr = findViewById<EditText>(R.id.sub_Phone_insert)
        txtEmailUsr = findViewById<EditText>(R.id.sub_email_insert)
        txtAddrssUsr = findViewById<EditText>(R.id.sub_address_insert)
        txtCountryUsr = findViewById<EditText>(R.id.sub_Country_insert)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERAPERMISIION
            )
        }

        sub_IMAGE_VIEW = findViewById<ImageView>(R.id.sub_imageProfile)

        val btn_PHOTO_PROFILE: Button = findViewById<Button>(R.id.sub_add_imagePr)
        btn_PHOTO_PROFILE.setOnClickListener(View.OnClickListener { view ->
            DialogIMG()
        })

        val btnAddAuction: Button = findViewById<Button>(R.id.sub_addBtn)
        btnAddAuction.setOnClickListener(View.OnClickListener { view ->
            addSub()
        })
    }

    private fun DialogIMG() {
        val options = arrayOf(getString(R.string.sub_photo_select),
            getString(R.string.sub_gallery_select))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.sub_selectOption))
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> open_Camera()
                1 -> Gallery()
            }
        }
        builder.show()
    }

    private fun Gallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, GALLERY_PERMISSION)
    }

    private fun open_Camera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createIMGFile()
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
                startActivityForResult(takePictureIntent, PHOTO_PERMISSION)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.subbtnsave -> {
                subAddEvery()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addSub() {
        subHo = txtHour.text.toString()
        subda = txtdate.text.toString()
        subam = txtamount.text.toString()
        subse = txtselledQ.text.toString()
        subco = txtCode.text.toString()
        subde = txtDesc.text.toString()
        Toast.makeText(this, getString(R.string.sub_success), Toast.LENGTH_SHORT).show()
    }


    fun subAddEvery () {
        val personaInstance = Persona()
        personaInstance.Name = txtNameUsr.text.toString()
        personaInstance.LastName = txtLNameUsr.text.toString()
        personaInstance.Phone = txtPhoneUsr.text.toString().toInt()
        personaInstance.Email = txtEmailUsr.text.toString()
        personaInstance.Address = txtAddrssUsr.text.toString()
        personaInstance.Country = txtCountryUsr.text.toString()

        try {
            val subModel = sub_model()
            val preparingToinsert = sub_datas(personaInstance, subHo, subda, subam, subco, subde, subse, URI_IMG)
            subModel.sub_add(preparingToinsert)
            Toast.makeText(this, getString(R.string.sub_success), Toast.LENGTH_SHORT).show()
        }catch (err: Exception){
            Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
        }

    }

    @Throws(IOException::class)
    private fun createIMGFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            PathPhoto = absolutePath
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_PERMISSION) {
                URI_IMG = PathPhoto.toString()
                findViewById<ImageView>(R.id.sub_imageProfile).setImageURI(Uri.parse(URI_IMG))
            } else if (requestCode == GALLERY_PERMISSION) {
                val selectedImageUri: Uri? = data?.data
                findViewById<ImageView>(R.id.sub_imageProfile).setImageURI(selectedImageUri)

            }
        }
    }
}