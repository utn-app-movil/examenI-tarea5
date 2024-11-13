package cr.ac.utn.movil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.cit_Citizen

class cit_ModifyInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cit_modify_information)


        val idEditText = findViewById<EditText>(R.id.cit_Identification)
        val firstName = findViewById<EditText>(R.id.cit_Name)
        val lastName = findViewById<EditText>(R.id.cit_LastName)
        val email = findViewById<EditText>(R.id.cit_Contact)
        val daySpinner = findViewById<Spinner>(R.id.cit_Spinner_day)
        val monthSpinner = findViewById<Spinner>(R.id.cit_Spinner_month)
        val yearSpinner = findViewById<Spinner>(R.id.cit_Spinner_year)
        val doctorNameSpinner = findViewById<Spinner>(R.id.cit_DoctorName)
        val specialtySpinner = findViewById<Spinner>(R.id.cit_Specialty)
        val genderSpinner = findViewById<Spinner>(R.id.cit_Spinner_gender)
        val btnModify = findViewById<Button>(R.id.cit_btnModify)

        btnModify.setOnClickListener {
            val idText = idEditText.text.toString()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Please enter an ID.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val citizen = cit_DataStore.getCitizenById(idText)

            if (citizen != null) {

                firstName.setText(citizen.cit_Name)
                lastName.setText(citizen.cit_LastName)
                email.setText(citizen.cit_Email)

                genderSpinner.setSelection(if (citizen.cit_Gender == "Male") 0 else 1)

                setSpinnerSelectionByValue(doctorNameSpinner, citizen.cit_DoctorName)


                setSpinnerSelectionByValue(specialtySpinner, citizen.cit_Specialty)

                val birthDate = citizen.cit_BirthDate.split("/")
                if (birthDate.size == 3) {
                    val day = birthDate[0].toIntOrNull() ?: 1
                    val month = birthDate[1]
                    val year = birthDate[2].toIntOrNull() ?: 1900


                    daySpinner.setSelection(day - 1) // Ajustamos porque los días empiezan en 0
                    setSpinnerSelectionByValue(monthSpinner, month)
                    setSpinnerSelectionByValue(yearSpinner, year.toString())
                }

                Toast.makeText(this, "Citizen found: ${citizen.cit_Name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Citizen not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSpinnerSelectionByValue(spinner: Spinner, value: String) {
        for (i in 0 until spinner.adapter.count) {
            if (spinner.adapter.getItem(i).toString() == value) {
                spinner.setSelection(i)
                break
            }
        }
    }
}
