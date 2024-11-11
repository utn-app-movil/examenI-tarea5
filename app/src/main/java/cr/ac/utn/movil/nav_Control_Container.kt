package cr.ac.utn.movil

import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.util
import identities.nav_Container
import model.nav_model
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class nav_Control_Container : AppCompatActivity() {

    private lateinit var nav_txtNamePers: EditText
    private lateinit var nav_txtContainerNum: EditText
    private lateinit var nav_txtDay: TextView

    private lateinit var nav_txtSpinner: Spinner
    private lateinit var nav_txtTemp: EditText
    private lateinit var nav_txtWeight: EditText
    private lateinit var nav_txtProduct: EditText
    private lateinit var nav_contModel: nav_model
    private var isEditionMode: Boolean = false

    //private lateinit var contactModel: ContactModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el contenido de la vista
        setContentView(R.layout.activity_nav_control_container)

        // Inicializa las vistas
        nav_contModel = nav_model(this)
        nav_txtNamePers = findViewById(R.id.nav_person)
        nav_txtContainerNum = findViewById(R.id.nav_contNum)
        nav_txtDay = findViewById(R.id.txt_day)

        nav_txtSpinner = findViewById(R.id.nav_Spiner)
        nav_txtTemp = findViewById(R.id.nav_temp)
        nav_txtWeight = findViewById(R.id.nav_weight)
        nav_txtProduct = findViewById(R.id.nav_product)

        // Inicializar la fecha y hora actual
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        nav_txtDay.text = currentDateTime

        // Inicializa y establece el Toolbar
        val toolbar = findViewById<Toolbar>(R.id.mnuCrud)
        setSupportActionBar(toolbar)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setEditTextLimits()
    }

    // Establece límites de caracteres
    private fun setEditTextLimits() {
        nav_txtNamePers.filters = arrayOf(InputFilter.LengthFilter(15))
        nav_txtContainerNum.filters = arrayOf(InputFilter.LengthFilter(30))

        nav_txtTemp.filters = arrayOf(InputFilter.LengthFilter(100))
        nav_txtWeight.filters = arrayOf(InputFilter.LengthFilter(10000))
        nav_txtProduct.filters = arrayOf(InputFilter.LengthFilter(50))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nav_mnu_container, menu)
        if (isEditionMode) {
            menu?.findItem(R.id.nav_delete)?.isVisible = true
            menu?.findItem(R.id.nav_delete)?.isEnabled = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_insert -> {
                saveContainer()
                util.openActivity(this, nav_contList::class.java, "Control of container", "Container")

                return true
            }

            R.id.nav_delete -> {
                delete()
                util.openActivity(this, nav_contList::class.java, "Control of container", "Container")

                return true
            }

            R.id.nav_list -> {
                util.openActivity(this, nav_contList::class.java, "Control of container", "List")
                return true
            }

            R.id.nav_upd -> {
              //  performUpdateContainer()
                return true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    // Método insert
    private fun saveContainer() {
        try {
            // Crear el objeto nav_Container
            val container = nav_Container(
                nav_namePer = nav_txtNamePers.text.toString(),
                nav_ContainerNum = nav_txtContainerNum.text.toString(),
                nav_day = Date(),

                nav_Spinner = nav_txtSpinner.selectedItem.toString(),
                nav_temp = nav_txtTemp.text.toString().toFloatOrNull() ?: 0.0f,
                nav_weigth = nav_txtWeight.text.toString().toFloatOrNull() ?: 0.0f,
                nav_product = nav_txtProduct.text.toString()
            )
            nav_contModel.addContainer(container)
            Toast.makeText(this, R.string.msgSucces, Toast.LENGTH_LONG).show()



        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun delete() {
        nav_contModel.removeContainer(nav_txtContainerNum.text.toString())
        Toast.makeText(this, R.string.delCont, Toast.LENGTH_LONG).show()

        util.openActivity(this, nav_contList::class.java)
    }

    private fun performUpdateContainer() {
        val container = nav_Container(
            nav_namePer = nav_txtNamePers.text.toString(),
            nav_ContainerNum = nav_txtContainerNum.text.toString(),
            nav_day = Date(),

            nav_Spinner = nav_txtSpinner.selectedItem.toString(),
            nav_temp = nav_txtTemp.text.toString().toFloatOrNull() ?: 0.0f,
            nav_weigth = nav_txtWeight.text.toString().toFloatOrNull() ?: 0.0f,
            nav_product = nav_txtProduct.text.toString()
        )
        nav_contModel.updateContainer(container)
        Toast.makeText(this, R.string.msgUpd, Toast.LENGTH_LONG).show()
        util.openActivity(this, nav_contList::class.java)
    }






    private fun cleanContact() {
        nav_txtNamePers.setText("")
        nav_txtContainerNum.setText("")
        nav_txtDay.setText("")


        nav_txtTemp.setText("")
        nav_txtWeight.setText("")
        nav_txtProduct.setText("")
    }



}