package cr.ac.utn.movil


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapters.ProductAdapter
import cr.ac.utn.appmovil.identities._invProduct
import cr.ac.utn.appmovil.model._invProductModel
import java.text.SimpleDateFormat
import java.util.Date

class InventoryActivity : AppCompatActivity() {

    private lateinit var productModel: _invProductModel
    private lateinit var productImageView: ImageView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: RecyclerView
    private var imagePath: String? = null

    // Constantes para solicitar la cámara o galería
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_inventory)


        productModel = _invProductModel()
        productImageView = findViewById(R.id.iv_productImage)
        productList = findViewById(R.id.rv_productList)
        productList.layoutManager = LinearLayoutManager(this)


        productAdapter = ProductAdapter(productModel.getAllProducts())
        productList.adapter = productAdapter

        // Configurar los botones para las operaciones CRUD y selección de foto
        findViewById<Button>(R.id.btn_addProduct).setOnClickListener { addProduct() }
        findViewById<Button>(R.id.btn_updateProduct).setOnClickListener { updateProduct() }
        findViewById<Button>(R.id.btn_deleteProduct).setOnClickListener { deleteProduct() }
        findViewById<Button>(R.id.btn_addPhoto).setOnClickListener { showImagePickerDialog() }
    }

    // Método para añadir un nuevo producto al inventario
    private fun addProduct() {
        val productCode = findViewById<EditText>(R.id.et_productCode).text.toString()
        val productName = findViewById<EditText>(R.id.et_productName).text.toString()
        val quantity = findViewById<EditText>(R.id.et_quantity).text.toString().toIntOrNull() ?: 0
        val supplierName = findViewById<EditText>(R.id.et_supplierName).text.toString()
        val unitCost = findViewById<EditText>(R.id.et_unitCost).text.toString().toDoubleOrNull() ?: 0.0
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

        val product = _invProduct(productCode, productCode, productName, quantity, date, supplierName, unitCost, imagePath)
        if (productModel.addProduct(product)) {
            Toast.makeText(this, "Product aggregado", Toast.LENGTH_SHORT).show()
            productAdapter.notifyDataSetChanged()
            clearFields()
        } else {
            Toast.makeText(this, "Producto ya existe", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para actualizar un producto existente
    private fun updateProduct() {
        val productCode = findViewById<EditText>(R.id.et_productCode).text.toString()
        val productName = findViewById<EditText>(R.id.et_productName).text.toString()
        val quantity = findViewById<EditText>(R.id.et_quantity).text.toString().toIntOrNull() ?: 0
        val supplierName = findViewById<EditText>(R.id.et_supplierName).text.toString()
        val unitCost = findViewById<EditText>(R.id.et_unitCost).text.toString().toDoubleOrNull() ?: 0.0
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

        val product = _invProduct(productCode, productCode, productName, quantity, date, supplierName, unitCost, imagePath)
        productModel.updateProduct(product)
        Toast.makeText(this, "Product actualizado", Toast.LENGTH_SHORT).show()
        productAdapter.notifyDataSetChanged()
        clearFields()
    }

    // Método para eliminar un producto
    private fun deleteProduct() {
        val productCode = findViewById<EditText>(R.id.et_productCode).text.toString()
        productModel.deleteProduct(productCode)
        Toast.makeText(this, "Producto deleted", Toast.LENGTH_SHORT).show()
        productAdapter.notifyDataSetChanged()
        clearFields()
    }

    // Método para limpiar los campos de entrada
    private fun clearFields() {
        findViewById<EditText>(R.id.et_productCode).text.clear()
        findViewById<EditText>(R.id.et_productName).text.clear()
        findViewById<EditText>(R.id.et_quantity).text.clear()
        findViewById<EditText>(R.id.et_supplierName).text.clear()
        findViewById<EditText>(R.id.et_unitCost).text.clear()
        productImageView.setImageURI(null)
        imagePath = null
    }


    private fun showImagePickerDialog() {
        val options = arrayOf("Tomar  foto", "Elegir de la galeria")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar imagen")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> launchCamera()
                1 -> pickImageFromGallery()
            }
        }
        builder.show()
    }


    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    private fun pickImageFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    productImageView.setImageBitmap(imageBitmap)

                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri: Uri? = data?.data
                    productImageView.setImageURI(imageUri)
                    imagePath = imageUri.toString() // Guarda la ruta de la imagen seleccionada
                }
            }
        }
    }
}
