package com.ae.mylibrary.component

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.text.TextStyle
import com.ae.mylibrary.theme.AEHealthTypography

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    basicMarqueeEnabled: Boolean = true,
    color: ColorProducer? = null,
    style: TextStyle,
) {
    BasicText(
        text = text,
        color = color,
        style = style,
        modifier = modifier
            .then(
                if (basicMarqueeEnabled)
                    Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        animationMode = MarqueeAnimationMode.Immediately
                    )
                else Modifier
            )
    )
}