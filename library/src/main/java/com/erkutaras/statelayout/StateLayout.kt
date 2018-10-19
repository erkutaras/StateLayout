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

    fun content(): StateLayout {
        state = State.CONTENT
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.VISIBLE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = GONE
        return this
    }

    fun infoImage(imageRes: Int): StateLayout {
        val image =
                infoLayout?.findViewById<ImageView>(R.id.imageView_state_layout_info)
        image?.setImageResource(imageRes)
        image?.visibility = View.VISIBLE
        return info()
    }

    fun infoTitle(title: String): StateLayout {
        val textViewTitle =
                infoLayout?.findViewById<TextView>(R.id.textView_state_layout_info_title)
        textViewTitle?.text = title
        textViewTitle?.visibility = View.VISIBLE
        return info()
    }

    fun infoMessage(message: String): StateLayout {
        val textViewMessage =
                infoLayout?.findViewById<TextView>(R.id.textView_state_layout_info_message)
        textViewMessage?.text = message
        textViewMessage?.visibility = View.VISIBLE
        return info()
    }

    fun infoButton(buttonText: String, onStateLayoutListener: OnStateLayoutListener?): StateLayout {
        val button = infoLayout?.findViewById<Button>(R.id.button_state_layout_info)
        button?.let { it ->
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

}