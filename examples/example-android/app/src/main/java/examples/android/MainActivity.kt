package examples.android

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.providers.MessagesProviderViaText
import examples.android.databinding.ActivityMainBinding
import x.y.MyMessages

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val buttons = mutableMapOf<Locale, RadioButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initI18n4k(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (locale in MyMessages.locales) {
            val button = RadioButton(this)
            button.text = locale.getDisplayNameInLocale()
            button.setOnClickListener {
                i18n4kConfig.locale = locale
                updateUI()
            }
            binding.radioGroupLanguages.addView(button)
            buttons[locale] = button
        }
        binding.textName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                updateUI()
            }

        })

        updateUI()
    }

    private fun updateUI() {
        binding.labelTitle.text = MyMessages.title()
        binding.labelHello.text = MyMessages.sayHello(binding.textName.text)
        buttons.forEach {
            it.value.isSelected = it.key == i18n4kConfig.locale
        }
    }


    companion object {
        private var inited = false

        val i18n4kConfig = I18n4kConfigDefault()

        fun initI18n4k(context: Context) {
            if (inited)
                return

            i18n4k = i18n4kConfig

            // load "fr", "nl" and "en_US_texas" at runtime
            MyMessages.registerTranslation(
                MessagesProviderViaText(
                    text = String(
                        context.resources.openRawResource(
                            R.raw.x_y_my_messages_fr_i18n4k
                        ).readBytes()
                    )
                )
            )
            MyMessages.registerTranslation(
                MessagesProviderViaText(
                    text = String(
                        context.resources.openRawResource(
                            R.raw.x_y_my_messages_nl_i18n4k
                        ).readBytes()
                    )
                )
            )
            MyMessages.registerTranslation(
                MessagesProviderViaText(
                    text = String(
                        context.resources.openRawResource(
                            R.raw.x_y_my_messages_en_us_texas_i18n4k
                        ).readBytes()
                    )
                )
            )
            inited = true
        }
    }


}