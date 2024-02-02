package com.swipeapp.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

fun <T> List<T>?.toJson():String? {
    if (this == null) return null

    val gson = Gson()
    val type: Type = object : TypeToken<List<T>?>() {}.type
    return gson.toJson(this, type)
}

fun <T> String?.fromJsonToList():List<T>? {
    if (this == null) return null

    val gson = Gson()
    val type: Type = object : TypeToken<List<T>?>() {}.type
    return gson.fromJson<List<T>>(this, type)
}

fun OffsetDateTime.getCurrentTimeStamp():String {
    return this.toString().convertTimeStamp()
}

fun String.setCharLimitEllipse(charLimit:Int):String {
    var newString = this
    if(this.length > charLimit) {
        newString = this.substring(0, charLimit-1)+"..."
    }

    return newString
}

fun String.convertTimeStamp():String {
    val str = this.split(".")[0]
    return str.replace("T", " ")
}

fun TextView.setCustomTextAppearance(@StyleRes resId:Int) {
    if (Build.VERSION.SDK_INT < 23) {
        this.setTextAppearance(context, resId);
    } else {
        this.setTextAppearance(resId);
    }
}

fun Context.getPxToDp(sizeInDp: Int): Int {
    val scale = resources.displayMetrics.density

    return (sizeInDp * scale + 0.5f).toInt()
}

fun Context.getPxToDp(sizeInDp: Float): Float {
    val scale = resources.displayMetrics.density

    return (sizeInDp * scale + 0.5f).toFloat()
}

fun Int.getMonth():String {
    return when(this) {
        0 -> {
           "January"
        }
        1 -> {
            "February"
        }
        2 -> {
            "March"
        }
        3 -> {
            "April"
        }
        4 -> {
            "May"
        }
        5 -> {
            "June"
        }
        6 -> {
            "July"
        }
        7 -> {
            "August"
        }
        8 -> {
            "September"
        }
        9 -> {
            "October"
        }
        10 -> {
            "November"
        }
        11 -> {
            "December"
        }else -> {
            ""
        }
    }
}

fun TextView.underline(s:String) {
    val text = SpannableString(s)
    text.setSpan(UnderlineSpan(), 0, text.length, 0)

    this.text = text
}

fun TextView.strikeThrough(s:String) {
    text = s
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

    this.text = text
}

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = this.getSystemService(
        INPUT_METHOD_SERVICE
    ) as InputMethodManager

    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Activity.openSoftKeyboard() {
    val inputMethodManager = this.getSystemService(
        INPUT_METHOD_SERVICE
    ) as InputMethodManager

    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.showSoftInput(
            this.currentFocus,
            0
        )
    }
}


/**
 * Context extension method to check network connected or not
 * @return boolean if network not connected at that time it returns false
 */
fun Context.isOnline(): Boolean {
//    var result = false
//    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val networkCapabilities = connectivityManager.activeNetwork ?: return false
//        val actNw =
//            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
//        result = when {
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            else -> false
//        }
//    } else {
//        connectivityManager.activeNetworkInfo?.run {
//            result = when (type) {
//                ConnectivityManager.TYPE_WIFI -> true
//                ConnectivityManager.TYPE_MOBILE -> true
//                ConnectivityManager.TYPE_ETHERNET -> true
//                else -> false
//            }
//
//        }
//    }
//    return result

    var isOnline = false
    val manager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    try {
        isOnline = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            val activeNetworkInfo = manager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return isOnline
}

fun Context.from24To12Hours(time24Hour:String?):String? {
    return LocalTime.parse(time24Hour, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"))
}

fun Context.from12To24Hours(time12Hour:String?):String? {

//    return try {
//        val date24HourFormat = SimpleDateFormat("HH:mm")
//        val date12HourFormat = SimpleDateFormat("hh:mm a")
//
//        val _12HourDt: Date? = time12Hour?.let { date12HourFormat.parse(it) }
//
//        _12HourDt?.let { date24HourFormat.format(it) }
//    }catch (e:java.lang.Exception) {
//
//        e.printStackTrace()
//        null
//    }
    return LocalTime.parse(time12Hour, DateTimeFormatter.ofPattern("hh:mm a")).format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun Context.hhMMSSToMinutes(duration:String?):Long {

//    return try {
//        val dateFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
//        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
//        val date: Date? = duration?.let { dateFormat.parse(it) }
//
//        date?.time?.div(60000L)
//    }catch (e:java.lang.Exception) {
//
//        e.printStackTrace()
//        null
//    }

    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH)
    val localDate: LocalDateTime = LocalDateTime.parse(duration, formatter)
    return localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli().div(60000L)

}

fun String.getTimeInMillsFromDateAndTime(): Long {

    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val localDate: LocalDateTime = LocalDateTime.parse(this, formatter)
        return localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()

    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

fun String.parseDate():String {

    val dateSplit = this.split("-")

    val year = dateSplit[0]
    val day = dateSplit[2]
    val month = dateSplit[1].toInt().getMonth()

    return "$month $day, $year"
}

fun Context.copyToClip(text:String){
    val clipboard: ClipboardManager? =
        this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("Word", text)
    clipboard?.setPrimaryClip(clip)
}

fun getTimeFromMillis() {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    val timeInMili = calendar.timeInMillis
}

