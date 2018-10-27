package com.example.dingjie.elder_launcher_2.reminder

import android.Manifest
import android.app.Service
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.*
import com.example.dingjie.elder_launcher_2.Client
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.alertDialogLayout
import java.net.Socket
import java.util.*

class RemindApp : AppCompatActivity() {
    var listView : RecyclerView? = null
    var swipe : SwipeRefreshLayout? = null
    var adapter : RemindAdapter? = null
    var remind_arrayList : ArrayList<Remind>? = null
    var add_Button : Button? = null
    var item : String? = null
    internal val client = Client("192.168.43.32", 1234)
    var date_Array : IntArray = intArrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59)
    val TextView.chango: Typeface? get() =
        ResourcesCompat.getFont(this.context, R.font.chango)
    val MY_READ = 90
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remind_list)
        var my_number = "0000000000"
        if(checkPermission()){
            val tm : TelephonyManager = getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            my_number = tm.line1Number


            initServer(my_number)
            if(adapter != null){
                Handler().postDelayed({
                    adapter!!.notifyDataSetChanged()
                },1000)
            }
        }
        swipe = find(R.id.swipe)

        swipe!!.setOnRefreshListener {
            remind_arrayList!!.clear()
            adapter!!.notifyDataSetChanged()
            client.connect()
            Handler().postDelayed({
                adapter!!.notifyDataSetChanged()
                swipe!!.isRefreshing = false

            }, 1000);


        }

        listView = find(R.id.remind_recycle)
        listView!!.addItemDecoration(SpacesItemDecoration(20))
        listView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(applicationContext)
        listView!!.layoutManager = layoutManager
        add_Button = find(R.id.add_thing)
        remind_arrayList = ArrayList<Remind>()
        adapter = RemindAdapter(remind_arrayList!!)
        listView!!.adapter = adapter
        add_Button!!.setOnClickListener{
            var month : Spinner? = null
            var day :Spinner? = null
            var hour : Spinner? = null
            var minute : Spinner? = null
            var thing : EditText? = null
            alert{
                customView {

                    verticalLayout {
                        //titleResource = R.string.remind
                        //backgroundColorResource = R.color.background_floating_material_dark
                        backgroundColor  = Color.rgb(250,200,150)
                        padding = dip(16)
                        textView(R.string.remind){
                            typeface = chango

                        }
                        thing = editText{
                            hintResource = R.string.remind_thing
                            typeface = chango
                        }
                        linearLayout {
                            weightSum =1f
                            backgroundColor = Color.rgb(250,250,0)
                            month = spinner {
                                adapter = ArrayAdapter(ctx,android.R.layout.simple_spinner_dropdown_item,date_Array.asList().subList(1,13))
                                //textAlignment = right


                            }.lparams(weight = 0.35f)
                            textView(" / "){
                                typeface = chango

                            }.lparams(weight = 0.3f)
                            day = spinner {
                                adapter = ArrayAdapter(ctx,android.R.layout.simple_spinner_dropdown_item,date_Array.asList().subList(1,32))
                                //textAlignment = right
                                //backgroundColor = Color.rgb(50,50,50)
                            }.lparams(weight = 0.35f)
                            month!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }

                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    val pos = position + 1
                                    if (pos == 2) {
                                        day!!.adapter = ArrayAdapter(ctx,android.R.layout.simple_spinner_dropdown_item,date_Array.asList().subList(1,29))


                                    } else if (pos == 1 || pos == 3 || pos == 5 || pos == 7 || pos == 8 || pos == 10 || pos == 12) {
                                        day!!.adapter = ArrayAdapter(ctx,android.R.layout.simple_spinner_dropdown_item,date_Array.asList().subList(1,32))

                                    } else {
                                        day!!.adapter = ArrayAdapter(ctx,android.R.layout.simple_spinner_dropdown_item,date_Array.asList().subList(1,31))

                                    }
                                }

                            }

                        }
                        linearLayout {
                            weightSum =1f
                            backgroundColor = Color.rgb(250,250,0)
                            hour = spinner {
                                adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, date_Array.asList().subList(0, 24))
                                //textAlignment = right

                            }.lparams(weight = 0.25f)
                            textView(R.string.hour){
                                typeface = chango
                            }.lparams(weight = 0.25f)

                            minute = spinner {
                                adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, date_Array.asList())
                                //textAlignment = right
                                //backgroundColor = Color.rgb(200,200,200)
                            }.lparams(weight = 0.25f)
                            textView(R.string.min){
                                typeface = chango
                            }.lparams(weight = 0.25f)
                        }

                    }

                }



                yesButton{

                    dialog ->
                        remind_arrayList!!.add(Remind(thing!!.text.toString(),hour!!.selectedItemPosition,minute!!.selectedItemPosition, 2018,month!!.selectedItemPosition+1,day!!.selectedItemPosition+1))
                        adapter!!.notifyDataSetChanged()

                        dialog.dismiss()


                }
                noButton { dialog ->  dialog.dismiss() }

            }.show()

        }



    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_READ -> {

                return
            }
        }
    }
    fun initServer(my_number : String){

        client.setClientCallback(object : Client.ClientCallback {
            override fun onMessage(message: String) {
                //Log.d('',message)


                client.disconnect()
                setItemfromServer(message)
                refreshList(getItemfromServer())



            }

            override fun onConnect(socket: Socket?) {


                client.send("schedule&$my_number")


            }

            override fun onDisconnect(socket: Socket?, message: String) {}

            override fun onConnectError(socket: Socket?, message: String) {}
        })

        client.connect()
    }
    fun setItemfromServer(message : String?){

        item = message

    }
    fun refreshList(schedules : List<String>){

        var calendar = Calendar.getInstance()
        for (i in 0.. schedules.size) {
            var array = schedules[i].split(':')


            if (array.size == 2) {
                calendar.time = Date(array[0].toLong())

                Log.d("add", "" + calendar.get(Calendar.DATE))

                remind_arrayList!!.add(Remind(array[1], calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE)))
            }
        }
        adapter!!.notifyDataSetChanged()
        swipe!!.isRefreshing = true
    }
    fun getItemfromServer() : List<String>{

        return item!!.split('&')
    }
    fun checkPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    MY_READ)
        }

        if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    override fun onPause() {
        super.onPause()

    }
}