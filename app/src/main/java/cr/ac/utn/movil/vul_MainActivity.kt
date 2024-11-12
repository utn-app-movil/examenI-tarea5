package cr.ac.utn.movil

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.util
import androidx.appcompat.app.AlertDialog

class vul_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vul_activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnvulPerson: Button = findViewById<Button>(R.id.vul_btnMainAddPerson)
        btnvulPerson.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, vul_PersonActivity::class.java)
        })

        val btnvulPersonList: Button = findViewById<Button>(R.id.vul_btnMainViewPersons)
        btnvulPersonList.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, vul_PersonListActivity::class.java)
        })

        val btnvulPersonCustomList: Button = findViewById<Button>(R.id.vul_btnCustomList)
        btnvulPersonCustomList.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, vul_CustomListActivity::class.java)
        })

        val btnMainDialog = findViewById<Button>(R.id.btnMainDialog)
        btnMainDialog.setOnClickListener(View.OnClickListener {
            DisplayDialog()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.vul_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.vul_menu_addPerson -> {
                util.openActivity(this, vul_PersonActivity::class.java)
                true
            }
            R.id.cap_mnu_ListTraining-> {
                util.openActivity(this, vul_PersonListActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun DisplayDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.vul_ASK_CloseApp))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.vul_Yes), DialogInterface.OnClickListener{
                    dialog, id -> finish()
            })
            .setNegativeButton(getString(R.string.vul_No), DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.vul_Yes))
        alert.show()
    }
}