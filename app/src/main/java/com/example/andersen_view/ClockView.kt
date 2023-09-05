package com.example.andersen_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ClockView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var isInit = false
    private var radius = 0f
    private val numbers = (1..12).toList()
    private val rect = Rect()
    private val paint = Paint()

    companion object {
        const val HOURS_HAND_LENGTH = 0.3f
        const val MINUTES_HAND_LENGTH = 0.5f
        const val SECONDS_HAND_LENGTH = 0.7f

        const val HOURS_HAND_COLOR = Color.BLUE
        const val MINUTES_HAND_COLOR = Color.RED
        const val SECONDS_HAND_COLOR = Color.GREEN

        const val HOURS_HAND_THICKNESS = 15f
        const val MINUTES_HAND_THICKNESS = 10f
        const val SECONDS_HAND_THICKNESS = 5f
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawHands(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)

        postInvalidateDelayed(1000)
        invalidate()
    }

    private fun initClock() {
        val height = height
        val width = width
        val padding = 20 // Общий отступ
        val minDimen = Math.min(height, width)
        radius = (minDimen / 2 - padding).toFloat()
        isInit = true
    }

    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        drawHand(
            canvas,
            (hour + minute / 60f) * 5f,
            HOURS_HAND_COLOR,
            HOURS_HAND_LENGTH,
            HOURS_HAND_THICKNESS
        )

        drawHand(
            canvas,
            minute.toFloat(),
            MINUTES_HAND_COLOR,
            MINUTES_HAND_LENGTH,
            MINUTES_HAND_THICKNESS
        )

        drawHand(
            canvas,
            second.toFloat(),
            SECONDS_HAND_COLOR,
            SECONDS_HAND_LENGTH,
            SECONDS_HAND_THICKNESS
        )
    }

    private fun drawHand(
        canvas: Canvas,
        loc: Float,
        color: Int,
        lengthMultiplier: Float,
        thickness: Float
    ) {
        val angle = Math.toRadians(((loc - 15) * 360 / 60).toDouble()).toFloat()
        val handLength = radius * lengthMultiplier
        paint.color = color
        paint.strokeWidth = thickness
        canvas.drawLine(
            width / 2f,
            height / 2f,
            width / 2f + cos(angle) * handLength,
            height / 2f + sin(angle) * handLength,
            paint
        )
    }

    private fun drawNumeral(canvas: Canvas) {
        val fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            20F,
            resources.displayMetrics
        ).toInt()
        paint.textSize = fontSize.toFloat()
        val fontMetrics = paint.fontMetrics
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        val paddingFromCircle = 20 // Отступ от круга

        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * (radius - fontHeight / 2 - paddingFromCircle) - rect.width() / 2)
            val y = (height / 2 + sin(angle) * (radius - fontHeight / 2 - paddingFromCircle) + fontHeight / 4)
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        canvas.drawCircle(width / 2f, height / 2f, 12F, paint)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset()
        paint.color = Color.BLACK
        paint.strokeWidth = 15f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }


}
