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
import com.erkutaras.statelayout.StateLayout.State.*

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

    private var state: State = NONE

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupContentState()
        setupLoadingState()
        setupInfoState()
        setupLoadingWithContentState()

        updateWithState()
        checkChildCount()
    }

    private fun setupContentState() {
        contentLayout = getChildAt(0)
        contentLayout?.visibility = View.GONE
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

    private fun updateWithState() {
        when (state) {
            LOADING -> loading()
            CONTENT -> content()
            INFO, ERROR, EMPTY -> info()
            LOADING_WITH_CONTENT -> loadingWithContent()
            NONE -> hideAll()
        }
    }

    private fun checkChildCount() {
        if (childCount > 4 || childCount == 0) {
            throwChildCountException()
        }
    }

    private fun hideAll() {
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.GONE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = GONE
    }

    private fun throwChildCountException(): Nothing =
        throw IllegalStateException("StateLayout can host only one direct child")

    private fun inflate(@LayoutRes layoutId: Int): View? {
        return LayoutInflater.from(context).inflate(layoutId, null)
    }

    fun initialState(state: State) {
        this.state = state
    }

    fun loading(): StateLayout {
        state = LOADING
        loadingLayout?.visibility = View.VISIBLE
        contentLayout?.visibility = View.GONE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = GONE
        return this
    }

    fun showLoading(stateInfo: StateInfo?) {
        showState(stateInfo)
    }

    fun showContent(stateInfo: StateInfo?) {
        showState(stateInfo)
    }

    fun showInfo(stateInfo: StateInfo?) {
        showState(stateInfo)
    }

    fun showEmpty(stateInfo: StateInfo?) {
        showState(stateInfo)
    }

    fun content(): StateLayout {
        state = CONTENT
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.VISIBLE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = GONE
        return this
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

    fun infoButtonListener(block: () -> Unit) {
        infoLayout?.findViewById<Button>(R.id.button_state_layout_info)?.setOnClickListener {
            block.invoke()
        }
        info()
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
        state = INFO
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.GONE
        infoLayout?.visibility = View.VISIBLE
        loadingWithContentLayout?.visibility = GONE
        return this
    }

    fun showState(stateInfo: StateInfo?) {
        when (stateInfo?.state) {
            LOADING -> loading()
            CONTENT -> content()
            LOADING_WITH_CONTENT -> loadingWithContent()
            INFO, ERROR, EMPTY -> {
                stateInfo.infoImage?.let { infoImage(it) }
                stateInfo.infoTitle?.let { infoTitle(it) }
                stateInfo.infoMessage?.let { infoMessage(it) }
                stateInfo.infoButtonText?.let { infoButtonText(it) }
            }
            null, NONE -> hideAll()
        }
    }

    fun loadingWithContent(): StateLayout {
        state = LOADING_WITH_CONTENT
        loadingLayout?.visibility = View.GONE
        contentLayout?.visibility = View.VISIBLE
        infoLayout?.visibility = View.GONE
        loadingWithContentLayout?.visibility = View.VISIBLE
        return this
    }

    companion object {
        @JvmStatic
        fun provideLoadingStateInfo() = StateInfo(state = LOADING)

        @JvmStatic
        fun provideContentStateInfo() = StateInfo(state = CONTENT)

        @JvmStatic
        fun provideErrorStateInfo() = StateInfo(state = ERROR)

        @JvmStatic
        fun provideLoadingWithContentStateInfo() = StateInfo(state = LOADING_WITH_CONTENT)

        @JvmStatic
        fun provideInfoStateInfo() = StateInfo(state = INFO)

        @JvmStatic
        fun provideEmptyStateInfo() = StateInfo(state = EMPTY)

        @JvmStatic
        fun provideNoneStateInfo() = StateInfo(state = NONE)
    }

    interface OnStateLayoutListener {
        fun onStateLayoutInfoButtonClick()
    }

    enum class State {
        LOADING, CONTENT, INFO, LOADING_WITH_CONTENT, ERROR, EMPTY, NONE
    }

    data class StateInfo(
        val infoImage: Int? = null,
        val infoTitle: String? = null,
        val infoMessage: String? = null,
        val infoButtonText: String? = null,
        val state: StateLayout.State = INFO,
        val onStateLayoutListener: StateLayout.OnStateLayoutListener? = null
    )
}