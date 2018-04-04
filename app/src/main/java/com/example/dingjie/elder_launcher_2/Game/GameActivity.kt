package com.example.dingjie.elder_launcher_2.Game

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.dingjie.elder_launcher_2.R
import com.example.dingjie.elder_launcher_2.UI.GameUI
import com.example.dingjie.elder_launcher_2.UI.SimpleUI
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class GameActivity : AppCompatActivity() {
    var bundle : Bundle = Bundle()
    lateinit var images: Array <ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GameUI().setContentView(this)
        //initUI()
        find<Button>(R.id.simple).setOnClickListener(View.OnClickListener {
            simple()
        })
        find<Button>(R.id.middle).setOnClickListener(View.OnClickListener {
            middle()
        })
        find<Button>(R.id.hard).setOnClickListener(View.OnClickListener {
            hard()
        })
    }
    fun simple(){
        var intent : Intent = Intent()
        intent.setClass(this@GameActivity, SimpleGame::class.java)
        bundle.putString("Mode","Simple")
        intent.putExtra("Mode",bundle)
        startActivity(intent)
    }
    fun middle(){
        var intent : Intent = Intent()
        intent.setClass(this@GameActivity, SimpleGame::class.java)
        bundle.putString("Mode","Middle")
        intent.putExtra("Mode",bundle)
        startActivity(intent)
    }
    fun hard(){
        var intent : Intent = Intent()
        intent.setClass(this@GameActivity, SimpleGame::class.java)
        bundle.putString("Mode","Hard")
        intent.putExtra("Mode",bundle)
        startActivity(intent)
    }

    fun initUI(){

        /*images = arrayOf(find(R.id.question)
        ,find(R.id.image1)
        , find(R.id.image2)
        , find(R.id.image3)
        ,find(R.id.image4)
        ,find(R.id.image5)
        ,find(R.id.image6)
        )

        for (image in images){
            image.layoutParams.height = screenHeight / 3
            image.layoutParams.width = screenWidth / 3
        }
        images[0].layoutParams.height = screenHeight/5*/



    }
    companion object {
        val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels

        val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
    }

}