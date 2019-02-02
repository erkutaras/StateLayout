package com.erkutaras.statelayout

import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * Created by erkutaras on 2.02.2019.
 */
internal fun <T : View> View?.findView(@IdRes id: Int, block: T.() -> Unit) {
    this?.findViewById<T>(id)?.let { block.invoke(it) }
}

internal fun View.inflate(@LayoutRes layoutId: Int): View? {
    return LayoutInflater.from(this.context).inflate(layoutId, null)
}

internal fun View?.visible() = visible { }

internal fun View?.visible(block: (View) -> Unit) {
    this?.apply {
        visibility = View.VISIBLE
        block.invoke(this)
    }
}

internal fun View?.gone() = gone { }

internal fun View?.gone(block: (View) -> Unit) {
    this?.apply {
        visibility = View.GONE
        block.invoke(this)
    }
}

internal fun View?.startViewAnimation(@IdRes id: Int, animation: Animation?) {
    animation?.let {
        findView<View>(id) {
            clearAnimation()
            startAnimation(it)
        }
    }
}

internal fun View?.clearViewAnimation(@IdRes id: Int) {
    findView<View>(id) { clearAnimation() }
}

internal fun Int.notZero(function: (i: Int) -> Unit) {
    if (this != 0) function(this)
}