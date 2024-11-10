package cr.ac.utn.movil

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.bib_Reservation
import cr.ac.utn.appmovil.model.bib_ReservationModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_ID
import cr.ac.utn.appmovil.util.util

class bib_Lista : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: bib_ReservationAdapter
    private var reservations = mutableListOf<bib_Reservation>()
    private val reservationModel = bib_ReservationModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bib_lista)

        recyclerView = findViewById(R.id.bib_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = bib_ReservationAdapter(reservations) { reservation ->
            if (!reservation.Id.isNullOrEmpty()) {
                util.openActivity(this, bib_CRUD::class.java, EXTRA_MESSAGE_ID, reservation.Id)
            } else {
                Toast.makeText(this, getString(R.string.reservation_no_valid_id), Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter = adapter

        loadReservations()
    }

    private fun loadReservations() {
        reservations.clear()
        reservations.addAll(reservationModel.getReservations())
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadReservations()
    }
}
