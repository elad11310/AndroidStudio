package com.example.test2

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnNewActivity: Button
    private lateinit var btnListActivity: Button
    private lateinit var btnPhoto: Button
    private lateinit var btnNotify: Button
    private lateinit var image: ImageView
    private lateinit var airplaneModeChangeReceiver: BroadcastReceiver

    // for camera
    private val STORAGE_PERMISSION_CODE = 1
    private val CAMERA_REQUEST_CODE = 0
    private val IMAGE_REQUEST = 1
    lateinit var bitmap: Bitmap
    private var imageUri: Uri? = null


    // for notification
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNewActivity = findViewById(R.id.buttonNewActivity);
        btnListActivity = findViewById(R.id.buttonListActivity)
        btnPhoto = findViewById(R.id.buttonImage)
        btnNotify =findViewById(R.id.buttonNotify)
        image = findViewById(R.id.imageViewPic)
        airplaneModeChangeReceiver = AirplaneModeChangeReceiver()


        btnNewActivity.setOnClickListener(this)
        btnListActivity.setOnClickListener(this)
        btnPhoto.setOnClickListener(this)
        btnNotify.setOnClickListener(this)

        var p: Point = Point(10, 12)
        p.print()
    }

    override fun onClick(p0: View?) {
        if (p0 == btnNewActivity) {
            val i = Intent(applicationContext, ActivitySecond::class.java)
            startActivity(i)
        } else if (p0 == btnListActivity) {
            val i = Intent(applicationContext, ListActivity::class.java)
            startActivity(i)
        } else if (p0 == btnPhoto) {

            onUploadOrCaptureClick()

        }
        else{
            makeNotification()
        }

    }

    private fun makeNotification() {
        // it is a class to notify the user of events that happen.
        // This is how you tell the user that something has happened in the
        // background.
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // pendingIntent is an intent for future use i.e after
        // the notification is clicked, this intent will come into action
        val intent = Intent(this, AfterNotification::class.java)


        // FLAG_UPDATE_CURRENT specifies that if a previous
        // PendingIntent already exists, then the current one
        // will update it with the latest intent
        // 0 is the request code, using it later with the
        // same method again will get back the same pending
        // intent for future reference
        // intent passed here is to our afterNotification class
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout
        val contentView = RemoteViews(packageName, R.layout.activity_after_notification)

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())

    }


    fun onUploadOrCaptureClick() {
        var userChoosenTask: String? = null

        Log.v("Elad", "GEEEE2")
        // need to ask for permission


        // first we check if the permission was granted.
        if (ContextCompat.checkSelfPermission(
                this.application,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val items = arrayOf<CharSequence>(
                "Take Photo", "Choose from gallery", "Cancel"
            )
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Photo")
            builder.setItems(items) { dialogInterface, i -> // boolean result = Utility
                if (items[i] == "Take Photo") {
                    userChoosenTask = "Take Photo"
                    cameraIntent()
                } else if (items[i] == "Choose from gallery") {
                    userChoosenTask = "Choose from gallery"

                    galleryIntent()
                } else if (items[i] == "Cancel") {
                    dialogInterface.dismiss()
                }
            }
            builder.show()
        } else {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {

        // if the user Deny the permission before we want to open dialog to explain why we ask permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {

            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permisiion is needed because of this and that")
                .setPositiveButton(
                    "ok"
                ) { dialogInterface, i -> //ActivityCompat.requestPermissions(FirstTimeLogin.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton(
                    "cancel"
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .create().show()
        } else {
            // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                STORAGE_PERMISSION_CODE
            )

        }
    }

    private fun cameraIntent() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)

    }

    private fun galleryIntent() {

        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), IMAGE_REQUEST)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imageUri = data!!.data
            if (requestCode == IMAGE_REQUEST) {
                onSelectFromHalleryResult(data)
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                onCaptureImageResult(data)
            }
        }
    }

    fun onCaptureImageResult(data: Intent) {
        bitmap = (data.extras!!["data"] as Bitmap?)!!
        val bytes = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val des: File = File(
            this.filesDir,
            System.currentTimeMillis().toString() + "jpg"
        )
        var fo: FileOutputStream? = null
        try {
            des.createNewFile()
            fo = FileOutputStream(des)
            fo.write(bytes.toByteArray())
            fo.close()
            imageUri = Uri.fromFile(des)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        image.setImageBitmap(bitmap)
    }

    fun onSelectFromHalleryResult(data: Intent?) {
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                    this?.getContentResolver(),
                    data.data
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            image.setImageBitmap(bitmap)

        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeChangeReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(airplaneModeChangeReceiver)
    }

}