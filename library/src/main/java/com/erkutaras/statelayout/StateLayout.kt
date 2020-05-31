package com.erkutaras.statelayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
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

    @LayoutRes
    private var loadingLayoutRes: Int = R.layout.layout_state_loading
    @LayoutRes
    private var infoLayoutRes: Int = R.layout.layout_state_info
    @LayoutRes
    private var loadingWithContentLayoutRes: Int = R.layout.layout_state_loading_with_content

    private var loadingAnimation: Animation? = null
    private var loadingWithContentAnimation: Animation? = null

    init {
        if (isInEditMode) {
            state = CONTENT
        }

        context.theme.obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0)
            .apply {
                try {
                    state = State.values()[getInteger(R.styleable.StateLayout_sl_state, NONE.ordinal)]
                    loadingLayoutRes = getResourceId(R.styleable.StateLayout_sl_loadingLayout, R.layout.layout_state_loading)
                    infoLayoutRes = getResourceId(R.styleable.StateLayout_sl_infoLayout, R.layout.layout_state_info)
                    loadingWithContentLayoutRes = getResourceId(R.styleable.StateLayout_sl_loadingWithContentLayout, R.layout.layout_state_loading_with_content)

                    getResourceId(R.styleable.StateLayout_sl_loadingAnimation, 0).notZero {
                        loadingAnimation = AnimationUtils.loadAnimation(context, it)
                    }
                    getResourceId(R.styleable.StateLayout_sl_loadingWithContentAnimation, 0).notZero {
                        loadingWithContentAnimation = AnimationUtils.loadAnimation(context, it)
                    }
                } finally {
                    recycle()
                }
            }
    }

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
        loadingLayout = inflate(loadingLayoutRes)
        loadingLayout?.visibility = View.GONE
        addView(loadingLayout)
    }

    private fun setupInfoState() {
        infoLayout = inflate(infoLayoutRes)
        infoLayout?.visibility = View.GONE
        addView(infoLayout)
    }

    private fun setupLoadingWithContentState() {
        loadingWithContentLayout = inflate(loadingWithContentLayoutRes)
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
        updateLoadingVisibility(View.GONE)
        contentLayout.gone()
        infoLayout.gone()
        updateLoadingWithContentVisibility(View.GONE)
    }

    private fun updateLoadingVisibility(visibility: Int) =
        when (visibility) {
            View.VISIBLE -> loadingLayout.visible { it.startViewAnimation(R.id.customView_state_layout_loading, loadingAnimation) }
            else -> loadingLayout.gone { it.clearViewAnimation(R.id.customView_state_layout_loading) }
        }

    private fun updateLoadingWithContentVisibility(visibility: Int) =
        when (visibility) {
            View.VISIBLE -> loadingWithContentLayout.visible { it.startViewAnimation(R.id.customView_state_layout_with_content, loadingWithContentAnimation) }
            else -> loadingWithContentLayout.gone { it.clearViewAnimation(R.id.customView_state_layout_with_content) }
        }

    private fun throwChildCountException(): Nothing =
        throw IllegalStateException("StateLayout can host only one direct child")

    fun initialState(state: State) {
        this.state = state
    }

    fun loadingMessage(message: String): StateLayout {
        loadingLayout.findView<TextView>(R.id.textView_state_layout_loading_message) {
            text = message
            visibility = View.VISIBLE
        }
        return loading()
    }

    fun loadingAnimation(animation: Animation): StateLayout {
        loadingAnimation = animation
        return loading()
    }

    fun loading(): StateLayout {
        state = LOADING
        updateLoadingVisibility(View.VISIBLE)
        contentLayout.gone()
        infoLayout.gone()
        updateLoadingWithContentVisibility(View.GONE)
        return this
    }

    fun loading(@LayoutRes layoutId: Int) {
        this.loadingLayoutRes = layoutId
        removeView(loadingLayout)
        setupLoadingState()
        showState(provideLoadingStateInfo())
    }

    fun content(): StateLayout {
        state = CONTENT
        updateLoadingVisibility(View.GONE)
        contentLayout.visible()
        infoLayout.gone()
        updateLoadingWithContentVisibility(View.GONE)
        return this
    }

    fun infoImage(imageRes: Int): StateLayout {
        infoLayout.findView<ImageView>(R.id.imageView_state_layout_info) {
            setImageResource(imageRes)
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoTitle(title: String): StateLayout {
        infoLayout.findView<TextView>(R.id.textView_state_layout_info_title) {
            text = title
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoMessage(message: String): StateLayout {
        infoLayout.findView<TextView>(R.id.textView_state_layout_info_message) {
            text = message
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoButtonListener(block: () -> Unit) {
        infoLayout.findView<Button>(R.id.button_state_layout_info) {
            setOnClickListener { block.invoke() }
        }
        info()
    }

    fun infoButtonVisibility(visibility: Int) {
        infoLayout.findView<Button>(R.id.button_state_layout_info) {
            this.visibility = visibility
        }
        info()
    }

    fun infoButtonText(buttonText: String): StateLayout {
        infoLayout.findView<Button>(R.id.button_state_layout_info) {
            text = buttonText
            visibility = View.VISIBLE
        }
        return info()
    }

    fun infoButton(buttonText: String, block: (() -> Unit)?): StateLayout {
        infoLayout.findView<Button>(R.id.button_state_layout_info) {
            text = buttonText
            setOnClickListener { block?.invoke() }
            visibility = View.VISIBLE
        }
        return info()
    }

    fun info(): StateLayout {
        state = INFO
        updateLoadingVisibility(View.GONE)
        contentLayout.gone()
        infoLayout.visible()
        updateLoadingWithContentVisibility(View.GONE)
        return this
    }

    fun info(@LayoutRes layoutId: Int) {
        this.infoLayoutRes = layoutId
        removeView(infoLayout)
        setupInfoState()
        showState(provideInfoStateInfo())
    }

    fun loadingWithContentAnimation(animation: Animation): StateLayout {
        loadingWithContentAnimation = animation
        return loadingWithContent()
    }

    fun loadingWithContent(): StateLayout {
        state = LOADING_WITH_CONTENT
        updateLoadingVisibility(View.GONE)
        contentLayout.visible()
        infoLayout.gone()
        updateLoadingWithContentVisibility(View.VISIBLE)
        return this
    }

    fun loadingWithContent(@LayoutRes layoutId: Int) {
        this.loadingWithContentLayoutRes = layoutId
        removeView(loadingWithContentLayout)
        setupLoadingWithContentState()
        showState(provideLoadingWithContentStateInfo())
    }

    fun showLoading(stateInfo: StateInfo?) = showState(stateInfo)

    fun showContent(stateInfo: StateInfo?) = showState(stateInfo)

    fun showInfo(stateInfo: StateInfo?) = showState(stateInfo)

    fun showLoadingWithContent(stateInfo: StateInfo?) = showState(stateInfo)

    fun showError(stateInfo: StateInfo?) = showState(stateInfo)

    fun showEmpty(stateInfo: StateInfo?) = showState(stateInfo)

    fun showState(stateInfo: StateInfo?) {
        loadingAnimation = stateInfo?.loadingAnimation
        loadingWithContentAnimation = stateInfo?.loadingWithContentAnimation
        when (stateInfo?.state) {
            LOADING -> loading()
            CONTENT -> content()
            LOADING_WITH_CONTENT -> loadingWithContent()
            INFO, ERROR, EMPTY -> {
                stateInfo.infoImage?.let { infoImage(it) }
                stateInfo.infoTitle?.let { infoTitle(it) }
                stateInfo.infoMessage?.let { infoMessage(it) }
                stateInfo.infoButtonText?.let { infoButtonText(it) }
                stateInfo.onInfoButtonClick?.let { infoButtonListener(it) }
            }
            null, NONE -> hideAll()
        }
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

    enum class State {
        LOADING, CONTENT, INFO, LOADING_WITH_CONTENT, ERROR, EMPTY, NONE
    }

    data class StateInfo(
        val infoImage: Int? = null,
        val infoTitle: String? = null,
        val infoMessage: String? = null,
        val infoButtonText: String? = null,
        val state: State = INFO,
        val onInfoButtonClick: (() -> Unit)? = null,
        val loadingAnimation: Animation? = null,
        val loadingWithContentAnimation: Animation? = null
    )
}
