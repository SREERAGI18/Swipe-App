package com.swipeapp.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.util.regex.Matcher
import java.util.regex.Pattern


fun getColor(context: Context, @ColorRes colorRes: Int):Int = ContextCompat.getColor(context, colorRes)
fun getDrawable(context: Context, @DrawableRes drawableRes: Int):Drawable? = ContextCompat.getDrawable(context, drawableRes)

fun View.showWithAnimation() {
    this.animate()
//        .translationY(0f)
        .alphaBy(1f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@showWithAnimation.visibility = View.VISIBLE
            }
        })
}

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}

fun isValidEmail(email:String): Boolean {

    val pattern: Pattern
    val EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
    pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher: Matcher = pattern.matcher(email)

    return (!TextUtils.isEmpty(email) && matcher.matches())
}

fun isValidPassword(password: CharSequence): Boolean {
//    val pattern: Pattern
//    val PASSWORD_PATTERN = "^[a-z]{5}$"
////        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{2,}$"
//    pattern = Pattern.compile(PASSWORD_PATTERN)
//    val matcher: Matcher = pattern.matcher(password)
//    return matcher.matches()

    return password.length > 4
}


