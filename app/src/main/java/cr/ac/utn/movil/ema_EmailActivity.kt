package cr.ac.utn.movil

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.CaseMap
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cr.ac.utn.appmovil.identities.ema_Email
import cr.ac.utn.appmovil.model.ema_EmailModel
import java.text.SimpleDateFormat
import java.util.*

class ema_EmailActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtTitle: EditText
    private lateinit var txtMessage: EditText
    private lateinit var txtSendDate: EditText
    private lateinit var txtCc: EditText
    private lateinit var txtCco: EditText
    private lateinit var emailModel: ema_EmailModel
    private lateinit var btnSelectImage: Button
    private lateinit var imgPreview: ImageView
    private var selectedPhotoBitmap: Bitmap? = null

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ema_email)

        // Inicializar vistas
        txtId = findViewById(R.id.txt_id)
        txtTitle = findViewById(R.id.txtTitle)
        txtMessage = findViewById(R.id.txtmenss)
        txtSendDate = findViewById(R.id.txtdate)
        txtCc = findViewById(R.id.txtcc)
        txtCco = findViewById(R.id.txtcco)
        btnSelectImage = findViewById(R.id.btn_select_image)
        imgPreview = findViewById(R.id.img_preview)

        emailModel = ema_EmailModel()

        // Configura lanzadores de actividad
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imgPreview.setImageBitmap(imageBitmap)
                selectedPhotoBitmap = imageBitmap
                imgPreview.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.ema_msgErrorCaptureImg, Toast.LENGTH_SHORT).show()
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                imgPreview.setImageBitmap(bitmap)
                selectedPhotoBitmap = bitmap
                imgPreview.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.ema_msgErrorPickImg, Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar listener del botón para selección de imagen
        btnSelectImage.setOnClickListener {
            val options = arrayOf("Tomar foto", "Elegir de la galería")
            AlertDialog.Builder(this)
                .setTitle("Seleccionar Imagen")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            if (checkAndRequestCameraPermission()) {
                                openCamera()
                            }
                        }
                        1 -> openGallery()
                    }
                }
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.ema_crudmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnusave -> {
                saveEmail()
                true
            }
            R.id.mnucancel -> {
                cleanScreen()
                true
            }
            R.id.mnudelete -> {
                removeEmail()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveEmail() {
        val id = txtId.text.toString().trim()
        val title = txtTitle.text.toString().trim()
        val message = txtMessage.text.toString().trim()
        val sendDate = txtSendDate.text.toString().trim()

        if (id.isEmpty() || title.isEmpty() || message.isEmpty() || sendDate.isEmpty()) {
            Toast.makeText(this, R.string.ema_msgcomplete, Toast.LENGTH_LONG).show()
            return
        }

        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(sendDate)
            val currentDate = Date()

            if (date == null || date.before(currentDate)) {
                Toast.makeText(this, R.string.ema_msgdate, Toast.LENGTH_LONG).show()
                return
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.ema_formdate, Toast.LENGTH_LONG).show()
            return
        }

        try {
            val email = ema_Email().apply {
                Id = id
                Title = title
                Message = message
                SendDate = sendDate
                CC = txtCc.text.toString()
                CCO = txtCco.text.toString()
                ImageBitmap = selectedPhotoBitmap  // Se guarda el Bitmap en lugar del Uri
            }
            emailModel.addEmail(email)
            cleanScreen()
            Toast.makeText(this, R.string.msgSave, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun removeEmail() {
        try {
            val emailId = txtId.text.toString()
            if (emailId.isNotEmpty()) {
                emailModel.removeEmail(emailId)
                cleanScreen()
                Toast.makeText(this, R.string.msgDelete, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.msgEmailIdMissing, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun cleanScreen() {
        txtId.setText("")
        txtTitle.setText("")
        txtMessage.setText("")
        txtSendDate.setText("")
        txtCc.setText("")
        txtCco.setText("")
        imgPreview.setImageBitmap(null)
        selectedPhotoBitmap = null
        imgPreview.visibility = View.GONE
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun checkAndRequestCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            false
        } else {
            true
        }
    }
}
