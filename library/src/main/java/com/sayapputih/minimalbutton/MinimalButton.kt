package com.sayapputih.minimalbutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.util.TypedValue

class MinimalButton @JvmOverloads constructor(context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.buttonStyle) :
    AppCompatButton(initMinimalDrawable(context, attrs, defStyleAttr), attrs, defStyleAttr) {

    companion object {

        private var inBackground: Drawable? = null

        private fun initMinimalDrawable(context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int): Context = context.apply {

            val basicDrawable =
                obtainStyledAttributes(attrs, R.styleable.MinimalButton, defStyleAttr, 0).let {
                    it.getDrawable(R.styleable.MinimalButton_sp_background).apply { it.recycle() }
                }

            // Use ripple drawable if SDK >= 21
            inBackground = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    obtainStyledAttributes(TypedValue().data,
                        intArrayOf(R.attr.colorControlHighlight)).let { a ->
                        val color = a.getColor(0, 0)
                        RippleDrawable(ColorStateList.valueOf(color), basicDrawable,
                            null).also { a.recycle() }
                    }
                }
                else -> basicDrawable
            }
        }
    }

    override fun setBackground(background: Drawable) {
        super.setBackground(if (inBackground != null) inBackground else background)
        inBackground = null
    }
}
