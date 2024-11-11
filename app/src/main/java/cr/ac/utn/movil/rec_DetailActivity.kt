package cr.ac.utn.movil

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class rec_DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_detail)

        // Obtener los datos enviados desde rec_ListActivity
        val name = intent.getStringExtra("name") ?: "N/A"
        val lastName = intent.getStringExtra("lastName") ?: "N/A"
        val phone = intent.getIntExtra("phone", 0)
        val email = intent.getStringExtra("email") ?: "N/A"
        val address = intent.getStringExtra("address") ?: "N/A"
        val country = intent.getStringExtra("country") ?: "N/A"
        val role = intent.getStringExtra("role") ?: "N/A"
        val photoPath = intent.getStringExtra("photoPath")

        // Asignar valores a los TextView
        findViewById<TextView>(R.id.detail_name).text = "Nombre: $name $lastName"
        findViewById<TextView>(R.id.detail_phone).text = "Teléfono: $phone"
        findViewById<TextView>(R.id.detail_email).text = "Correo: $email"
        findViewById<TextView>(R.id.detail_address).text = "Dirección: $address"
        findViewById<TextView>(R.id.detail_country).text = "País: $country"
        findViewById<TextView>(R.id.detail_role).text = "Rol: $role"

        // Cargar la foto si existe
        val imageViewPhoto = findViewById<ImageView>(R.id.detail_photo)
        if (photoPath != null && photoPath.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(photoPath)
            imageViewPhoto.setImageBitmap(bitmap)
        } else {
            imageViewPhoto.setImageResource(R.drawable.ic_placeholder_image)
        }
    }
}
