package com.example.dingjie.elder_launcher_2.reminder

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacesItemDecoration(private var space : Int) : RecyclerView.ItemDecoration() {




    override fun getItemOffsets(outRect: Rect, view: View,
                                 parent:RecyclerView , state:RecyclerView.State ) {
        outRect.left = space
        outRect.bottom = space

    }
}

