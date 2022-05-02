package com.soninhodobeb.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.soninhodobeb.R

class SelectorButton(ctx: Context, attrs: AttributeSet? = null) : ConstraintLayout(ctx, attrs) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.selector_button, this)
        val styleAttributes = context.obtainStyledAttributes(attrs, R.styleable.SelectorButton)
        try {
            val text = styleAttributes.getText(R.styleable.SelectorButton_text)
            val drawableId = styleAttributes.getResourceId(R.styleable.SelectorButton_image, 0)
            if (drawableId != 0) {
                findViewById<TextView>(R.id.selector_title).text = text
                findViewById<ImageView>(R.id.img_selector).setImageResource(drawableId)
            }
        } finally {
            styleAttributes.recycle()
        }
    }

    fun setText(text: String) {
        findViewById<TextView>(R.id.selector_title).text = text
    }

    fun setImage(drawable: Drawable?) {
        findViewById<ImageView>(R.id.img_selector).setImageDrawable(drawable)
    }
}