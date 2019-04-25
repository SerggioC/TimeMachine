package com.sergiocruz.timecalculator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.TypedValue
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.cos
import kotlin.math.sin

private fun EditText.error(msg: String) {
    error = msg
    postDelayed({ error = null }, 8000L)
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        arrayOf(topH, topMin, topS, bottomH, bottomMin, bottomS, resultH, resultMin, resultS)
//            .forEach {
//                it.apply { watchSize() }
//            }

        star.setOnClickListener {
            calculate()
            if (editText.text?.length ?: 0 < minLength) {
                editText.error("Min length not reached $minLength")
            } else if (editText.text?.length ?: 0 > maxLength) {
                editText.error("Can't exceed max length $maxLength")
            }
        }

        editText.addTextChangedListener(MyTextWatcher(editText))

        editText.filters = arrayOf(InputFilter.LengthFilter(maxLength + 1))


//        override fun afterTextChanged(editable: Editable?) {
//            if (editable != null && editable.length > maxLength) {
//                editable.replace(editable.length - 1, editable.length, "")
//                editText.error("Max length reached $maxLength")
//            }
//        }
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        }
//    }
//        numberAnimation()
//        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                star.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                starAnimation()
//
//            }
//        }
//        star.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    class MyTextWatcher(private val editText: EditText) : TextWatcher {

        override fun afterTextChanged(editable: Editable?) {
            if (editable != null && editable.length > maxLength) {
                editable.replace(editable.length - 1, editable.length, "")
                editText.error("Max length reached $maxLength")
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    private fun calculate() {
        val top = topS?.text?.toString()
        val segundo1 = if (top?.isNotBlank() == true) top.toInt() else 0
        val bottom = bottomS?.text?.toString()
        val segundo2 = if (bottom?.isNotBlank() == true) bottom.toInt() else 0
        resultS.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.values_text_size)
        )
        animateCalculation(segundo1, segundo1 + segundo2, resultS)
    }

    private fun animateCalculation(
        initialValue: Int,
        finalValue: Int,
        view: TextView
    ) {

        val initialSize: Float = resultS.textSize
        val finalSize: Float =
            resources.getDimension(R.dimen.values_text_size_triple)

        val countAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        val resizeAnimator = animateTextSizeChange(initialSize, finalSize, resultS)

        resizeAnimator?.duration = 200
        countAnimator.apply {
            duration = 5000
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                val intValue = animation?.animatedValue as Int
                val stringValue = intValue.toString()
                view.text = stringValue
//                if (stringValue.length == 3) {
//                    val nextDuration = countAnimator.duration - countAnimator.currentPlayTime
//                    countAnimator.cancel()
//
//                    val animatorSet = AnimatorSet()
//                    val nextCountAnimator = ValueAnimator.ofInt(intValue, finalValue).apply {
//                        duration = nextDuration
//                        interpolator = LinearInterpolator()
//                        addUpdateListener { animation ->
//                            val currentValue = animation.animatedValue as Int
//                            val stringValue = currentValue.toString()
//
//                            if (stringValue.length == 4) {
//
//
//                            } else {
//                                view.text = stringValue
//                            }
//                        }
//                    }
//                    animatorSet.play(nextCountAnimator).with(resizeAnimator)
//                    animatorSet.start()
//
//
//                } else {
//                    view.text = stringValue
//                }

            }
        }
        countAnimator.start()
    }

    private fun TextView.watchSize() {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit

            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {
                when {
                    s?.length ?: 0 == 4 -> {
                        val initialSize: Float = textSize
                        val finalSize: Float =
                            resources.getDimension(R.dimen.values_text_size_tetra)
                        animateTextSizeChange(initialSize, finalSize, this@watchSize)
                    }
                    s?.length ?: 0 == 3 -> {
                        val initialSize: Float = textSize
                        val finalSize: Float =
                            resources.getDimension(R.dimen.values_text_size_triple)
                        animateTextSizeChange(initialSize, finalSize, this@watchSize)
                    }
                    else -> {
                        val initialSize: Float = textSize
                        val finalSize: Float =
                            resources.getDimension(R.dimen.values_text_size)
                        animateTextSizeChange(initialSize, finalSize, this@watchSize)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        addTextChangedListener(listener)
    }

    private fun animateTextSizeChange(
        initialSize: Float,
        finalSize: Float,
        view: TextView
    ): ValueAnimator? {
        return ValueAnimator.ofFloat(initialSize, finalSize)
            .apply {
                duration = ANIMATION_DURATION
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    val currentValue = animation?.animatedValue as Float
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentValue)
                }
                //start()
            }
    }

    //
    private fun starAnimation() {
        val centerX = star.left
        val centerY = star.top
        val duration = 40000L
        val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
        val animator = valueAnimator
        animator.duration = duration
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = INFINITE
        animator.interpolator = LinearInterpolator()
        val radius = 40.0
        animator.addUpdateListener { animation ->
            val angle = animation?.animatedValue as Float
            star.x = (radius * cos(angle)).toFloat() + centerX
            star.y = (radius * sin(angle)).toFloat() + centerY
        }
        animator.start()
    }

//    private fun numberAnimation() {
//        val animator = ValueAnimator.ofInt(0, 60)
//        animator.duration = 6000
//        animator.repeatMode = ValueAnimator.RESTART
//        animator.repeatCount = INFINITE
//
//        //addPathInterpolator(animator)
//
//        animator.interpolator = object: Interpolator {
//            override fun getInterpolation(t: Float): Float {
////                return 0.5f * ((2.0f * t - 1.0f).pow(3) + 1.0f)
//                val cycles = 2
//                return sin(2 * PI * t * cycles).toFloat()
//            }
//        }
//
//        animator.addUpdateListener { animation ->
//            val animatedValue = animation?.animatedValue as Int
//            topS.text = animatedValue.toString()
////            someView.getLayoutParams().width = animatedValue
////            someView.requestLayout()
//        }
//        animator.start()
//    }

    private fun addPathInterpolator(animator: ValueAnimator) {
        val pathInterpolator: PathInterpolator =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PathInterpolator(1.0f, 0.0f, 0.0f, 1.0f)
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
        animator.interpolator = pathInterpolator
    }

    companion object {
        private const val ANIMATION_DURATION: Long = 200
        const val minLength = 2
        const val maxLength = 5
    }


}



