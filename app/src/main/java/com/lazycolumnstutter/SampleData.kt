package com.lazycolumnstutter

import java.util.UUID
import kotlin.random.Random

internal object SampleData {

    fun ComplexData(index: Int = 0): ComplexData {
        val hasVisibilityToggle = Random.nextBoolean()
        return ComplexData(
            id = UUID.randomUUID().toString(),
            groupId = null,
            contentDescription = "",
            formattedDate = "12. 3. 4567",
            title = "Cashflow item #$index",
            amountString = "${Random.nextInt()} EUR",
            originalAmountString = null,
            type = "Manual Entry",
            typeColor = listOf(0xffff00ff, 0xffffff00, 0xff00ffff).random(),
            primaryAction = null,
            iconAction = null,
            secondaryActions = listOf(),
            showLiquidityWarning = Random.nextBoolean(),
            hasVisibilityToggle = hasVisibilityToggle,
            isHidden = hasVisibilityToggle && Random.nextBoolean(),
        )
    }
}