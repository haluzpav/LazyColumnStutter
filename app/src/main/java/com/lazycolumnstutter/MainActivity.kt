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
    val cashflowItems = remember { List(10_000) { SampleData.ComplexData(index = it) } }
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
                        state.animateScrollToItem(cashflowItems.lastIndex)
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
                items(
                    items = cashflowItems,
                    key = ComplexData::id,
                ) {
                    ComplexItem(
                        data = it,
                        onClick = {},
                    )
                }
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
