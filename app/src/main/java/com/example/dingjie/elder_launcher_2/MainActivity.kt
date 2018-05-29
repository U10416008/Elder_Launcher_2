package com.example.dingjie.elder_launcher_2

import android.content.Intent
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.Contact.ContactActivity
import com.example.dingjie.elder_launcher_2.Game.GameActivity
import com.example.dingjie.elder_launcher_2.UI.MainActivityUI
import org.jetbrains.anko.*
import java.util.*
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.dingjie.elder_launcher_2.reminder.RemindApp


class MainActivity : AppCompatActivity(),SensorEventListener {


    internal lateinit var button: Button
    internal lateinit var more: ImageView
    internal lateinit var contact: ImageView
    internal lateinit var chat: ImageView
    internal lateinit var game: ImageView
    var timeText : TextView?  = null
    var time = Calendar.getInstance().time;


    override fun onCreate(savedIntanceState: Bundle?) {
        super.onCreate(savedIntanceState)
        MainActivityUI().setContentView(this)

        button = find<Button>(R.id.button)

        button.setOnClickListener { }
        initChat()
        initGame()
        initMore()
        initContacts()
        /*val isAppInstalled = appInstalledOrNot("jp.naver.line.android")

        if (isAppInstalled) {
            //This intent will help you to launch if the package is already installed
            val LaunchIntent = packageManager
                    .getLaunchIntentForPackage("jp.naver.line.android")
            startActivity(LaunchIntent)

            Log.i("Application"," is already installed.")
        } else {
            // Do whatever we want to do if application not installed
            // For example, Redirect to play store
            try {
                val viewIntent = Intent("android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=jp.naver.line.android"))
                startActivity(viewIntent)
            } catch (e: Exception) {

                toast("Unable to Connect Try Again...")
                e.printStackTrace()
            }


            Log.i("Application"," is not currently installed.")
        }*/
        //initTime()

    }
    override fun onSensorChanged(p0: SensorEvent?) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
    fun initChat() {
        chat = find(R.id.chat)
        chat.layoutParams.height = screenHeight / 3
        chat.layoutParams.width = screenWidth / 3
        chat.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, RemindApp::class.java)
            startActivity(intent)
        }
        chat.setOnLongClickListener { false }
    }
    fun initGame() {
        game = find(R.id.game)
        game.layoutParams.height = screenHeight / 3
        game.layoutParams.width = screenWidth / 3
        game.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
        game.setOnLongClickListener { false }
    }
    fun initContacts() {
        contact = find(R.id.contacts)
        contact.layoutParams.height = screenHeight / 3
        contact.layoutParams.width = screenWidth / 3
        contact.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, ContactActivity::class.java)
            startActivity(intent)
        }
        contact.setOnLongClickListener { false }
    }

    fun initMore() {
        more = find(R.id.more)
        more.layoutParams.height = screenHeight / 3
        more.layoutParams.width = screenWidth / 3
        more.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, AppList::class.java)
            startActivity(intent)
        }
        more.setOnLongClickListener { false }
    }


    fun initTime(){
        timeText = find<TextView>(R.id.time)
        timeText?.text = time.toString()
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }
    companion object {
        val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels

        val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
    }
}
