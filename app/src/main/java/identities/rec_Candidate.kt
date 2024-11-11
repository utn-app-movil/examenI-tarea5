package cr.ac.utn.appmovil.identities

data class Candidate(
    val id: String,
    val name: String,
    val lastName: String,
    val phone: Int,
    val email: String,
    val address: String,
    val country: String,
    val role: String,
    val photoPath: String? = null // Asegúrate de incluir este parámetro
)
