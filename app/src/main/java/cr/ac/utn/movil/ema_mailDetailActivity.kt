package cr.ac.utn.movil

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import cr.ac.utn.appmovil.model.ema_EmailModel
import cr.ac.utn.appmovil.identities.ema_Email
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID

class ema_mailDetailActivity : AppCompatActivity() {
    private lateinit var emailModel: ema_EmailModel
    private lateinit var emailId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ema_mail_detail)

        // Inicializar el modelo de correo electrónico
        emailModel = ema_EmailModel()

        // Obtener el ID del correo electrónico del Intent
        emailId = intent.getStringExtra(EXTRA_MESSAGE_ID) ?: ""

        // Obtener el correo electrónico por ID
        val email: ema_Email? = emailModel.getEmail(emailId)

        // Mostrar la información del correo electrónico en los TextViews
        email?.let {
            findViewById<TextView>(R.id.editDetailTitle).text = it.Title
            findViewById<TextView>(R.id.editDetailMessage).text = it.Message
            findViewById<TextView>(R.id.editDetailSendDate).text = it.SendDate
            findViewById<TextView>(R.id.editDetailCc).text = it.CC
            findViewById<TextView>(R.id.editDetailCco).text = it.CCO

            val imageView = findViewById<ImageView>(R.id.ema_imadetail)
            imageView.setImageBitmap(it.ImageBitmap)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.ema_crudmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnusave -> {
                showupdateConfirmationDialog()  // Llamamos al método para guardar el correo actualizado
                true
            }
            R.id.mnudelete -> {
                showDeleteConfirmationDialog()  // Mostrar alerta de confirmación antes de eliminar
                true
            }
            R.id.mnucancel -> {
                finish()  // Cierra la actividad
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateEmail() {
        try {
            // Crear un objeto email con la información actualizada desde los TextViews
            val updatedEmail = ema_Email(
                id = emailId,
                title = findViewById<TextView>(R.id.editDetailTitle).text.toString(),
                message = findViewById<TextView>(R.id.editDetailMessage).text.toString(),
                sendDate = findViewById<TextView>(R.id.editDetailSendDate).text.toString(),
                cc = findViewById<TextView>(R.id.editDetailCc).text.toString(),
                cco = findViewById<TextView>(R.id.editDetailCco).text.toString(),

                ).apply {
                ImageBitmap = findViewById<ImageView>(R.id.ema_imadetail).drawable.toBitmap()

            }

            // Llamar al método updateEmail del modelo
            emailModel.updateEmail(updatedEmail)
            Toast.makeText(this, R.string.ema_msgUpdateSuccess, Toast.LENGTH_LONG).show()

            // Enviar el resultado de vuelta y cerrar la actividad
            val resultIntent = Intent()
            resultIntent.putExtra("updatedEmailId", emailId)
            setResult(RESULT_OK, resultIntent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    // Función para mostrar el cuadro de diálogo de confirmación de actualizacion
    private fun showupdateConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.ema_confupdate)
            .setMessage(R.string.ema_msgconfupdate)
            .setPositiveButton(R.string.ema_yes) { _, _ ->
                updateEmail()  // Llamamos a la función para eliminar el correo si el usuario confirma
            }
            .setNegativeButton(R.string.ema_no, null)  // No se realiza ninguna acción si el usuario cancela
            .show()
    }
    // Función para mostrar el cuadro de diálogo de confirmación de eliminación
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.ema_confdel)
            .setMessage(R.string.ema_msgdelconf)
            .setPositiveButton(R.string.ema_yes) { _, _ ->
                removeEmail()  // Llamamos a la función para eliminar el correo si el usuario confirma
            }
            .setNegativeButton(R.string.ema_no, null)  // No se realiza ninguna acción si el usuario cancela
            .show()
    }

    private fun removeEmail() {
        try {
            if (emailId.isNotEmpty()) {
                emailModel.removeEmail(emailId)  // Elimina el correo del modelo
                Toast.makeText(this, R.string.msgDelete, Toast.LENGTH_LONG).show()

                val resultIntent = Intent()
                resultIntent.putExtra("emailId", emailId)
                setResult(RESULT_OK, resultIntent)

                val intent = Intent(this, ema_listEmail::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, R.string.msgEmailIdMissing, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
