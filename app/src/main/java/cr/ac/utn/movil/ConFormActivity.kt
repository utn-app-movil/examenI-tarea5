package cr.ac.utn.movil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import identities.ConContribution

class ConFormActivity : AppCompatActivity() {

    private lateinit var contribution: ConContribution
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_con_form)

        imageView = findViewById(R.id.imageView)

        val contributionExtra: ConContribution? = intent.getParcelableExtra("contribution")

        if (contributionExtra != null) {
            contribution = contributionExtra
            fillFormFields()
        } else {
            contribution = ConContribution(
                Id = "1",
                con_name = "Contribución de ejemplo",
                con_totalcontributions = 10,
                con_day = 15,
                con_year = 2024
            )
            fillFormFields()
        }

        // Agregar el listener para el botón de "Guardar"
        val saveButton = findViewById<Button>(R.id.con_btnsave)
        saveButton.setOnClickListener {
            updateContributionFromForm()
            Toast.makeText(this, "Contribución guardada", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Agregar el listener para seleccionar imagen
        val selectImage = findViewById<Button>(R.id.con_btnSelectImage)
        selectImage.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun fillFormFields() {
        val nameEditText = findViewById<EditText>(R.id.con_name)
        nameEditText.setText(contribution.con_name)

        val totalContributionsEditText = findViewById<EditText>(R.id.con_totalcontributions)
        totalContributionsEditText.setText(contribution.con_totalcontributions.toString())

        val dayEditText = findViewById<EditText>(R.id.con_day)
        dayEditText.setText(contribution.con_day.toString())

        val yearEditText = findViewById<EditText>(R.id.con_year)
        yearEditText.setText(contribution.con_year.toString())
    }

    private fun updateContributionFromForm() {
        val nameEditText = findViewById<EditText>(R.id.con_name)
        val totalContributionsEditText = findViewById<EditText>(R.id.con_totalcontributions)
        val dayEditText = findViewById<EditText>(R.id.con_day)
        val yearEditText = findViewById<EditText>(R.id.con_year)

        contribution.con_name = nameEditText.text.toString()
        contribution.con_totalcontributions = totalContributionsEditText.text.toString().toInt()
        contribution.con_day = dayEditText.text.toString().toInt()
        contribution.con_year = yearEditText.text.toString().toInt()
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Tomar Foto", "Seleccionar de Galería")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar Imagen")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri = data?.data
                    imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }
}

// Clase CustomAdapter
class CustomAdapter(private val context: Context, private val items: List<ConContribution>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        // Otros elementos de tu fila
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_con_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Cargar la imagen en el ImageView
        holder.imageView.setImageURI(item.imageView) // o usar una librería como Glide/Picasso
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
