package com.lazycolumnstutter

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.lazycolumnstutter.ui.theme.LazyColumnStutterTheme
import kotlin.math.ceil
import kotlin.math.roundToInt

@SuppressLint("Range")
@Suppress("LongMethod")
@Composable
fun ComplexItem(
    data: ComplexData,
    onClick: (ActionData) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { data.primaryAction?.run(onClick) },
    ) {
        val (date, title, amount, originalAmount, visibilityToggle, actionButton, type, stripe) = createRefs()
        val contentAlpha = if (data.isHidden) 0.5f else 1f

        Text(
            text = data.formattedDate,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .constrainAs(date) {
                    start.linkTo(parent.start, margin = 16.dp)
                    top.linkTo(parent.top, margin = 8.dp)
                    alpha = contentAlpha
                },
        )

        Text(
            text = data.title,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start, margin = 16.dp)
                    top.linkTo(date.bottom, margin = 4.dp)
                    end.linkTo(amount.start, margin = 16.dp)
                    alpha = contentAlpha
                },
        )

        Text(
            text = data.amountString ?: "",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .constrainAs(amount) {
                    start.linkTo(title.end)
                    top.linkTo(title.top)
                    end.linkTo(visibilityToggle.start, goneMargin = 16.dp)
                    bottom.linkTo(title.bottom)
                    isVisible = data.amountString != null && data.iconAction == null
                    alpha = contentAlpha
                },
        )

        Text(
            text = data.originalAmountString ?: "",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .constrainAs(originalAmount) {
                    top.linkTo(amount.bottom, margin = 4.dp)
                    end.linkTo(amount.end)
                    isVisible = data.originalAmountString != null && data.iconAction == null
                    alpha = contentAlpha
                },
        )

        IconButton(
            onClick = { data.iconAction?.run(onClick) },
            modifier = Modifier
                .constrainAs(visibilityToggle) {
                    centerVerticallyTo(title)
                    start.linkTo(amount.end)
                    end.linkTo(actionButton.start)
                    isVisible = data.hasVisibilityToggle
                },
        ) {
            val icon = if (data.isHidden) Icons.Default.Delete else Icons.Default.Done
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (data.isHidden) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
            )
        }

        TextButton(
            onClick = { data.iconAction?.run(onClick) },
            modifier = Modifier
                .constrainAs(actionButton) {
                    centerVerticallyTo(title)
                    start.linkTo(visibilityToggle.end)
                    end.linkTo(parent.end, margin = 2.dp)
                    isVisible = data.iconAction != null
                },
        ) {
            Text(text = data.iconAction?.actionLabel ?: "")
        }

        TypeBadge(
            text = data.type,
            color = data.typeColor,
            modifier = Modifier
                .constrainAs(type) {
                    width = Dimension.preferredWrapContent
                    linkTo(
                        start = parent.start,
                        end = originalAmount.start,
                        startMargin = 16.dp,
                        endMargin = 16.dp,
                        endGoneMargin = 0.dp,
                        bias = 0f,
                    )
                    top.linkTo(title.bottom, margin = 2.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    alpha = contentAlpha
                }
        )

        Stripe(
            color = MaterialTheme.colors.error,
            modifier = Modifier
                .constrainAs(stripe) {
                    width = Dimension.value(3.dp)
                    height = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    isVisible = data.showLiquidityWarning
                },
        )
    }
}

@Composable
private fun TypeBadge(
    text: String,
    color: Long?,
    modifier: Modifier = Modifier,
) {
    if (color == null) {
        OutlineBadge(
            text = text,
            color = MaterialTheme.colors.secondary,
            badgeStyle = BadgeStyle.Small,
            modifier = modifier,
        )
    } else {
        Badge(
            text = text,
            color = Color(color),
            badgeStyle = BadgeStyle.Small,
            modifier = modifier,
        )
    }
}

@Composable
private fun Stripe(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val (width, height) = size
        val stripeHeight = 5.dp.toPx()
        @Suppress("UnnecessaryVariable")
        val spacing = stripeHeight
        val elementHeight = stripeHeight + spacing
        val heightToFill = height + width // bottom-right corner has to be also filled
        val count = ceil(heightToFill / elementHeight).roundToInt()
        val path = Path().apply {
            relativeMoveTo(0f, spacing)
            repeat(count) {
                relativeLineTo(width, -width)
                relativeLineTo(0f, stripeHeight)
                relativeLineTo(-width, width)
                close()
                relativeMoveTo(0f, spacing)
            }
        }
        drawPath(path = path, color = color)
    }
}

@Preview
@Composable
private fun ComplexItemPreview(
    @PreviewParameter(ComplexDataPreviewProvider::class) data: ComplexData,
) {
    LazyColumnStutterTheme {
        Surface {
            ComplexItem(
                data = data,
                onClick = {},
            )
        }
    }
}

@Suppress("StringLiteralDuplication", "MagicNumber")
internal class ComplexDataPreviewProvider :
    PreviewParameterProvider<ComplexData> {
    override val values: Sequence<ComplexData> = sequenceOf(
        ComplexData(
            id = "",
            groupId = null,
            contentDescription = "",
            formattedDate = "01.01.2020",
            title = "VISA Invoice",
            amountString = "-€ 500.0",
            originalAmountString = "-\$ 600.0",
            type = "Totally crazy long explanation why I used carrot color, and all the other stupid things you see",
            typeColor = 0,
            primaryAction = null,
            iconAction = null,
            secondaryActions = listOf(),
            showLiquidityWarning = false,
            hasVisibilityToggle = false,
            isHidden = false,
        ),
        ComplexData(
            id = "",
            groupId = null,
            contentDescription = "",
            formattedDate = "01.01.2020",
            title = "VISA Invoice",
            amountString = "-€ 500.0",
            originalAmountString = null,
            type = "CREDIT CARD",
            typeColor = 0,
            primaryAction = null,
            iconAction = null,
            secondaryActions = listOf(),
            showLiquidityWarning = false,
            hasVisibilityToggle = true,
            isHidden = true,
        ),
        ComplexData(
            id = "",
            groupId = null,
            contentDescription = "",
            formattedDate = "01.01.2020",
            title = "I wonder what happens when title and amount touch",
            amountString = "-€ 500,000,000.00",
            originalAmountString = null,
            type = "CREDIT CARD",
            typeColor = null,
            primaryAction = null,
            iconAction = null,
            secondaryActions = listOf(),
            showLiquidityWarning = true,
            hasVisibilityToggle = true,
            isHidden = false,
        ),
        ComplexData(
            id = "",
            groupId = null,
            contentDescription = "",
            formattedDate = "01.01.2020",
            title = "VISA Invoice",
            amountString = null,
            originalAmountString = null,
            type = "Totally crazy long explanation why I used carrot color, and all the other stupid things you see",
            typeColor = null,
            primaryAction = null,
            iconAction = ActionData(actionLabel = "Set Amount", icon = "icon:note"),
            secondaryActions = listOf(),
            showLiquidityWarning = false,
            hasVisibilityToggle = false,
            isHidden = false,
        ),
    )
}

var ConstrainScope.isVisible: Boolean
    get() = visibility == Visibility.Visible
    set(value) {
        visibility = if (value) Visibility.Visible else Visibility.Gone
    }
