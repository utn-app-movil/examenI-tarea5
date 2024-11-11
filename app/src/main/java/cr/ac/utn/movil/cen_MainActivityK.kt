package cr.ac.utn.movil

import cr.ac.utn.appmovil.util.util
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

class cen_MainActivityK : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cen_main_k)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cen_addcensus= findViewById<Button>(R.id.cen_btn_census)
        cen_addcensus.setOnClickListener(View.OnClickListener {
            util.openActivity(this,cen_add_persona_activity::class.java,)
        })
        val cen_viewlist= findViewById<Button>(R.id.cen_btn_list)
        cen_viewlist.setOnClickListener(View.OnClickListener {
            util.openActivity(this,cen_custom_list_activity::class.java,)
        })
    }
}