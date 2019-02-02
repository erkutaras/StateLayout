package com.erkutaras.statelayout

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * Created by erkutaras on 2.02.2019.
 */

internal fun <T : View> View?.findView(@IdRes id: Int, block: T.() -> Unit) {
    this?.findViewById<T>(id)?.let { block(it) }
}

internal fun View.inflate(@LayoutRes layoutId: Int): View? {
    return LayoutInflater.from(this.context).inflate(layoutId, null)
}