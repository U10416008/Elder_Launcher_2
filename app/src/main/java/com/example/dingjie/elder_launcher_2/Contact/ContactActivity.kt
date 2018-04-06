package com.example.dingjie.elder_launcher_2.Contact

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.io.*

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
    internal val MY_ExternalStorage = 6
    internal val IMAGE_GALLERY_REQUEST = 5
    internal val CAMERA_REQUEST = 7

    var currentID : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact)

        if (checkPermission()){
            createContacts()
        }


    }

    fun createContacts() {
        val items = listOf(getString(R.string.change_photo), getString(R.string.record),getString(R.string.cancel))
        val items2 = listOf(getString(R.string.take_photo), getString(R.string.gallery))
        listView = findViewById(R.id.recycle)
        listView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2)
        listView.layoutManager = layoutManager
        contact()
        contacts_list.add(Contacts(getString(R.string.add_contact),"0",resources.getDrawable(R.drawable.add_contact,null)))
        adapter = ContactsAdapter(contacts_list)
        listView.adapter = adapter
        adapter.setOnRecyclerViewListener(object : ContactsAdapter.OnRecyclerViewListener {
            override fun onItemClick(view: View, position: Int) {

                val callContacts = contacts_list[position]
                currentID =position.toLong()+1

            }

            override fun onItemLongClick(position: Int): Boolean {
                currentID = position.toLong()+1
                selector(getString(R.string.INFO), items, { dialogInterface, i ->
                    when (i) {
                        0 -> {

                            selector("How to Change", items2, { dialogInterface2, pos ->
                                when (pos) {
                                    0 -> {
                                        onCameraSelected()
                                        dialogInterface2.dismiss()
                                    }
                                    else -> {
                                        onImageGallerySelected()
                                        dialogInterface2.dismiss()
                                    }
                                }
                            })
                            dialogInterface.dismiss()

                        }
                        1 -> dialogInterface.dismiss()
                        else -> dialogInterface.dismiss()
                    }

                })


                return false
            }
        })
    }
    fun onCameraSelected(){
        var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    fun onImageGallerySelected(){
        var photoPicker = Intent(Intent.ACTION_PICK)
        photoPicker.type = "image/*"
        startActivityForResult(photoPicker,IMAGE_GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_GALLERY_REQUEST -> try{
                //val bitmap = BitmapFactory.decodeResource(resources,R.drawable.play_button)
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                var image = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG,100,image)
                setContactPicture(this,currentID.toString(),image)
                contacts_list[currentID.toInt()-1].image = BitmapDrawable(resources, bitmap)

                adapter.notifyDataSetChanged()
            }catch(e : Exception){
                Log.e("","Error file")
            }
            CAMERA_REQUEST ->{
                var photo = data?.extras?.get("data") as Bitmap
                var image = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.PNG,100,image)
                setContactPicture(this,currentID.toString(),image)
                contacts_list[currentID.toInt()-1].image = BitmapDrawable(resources, photo)
                adapter.notifyDataSetChanged()

            }
        }

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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            MY_CALL_PHONE)
                }
                return
            }
            MY_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_ExternalStorage)
                }
                return
            }

            MY_ExternalStorage ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    createContacts()
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

            val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)).toLong()))
            if (inputStream != null) {

                contacts_list.add(Contacts(name, number,BitmapDrawable(resources,BitmapFactory.decodeStream(inputStream))))
                inputStream.close()

            }else {
                contacts_list.add(Contacts(name, number))
            }
            //String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //Log.d("RECORD", id + "/" + name + "/" + number);
        }
        for (i in contacts_list.indices) {
            Log.d("RECORD", i.toString() + "/" + contacts_list[i].name + "/" + contacts_list[i].number)
        }
    }
    fun checkPermission() : Boolean{
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
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_ExternalStorage)
        }

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission", "pass")
            return true
        }
        return false
    }
    fun setContactPicture(context:Context ,id : String,bitmap:ByteArrayOutputStream){
        val cr = context.contentResolver
        val rawContactUri = getPicture(context , id)
        if(rawContactUri == null){
            Log.e("rawContactUri", "is null");
            return;
        }
        val values = ContentValues()
        var photoRow = -1
        var where = ContactsContract.Data.RAW_CONTACT_ID + " == " +
                ContentUris.parseId(rawContactUri) + " AND " + ContactsContract.RawContacts.Data.MIMETYPE + "=='" +
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'"
        var cursor = cr.query(ContactsContract.Data.CONTENT_URI,	null, where, null, null);
        var idIdx  = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID)
        if(cursor.moveToFirst()){
            photoRow = cursor.getInt(idIdx);
        }
        cursor.close();
		values.put(ContactsContract.Data.RAW_CONTACT_ID,
				ContentUris.parseId(rawContactUri));
		values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
		values.put(ContactsContract.CommonDataKinds.Photo.PHOTO,bitmap.toByteArray() );
		values.put(ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
		try{
			if(photoRow >= 0){
				cr.update(
						ContactsContract.Data.CONTENT_URI,
						values,
						ContactsContract.Data._ID + " = " + photoRow, null);
			} else {
				cr.insert(
						ContactsContract.Data.CONTENT_URI,
						values);
			}
		}catch(dIOe : SQLException){
			//TODO: should show this to the user..
			dIOe.printStackTrace();
		}
    }
    fun getPicture(context:Context, id:String) : Uri? {
        var cr = context.contentResolver
        var rawContactUri : Uri? = null
        var rawContactCursor = cr.query(ContactsContract.RawContacts.CONTENT_URI, arrayOf( ContactsContract.RawContacts._ID) , ContactsContract.RawContacts.CONTACT_ID + " = " + id, null, null);
        if(!rawContactCursor.isAfterLast()) {
            rawContactCursor.moveToFirst();
            rawContactUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendPath(""+rawContactCursor.getLong(0)).build();
        }
        rawContactCursor.close()

        return rawContactUri
    }
}

