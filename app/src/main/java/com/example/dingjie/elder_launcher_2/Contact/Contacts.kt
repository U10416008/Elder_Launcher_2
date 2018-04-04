package com.example.dingjie.elder_launcher_2.Contact

import android.graphics.drawable.Drawable

/**
 * Created by dingjie on 2018/3/18.
 */

class Contacts {
    var name = ""
    var number = ""
    var image: Drawable? = null

    internal constructor(name: String, number: String, image: Drawable) {
        this.name = name
        this.number = number
        this.image = image
    }

    internal constructor(name: String, number: String) {
        this.name = name
        this.number = number
    }

}
