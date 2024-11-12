package cr.ac.utn.appmovil.identities

import android.graphics.Bitmap
import android.widget.ImageView
import identities.Identifier

// Definición de la entidad Email que hereda de Identifier
class ema_Email : Identifier {

    private var _title: String = ""
    private var _message: String = ""
    private var _sendDate: String = ""
    private var _cc: String = ""
    private var _cco: String = ""
    var ImageBitmap: Bitmap? = null

    constructor()

    constructor(
        id: String,
        title: String,
        message: String,
        sendDate: String,
        cc: String,
        cco: String,


        ) {
        this.Id = id
        this._title = title
        this._message = message
        this._sendDate = sendDate
        this._cc = cc
        this._cco = cco


    }

    override val FullDescription: String
        get() = ""


    var Message: String
        get() = _message
        set(value) { _message = value }

    var SendDate: String
        get() = _sendDate
        set(value) { _sendDate = value }

    var CC: String
        get() = _cc
        set(value) { _cc = value }


    var Title: String
        get() = _title
        set(value) { _title = value }

    var CCO: String
        get() = _cco
        set(value) { _cco = value }


}


