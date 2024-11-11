package identities

import java.util.Date

class nav_Container(
    private var nav_namePer: String = "",
    private var nav_ContainerNum: String = "",
    private var nav_day: Date,

    private var nav_Spinner: String = "",
    private var nav_temp: Float = 0.0f,
    private var nav_weigth: Float = 0.0f,
    private var nav_product: String = ""
) : Identifier() {

    // Implementación de la propiedad abstracta de Identifier
    override val FullDescription: String
        get() = "Container #$containerNum: $namePer - Product: $product"

    init {
        Id = nav_ContainerNum // Asigna el ContainerNum como Id
    }

    var namePer: String
        get() = this.nav_namePer
        set(value) {
            this.nav_namePer = value
        }

    var containerNum: String
        get() = this.nav_ContainerNum
        set(value) {
            this.nav_ContainerNum = value
            Id = value // Actualizar Id cuando se cambia containerNum
        }





    var spinner: String
        get() = this.nav_Spinner
        set(value) {
            this.nav_Spinner = value
        }

    var temp: Float
        get() = this.nav_temp
        set(value) {
            this.nav_temp = value
        }

    var weight: Float
        get() = this.nav_weigth
        set(value) {
            this.nav_weigth = value
        }

    var product: String
        get() = this.nav_product
        set(value) {
            this.nav_product = value
        }
    var day: Date
        get() = this.nav_day
        set(value) {
            this.nav_day = value
        }
}
