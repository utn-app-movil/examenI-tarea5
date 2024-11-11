package cr.ac.utn.movil

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import identities.Cen_Persona
import model.Cen_model
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.Locale

class cen_add_persona_activity : AppCompatActivity() {


    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtProvince : EditText
    private lateinit var txtCanton: EditText
    private lateinit var txtDistrits: EditText
    private lateinit var cenModel: Cen_model
    private var isEditionMode: Boolean = false
    private lateinit var menuitemDelete: MenuItem
    private  var CensInfo: String? =""
    private lateinit var txtService: EditText
    private var servicesList: MutableList<String> = mutableListOf()
    private var imageUri: Uri? = null
    private var imageUriPhoto : Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private var Cen_currentPhotoPath: String? = null
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 0
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cen_add_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        cenModel = Cen_model(this)
        txtId = findViewById<EditText>(R.id.Cen_txtId)
        txtName = findViewById<EditText>(R.id.Cen_txtName)
        txtLastName = findViewById<EditText>(R.id.Cen_txtLastName)
        txtPhone = findViewById<EditText>(R.id.Cen_txtPhone)
        txtEmail = findViewById<EditText>(R.id.Cen_txtEmail)
        txtAddress = findViewById<EditText>(R.id.Cen_txtAddress)
        txtProvince = findViewById<EditText>(R.id.Cen_txtPrivince)
        txtCanton = findViewById<EditText>(R.id.Cen_txtCanton)
        txtDistrits = findViewById<EditText>(R.id.Cen_txtDistrict)
        txtService = findViewById(R.id.Cen_txt_service)
        CensInfo = intent.getStringExtra(EXTRA_MESSAGE_ID)
       if (CensInfo != null && CensInfo != "") loadContact(CensInfo.toString())

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CAMERA_PERMISSION
            )
        }



        val btnAddService: Button = findViewById(R.id.Cen_btn_save_service)
        btnAddService.setOnClickListener {
            addService()
        }


    }
    private fun showImageSourceDialog() {
        val options = arrayOf(getString(R.string.Cen_take_photo), getString(R.string.Cen_take_gallery))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.Cen_fout_image))
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
                    "cr.ac.utn.movil.fileprovider",  // Reemplaza con tu nombre de paquete
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            Cen_currentPhotoPath = absolutePath
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Manejar imagen capturada por la cámara
                val imageView: ImageView = findViewById(R.id.Cen_imagen_view)
                imageUriPhoto = Uri.parse(Cen_currentPhotoPath)
                imageView.setImageURI(imageUriPhoto)
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                // Obtener URI de la imagen seleccionada desde la galería
                val imageUri: Uri? = data?.data  // Aquí se obtiene la URI de la imagen de la galería

                // Guardar la imagen en el almacenamiento interno
                imageUri?.let { uri ->
                    val savedImageUri: Uri? = saveImageToInternalStorage(uri)  // Guardar imagen internamente
                    savedImageUri?.let { savedUri ->
                        // Mostrar la imagen guardada en un ImageView
                        val imageView: ImageView = findViewById(R.id.Cen_imagen_view)
                        imageView.setImageURI(savedUri)  // Muestra la imagen desde el almacenamiento interno
                    }
                }
            }
        }
    }

    // Función para guardar la imagen en el almacenamiento interno
    private fun saveImageToInternalStorage(uri: Uri): Uri? {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(filesDir, "image_${System.currentTimeMillis()}.jpg")

        try {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            // Aquí actualizamos la variable global imageUri con la URI del archivo guardado
            imageUri = Uri.fromFile(file)  // Guarda la URI del archivo en la variable global
            return imageUri  // Devuelve la misma URI
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }






    private fun DisplayDialog(mesage: String = "", title: String = "", showNegativeButton: Boolean = true, showPositiveButton: Boolean = true, positiveText: String = "", negativeText: String = "", onPositiveClick: (() -> Unit)? = null){

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setMessage(mesage)
            .setCancelable(false)
        if (showPositiveButton) {
            dialogBuilder.setPositiveButton(positiveText) {
                    dialog, id -> onPositiveClick?.invoke()
                dialog.dismiss()
            }
        }

        if (showNegativeButton){
            dialogBuilder.setNegativeButton(negativeText) {
                    dialog, id -> dialog.dismiss()
            }
        }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cen_crud_menu, menu)

        menuitemDelete = menu!!.findItem(R.id.Cen_mn_remove)
        if (isEditionMode)
            menuitemDelete.isVisible = true
        else
            menuitemDelete.isVisible =false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Cen_mn_save -> {
                saveContact()
                return true
            }

            R.id.Cen_mn_remove -> {
                DisplayDialog(
                    getString(R.string.Cen_msg_sure),
                    getString(R.string.Cen_msg_sure), showNegativeButton = true, showPositiveButton = false,getString(R.string.Cen_msg_ready), getString(R.string.Cen_No) , onPositiveClick = {deleteContact(CensInfo.toString())})

                return true
            }

            R.id.Cen_mn_cancel -> {
                cleanScreen()
                return true
            }

            R.id.Cen_mn_service -> {
                showServicesDialog()
                return true
            }
            R.id.Cen_mn_camera -> {
                showImageSourceDialog()
                return true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addService() {
        val service = txtService.text.toString()
        if (service.isNotEmpty()) {
            servicesList.add(service)
            txtService.text.clear()
            Toast.makeText(this, "Servicio agregado: $service", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Por favor, ingrese un servicio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveContact(){
                    try {
                        val Cen_identi = Cen_Persona()
                        Cen_identi.id = txtId.text.toString()
                        Cen_identi.name = txtName.text.toString()
                        Cen_identi.lastName = txtLastName.text.toString()
                        Cen_identi.phone = txtPhone.text.toString()?.toInt()!!
                        Cen_identi.email = txtEmail.text.toString()
                        Cen_identi.address = txtAddress.text.toString()
                        Cen_identi.province = txtProvince.text.toString()
                        Cen_identi.canton = txtCanton.text.toString()
                        Cen_identi.distrits = txtDistrits.text.toString()
                        Cen_identi.service = servicesList
                        Cen_identi.imageUri = imageUri
                        Cen_identi.imageUriPhoto = imageUriPhoto

                        if (dataValidation(Cen_identi)) {
                            if (!isEditionMode) {
                                cenModel.addContact(Cen_identi)
                                cleanScreen()
                            } else {
                                DisplayDialog(
                                    getString(R.string.Cen_msg_update),
                                    getString(R.string.Cen_msg_update),
                                    showNegativeButton = true,
                                    showPositiveButton = true,
                                    getString(R.string.Cen_msg_ready),
                                    getString(R.string.Cen_No),
                                    onPositiveClick = {
                                        cenModel.updateContact(Cen_identi)
                                        cleanScreen()
                                    })
                            }
                            Toast.makeText(this, R.string.Cen_msg_save_user, Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, R.string.Cen_msg_missing_data, Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(Cen_user: Cen_Persona): Boolean{
        return Cen_user.id.isNotEmpty() && Cen_user.name.isNotEmpty() &&
                Cen_user.lastName.isNotEmpty() && Cen_user.address.isNotEmpty() &&
                Cen_user.email.isNotEmpty() && Cen_user.province.isNotEmpty() &&
                Cen_user.canton.isNotEmpty() && Cen_user.distrits.isNotEmpty() &&
                (Cen_user.phone !=null && Cen_user.phone > 0)
    }

    private fun deleteContact(contactInfo: String){
        val clearscree = cenModel.getContactByFullName(contactInfo)
        cenModel.removeContact(clearscree.id)
    }

    private fun cleanScreen(){
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtProvince.setText("")
        txtCanton.setText("")
        txtDistrits.setText("")
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadContact(CensInfo: String){
        try {
            val Cen_idperso = cenModel.getContactByFullName(CensInfo)
            txtId.setText(Cen_idperso.id)
            txtName.setText(Cen_idperso.name)
            txtLastName.setText(Cen_idperso.lastName)
            txtPhone.setText(Cen_idperso.phone.toString())
            txtEmail.setText(Cen_idperso.email)
            txtAddress.setText(Cen_idperso.address)
            txtProvince.setText(Cen_idperso.province)
            txtCanton.setText(Cen_idperso.canton)
            txtDistrits.setText(Cen_idperso.distrits)
            servicesList.add(Cen_idperso.service.toString())
            // Verificar si hay un URI guardado para la imagen del contacto y cargarlo en el ImageView
            val imagegallery = findViewById<ImageView>(R.id.Cen_imagen_view)
            Cen_idperso.imageUri?.let { uri ->  // Si imageUri no es nulo
                imagegallery.setImageURI(uri)       // Establece la imagen en el ImageView
            }
            val imagenphoto = findViewById<ImageView>(R.id.Cen_imagen_view)
            Cen_idperso.imageUriPhoto?.let { uri ->  // Si imageUri no es nulo
                imagenphoto.setImageURI(uri)       // Establece la imagen en el ImageView
            }



            isEditionMode = true
            txtId.isEnabled = false
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
    private fun showServicesDialog() {
        // Convertir `servicesList` a un array para mostrarlo en el diálogo
        val servicesArray = servicesList.toTypedArray()

        // Crear el diálogo
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Lista de Servicios")

        // Configurar el diálogo para mostrar la lista de servicios
        builder.setItems(servicesArray) { dialog, which ->
            // Acción al seleccionar un elemento (opcional)
            val selectedService = servicesArray[which]
            Toast.makeText(this, "Seleccionaste: $selectedService", Toast.LENGTH_SHORT).show()
        }

        // Agregar un botón para cerrar el diálogo
        builder.setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() }

        // Mostrar el diálogo
        builder.create().show()
    }



}