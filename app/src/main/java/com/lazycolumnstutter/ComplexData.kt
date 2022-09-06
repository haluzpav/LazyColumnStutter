package com.lazycolumnstutter

import androidx.compose.runtime.Immutable

@Immutable
data class ComplexData(
    val id: String,
    val groupId: String?,
    val contentDescription: String,
    val formattedDate: String,
    val title: String,
    val amountString: String?,
    val originalAmountString: String?,
    val type: String,
    val typeColor: Long?,
    val primaryAction: ActionData?,
    val iconAction: ActionData?,
    val secondaryActions: List<ActionData>,
    val showLiquidityWarning: Boolean,
    val hasVisibilityToggle: Boolean,
    val isHidden: Boolean,
)