package com.example.dingjie.elder_launcher_2.Contact

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.dingjie.elder_launcher_2.UI.KeyBoard
import org.jetbrains.anko.setContentView

class ContactKeyBoard:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeyBoard<ContactKeyBoard>().setContentView(this)

    }
}