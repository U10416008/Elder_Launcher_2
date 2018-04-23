package com.example.dingjie.elder_launcher_2.UI

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.Game.GameActivity
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.*

class GameUI : AnkoComponent<GameActivity>{
    val TextView.chango: Typeface? get() =
        ResourcesCompat.getFont(this.context, R.font.chango)
    override fun createView(ui: AnkoContext<GameActivity>)= with(ui) {


            verticalLayout {
                textView(R.string.train) {
                    setTypeface(chango)
                    textSize = 50f

                }

                button(R.string.simple) {
                    id = R.id.simple
                    setTypeface(chango)
                    textSize = 50f

                }
                button(R.string.middle) {
                    id = R.id.middle
                    setTypeface(chango)
                    textSize = 50f

                }
                button(R.string.hard) {
                    id = R.id.hard
                    setTypeface(chango)
                    textSize = 50f

                }

            }

    }

}