package io.github.potors.Imaginator

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.DynamicColors
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {

    private var brightness: Slider? = null
    private var contrast: Slider? = null
    private var temperature: Slider? = null
    private var hue: Slider? = null

    private var image: Image? = null
    private var preview: ImageView? = null

    private val imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val image = Image(applicationContext, uri)

            brightness!!.value = image.brightness.toFloat()
            contrast!!.value = image.contrast.toFloat()
            temperature!!.value = image.temperature.toFloat()
            hue!!.value = image.temperature.toFloat()

            preview!!.setImageBitmap(image.original)

            brightness!!.isEnabled = true
            contrast!!.isEnabled = true
            temperature!!.isEnabled = true
            hue!!.isEnabled = true

            this.image = image
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        DynamicColors.applyToActivityIfAvailable(this)

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val padding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom)
            insets
        }

        brightness = findViewById(R.id.slider_brightness)
        contrast = findViewById(R.id.slider_contrast)
        temperature = findViewById(R.id.slider_temperature)
        hue = findViewById(R.id.slider_hue)

        preview = findViewById(R.id.preview)
        preview!!.setOnClickListener {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        brightness!!.addOnChangeListener(update { brightness = it.toInt() })
        contrast!!.addOnChangeListener(update { contrast = it.toInt() })
        temperature!!.addOnChangeListener(update { temperature = it.toInt() })
        hue!!.addOnChangeListener(update { hue = it.toInt() })
    }

    private fun update(change: Image.(Float) -> Unit): (Slider, Float, Boolean) -> Unit = { _, value, _ ->
        image?.run {
            change(value)
            update { preview?.setImageBitmap(it) }
        }
    }
}