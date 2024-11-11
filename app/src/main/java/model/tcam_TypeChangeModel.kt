package identities

import java.util.Date

class tcam_TypeChangeModel {

    private val typeChangeList = mutableListOf<tcam_TypeChange>()

    // Método para agregar una nueva transacción de tipo de cambio
    fun addTypeChange(typeChange: tcam_TypeChange): Boolean {
        if (!isDuplicate(typeChange)) {
            typeChangeList.add(typeChange)
            return true
        }
        return false // Si el registro es duplicado, no lo agrega
    }

    // Método para verificar duplicados por nombre y fecha de la transacción
    private fun isDuplicate(typeChange: tcam_TypeChange): Boolean {
        return typeChangeList.any { it.fullName == typeChange.fullName && it.dateTime == typeChange.dateTime }
    }

    // Método para actualizar una transacción existente
    fun updateTypeChange(index: Int, updatedTypeChange: tcam_TypeChange): Boolean {
        if (index >= 0 && index < typeChangeList.size) {
            typeChangeList[index] = updatedTypeChange
            return true
        }
        return false
    }

    // Método para eliminar una transacción
    fun deleteTypeChange(index: Int): Boolean {
        if (index >= 0 && index < typeChangeList.size) {
            typeChangeList.removeAt(index)
            return true
        }
        return false
    }

    // Método para obtener una lista de todas las transacciones
    fun getAllTypeChanges(): List<tcam_TypeChange> {
        return typeChangeList
    }

    // Método para buscar una transacción por nombre completo
    fun findTypeChangeByFullName(fullName: String): tcam_TypeChange? {
        return typeChangeList.find { it.fullName == fullName }
    }

    // Método para calcular el monto total de una transacción
    fun calculateTotalAmount(typeChange: tcam_TypeChange): Double {
        return typeChange.valueAmount * typeChange.valueChange
    }
}
