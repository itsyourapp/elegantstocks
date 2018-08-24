package app.itsyour.elegantstocks.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.itsyour.elegantstocks.R
import kotlinx.android.synthetic.main.loadingframe_error.view.*

/**
 * A layout view that simplifies loading, error, and success states of a view
 */
class LoadingFrame : FrameLayout {

    companion object {
        const val ERROR = -1
        const val LOADING = 0
        const val SUCCESS = 1

        private const val DEFAULT_ANIMATION_DURATION = 250
    }

    private var loadingView: View? = null
    private var errorView: View? = null
    private var successView: View? = null

    private var listener: StateUpdateListener? = null

    @State
    private var state = LOADING

    @IntDef(ERROR, LOADING, SUCCESS)
    @Retention
    private annotation class State()

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)

    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadingFrame)
        @LayoutRes var loadingLayout = R.layout.loadingframe_loading
        @LayoutRes var errorLayout = R.layout.loadingframe_error

        loadingLayout = a.getResourceId(R.styleable.LoadingFrame_loading_layout, loadingLayout)
        errorLayout = a.getResourceId(R.styleable.LoadingFrame_error_layout, errorLayout)

        View.inflate(context, loadingLayout, this)
        View.inflate(context, errorLayout, this)

        state = a.getInt(R.styleable.LoadingFrame_loading_state, LOADING)
        a.recycle()
    }
    private val viewMatrix: Matrix = Matrix()
    private lateinit var borderPaint: Paint

    override fun onDraw(canvas: Canvas) {
        val tempBounds = RectF()
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        if(!viewMatrix.isIdentity) {
            viewMatrix.mapRect(tempBounds)
            canvas.drawRect(tempBounds, borderPaint)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 3) {
            throw RuntimeException("This layout requires one child!")
        }

        loadingView = getChildAt(0)
        errorView = getChildAt(1)
        successView = getChildAt(2)

        loadingView!!.visibility = View.GONE
        errorView!!.visibility = View.GONE
        successView!!.visibility = View.GONE

        updateViewState()
    }

    @State
    fun getState(): Int {
        return state
    }

    // TODO: Working implementation
    fun setErrorView(view: View) {
        errorView = view
    }

    fun setErrorText(text: Int) {
        loadingFrame_errorText.text = context.resources.getText(text)
    }

    fun setState(@State state: Int, animate: Boolean = true, triggerNotify: Boolean = true) {
        if (this.state == state) {
            if (triggerNotify) {
                post { listener?.onStateUpdated(state) }
            }
            return
        }
        this.state = state
        post {
            when {
                loadingView is SwipeRefreshLayout -> updateSwipeRefreshViewState()
                animate -> animateViewState()
                else -> updateViewState()
            }

            if (triggerNotify) {
                if (listener != null) {
                    listener!!.onStateUpdated(state)
                }
            }
        }
    }

    fun setStateChangeListener(listener: StateUpdateListener?) {
        this.listener = listener
    }

    private fun animateViewState() {
        errorView!!.animate().cancel()
        loadingView!!.animate().cancel()
        successView!!.animate().cancel()

        val activeView = children().first { i -> i.visibility == View.VISIBLE }

        val newActiveView: View = (
                when (state) {
                    ERROR -> errorView
                    LOADING -> loadingView
                    SUCCESS -> successView
                    else -> throw RuntimeException("Invalid LoadingFrame.State")
                }) as View

        getInactiveAnimation(activeView, getActiveAnimation(newActiveView)).start()
    }

    private fun updateViewState() {
        val newActiveView: View = (
                when (state) {
                    ERROR -> errorView
                    LOADING -> loadingView
                    SUCCESS -> successView
                    else -> throw RuntimeException("Invalid LoadingFrame.State")
                }) as View

        children().filter { i -> newActiveView != i }.forEach { i -> i.visibility = View.GONE }
        newActiveView.visibility = View.VISIBLE
        newActiveView.bringToFront()
    }

    private fun updateSwipeRefreshViewState() {
        val loadingView = this.loadingView as SwipeRefreshLayout?
        loadingView!!.isRefreshing = state == LOADING
        if (state == SUCCESS) {
            getInactiveAnimation(errorView, getActiveAnimation(successView)).start()
        } else if (state == ERROR) {
            getInactiveAnimation(successView, getActiveAnimation(errorView)).start()
        }
    }

    private fun getActiveAnimation(view: View?): ViewPropertyAnimator {
        view!!.visibility = View.VISIBLE
        view.alpha = 0f
        view.bringToFront()
        return view.animate().alpha(1f)
                .setStartDelay(DEFAULT_ANIMATION_DURATION.toLong())
                .setDuration(DEFAULT_ANIMATION_DURATION.toLong())
    }

    private fun getInactiveAnimation(view: View?,
                                     activeAnimation: ViewPropertyAnimator): ViewPropertyAnimator {
        return view!!.animate().alpha(0f)
                .setStartDelay(DEFAULT_ANIMATION_DURATION.toLong())
                .setDuration(DEFAULT_ANIMATION_DURATION.toLong())
                .withEndAction {
                    view.visibility = View.GONE
                    activeAnimation.start()
                }
    }

    private fun children(): List<View> {
        return IntRange(0, childCount - 1).map(this::getChildAt)
    }

    interface StateUpdateListener {
        fun onStateUpdated(@State state: Int)
    }
}