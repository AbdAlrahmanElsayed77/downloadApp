package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var valueOfProgress = 0.0
    private var valueOfAnimator = ValueAnimator()

    private var StateOfButton: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
    }

    private val rect = RectF()
    private val textBoundRect = Rect()

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        valueOfProgress = (it.animatedValue as Float).toDouble()
        invalidate()
        if (valueOfProgress == 100.0) {
            onDownloadComplete()
        }
    }

    fun onDownloadComplete() {
        valueOfAnimator.cancel()
        StateOfButton = ButtonState.Completed
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //text
        val Text = if (StateOfButton == ButtonState.Loading) {
            resources.getString(R.string.button_loading)
        } else{"download"}
        canvas?.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), draw)
        //loadingBar
        if (StateOfButton == ButtonState.Loading) {
            draw.color = resources.getColor(R.color.colorPrimaryDark)
            canvas?.drawRect(0f, 0f, (widthSize * (valueOfProgress / 100)).toFloat(), height.toFloat(), draw)
            draw.getTextBounds(Text,0,Text.length,textBoundRect)
            val vertical = measuredWidth.toFloat() / 2
            val horizontal = measuredHeight.toFloat() / 2
            //circle
            draw.color = resources.getColor(R.color.colorAccent)
            rect.set(vertical+textBoundRect.right/2+40.0f, 30.0f, vertical+textBoundRect.right/2+80.0f, measuredHeight.toFloat() -25.0f )
            canvas?.drawArc(rect, 0f, (360 * (valueOfProgress / 100)).toFloat(), true, draw)
        }
        //after download is complete
        else if (StateOfButton == ButtonState.Completed) {
            draw.color = resources.getColor(R.color.colorPrimary)
            canvas?.drawRect(0f, 0f, (widthSize * (valueOfProgress / 100)).toFloat(), heightSize.toFloat(), draw)
        }
        //default text
        draw.color = Color.WHITE
        canvas?.drawText(Text, (width / 2).toFloat(), ((height) / 2).toFloat(), draw)
        draw.color = resources.getColor(R.color.colorPrimary)
    }


    private val draw = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 35.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = resources.getColor(R.color.colorPrimary)
    }

    init {
        isClickable = true
        valueOfAnimator = AnimatorInflater.loadAnimator(context, R.animator.loading) as ValueAnimator
        valueOfAnimator.addUpdateListener(updateListener)
    }

    override fun performClick(): Boolean {
        super.performClick()
        StateOfButton = ButtonState.Loading
        valueOfAnimator.start()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthSize = w
        heightSize = h
    }
}