package cr.ac.utn.movil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import identities.Evento

class EventAdapter(
    private val events: List<Evento>,
    private val onEditClick: (Evento) -> Unit,
    private val onDeleteClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val txtEventName: TextView = itemView.findViewById(R.id.txtEventName)
//        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
//        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.event_item, parent, false)
//        return EventViewHolder(view)
        return EventViewHolder(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
//        holder.txtEventName.text = event.id
//        holder.btnEdit.setOnClickListener { onEditClick(event) }
//        holder.btnDelete.setOnClickListener { onDeleteClick(event) }
    }

    override fun getItemCount(): Int = events.size
}
