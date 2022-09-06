package com.lazycolumnstutter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed interface BadgeStyle {
    @Composable
    fun height(): Dp
    @Composable
    fun innerPadding(): Dp
    @Composable
    fun textStyle(): TextStyle
    @Composable
    fun iconSize(): Dp

    object Default : BadgeStyle {
        @Composable
        override fun height() = 24.dp
        @Composable
        override fun innerPadding() = 8.dp
        @Composable
        override fun textStyle() = MaterialTheme.typography.subtitle1
        @Composable
        override fun iconSize() = 16.dp
    }

    object Small : BadgeStyle {
        @Composable
        override fun height() = 16.dp
        @Composable
        override fun innerPadding() = 6.dp
        @Composable
        override fun textStyle() = MaterialTheme.typography.subtitle2
        @Composable
        override fun iconSize() = 12.dp
    }
}

@Composable
fun Badge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    badgeStyle: BadgeStyle = BadgeStyle.Default,
    iconPainter: Painter? = null,
    testAutomationId: String? = null,
) {
    InternalBadge(
        text = text,
        backgroundColor = color,
        textColor = MaterialTheme.colors.onPrimary,
        style = badgeStyle,
        modifier = modifier,
        iconPainter = iconPainter,
        testAutomationId = testAutomationId
    )
}

@Composable
fun OutlineBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    badgeStyle: BadgeStyle = BadgeStyle.Default,
    iconPainter: Painter? = null,
    testAutomationId: String? = null,
) {
    InternalBadge(
        text = text,
        textColor = color,
        style = badgeStyle,
        modifier = modifier,
        borderStroke = BorderStroke(1.dp, color),
        iconPainter = iconPainter,
        testAutomationId = testAutomationId
    )
}

@Composable
private fun InternalBadge(
    text: String,
    backgroundColor: Color = MaterialTheme.colors.primary,
    textColor: Color,
    style: BadgeStyle,
    modifier: Modifier = Modifier,
    borderStroke: BorderStroke? = null,
    iconPainter: Painter? = null,
    testAutomationId: String? = null,
) {
    val height = style.height()
    val useChipShape = text.length > 1 || iconPainter != null
    Surface(
        color = backgroundColor,
        shape = CircleShape,
        modifier = if (useChipShape) {
            modifier.heightIn(min = height)
        } else {
            modifier.sizeIn(minHeight = height, minWidth = height)
        },
        border = borderStroke
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
                .padding(horizontal = style.innerPadding()),
        ) {
            if (iconPainter != null) {
                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(style.iconSize()),
                    tint = textColor,
                )
            }
            Text(
                text = text,
                color = textColor,
                style = style.textStyle(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}