package io.github.potors.Imaginator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.core.graphics.*
import kotlin.math.min
import kotlin.math.max

private fun Int.brightness(percentage: Int): Int {
    return min(this * percentage / 100, 255)
}

private fun Int.contrast(percentage: Int): Int {
    if (percentage < 0) {
        val limit = (100 + percentage) * 255 / 100

        return if (this < limit) this else 255
    } else {
        val limit = percentage * 255 / 100

        return if (this > limit) this else 0
    }
}

private fun Int.temperature(degrees: Int): Int {
    return min(max(0, this + degrees), 255)
}

class Image(private val context: Context, uri: Uri) {

    private val source = ImageDecoder.createSource(context.contentResolver, uri)
    var original = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
    }
        private set

    var brightness = context.resources.getInteger(R.integer.brightness)
    var contrast = context.resources.getInteger(R.integer.contrast)
    var temperature = context.resources.getInteger(R.integer.temperature)
    var hue = context.resources.getInteger(R.integer.hue)

    private var thread: Thread? = null

    fun update(after: Image.(Bitmap) -> Unit) {
        thread?.run {
            if (isAlive) {
                interrupt()
            }
        }

        thread = Thread {
            val current = thread!!

            val pixels = IntArray(original.byteCount)
            original.getPixels(pixels, 0, original.width, 0, 0, original.width, original.height)

            val defaultBrightness = context.resources.getInteger(R.integer.brightness)
            val defaultContrast = context.resources.getInteger(R.integer.contrast)
            val defaultTemperature = context.resources.getInteger(R.integer.temperature)
            val defaultHue = context.resources.getInteger(R.integer.hue)

            for (i in pixels.indices) {
                if (current.isInterrupted) break

                if (brightness != defaultBrightness)
                    pixels[i] = pixels[i].brightness()

                if (contrast != defaultContrast)
                    pixels[i] = pixels[i].contrast()

                if (temperature != defaultTemperature)
                    pixels[i] = pixels[i].temperature()
                if (hue != defaultHue)
                    pixels[i] = pixels[i].hue()
            }

            if (!current.isInterrupted) {
                after(Bitmap.createBitmap(pixels, 0, original.width, original.width, original.height, Bitmap.Config.ARGB_8888))
            }

            Log.i("Filter", "($brightness%, $contrast%, $temperature%) Finished")
        }

        Log.i("Filter", "($brightness%, $contrast%, $temperature%) Started")
        thread!!.start()
    }

    private fun Int.brightness(): Int {
        return Color.argb(
            alpha,
            red.brightness(this@Image.brightness),
            green.brightness(this@Image.brightness),
            blue.brightness(this@Image.brightness),
        )
    }

    private fun Int.contrast(): Int {
        return Color.argb(
            alpha,
            red.contrast(this@Image.contrast),
            green.contrast(this@Image.contrast),
            blue.contrast(this@Image.contrast),
        )
    }

    private fun Int.temperature(): Int {
        return Color.argb(
            alpha,
            red.temperature(this@Image.temperature),
            green,
            blue.temperature(-this@Image.temperature),
        )
    }

    private fun Int.hue(): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(this, hsv)

        hsv[0] = (hsv[0] + hue) % 360

        return Color.HSVToColor(hsv)
    }
}
