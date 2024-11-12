package cr.ac.utn.movil

import identities.Cen_Persona
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
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
import model.Cen_model

class cen_add_person_activity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtProvince :EditText
    private lateinit var txtCanton:EditText
    private lateinit var txtDistrits: EditText
    private lateinit var cenModel: Cen_model
    private var isEditionMode: Boolean = false
    private lateinit var menuitemDelete: MenuItem
    private  var CensInfo: String? =""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cen_add_person)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cenModel = Cen_model(this)

        txtId = findViewById<EditText>(R.id.Cen_txtId)
        txtName = findViewById<EditText>(R.id.Cen_txtName)
        txtLastName = findViewById<EditText>(R.id.Cen_txtLastName)
        txtPhone = findViewById<EditText>(R.id.Cen_txtPhone)
        txtEmail = findViewById<EditText>(R.id.Cen_txtEmail)
        txtAddress = findViewById<EditText>(R.id.Cen_txtAddress)
        txtProvince = findViewById<EditText>(R.id.Cen_txtPrivince)
        txtCanton = findViewById<EditText>(R.id.Cen_txtCanton)
        txtDistrits = findViewById<EditText>(R.id.Cen_txtDistrict)

    }
    private fun DisplayDialog(mesage: String = "", title: String = "", showNegativeButton: Boolean = true, showPositiveButton: Boolean = true, positiveText: String = "", negativeText: String = "", onPositiveClick: (() -> Unit)? = null){

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setMessage(mesage)
            .setCancelable(false)
        if (showPositiveButton) {
            dialogBuilder.setPositiveButton(positiveText) {
                    dialog, id -> onPositiveClick?.invoke()
                dialog.dismiss()
            }
        }

        if (showNegativeButton){
            dialogBuilder.setNegativeButton(negativeText) {
                    dialog, id -> dialog.dismiss()
            }
        }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cen_crud_menu, menu)

        menuitemDelete = menu!!.findItem(R.id.Cen_mn_remove)
        if (isEditionMode)
            menuitemDelete.isVisible = true
        else
            menuitemDelete.isVisible =false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Cen_mn_save -> {
                saveContact()
                return true
            }

            R.id.Cen_mn_remove -> {
                DisplayDialog(
                    getString(R.string.Cen_msg_sure),
                    getString(R.string.Cen_msg_sure), showNegativeButton = true, showPositiveButton = false,getString(R.string.Cen_msg_ready), getString(R.string.Cen_No) , onPositiveClick = {deleteContact(CensInfo.toString())})

                return true
            }

            R.id.Cen_mn_cancel -> {
                cleanScreen()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveContact(){
        try {
            val Cen_identi = Cen_Persona()
            Cen_identi.id = txtId.text.toString()
            Cen_identi.name = txtName.text.toString()
            Cen_identi.lastName = txtLastName.text.toString()
            Cen_identi.phone = txtPhone.text.toString()?.toInt()!!
            Cen_identi.email = txtEmail.text.toString()
            Cen_identi.address = txtAddress.text.toString()
            if (dataValidation(Cen_identi)) {
                if (!isEditionMode) {
                    cenModel.addContact(Cen_identi)
                    cleanScreen()
                } else {
                    DisplayDialog(
                        getString(R.string.Cen_msg_update),
                        getString(R.string.Cen_msg_update),
                        showNegativeButton = true,
                        showPositiveButton = true,
                        getString(R.string.Cen_msg_ready),
                        getString(R.string.Cen_No),
                        onPositiveClick = {
                            cenModel.updateContact(Cen_identi)
                            cleanScreen()
                        })
                }
                Toast.makeText(this, R.string.Cen_msg_save_user, Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, R.string.Cen_msg_missing_data, Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(Cen_user: Cen_Persona): Boolean{
        val phoneRegu = Regex("^\\d{1,8}$")
        val emailRegu = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        val contactId = cenModel.getContact(Cen_user.id)
        if (!isEditionMode){
            if ( contactId?.id == Cen_user.id ) {
                DisplayDialog(
                    getString(R.string.Cen_user_exist),
                    getString(R.string.Cen_user_exist), showNegativeButton = true, showPositiveButton = false,
                    "", getString(R.string.Cen_msg_ready) ,null)
                return false
            }
        }


        if (!phoneRegu.matches(Cen_user.phone.toString())) {
            DisplayDialog(
                getString(R.string.Cen_much_number),
                getString(R.string.Cen_much_number), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.Cen_msg_ready) ,null)
            return false
        }

        if (!emailRegu.matches(Cen_user.email)) {
            DisplayDialog(
                getString(R.string.Cen_not_email),
                getString(R.string.Cen_not_email), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.Cen_msg_ready) ,null)
            return false
        }




        if (Cen_user.id.length > 9 || Cen_user.name.length > 30 || Cen_user.lastName.length  > 40 || Cen_user.address.length > 50)
        {
            DisplayDialog(
                getString(R.string.Cen_much_text),
                getString(R.string.Cen_much_text), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.Cen_msg_ready) ,null)
            return false
        }

        if (Cen_user.id.isEmpty() && Cen_user.name.isEmpty() && Cen_user.lastName.isEmpty() && Cen_user.address.isEmpty() && Cen_user.email.isEmpty() &&Cen_user.province.isEmpty() &&Cen_user.distrits.isEmpty() &&Cen_user.canton.isEmpty() && !(Cen_user.phone != null && Cen_user.phone > 0))
        {
            DisplayDialog(
                getString(R.string.Cen_msg_missing_data),
                getString(R.string.Cen_msg_missing_data), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.Cen_msg_ready) ,null)
            return false
        }
        return true
    }

    private fun deleteContact(contactInfo: String){
        val clearscree = cenModel.getContactByFullName(contactInfo)
        cenModel.removeContact(clearscree.id)
    }

    private fun cleanScreen(){
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadContact(contactInfo: String){
        try {
            val contact = cenModel.getContactByFullName(contactInfo)
            txtId.setText(contact.id)
            txtName.setText(contact.name)
            txtLastName.setText(contact.lastName)
            txtPhone.setText(contact.phone.toString())
            txtEmail.setText(contact.email)
            txtAddress.setText(contact.address)
            isEditionMode = true
            txtId.isEnabled = false
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}