package com.echoofyourvoice.android.criminalintent

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import kotlin.math.round

class PictureUtils {
    companion object {
        private fun getScaledBitMap(path: String, destWidth: Int, destHeight: Int): Bitmap {
            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            val srcWidth = options.outWidth
            val srcHeight = options.outHeight

            var inSampleSize = 1F
            if (srcHeight > destHeight || srcWidth > destWidth) {
                val heightScale = (srcHeight / destHeight).toFloat()
                val widthScale = (srcWidth / destWidth).toFloat()
                inSampleSize = round(if (heightScale > widthScale) heightScale else widthScale)
            }
            options = BitmapFactory.Options()
            options.inSampleSize = inSampleSize.toInt()

            return BitmapFactory.decodeFile(path, options)
        }

        fun getScaledBitMap(path: String, activity: Activity): Bitmap {
            val size = Point()
            activity.windowManager.defaultDisplay.getSize(size)
            return getScaledBitMap(path, size.x, size.y)
        }
    }
}