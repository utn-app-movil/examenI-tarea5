package cr.ac.utn.movil

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.util

class sub_principalMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal_sub_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGoadd: Button = findViewById<Button>(R.id.sub_btn_addperson)
        btnGoadd.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, sub_main::class.java)
        })

        val btnGoList: Button = findViewById<Button>(R.id.sub_btnshowList)
        btnGoList.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, subList::class.java)
        })
    }
}