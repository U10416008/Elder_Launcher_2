package com.example.dingjie.elder_launcher_2

import android.Manifest
import android.app.Service
import android.content.Context
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
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.example.dingjie.elder_launcher_2.reminder.RemindApp
import java.net.Socket


class MainActivity : AppCompatActivity(),SensorEventListener,LocationListener {
    override fun onProviderDisabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    internal lateinit var button: Button

    internal lateinit var more: ImageView
    internal lateinit var contact: ImageView
    internal lateinit var chat: ImageView
    internal lateinit var game: ImageView
    internal val client = Client("192.168.0.182", 1234)
    var timeText : TextView?  = null
    var time = Calendar.getInstance().time;
    internal val MY_LOCATION = 100
    internal val MY_FINE_LOCATION = 101
    internal val MY_COARSE_LOCATION = 102
    override fun onCreate(savedIntanceState: Bundle?) {
        super.onCreate(savedIntanceState)
        MainActivityUI().setContentView(this)

        initSOS()
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
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_LOCATION ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_FINE_LOCATION)
                }
                return
            }
            MY_FINE_LOCATION->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                            MY_COARSE_LOCATION)
                }
                return
            }
            MY_COARSE_LOCATION->{

                return
            }
        }
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
    fun initSOS(){
        button = find<Button>(R.id.sosbutton)
        var longitude = 0.0
        var latitude = 0.0
        button.setOnClickListener {
            var loc : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var location : Location
            //Log.d("SOS","CLICK")
            if(checkPermission() && (loc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)|| loc.isProviderEnabled(LocationManager.GPS_PROVIDER))){
                //Log.d("","NET")

                var criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.isAltitudeRequired = false
                criteria.isBearingRequired = false
                criteria.isCostAllowed = true
                criteria.powerRequirement = Criteria.POWER_LOW
                var bestProvider = loc.getBestProvider(criteria,true)
                if(bestProvider != null) {
                    location = loc.getLastKnownLocation(bestProvider);

                    if (location != null) {
                        val tm : TelephonyManager = getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
                        var my_number = "0000000000"
                        my_number = tm.line1Number
                        longitude = location.longitude;
                        latitude = location.latitude;
                        initServer(my_number,longitude.toString(), latitude.toString())
                        Log.d("" + longitude, "" + latitude)
                    }
                }

            }
        }
    }
    fun initServer(my_number : String,longitude :  String, latitude : String) {

        client.setClientCallback(object : Client.ClientCallback {
            override fun onMessage(message: String) {}

            override fun onConnect(socket: Socket?) {


                client.send("location&$my_number&$longitude&$latitude")

                client.disconnect();
            }

            override fun onDisconnect(socket: Socket?, message: String) {}

            override fun onConnectError(socket: Socket?, message: String) {}
        })

        client.connect()
    }
    fun checkPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                    MY_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_COARSE_LOCATION)
        }
        if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
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
