package com.example.myapplication.ui.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object NavigationScreen {
    @Composable
    fun FirstScreen(viewModel: MainViewModel) {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                //flow
                val time = viewModel.countDownFlow.collectAsStateWithLifecycle(initialValue = 10)

                //shared flow
                val result = viewModel.sharedFlow.collectAsStateWithLifecycle(initialValue = 0)

                //state flow
                val countDownTime =
                    viewModel.countDownStateFlow.collectAsStateWithLifecycle(initialValue = 20)

                //rememberUpdatedState
                val currentTime = rememberUpdatedState(newValue = countDownTime)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "First Screen",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color.Magenta
                    )
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                    ) {
                        Text(text = "Flow")

                        Text(
                            text = time.value.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Magenta
                        )
                    }
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                    ) {
                        Text(text = "State Flow")

                        Text(
                            text = countDownTime.value.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Green
                        )
                    }
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Shared Flow")

                        Text(
                            text = result.value.toString(),
                            color = Color.Cyan
                        )
                    }
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "RememberUpdatedState")

                        Text(
                            text = currentTime.value.value.toString(),
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun SecondScreen(viewModel: MainViewModel) {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                //state flow
                val count = viewModel.stateFlow.collectAsStateWithLifecycle(initialValue = 0)

                val counter = remember { mutableIntStateOf(0) }

                SideEffect {
                    Log.d("SecondScreen", "Outer Count is: ${counter.intValue}")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Second Screen",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color.Black,
                    )
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Button(onClick = {
                            counter.intValue++
                            viewModel.incrementCounter()
                        }) {
                            SideEffect {
                                Log.d("SecondScreen", "Inner Count is: ${counter.intValue}")
                            }
                            Text(
                                text = "Counter: ${count.value}", color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ThirdScreen(viewModel: MainViewModel) {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Third Screen",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color.Cyan
                    )

                    val isLoading = remember { mutableStateOf(false) }
                    val data = remember { mutableStateOf(listOf<String>()) }

                    // Define a LaunchedEffect to perform a long-running operation asynchronously
                    // `LaunchedEffect` will cancel and re-launch if
                    // `isLoading.value` changes
                    LaunchedEffect(isLoading.value) {
                        if (isLoading.value) {
                            // Perform a long-running operation, such as fetching data from a network
                            val newData = viewModel.fetchData()
                            // Update the state with the new data
                            data.value = newData
                            isLoading.value = false
                        }
                    }

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Button(modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                            onClick = { isLoading.value = true }) {
                            Text("Fetch Data")
                        }
                        if (isLoading.value) {
                            // Show a loading indicator
                            CircularProgressIndicator()
                        } else {
                            // Show the data
                            val listState = rememberLazyListState()

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f), state = listState
                            ) {
                                items(data.value.size) { index ->
                                    Text(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(16.dp),
                                        text = data.value[index],
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Button to scroll to the top
                            val coroutineScope = rememberCoroutineScope()

                            val showButton = remember {
                                derivedStateOf {
                                    listState.firstVisibleItemIndex > 0
                                }
                            }

                            AnimatedVisibility(
                                modifier = Modifier.align(Alignment.End), visible = showButton.value
                            ) {
                                Button(onClick = {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(0)
                                    }
                                }) {
                                    Text("Scroll to Top")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FourthScreen() {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                val isVisible = remember { mutableStateOf(true) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    if (isVisible.value) TimerScreen()

                    Button(onClick = {
                        isVisible.value = !isVisible.value
                    }) {
                        Text(
                            text = if (isVisible.value) "Hide the timer"
                            else "Show the timer"
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TimerScreen() {
        val elapsedTime = remember { mutableIntStateOf(0) }

        DisposableEffect(Unit) {
            val scope = CoroutineScope(Dispatchers.Default)
            val job = scope.launch {
                while (true) {
                    delay(1000)
                    elapsedTime.intValue += 1
                }
            }

            onDispose {
                job.cancel()
            }
        }

        Text(
            text = "Elapsed Time: ${elapsedTime.intValue}",
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp
        )
    }
}