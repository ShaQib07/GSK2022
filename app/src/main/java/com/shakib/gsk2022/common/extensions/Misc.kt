package com.shakib.gsk2022.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

inline fun <reified T> Any?.cast(): T? = if (this is T) this else null

fun Context.showShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showLongToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun ImageView.loadImageFromUrl(url: String) =
    Glide
        .with(this.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .into(this)

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun URL.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeStream(openStream())
    } catch (e: IOException) {
        null
    }
}

fun <T> Fragment.collectFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        // repeatOnLifecycle launches the block in a new coroutine every time the
        // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            // Trigger the flow and start listening for values.
            // Note that this happens when lifecycle is STARTED and stops
            // collecting when the lifecycle is STOPPED
            flow.collectLatest(collect)
        }
    }
}
