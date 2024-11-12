package cr.ac.utn.movil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.bib_Reservation
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util

class bib_Lista : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: bib_ReservationAdapter
    private var reservations = mutableListOf<bib_Reservation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bib_lista)

        recyclerView = findViewById(R.id.bib_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadReservations()
        adapter = bib_ReservationAdapter(reservations) { reservation ->
            util.openActivity(this, bib_CRUD::class.java, EXTRA_MESSAGE_ID, reservation.Id)
        }
        recyclerView.adapter = adapter
    }

    private fun loadReservations() {
        reservations = MemoryManager.getAll().filterIsInstance<bib_Reservation>().toMutableList()
    }

    override fun onResume() {
        super.onResume()
        loadReservations()
        adapter.notifyDataSetChanged()
    }
}
