package com.example.dingjie.elder_launcher_2.UI

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.Contact.AudioRecordActivity
import com.example.dingjie.elder_launcher_2.MainActivity
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.*

class RecordUI : AnkoComponent<AudioRecordActivity>{
    val TextView.chango: Typeface? get() =
        ResourcesCompat.getFont(this.context, R.font.chango)
    override fun createView(ui: AnkoContext<AudioRecordActivity>) = with(ui){
        verticalLayout{
            linearLayout {
                button {
                    backgroundResource = R.drawable.play_button
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                    scaleX /= 2
                    scaleY /= 2

                    id = R.id.play_record
                }
            }.lparams(width = matchParent,height = matchParent,weight = 0.2f)
            seekBar {
                id = R.id.seek

            }.lparams(width = matchParent,height = matchParent,weight = 0.3f)
            linearLayout {
                button {
                    backgroundResource = R.drawable.microphone
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                    id = R.id.start_record
                    scaleX /= 2
                    scaleY /= 2
                }
            }.lparams(width = matchParent,height = matchParent,weight = 0.2f)
            button(R.string.store){
                backgroundResource = R.drawable.store_button
                typeface = chango
                textColor = Color.BLACK

                textSize = 32f
            }.lparams(width = matchParent,height = matchParent,weight = 0.3f)
        }
    }

}