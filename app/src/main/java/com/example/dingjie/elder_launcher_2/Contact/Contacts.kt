package com.example.dingjie.elder_launcher_2.Contact

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

/**
 * Created by dingjie on 2018/3/18.
 */

class Contacts {
    var name = ""
    var number = ""
    var image: Drawable? = null
    var bitmap : Bitmap? = null
    var time  = 0
    internal constructor(name: String, number: String, image: Drawable,time:Int) {
        this.name = name
        this.number = number
        this.image = image
        this.time = time
    }
    internal constructor(name: String, number: String, image: Drawable) {
        this.name = name
        this.number = number
        this.image = image
        this.time = 0
    }
    internal constructor(name: String, number: String, image: Bitmap) {
        this.name = name
        this.number = number
        this.bitmap = image
        this.time = time
    }

    internal constructor(name: String, number: String) {
        this.name = name
        this.number = number
        this.time = 0
    }
    internal constructor(name: String, number: String,time:Int) {
        this.name = name
        this.number = number
        this.time = time
    }

}
