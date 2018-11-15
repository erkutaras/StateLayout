package com.erkutaras.statelayout

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by erkutaras on 9.09.2018.
 */
class StateLayout @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var contentLayout: View? = null
    private var loadingLayout: View? = null
    private var infoLayout: View? = null
    private var loadingWithContentLayout: View? = null

    private var state: State = State.CONTENT

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupContentState()
        setupLoadingState()
        setupInfoState()
        setupLoadingWithContentState()

        checkChildCount()
    }

    private fun setupContentState() {
        contentLayout = getChildAt(0)
        contentLayout?.visibility = View.VISIBLE
    }

    private fun setupLoadingState() {
        loadingLayout = inflate(R.layout.layout_state_loading)
        loadingLayout?.visibility = View.GONE
        addView(loadingLayout)
    }

    private fun setupInfoState() {
        infoLayout = inflate(R.layout.layout_state_info)
        infoLayout?.visibility = View.GONE
        addView(infoLayout)
    }

    private fun setupLoadingWithContentState() {
        loadingWithContentLayout = inflate(R.layout.layout_state_loading_with_content)
        loadingWithContentLayout?.visibility = View.GONE
        addView(loadingWithContentLayout)
    }

    fun loading(): StateLayout {
        state = State.LOADING
        loadingLayout?.visibility = View.VISIBLE
        contentLayout?.visibility = View.GONE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = GONE
        return this
    }

    fun showLoadingState(showLoading: Boolean) {
        if (showLoading) {
            loading()
        }
    }

    fun content(): StateLayout {
        state = State.CONTENT
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.VISIBLE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = GONE
        return this
    }

    fun showContentState(showContent: Boolean) {
        if (showContent) {
            content()
        }
    }

    fun infoImage(imageRes: Int): StateLayout {
        infoLayout?.findViewById<ImageView>(R.id.imageView_state_layout_info)?.let {
            it.setImageResource(imageRes)
            it.visibility = View.VISIBLE
        }
        return info()
    }

    fun infoTitle(title: String): StateLayout {
        infoLayout?.findViewById<TextView>(R.id.textView_state_layout_info_title)?.let {
            it.text = title
            it.visibility = View.VISIBLE
        }
        return info()
    }

    fun infoMessage(message: String): StateLayout {
        infoLayout?.findViewById<TextView>(R.id.textView_state_layout_info_message)?.let {
            it.text = message
            it.visibility = View.VISIBLE
        }
        return info()
    }

    fun infoButtonListener(onStateLayoutListener: OnStateLayoutListener?): StateLayout {
        infoLayout?.findViewById<Button>(R.id.button_state_layout_info)?.setOnClickListener {
            onStateLayoutListener?.onStateLayoutInfoButtonClick()
        }
        return info()
    }

    fun infoButtonText(buttonText: String): StateLayout {
        infoLayout?.findViewById<Button>(R.id.button_state_layout_info)?.let {
            it.text = buttonText
            it.visibility = View.VISIBLE
        }
        return info()
    }

    fun infoButton(buttonText: String, onStateLayoutListener: OnStateLayoutListener?): StateLayout {
        infoLayout?.findViewById<Button>(R.id.button_state_layout_info)?.let { it ->
            it.text = buttonText
            it.setOnClickListener { onStateLayoutListener?.onStateLayoutInfoButtonClick() }
            it.visibility = View.VISIBLE
        }
        return info()
    }

    fun info(): StateLayout {
        state = State.INFO
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.GONE
        infoLayout?.visibility = View.VISIBLE
        loadingWithContentLayout?.visibility = GONE
        return this
    }

    fun showInfoState(stateInfo: StateInfo?) {
        stateInfo?.let {
            with(it) {
                infoImage(infoImage)
                infoTitle(infoTitle)
                infoMessage(infoMessage)
                infoButtonText(infoButtonText)
                infoButtonListener(stateInfo.onStateLayoutListener)
                this@StateLayout.state = it.state
            }
        }
    }

    fun loadingWithContent(): StateLayout {
        state = State.LOADING_WITH_CONTENT
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.VISIBLE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = View.VISIBLE
        return this
    }

    private fun checkChildCount() {
        if (childCount > 4 || childCount == 0) {
            throwChildCountException()
        }
    }

    private fun throwChildCountException(): Nothing =
        throw IllegalStateException("StateLayout can host only one direct child")

    private fun inflate(@LayoutRes layoutId: Int): View? {
        return LayoutInflater.from(context).inflate(layoutId, null)
    }

    interface OnStateLayoutListener {
        fun onStateLayoutInfoButtonClick()
    }

    enum class State {
        LOADING, CONTENT, INFO, LOADING_WITH_CONTENT, ERROR, EMPTY
    }

    data class StateInfo(
        val infoImage: Int,
        val infoTitle: String,
        val infoMessage: String,
        val infoButtonText: String,
        val state: StateLayout.State = State.INFO,
        val onStateLayoutListener: StateLayout.OnStateLayoutListener? = null
    )
}