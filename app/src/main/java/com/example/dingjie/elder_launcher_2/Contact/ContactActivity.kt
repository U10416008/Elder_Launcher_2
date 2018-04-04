package com.example.dingjie.elder_launcher_2.Contact

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.dingjie.elder_launcher_2.R

import java.util.ArrayList

/**
 * Created by dingjie on 2018/3/15.
 */

class ContactActivity : AppCompatActivity() {
    internal lateinit var listView: RecyclerView
    internal lateinit var adapter: ContactsAdapter
    internal var contacts_list = ArrayList<Contacts>()
    internal val MY_READ_CONTACT = 3
    internal val MY_CALL_PHONE = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    MY_READ_CONTACT)
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MY_CALL_PHONE)
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission", "pass")
            createContacts()
        }


    }

    fun createContacts() {
        listView = findViewById(R.id.recycle)
        listView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2)
        listView.setLayoutManager(layoutManager)
        contact()
        adapter = ContactsAdapter(contacts_list)
        listView.setAdapter(adapter)
        adapter.setOnRecyclerViewListener(object : ContactsAdapter.OnRecyclerViewListener {
            override fun onItemClick(view: View, position: Int) {

                val callContacts = contacts_list[position]

            }

            override fun onItemLongClick(position: Int): Boolean {
                return false
            }
        })
    }

    fun callAlert(name: String, number: String) {
        AlertDialog.Builder(this)
                .setMessage("Call $name ?")
                .setPositiveButton("CALL") { arg0, arg1 ->
                    //showToast("正在撥給"+number);
                    val call = Intent("android.intent.action.CALL", Uri.parse("tel:" + number))
                    startActivity(call)
                }
                .setNegativeButton("BACK", null)
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_READ_CONTACT -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.CALL_PHONE),
                                MY_CALL_PHONE)
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                }
                return
            }
            MY_CALL_PHONE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        createContacts()
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                }
                return
            }
        }
    }

    fun contact() {


        val resolver = contentResolver
        val cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        while (cursor!!.moveToNext()) {
            val phoneProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

            val id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val phonesCusor = this.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    phoneProjection,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null)
            var number = ""
            var i = 1
            if (phonesCusor != null && phonesCusor.moveToFirst()) {
                do {
                    val num = phonesCusor.getString(0)
                    number = number + i++.toString() + "." + num + "   "

                } while (phonesCusor.moveToNext())
            }


            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            contacts_list.add(Contacts(name, number))
            //String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //Log.d("RECORD", id + "/" + name + "/" + number);
        }
        for (i in contacts_list.indices) {
            Log.d("RECORD", i.toString() + "/" + contacts_list[i].name + "/" + contacts_list[i].number)
        }
    }
}

