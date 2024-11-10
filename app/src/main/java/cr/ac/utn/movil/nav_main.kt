package cr.ac.utn.movil

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.util.util


class nav_main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btncontrolView: Button = findViewById<Button>(R.id.nav_btnControl)
        btncontrolView.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, nav_Container_control::class.java)

            Toast.makeText(this, getString(R.string.welcome).toString(), Toast.LENGTH_LONG)
                .show()
        })

        val btncontrolList: Button = findViewById<Button>(R.id.nav_btnlist)
        btncontrolList.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, nav_custom_list::class.java)

            Toast.makeText(this, getString(R.string.welcome).toString(), Toast.LENGTH_LONG)
                .show()
        })
    }
}