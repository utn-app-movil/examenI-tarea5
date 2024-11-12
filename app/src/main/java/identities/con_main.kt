package identities

import java.time.LocalDate

fun main() {

    val contribution = ConContribution(
        Id = "1",
        con_name = "Contribución a Proyecto X",
        con_totalcontributions = 0,
        con_day = LocalDate.now().dayOfMonth,
        con_year = LocalDate.now().year
    )

    println("Contribution ID: ${contribution.Id}")
    println("Person name: ${contribution.con_name}")
    println("Total Contributions: ${contribution.con_totalcontributions}")
    println("Contribution date: ${contribution.con_date}")
    println("Formatted Contribution date: ${contribution.formattedDate()}")
}
