package com.caretracker.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

class SparklineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var values: List<Float> = emptyList()
    private var lineColor: Int = Color.parseColor("#4f9cf9")

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2a2a2a")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    fun setData(newValues: List<Float>, color: Int) {
        values = newValues
        lineColor = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = paddingLeft.toFloat() + 8f
        val top = paddingTop.toFloat() + 8f
        val right = width - paddingRight.toFloat() - 8f
        val bottom = height - paddingBottom.toFloat() - 8f
        val w = right - left
        val h = bottom - top

        if (w <= 0 || h <= 0) return

        canvas.drawLine(left, top, right, top, gridPaint)
        canvas.drawLine(left, top + h / 2f, right, top + h / 2f, gridPaint)
        canvas.drawLine(left, bottom, right, bottom, gridPaint)

        if (values.isEmpty()) return

        val minV = values.minOrNull() ?: 0f
        val maxV = values.maxOrNull() ?: 0f
        val range = max(1f, maxV - minV)

        val path = Path()
        val fillPath = Path()

        values.forEachIndexed { i, v ->
            val x = if (values.size == 1) left + w / 2f else left + (i.toFloat() / (values.size - 1)) * w
            val normalized = (v - minV) / range
            val y = bottom - (normalized * h)

            if (i == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, bottom)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        val lastX = if (values.size == 1) left + w / 2f else right
        fillPath.lineTo(lastX, bottom)
        fillPath.close()

        linePaint.color = lineColor
        fillPaint.color = adjustAlpha(lineColor, 0.18f)
        pointPaint.color = lineColor

        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(path, linePaint)

        values.forEachIndexed { i, v ->
            val x = if (values.size == 1) left + w / 2f else left + (i.toFloat() / (values.size - 1)) * w
            val normalized = (v - minV) / range
            val y = bottom - (normalized * h)
            canvas.drawCircle(x, y, 5f, pointPaint)
        }
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = min(255, max(0, (Color.alpha(color) * factor).toInt()))
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }
}
