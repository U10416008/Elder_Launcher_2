package com.example.dingjie.elder_launcher_2.Game

import android.annotation.SuppressLint
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
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.media.MediaPlayer
import com.example.dingjie.elder_launcher_2.Contact.Contacts
import java.io.IOException
import kotlin.collections.ArrayList
import com.example.dingjie.elder_launcher_2.MainActivity
import org.jetbrains.anko.*
import java.io.File


class SimpleGame : AppCompatActivity() {
    var mode : String = "Simple"
    lateinit var images : Array<ImageView>
    var mediaPlayer : MediaPlayer? = null
    var photo: ArrayList<Contacts> = ArrayList()
    var random :Random = Random()
    var mFileName :String =""
    var answer :Int = 0
    var photo_answer = 0
    var prepared = false
    val MAX_QUES = 20
    var ques = 1
    var clicked = 0
    var correct = 0
    var noRecord = false
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
                when (i) {

                    answer -> {
                        clicked ++
                        correct ++
                        if(ques < MAX_QUES) {
                            if (mode != "Hard" || noRecord)
                                randomImage()
                            else {
                                if (prepared) {
                                    stopPlayer()
                                }
                                randomRecord()
                            }

                            ques++
                            Log.d("problem",ques.toString())
                        }else{
                            alert{
                                customView {
                                    var result = correct.toFloat() / clicked.toFloat()*100.00

                                    textView("正確率：%.2f".format(result))

                                    positiveButton("OK") {
                                        finish()
                                    }

                                }
                            }.show()

                        }
                    }
                    0 -> {
                        if (mode == "Hard") {

                            play()
                        }

                    }
                    else ->{
                        clicked ++
                        toast("Wrong Answer")
                    }

                }
                //else
            }
            if(i != 0 ) {
                images[i].layoutParams.height = MainActivity.screenHeight / 3
                images[i].layoutParams.width = MainActivity.screenWidth / 2
            }
            if(mode != "Simple"){
                images[i].visibility = View.VISIBLE
            }
        }
        //first
        if(mode != "Hard")
            randomImage()
        else{
            randomRecord()
        }


    }
    fun play(){
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setOnPreparedListener(playerPreparedHandler)
            mediaPlayer!!.setOnCompletionListener {
                stopPlayer()
            }
        }
        if(mediaPlayer!!.isPlaying){
            mediaPlayer?.pause()
            images[0].setImageResource(R.drawable.play_button)
        }else if(!prepared){
            images[0].setImageResource(R.drawable.pause)
            try {
                mediaPlayer?.setDataSource(mFileName)
                mediaPlayer?.prepare()
                prepared = true
            } catch(e: Exception) {
                Log.e("PlayRecording","Failed")
            }
        }else{
            mediaPlayer?.start()
            images[0].setImageResource(R.drawable.pause)

        }
    }
    private fun stopPlayer() {
        mediaPlayer?.release()
        prepared = false
        mediaPlayer = null
        images[0].setImageResource(R.drawable.play_button)


    }
    private var playerPreparedHandler = MediaPlayer.OnPreparedListener {
        mediaPlayer?.start()
    }
    fun randomRecord(){
        mFileName = externalCacheDir.absolutePath
        photo_answer = random.nextInt(photo.size)
        var hasFile = photo.size

        while(hasFile != 0){

            var mFileName2 = "$mFileName/${photo[photo_answer].name}.3gp"
            Log.d("filename",mFileName2)

            if(File(mFileName2).exists()){


                mFileName = mFileName2
                break
            }
            photo_answer = (photo_answer+1)%photo.size
            hasFile --
        }

        //mFileName = "$mFileName/${photo[photo_answer].name}${photo[photo_answer].number}.3gp"
        answer = random.nextInt(images.size-1)+1
        if(hasFile == 0){
            noRecord = true
            randomImage()

        }else {
            noRecord = false
            random_image(answer, photo_answer)
        }

    }
    fun randomImage(){
        var photo_answer = random.nextInt(photo.size)

        images[0].setImageBitmap(photo.get(photo_answer).bitmap)

        answer = random.nextInt(images.size-1)+1
        random_image(answer,photo_answer)
        if(mode == "Simple")
            randomShow()

    }
    fun random_image(answer :Int ,answer_photo : Int){
        for(i in images.indices){
            //image.visibility = View.VISIBLE
            if(i != 0 ) {
                if (i == answer) {
                    if(mode != "Hard")
                        images[i].setImageDrawable(images[0].drawable)
                    else
                        images[i].setImageBitmap(photo[answer_photo].bitmap)
                } else {
                    images[i].setImageBitmap(photo.get(random_question(answer_photo)).bitmap)
                }


            }

        }
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
        photo = getContactIDs()


        try {
            for (contactID in photo) {

                val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID.number.toLong()))
                if (inputStream != null) {
                    contactID.bitmap = BitmapFactory.decodeStream(inputStream)
                    Log.d("photo",""+photo.size)

                    //val imageView = findViewById(R.id.img_contact) as ImageView
                    //imageView.setImageBitmap(photo)

                    inputStream.close()
                }


            }
            photo = photo.filter{it.bitmap != null} as ArrayList<Contacts>
            Log.d("photo",""+photo.size)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    @SuppressLint("Recycle")
    fun getContactIDs(): ArrayList<Contacts>{
        val c = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        var contactIDs: ArrayList<Contacts> = arrayListOf()
        if(c.count >0){
            while (c.moveToNext()) {
                contactIDs.add(Contacts(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))))
                Log.d("ContactID",c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)))
            }
        }

        return contactIDs
    }
    override fun onStop(){
        super.onStop()

    }

    override fun onPause() {
        if(ques >= 15) {
            val sp: SharedPreferences = getSharedPreferences("TouchAbility$mode", 0)
            val ability = correct.toFloat() / clicked.toFloat()
            var success = sp.edit().putFloat("ability", ability).commit()

            Log.d("Success", success.toString() + ability)
        }
        super.onPause()

    }
    override fun onDestroy() {

        super.onDestroy()

    }


}