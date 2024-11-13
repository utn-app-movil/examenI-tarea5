package cr.ac.utn.movil

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.cit_Citizen

class cit_RegisterInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cit_register_information)

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
        val btnSave = findViewById<Button>(R.id.cit_btnSave)


        val doctors = listOf("Dr. John", "Dr. Emma", "Dr. Michael")
        val specialties = listOf("Cardiology", "Pediatrics", "Neurology")

        val doctorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, doctors)
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doctorNameSpinner.adapter = doctorAdapter

        val specialtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, specialties)
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        specialtySpinner.adapter = specialtyAdapter


        val days = (1..31).toList()
        val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val years = (1900..2024).toList()

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = dayAdapter

        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        val genderOptions = arrayOf("Male", "Female")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter


        btnSave.setOnClickListener {
            val idText = idEditText.text.toString()
            val firstNameText = firstName.text.toString()
            val lastNameText = lastName.text.toString()
            val emailText = email.text.toString()
            val selectedDoctor = doctorNameSpinner.selectedItem.toString()
            val selectedSpecialty = specialtySpinner.selectedItem.toString()


            val selectedDay = daySpinner.selectedItem.toString()
            val selectedMonth = monthSpinner.selectedItem.toString()
            val selectedYear = yearSpinner.selectedItem.toString()
            val birthDate = "$selectedDay/$selectedMonth/$selectedYear"

            // Validaciones
            if (idText.isEmpty() || firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty()) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(emailText)) {
                Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val newRegister = cit_Citizen(
                cit_Identification = idText,
                cit_Name = firstNameText,
                cit_LastName = lastNameText,
                cit_Email = emailText,
                cit_BirthDate = birthDate,
                cit_DoctorName = selectedDoctor,
                cit_Specialty = selectedSpecialty,
                cit_Gender = genderSpinner.selectedItem.toString()
            )


            cit_DataStore.addCitizen(newRegister)


            Toast.makeText(this, "Successfully registered: $birthDate", Toast.LENGTH_SHORT).show()


            idEditText.text.clear()
            firstName.text.clear()
            lastName.text.clear()
            email.text.clear()
            genderSpinner.setSelection(0)
            daySpinner.setSelection(0)
            monthSpinner.setSelection(0)
            yearSpinner.setSelection(0)
            doctorNameSpinner.setSelection(0)
            specialtySpinner.setSelection(0)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
