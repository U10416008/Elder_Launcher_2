package com.example.dingjie.elder_launcher_2.Contact

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import com.example.dingjie.elder_launcher_2.R
import com.example.dingjie.elder_launcher_2.UI.RecordUI
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import java.util.*

class AudioRecordActivity :AppCompatActivity(){
    private var mFileName :String  = ""
    var mediaRecord : MediaRecorder? = null
    var mediaPlayer :MediaPlayer? = null
    lateinit var playButton:Button
    lateinit var progressSeekBar: SeekBar
    private var isSeeking = false
    var started  = false
    var prepared = false
    var longPress = false
    var threadStart = false
    var startClickTime : Long = 0
    val thread = Thread(Runnable {
        while (true) {
            Thread.sleep(500)
            if (!isSeeking && mediaPlayer != null) {
                progressSeekBar.progress = mediaPlayer!!.currentPosition
                //Log.e("Current",""+progressSeekBar.progress)
            }
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFileName = externalCacheDir.absolutePath
        val filename: String? = intent.getStringExtra("contact")

        if(filename != null) {
            mFileName = "$mFileName/$filename.3gp"
        }else
            mFileName += "/hello.3gp"

        RecordUI().setContentView(this)

        initListener()



    }
    @SuppressLint("ClickableViewAccessibility")
    fun initListener(){
        progressSeekBar = find<SeekBar>(R.id.seek)
        playButton =find<Button>(R.id.play_record)
        playButton.setOnClickListener(View.OnClickListener{
            play()
        })
        val startButton =find<Button>(R.id.start_record)

        startButton.setOnLongClickListener{
            if(!started) {
               // start()
            }
            true
        }
        startButton.setOnTouchListener{v:View,m:MotionEvent->
            when(m.action){
                MotionEvent.ACTION_UP->{
                    if(longPress){
                        var clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime
                        if(clickDuration >= 500){
                            if(started) {
                                toast("Finish")

                                stop()
                            }
                        }else{
                            mediaRecord?.reset()
                            started = false

                        }

                        longPress = false

                    }

                    true
                }
                MotionEvent.ACTION_DOWN ->{
                    if(!longPress){
                        longPress = true
                        startClickTime = Calendar.getInstance().timeInMillis
                        if(!started) {
                            toast("Start")
                            start()
                        }
                    }

                    true
                }

                MotionEvent.ACTION_MOVE->{
                    /*if(longPress){
                        var clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime
                        if(clickDuration >= 500){
                            toast("Long Press")
                            longPress = false
                        }
                    }*/
                    true
                }
                else -> false
            }
        }
        progressSeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                if(isSeeking) {
                    mediaPlayer?.seekTo(progressSeekBar.progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                isSeeking = false
            }

        })

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
            playButton.backgroundResource = R.drawable.play_button
        }else if(!prepared){
            playButton.backgroundResource = R.drawable.pause
            try {
                mediaPlayer?.setDataSource(mFileName)
                mediaPlayer?.prepare()
                prepared = true
            } catch(e: Exception) {
                Log.e("PlayRecording","Failed")
            }
        }else{
            mediaPlayer?.start()
            playButton.backgroundResource = R.drawable.pause

        }
    }
    private var playerPreparedHandler = MediaPlayer.OnPreparedListener {
        mediaPlayer?.start()
        progressSeekBar.max = mediaPlayer!!.duration
        Log.e("MAX",""+progressSeekBar.max)
        if(!threadStart) {
            thread.start()
            threadStart = true
        }
    }
    private fun stopPlayer() {
        mediaPlayer?.release()
        progressSeekBar.progress = 0
        prepared = false
        mediaPlayer = null
        playButton.backgroundResource = R.drawable.play_button


    }
    fun start(){
        mediaRecord = MediaRecorder()
        mediaRecord?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecord?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecord?.setOutputFile(mFileName)
        mediaRecord?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            mediaRecord?.prepare()
        }catch (e: Exception){
            Log.e("Err","prepare fail")
        }
        mediaRecord?.start()
        started = true

    }
    fun stop(){

            mediaRecord?.stop()
            mediaRecord?.release()
            mediaRecord = null
            started = false
            Log.d("NAME", mFileName)
            Log.d("Complete", "OK")

    }

    override fun onStop() {
        super.onStop()
    }
}