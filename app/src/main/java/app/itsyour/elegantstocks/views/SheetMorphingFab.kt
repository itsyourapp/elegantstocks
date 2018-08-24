package app.itsyour.elegantstocks.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gordonwong.materialsheetfab.AnimatedFab

/**
 * A floating action button that morphs into a sheet.
 */
class SheetMorphingFab
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet?,
            style: Int = 0)
        : FloatingActionButton(context, attrs, style),
          AnimatedFab {

    override fun show() {
        show(0f, 300f)
    }

    override fun show(translationX: Float, translationY: Float) {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.INVISIBLE
    }
}