package cr.ac.utn.movil

import cr.ac.utn.appmovil.util.util
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.MemoryManager
import identities.tcam_TypeChange
import java.text.SimpleDateFormat
import java.util.*

class tcam_MainActivity : AppCompatActivity() {

    private lateinit var txtFullName: EditText
    private lateinit var txtDateTime: TextView
    private lateinit var edtExchangeRate: EditText
    private lateinit var edtAmount: EditText
    private lateinit var spnExchangeType: Spinner
    private lateinit var btnCalculate: Button
    private lateinit var txtAmountToPay: TextView
    private var isEditMode: Boolean = false
    private var transactionId: String = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tcam_main)

        txtFullName = findViewById(R.id.tcam_txtFullName)
        txtDateTime = findViewById(R.id.tcam_txtDateTime)
        edtExchangeRate = findViewById(R.id.tcam_ExchangeRate)
        edtAmount = findViewById(R.id.tcam_edtAmount)
        spnExchangeType = findViewById(R.id.tcam_spnExchangeType)
        btnCalculate = findViewById(R.id.tcam_btnCalculate)
        txtAmountToPay = findViewById(R.id.tcam_txtAmountToPay)

        // Inicializar la fecha y hora actual
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        txtDateTime.text = currentDateTime

        // Configurar el Spinner
        val exchangeTypes = arrayOf("Colones a Dólares", "Dólares a Colones")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exchangeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnExchangeType.adapter = adapter

        btnCalculate.setOnClickListener {
            if (validateInputs()) {
                calculateAmount()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tcam_crud_menu, menu)

        if (isEditMode) {
            menu?.findItem(R.id.menu_delete)?.isVisible = true
            menu?.findItem(R.id.menu_delete)?.isEnabled = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveTransaction()
                true
            }
            R.id.menu_delete -> {
                showConfirmationDialog("delete")
                true
            }
            R.id.menu_cancel -> {
                cleanTransaction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateInputs(): Boolean {
        return txtFullName.text.isNotEmpty() &&
                edtExchangeRate.text.isNotEmpty() &&
                edtAmount.text.isNotEmpty()
    }

    private fun calculateAmount() {
        val exchangeRate = edtExchangeRate.text.toString().toDoubleOrNull() ?: 0.0
        val amount = edtAmount.text.toString().toDoubleOrNull() ?: 0.0
        val selectedExchangeType = spnExchangeType.selectedItem.toString()

        val totalAmount = when (selectedExchangeType) {
            "Colones a Dólares" -> if (exchangeRate != 0.0) roundUp(amount / exchangeRate, 2) else 0.0
            "Dólares a Colones" -> roundUp(amount * exchangeRate, 2)
            else -> 0.0
        }

        txtAmountToPay.text = "Monto a pagar: $totalAmount"

        // Guardar la transacción en MemoryManager
        val transaction = tcam_TypeChange(
            id = transactionId,
            _fullName = txtFullName.text.toString(),
            _valueChange = exchangeRate,
            _valueAmount = amount,
            _totalAmount = totalAmount,
            _dateTime = Date()
        )
        MemoryManager.add(transaction)

        Toast.makeText(this, "Transacción guardada", Toast.LENGTH_LONG).show()
    }

    private fun saveTransaction() {
        if (validateInputs()) {
            calculateAmount()
            util.openActivity(this, tcam_Custom_List_Activity::class.java, "", "")
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConfirmationDialog(action: String) {
        val message = if (action == "delete") "¿Seguro que deseas eliminar la transacción?" else "¿Seguro que deseas limpiar los campos?"
        AlertDialog.Builder(this).apply {
            setTitle("Confirmación")
            setMessage(message)
            setPositiveButton("Sí") { _, _ ->
                if (action == "delete") {
                    performDeleteTransaction()
                } else {
                    cleanFields()
                }
            }
            setNegativeButton("No", null)
        }.show()
    }

    private fun performDeleteTransaction() {
        Toast.makeText(this, "Transacción eliminada", Toast.LENGTH_SHORT).show()
        cleanFields()
    }

    private fun cleanTransaction() {
        cleanFields()
    }

    private fun cleanFields() {
        txtFullName.text.clear()
        edtExchangeRate.text.clear()
        edtAmount.text.clear()
        txtAmountToPay.text = ""
    }

    private fun roundUp(value: Double, places: Int): Double {
        val scale = Math.pow(10.0, places.toDouble()).toInt()
        return Math.ceil(value * scale.toDouble()) / scale.toDouble()
    }

}
