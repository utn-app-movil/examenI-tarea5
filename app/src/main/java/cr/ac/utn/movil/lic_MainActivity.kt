package cr.ac.utn.movil

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.movil.identities.LicenseRenewal
import java.lang.reflect.Array.set

fun set(value: Int, function: () -> Unit) {
    TODO("Not yet implemented")
}

private var Unit.Id: String
    get(){
        toString()
        val value = 0
        set(value) {}

class lic_MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lic_main)

        val btnAdd: Button = findViewById(R.id.btnAdd)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        val btnDelete: Button = findViewById(R.id.btnDelete)
        val btnList: Button = findViewById(R.id.btnList)

        // Evento para agregar una renovación de licencia
        btnAdd.setOnClickListener {
            val renewal = LicenseRenewal().apply {
                var lic_licenseType = "A"
                var lic_medicalReportCode = "123456"
                var lic_currentScore = 80
                var lic_renewalDateTime = "2024-11-10 12:00"
            }
            addLicenseRenewal(renewal)
        }

        // Evento para actualizar una renovación de licencia
        btnUpdate.setOnClickListener {
            val renewal = LicenseRenewal().apply {
                var Id = "1"  // Debes setear el ID del registro a actualizar
                var lic_licenseType = "B"
                var lic_medicalReportCode = "123456"
                var lic_currentScore = 85
                var lic_renewalDateTime = "2024-11-11 10:00"
            }
            updateLicenseRenewal(renewal.toString(), renewal)
        }

        // Evento para eliminar una renovación de licencia
        btnDelete.setOnClickListener {
            val idToDelete = "1"  // Ingresa el ID del registro a eliminar
            deleteLicenseRenewal(idToDelete)
        }

        // Evento para listar todas las renovaciones
        btnList.setOnClickListener {
            listAllRenewals()
        }
    }

    private fun LicenseRenewal() {

    }

    private fun addLicenseRenewal(renewal: Unit) {
        val existingRenewal = MemoryManager.getAll().filterIsInstance<LicenseRenewal>()
            .find { it.lic_medicalReportCodesss == renewal.Id}

        if (existingRenewal != null) {
            Toast.makeText(this, "Ya existe una renovación con este código médico.", Toast.LENGTH_SHORT).show()
        } else {
            renewal.Id = (MemoryManager.getAll().size + 1).toString() // Generar un ID único
            MemoryManager.add(renewal)
            Toast.makeText(this, "Renovación de licencia agregada.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLicenseRenewal(id: String, updatedRenewal: Unit) {
        val existingRenewal = MemoryManager.getByid(id) as? LicenseRenewal
        if (existingRenewal != null) {
            MemoryManager.update(updatedRenewal)
            Toast.makeText(this, "Renovación de licencia actualizada.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró una renovación con este ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteLicenseRenewal(id: String) {
        val existingRenewal = MemoryManager.getByid(id)
        if (existingRenewal != null) {
            MemoryManager.remove(id)
            Toast.makeText(this, "Renovación de licencia eliminada.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró una renovación con este ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listAllRenewals() {
        val renewals = MemoryManager.getAll().filterIsInstance<LicenseRenewal>()
        renewals.forEach {
            println(it.toString())
        }
        Toast.makeText(this, "Renovaciones listadas en consola.", Toast.LENGTH_SHORT).show()
        }
