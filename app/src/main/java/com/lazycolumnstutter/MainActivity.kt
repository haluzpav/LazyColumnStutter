package com.lazycolumnstutter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazycolumnstutter.ui.theme.LazyColumnStutterTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnStutterTheme {
                MyList()
            }
        }
    }
}

@Composable
fun MyList() {
    val myItems = remember {
        List(10000) {
            ItemData(
                id = it,
                title = "List item #$it",
                description = "Description",
                status = listOf("Open", "Closed", "Pending", "In Review", "Canceled").random(),
                date = LocalDate.ofEpochDay(Random.nextLong(-999999999, 999999999)).toString(),
                showButton = Random.nextBoolean(),
            )
        }
    }
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        state.animateScrollToItem(0)
                    }
                }) {
                    Text(text = "Scroll to top")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        state.animateScrollToItem(myItems.lastIndex)
                    }
                }) {
                    Text(text = "Scroll to bottom")
                }
            }

            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(myItems, key = { it.id }) {
                    Item(
                        itemData = it,
                        onClick = { Log.d("ITEM", "clicked $it") },
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    itemData: ItemData,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = itemData.title,
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = itemData.description,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = itemData.status,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary,
            )
            Text(
                text = itemData.date,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.secondaryVariant,
            )
        }
        if (itemData.showButton) {
            TextButton(onClick = { Log.d("ITEM", "button click") }) {
                Text(text = "Delete", color = MaterialTheme.colors.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LazyColumnStutterTheme {
        MyList()
    }
}

data class ItemData(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val date: String,
    val showButton: Boolean,
)
