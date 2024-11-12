package cr.ac.utn.movil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class cit_DeleteInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cit_delete_information)

        val idEditText = findViewById<EditText>(R.id.cit_Identification)
        val deleteButton = findViewById<Button>(R.id.cit_btnDelete)

        deleteButton.setOnClickListener {
            val idText = idEditText.text.toString()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Please enter an ID.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llama al método deleteCitizenById para eliminar el ciudadano

            val success = cit_DataStore.deleteCitizen(index = 0)

            if (success) {
                Toast.makeText(this, "Citizen deleted successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Citizen not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
