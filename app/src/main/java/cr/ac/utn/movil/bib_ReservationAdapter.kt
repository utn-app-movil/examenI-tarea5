package cr.ac.utn.movil

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.bib_Reservation
import java.text.SimpleDateFormat
import java.util.Locale

class bib_ReservationAdapter(
    private val reservations: List<bib_Reservation>,
    private val onItemClick: (bib_Reservation) -> Unit
) : RecyclerView.Adapter<bib_ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgReservationPhoto: ImageView = itemView.findViewById(R.id.img_reservation_photo)
        val txtStudentName: TextView = itemView.findViewById(R.id.txt_student_name)
        val txtBookName: TextView = itemView.findViewById(R.id.txt_book_name)
        val txtReservationDate: TextView = itemView.findViewById(R.id.txt_reservation_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bib_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.txtStudentName.text = reservation.StudentName
        holder.txtBookName.text = reservation.BookName
        holder.txtReservationDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(reservation.ReservationDate)

        if (reservation.PhotoUri.isNotEmpty()) {
            val imageUri = Uri.parse(reservation.PhotoUri)
            holder.imgReservationPhoto.setImageURI(imageUri)
        } else {
            holder.imgReservationPhoto.setImageResource(R.drawable.ic_placeholder_image)
        }

        holder.itemView.setOnClickListener { onItemClick(reservation) }
    }

    override fun getItemCount() = reservations.size
}
