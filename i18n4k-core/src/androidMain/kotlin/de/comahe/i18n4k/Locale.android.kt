package de.comahe.i18n4k

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat

/**
 * Current locale of the device (system-wide).
 * Note: This API doesn't support Per-app language introduced in Android 13, please refer to the
 * [documentation](https://developer.android.com/guide/topics/resources/app-languages) for getting per-app locale.
 *
 * [ConfigurationCompat.getLocales] should return a list with at least one item, but if the list is empty we fallback to jvm's system locale.
 * */
actual val systemLocale: Locale
    get() = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0) ?: jvmSystemLocale
