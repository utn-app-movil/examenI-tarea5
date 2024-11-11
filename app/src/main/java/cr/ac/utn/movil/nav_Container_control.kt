package cr.ac.utn.movil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.util
import identities.nav_container
import model.nav_model
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class nav_Container_control : AppCompatActivity() {
    private lateinit var nav_txtNamePers: EditText
    private lateinit var nav_txtContainerNum: EditText
    private lateinit var nav_txtDay: TextView
    private lateinit var nav_txtSpinner: Spinner
    private lateinit var nav_txtTemp: EditText
    private lateinit var nav_txtWeight: EditText
    private lateinit var nav_txtProduct: EditText
    private lateinit var nav_contModel: nav_model
    private val IMAGE_REQUEST_CODE = 1001

    private lateinit var nav_image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_container_control)


        nav_contModel = nav_model(this)
        nav_txtNamePers = findViewById(R.id.nav_person)
        nav_txtContainerNum = findViewById(R.id.nav_contNum)
        nav_txtDay = findViewById(R.id.txt_day)
        nav_txtSpinner = findViewById(R.id.nav_Spiner)
        nav_txtTemp = findViewById(R.id.nav_temp)
        nav_txtWeight = findViewById(R.id.nav_weight)
        nav_txtProduct = findViewById(R.id.nav_product)
        nav_image = findViewById(R.id.nav_imagev)

        // Inicializar la fecha y hora actual
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        nav_txtDay.text = currentDateTime

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btncamera: Button = findViewById<Button>(R.id.nav_btn_image)
        btncamera.setOnClickListener(View.OnClickListener { view ->
            showImageSourceDialog()
        })
        setEditTextLimits()

        // aqui recupero id
        val containerId = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)

        // los mando a la funcion load
        if (containerId != null) {
            loadContainerData(containerId)
        }
        val btnSelecti = findViewById<Button>(R.id.nav_btn_image)


    }
    // Establece límites de caracteres
    private fun setEditTextLimits() {
        nav_txtNamePers.filters = arrayOf(InputFilter.LengthFilter(15))
        nav_txtContainerNum.filters = arrayOf(InputFilter.LengthFilter(30))

        nav_txtTemp.filters = arrayOf(InputFilter.LengthFilter(100))
        nav_txtWeight.filters = arrayOf(InputFilter.LengthFilter(10000))
        nav_txtProduct.filters = arrayOf(InputFilter.LengthFilter(50))

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_mnu_crud, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_insert -> {
                saveContainer()
                cleanContainer()


                true
            }

            R.id.nav_delete -> {
                showConfirmationDialog("delete")
                true
            }

            R.id.nav_update -> {
                showConfirmationDialog("update")
                true
            }

            R.id.nav_list -> {
                util.openActivity(this, nav_custom_list::class.java)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    // Abre la galería
    private fun openGall() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val extras = data?.extras
            val imgBitmap = extras?.get("data") as? Bitmap
            nav_image.setImageBitmap(imgBitmap)
        }//falta funcion para agregar por galeria...
    }


    // Abre la cámara
    private fun openCam() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }


    // Método insert
    private fun saveContainer() {
        try {
            // Guardar imagen en almacenamiento interno y obtener la URI
            val imgUri = nav_image.drawable?.let { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                saveImageToInternalStorage(bitmap)
            }
            // Crear el objeto nav_Container
            val container = nav_container(
                nav_namePer = nav_txtNamePers.text.toString(),
                nav_ContainerNum = nav_txtContainerNum.text.toString(),
                nav_day = Date(),

                nav_Spinner = nav_txtSpinner.selectedItem.toString(),
                nav_temp = nav_txtTemp.text.toString().toFloatOrNull() ?: 0.0f,
                nav_weigth = nav_txtWeight.text.toString().toFloatOrNull() ?: 0.0f,
                nav_product = nav_txtProduct.text.toString() ,
                nav_imageUri = imgUri.toString()
            )
            if(nav_contModel.isDuplicate(container)){
                Toast.makeText(this, R.string.msgContDup, Toast.LENGTH_LONG).show()
            }else{
                nav_contModel.addContainer(container)
                Toast.makeText(this, R.string.Succesins, Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
    // guardar imagen en almacenamiento
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val file = File(filesDir, "container_${System.currentTimeMillis()}.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return Uri.fromFile(file)
    }
    private fun performUpdateContainer() {
        val container = nav_container(
            nav_namePer = nav_txtNamePers.text.toString(),
            nav_ContainerNum = nav_txtContainerNum.text.toString(),
            nav_day = Date(),

            nav_Spinner = nav_txtSpinner.selectedItem.toString(),
            nav_temp = nav_txtTemp.text.toString().toFloatOrNull() ?: 0.0f,
            nav_weigth = nav_txtWeight.text.toString().toFloatOrNull() ?: 0.0f,
            nav_product = nav_txtProduct.text.toString(),
            nav_imageUri = nav_image.toString()
        )
        nav_contModel.updateContainer(container)
        Toast.makeText(this, R.string.Succesupd, Toast.LENGTH_LONG).show()
        util.openActivity(this, nav_custom_list::class.java)
    }
    private fun cleanContainer() {
        nav_txtNamePers.setText("")
        nav_txtContainerNum.setText("")
        nav_txtDay.setText("")


        nav_txtTemp.setText("")
        nav_txtWeight.setText("")
        nav_txtProduct.setText("")
        nav_image.setImageDrawable(null)
    }
    private fun loadContainerData(containerId: String) {
        try {

            val container = nav_contModel.getContainers(containerId) as? nav_container

            //
            if (container != null) {
                nav_txtNamePers.setText(container.namePer)
                nav_txtContainerNum.setText(container.containerNum)
                nav_txtDay.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(container.day)

                nav_txtTemp.setText(container.temp.toString())
                nav_txtWeight.setText(container.weight.toString())
                nav_txtProduct.setText(container.product)
            } else {
                Toast.makeText(this, "Contenedor no encontrado", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar los datos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performDeleteContainer() {
        nav_contModel.removeContainer(nav_txtContainerNum.text.toString())
        Toast.makeText(this, R.string.Succesdel, Toast.LENGTH_LONG).show()
        cleanContainer()
        util.openActivity(this, nav_custom_list::class.java)
    }

    private fun showConfirmationDialog(action: String) {
        val message =
            if (action == "delete") "¿Seguro que deseas eliminar el contenedor?" else "¿Seguro que deseas actualizar el contenedor?"

        AlertDialog.Builder(this).apply {
            setTitle("Confirmación")
            setMessage(message)
            setPositiveButton("Sí") { _, _ ->
                when (action) {
                    "delete" -> performDeleteContainer()
                    "update" -> performUpdateContainer()
                }
            }
            setNegativeButton("No", null)
        }.show()
    }
    private fun showImageSourceDialog() {
        val options = arrayOf("Tomar Foto", "Seleccionar de la Galería")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar Imagen")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openCam() // Abre la cámara
                    1 -> openGall() // Abre la galería
                }
            }
            .show()
    }






}