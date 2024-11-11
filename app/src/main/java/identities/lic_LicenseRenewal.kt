package cr.ac.utn.movil.identities

import identities.Identifier


abstract class LicenseRenewal(override val FullDescription: String) : Identifier() {
    var lic_licenseType: String = ""
    var lic_medicalReportCode: String = ""
    var lic_currentScore: Int = 0
    var lic_renewalDateTime: String = ""
}
