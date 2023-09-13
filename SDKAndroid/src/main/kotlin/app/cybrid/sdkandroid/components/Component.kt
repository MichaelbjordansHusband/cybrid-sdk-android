package app.cybrid.sdkandroid.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import app.cybrid.sdkandroid.R
import app.cybrid.sdkandroid.core.Constants

open class Component @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var updateInterval = 5000L

    private var _handler: Handler? = null
    private var _runnable: Runnable? = null
    protected var composeView:ComposeView? = null
    var isUpdating = false

    fun setupRunnable(runner: () -> Unit) {

        _handler = Handler(Looper.getMainLooper())
        _runnable = Runnable {

            isUpdating = true
            runner.invoke()
            _handler.let {
                _runnable.let { _it ->
                    it?.postDelayed(_it!!, updateInterval)
                }
            }
        }
        _handler?.postDelayed(_runnable ?: Runnable {}, updateInterval)
    }

    companion object {

        @Composable
        fun CreateLoader(modifier: Modifier, message: String) {
            Column(
                modifier = modifier.height(180.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    style = TextStyle(
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontWeight = FontWeight(400),
                        color = colorResource(id = R.color.black),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.35.sp,
                    )
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .testTag(Constants.AccountsViewTestTags.Loading.id),
                    color = colorResource(id = R.color.primary_color)
                )
            }
        }

        @Composable
        fun Label(modifier: Modifier,
                  message: String,
                  fontSize: TextUnit = 21.sp,
                  lineHeight: TextUnit = 28.sp,
                  fontWeight: Int = 600,
                  letterSpacing: TextUnit = 1.sp,
                  align: TextAlign = TextAlign.Left) {
            Text(
                modifier = modifier,
                text = message,
                style = TextStyle(
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontWeight = FontWeight(fontWeight),
                    color = Color.Black,
                    textAlign = align,
                    letterSpacing = letterSpacing,
                )
            )
        }
    }
}