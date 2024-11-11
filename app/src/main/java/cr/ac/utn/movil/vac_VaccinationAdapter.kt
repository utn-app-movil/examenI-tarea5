package cr.ac.utn.movil

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import identities.vac_VaccinationRecord
import java.text.SimpleDateFormat
import java.util.*

class vac_VaccinationAdapter(
    private val records: List<vac_VaccinationRecord>,
    private val onItemClick: (vac_VaccinationRecord) -> Unit
) : RecyclerView.Adapter<vac_VaccinationAdapter.VaccinationViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es", "CR"))
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale("es", "CR"))

    class VaccinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.vac_txtItem_Name)
        val txtVaccineType: TextView = itemView.findViewById(R.id.vac_txtItem_VaccineType)
        val txtVaccinationDate: TextView = itemView.findViewById(R.id.vac_txtItem_VaccinationDate)
        val imgPhoto: ImageView = itemView.findViewById(R.id.vac_imgItem_Photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vac_vaccination_record, parent, false)
        return VaccinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        val record = records[position]

        holder.txtName.text = "${record.Name} ${record.LastName}"
        holder.txtVaccineType.text = record.VaccineType

        val formattedDate = dateFormat.format(record.VaccinationDate)
        val formattedTime = timeFormat.format(record.VaccinationDate)
        holder.txtVaccinationDate.text = "$formattedDate $formattedTime"

        if (record.PhotoUri != null) {
            holder.imgPhoto.setImageURI(Uri.parse(record.PhotoUri))
        } else {
            holder.imgPhoto.setImageDrawable(null)
        }

        holder.itemView.setOnClickListener { onItemClick(record) }
    }

    override fun getItemCount() = records.size
}
