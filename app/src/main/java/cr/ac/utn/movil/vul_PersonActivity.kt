package cr.ac.utn.movil

import cr.ac.utn.appmovil.vuelos.vul_Person
import model.vul_PersonModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class vul_PersonActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtConutry: EditText
    private lateinit var txtDestinationCountry: EditText
    private lateinit var txtFlightNumber: EditText
    private lateinit var txtFlightDate: EditText
    private lateinit var txtFlightTime: EditText
    private lateinit var personModel: vul_PersonModel
    private var isEditionMode: Boolean = false
    private lateinit var menuitemDelete: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vul_activity_person)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        personModel = vul_PersonModel(this)

        txtId = findViewById(R.id.vul_txtPersonID)
        txtName = findViewById(R.id.vul_txtPersonName)
        txtLastName = findViewById(R.id.vul_txtPersonLastName)
        txtPhone = findViewById(R.id.vul_txtPersonPhone)
        txtEmail = findViewById(R.id.vul_txtPersonEmail)
        txtAddress = findViewById(R.id.vul_txtPersonAddress)
        txtConutry = findViewById(R.id.vul_txtPersonCountry)
        txtDestinationCountry = findViewById(R.id.vul_txtPersonDestinatioCountry)
        txtFlightNumber = findViewById(R.id.vul_txtPersonFlightNumber)
        txtFlightDate = findViewById(R.id.vul_txtPersonFlightDate)
        txtFlightTime = findViewById(R.id.vul_txtPersonFlightTime)

        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_ID)
        if (!contactInfo.isNullOrEmpty()) {
            isEditionMode = true
            loadContact(contactInfo.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.vul_crud_menu, menu)

        menuitemDelete = menu!!.findItem(R.id.mnu_delete)
        menuitemDelete.isVisible = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveContact()
                true
            }

            R.id.mnu_delete -> {
                showDeleteDialog()
                true
            }

            R.id.mnu_cancel -> {
                cleanScreen()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveContact() {
        try {
            val contact = vul_Person()
            contact.Id = txtId.text.toString()
            contact.Name = txtName.text.toString()
            contact.LastName = txtLastName.text.toString()
            contact.Phone = txtPhone.text.toString().toIntOrNull() ?: -1
            contact.Email = txtEmail.text.toString()
            contact.Address = txtAddress.text.toString()
            contact.Country = txtConutry.text.toString()
            contact.vul_destinationCountry = txtDestinationCountry.text.toString()
            contact.vul_flightNumber = txtFlightNumber.text.toString()
            contact.vul_flightDate = txtFlightDate.text.toString()
            contact.vul_flightTime = txtFlightTime.text.toString()

            if (dataValidation(contact)) {
                if (isEditionMode && personModel.getPersonByFullDescription(contact.FullDescription) != null) {
                    showWarning(getString(R.string.vul_PersonExists))
                    return
                }

                if (!isEditionMode) {
                    personModel.addPerson(contact)
                } else {
                    personModel.updatePerson(contact)
                }
                cleanScreen()
                showWarning(getString(R.string.vul_PersonSaved))
            } else {
                showWarning(getString(R.string.vul_FieldsRequired))
            }

        } catch (e: Exception) {
            showWarning("Error: ${e.message}")
        }
    }

    private fun dataValidation(contact: vul_Person): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val costaRicaPhonePattern = "^[2687]\\d{7}$"
        val internationalPhonePattern = "^\\+\\d{1,3}\\d{4,14}$"
        val datePattern = "^\\d{2}/\\d{2}/\\d{4}$"
        val timePattern = "^([01]\\d|2[0-3]):[0-5]\\d$"

        return when {
            contact.Id.isEmpty() -> {
                showWarning(getString(R.string.vul_IdRequired))
                false
            }
            contact.Name.isEmpty() -> {
                showWarning(getString(R.string.vul_NameRequired))
                false
            }
            contact.LastName.isEmpty() -> {
                showWarning(getString(R.string.vul_LastNameRequired))
                false
            }
            !(contact.Phone.toString().matches(costaRicaPhonePattern.toRegex()) ||
                    contact.Phone.toString().matches(internationalPhonePattern.toRegex())) -> {
                showWarning(getString(R.string.vul_PhoneRequired))
                false
            }
            !contact.Email.matches(emailPattern.toRegex()) -> {
                showWarning(getString(R.string.vul_EmailRequired))
                false
            }
            contact.Address.isEmpty() -> {
                showWarning(getString(R.string.vul_AddressRequired))
                false
            }
            contact.Country.isEmpty() -> {
                showWarning(getString(R.string.vul_CountryRequired))
                false
            }
            contact.vul_destinationCountry.isEmpty() -> {
                showWarning(getString(R.string.vul_DestinationCountryRequired))
                false
            }
            contact.vul_flightNumber.isEmpty() -> {
                showWarning(getString(R.string.vul_FlightNumberRequired))
                false
            }
            !contact.vul_flightDate.matches(datePattern.toRegex()) -> {
                showWarning(getString(R.string.vul_DateRequired))
                false
            }
            !contact.vul_flightTime.matches(timePattern.toRegex()) -> {
                showWarning(getString(R.string.vul_TimeRequired))
                false
            }
            else -> true
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.vul_DeleteDIalog))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.vul_Yes)) { _, _ -> deleteContact() }
            .setNegativeButton(getString(R.string.vul_No)) { dialog, _ -> dialog.cancel() }
            .setTitle(getString(R.string.vul_Confirmation))
            .show()
    }

    private fun deleteContact() {
        showWarning(getString(R.string.vul_Delete))
    }

    private fun cleanScreen() {
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtConutry.setText("")
        txtDestinationCountry.setText("")
        txtFlightNumber.setText("")
        txtFlightDate.setText("")
        txtFlightTime.setText("")
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadContact(contactInfo: String) {
        try {
            val contact = personModel.getPersonByFullDescription(contactInfo)
            if (contact != null) {
                txtId.setText(contact.Id)
                txtName.setText(contact.Name)
                txtLastName.setText(contact.LastName)
                txtPhone.setText(contact.Phone.toString())
                txtEmail.setText(contact.Email)
                txtAddress.setText(contact.Address)
                txtConutry.setText(contact.Country)
                txtDestinationCountry.setText(contact.vul_destinationCountry)
                txtFlightNumber.setText(contact.vul_flightNumber)
                txtFlightDate.setText(contact.vul_flightDate)
                txtFlightTime.setText(contact.vul_flightTime)
                isEditionMode = true
                txtId.isEnabled = false
            } else {
                showWarning(getString(R.string.vul_PersonNotFound))
            }
        } catch (e: Exception) {
            showWarning("Error: ${e.message}")
        }
    }

    private fun showWarning(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
