package cr.ac.utn.movil

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.identities.Persona
import model.far_Facture
import identities.far_Medicine
import model.far_factureModel

class far_adding_patient : AppCompatActivity() {
    private var temporaryListOfMedicine: MutableList<far_Medicine> = mutableListOf()

    private lateinit var txtPrice: EditText
    private lateinit var txtQuantity: EditText
    private lateinit var txtName: EditText

    private lateinit var txtIDPatient: EditText
    private lateinit var txtNamePatient: EditText
    private lateinit var txtLNamePatient: EditText
    private lateinit var txtPhonePatient: EditText
    private lateinit var txtEmailPatient: EditText
    private lateinit var txtAddressPatient: EditText
    private lateinit var txtCountryPatient: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_far_adding_patient)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtPrice = findViewById(R.id.far_price)
        txtQuantity = findViewById(R.id.far_quantityMedicine)
        txtName = findViewById(R.id.far_nameMedicine)

        txtNamePatient = findViewById(R.id.far_name_inp)
        txtIDPatient = findViewById(R.id.far_inpt_id)
        txtLNamePatient = findViewById(R.id.far_last_name)
        txtPhonePatient = findViewById(R.id.far_phone_inp)
        txtEmailPatient = findViewById(R.id.far_email_inp)
        txtAddressPatient = findViewById(R.id.far_inp_address)
        txtCountryPatient = findViewById(R.id.far_Country_inp)

        val btnAdding: Button = findViewById<Button>(R.id.far_btn_add)
        btnAdding.setOnClickListener(View.OnClickListener { view ->
            far_addMedicine()
        })
    }

    private fun far_cleanMedicineFields(){
        txtPrice.setText("")
        txtQuantity.setText("")
        txtName.setText("")
    }

    private fun far_addFactureDone (){
        try {
            val patient = Persona()

            patient.Id = txtIDPatient.text.toString()
            patient.Name = txtName.text.toString()
            patient.LastName = txtLNamePatient.text.toString()
            patient.Email = txtEmailPatient.text.toString()
            patient.Phone = txtPhonePatient.text.toString()?.toInt()!!
            patient.Address = txtAddressPatient.text.toString()
            patient.Country = txtCountryPatient.text.toString()

            if (txtIDPatient.text.isEmpty() && txtName.text.isEmpty() &&
                txtName.text.isEmpty() && txtEmailPatient.text.isEmpty() &&
                txtPhonePatient.text.isEmpty() && txtAddressPatient.text.isEmpty() &&
                txtCountryPatient.text.isEmpty()){
                return  Toast.makeText(this, getString(R.string.far_txt_failed),
                    Toast.LENGTH_LONG).show()
            }


            val Prescription = far_Facture(patient, temporaryListOfMedicine)
            val farModel = far_factureModel()
            farModel.far_addFacture(Prescription)

        }catch (err: Exception){
            Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun far_addMedicine(){
        val medicine = far_Medicine()


        if (txtName.text.isEmpty() && txtPrice.text.isEmpty()
            && txtQuantity.text.isEmpty()){
            return Toast.makeText(this, getString(R.string.far_txt_failed), Toast.LENGTH_LONG).show()
        }

        medicine.name = txtName.text.toString()
        medicine.price = txtPrice.text.toString()?.toInt()!!
        medicine.quantity = txtQuantity.text.toString()?.toInt()!!
        try {
            temporaryListOfMedicine.add(medicine)
            far_cleanMedicineFields()
            Toast.makeText(this, getString(R.string.far_success_addMedicine) + temporaryListOfMedicine.size, Toast.LENGTH_LONG).show()
        } catch (err: Exception){
            Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun far_addFacture (){

    }
}