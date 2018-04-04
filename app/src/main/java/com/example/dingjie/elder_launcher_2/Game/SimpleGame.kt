package com.example.dingjie.elder_launcher_2.Game

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.dingjie.elder_launcher_2.R
import com.example.dingjie.elder_launcher_2.UI.SimpleUI
import java.util.*
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.content.ContentUris
import android.graphics.Bitmap
import java.io.IOException
import kotlin.collections.ArrayList
import com.example.dingjie.elder_launcher_2.MainActivity
import org.jetbrains.anko.*


class SimpleGame : AppCompatActivity() {
    var mode : String = ""
    lateinit var images : Array<ImageView>
    var photo: ArrayList<Bitmap> = ArrayList()
    var random :Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = getIntent()
        var bundle = intent.getBundleExtra("Mode")
        if(bundle != null) {
            retrieveContactPhoto()
            mode = bundle.getString("Mode")
            Log.d("Mode",mode)
            SimpleUI().setContentView(this)
            initSimple(mode)
            //Simple

        }

    }
    fun initSimple(mode:String){
        images = arrayOf(
                find<ImageView>(R.id.question),
                find<ImageView>(R.id.image1),
                find<ImageView>(R.id.image2),
                find<ImageView>(R.id.image3),
                find<ImageView>(R.id.image4)
        )
        images[0].layoutParams.height = matchParent
        images[0].layoutParams.width =matchParent
        if(mode == "Hard"){
            images[0].setImageResource(R.drawable.play_button)
        }
        for(i in images.indices){
            images[i].setOnClickListener {
                if(i !=0)
                    if(images[i].drawable == images[0].drawable){
                        if(mode != "Hard")
                            randomImage()

                    }
                //else
            }
            if(i == 0 )
                continue
            images[i].layoutParams.height = MainActivity.screenHeight / 3
            images[i].layoutParams.width = MainActivity.screenWidth / 2
            if(mode != "Simple"){
                images[i].visibility = View.VISIBLE
            }
        }
        //first
        if(mode != "Hard")
            randomImage()

    }
    fun randomImage(){
        var photo_answer = random.nextInt(photo.size)

        images[0].setImageBitmap(photo.get(photo_answer))

        var answer = random.nextInt(images.size-1)+1
        for(i in images.indices){
            //image.visibility = View.VISIBLE
            if(i != 0 ) {
                if (i == answer) {
                    images[i].setImageDrawable(images[0].drawable)
                } else {
                    images[i].setImageBitmap(photo.get(random_question(photo_answer)))
                }


            }

        }
        if(mode == "Simple")
            randomShow()
    }

    fun random_question(answer_photo : Int) : Int{
        var question = random.nextInt(photo.size)
        while(answer_photo ==question )
            question = random.nextInt(photo.size)

        return question
    }
    fun randomShow(){
        for(i in images.indices ){
            if(i==0)
                continue
            if(random.nextInt()%2 == 1 || images[0].drawable == images[i].drawable)
                images[i].visibility = View.VISIBLE
            else
                images[i].visibility = View.INVISIBLE
        }
    }

    private fun retrieveContactPhoto() {
        var contactIDs: ArrayList<Long> = getContactIDs()


        try {
            for (contactID in contactIDs) {

                val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID))
                if (inputStream != null) {
                    photo.add(BitmapFactory.decodeStream(inputStream))

                    Log.d("photo",""+photo.size)
                    //val imageView = findViewById(R.id.img_contact) as ImageView
                    //imageView.setImageBitmap(photo)

                    inputStream.close()
                }


            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    fun getContactIDs(): ArrayList<Long>{
        val c = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        var contactIDs: ArrayList<Long> = arrayListOf()
        if(c.count >0){
            while (c.moveToNext()) {
                contactIDs.add(c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)).toLong())
                Log.d("ContactID",c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)))
            }
        }

        return contactIDs
    }

}