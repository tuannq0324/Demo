package com.example.myapplication.ui.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object NavigationScreen {

    /**
     * compose functions can execute in any order
     * can't set global variable because the calls to Screen might happen in any order
     * like this
     * val clicks = remember { mutableIntStateOf(0)}

     * compose functions must have annotated with the @Composable
     * functions can accept parameters (String, Int, etc.)
     * compose functions emit UI elements (Text, Button, etc.) by calling other composable functions
     * compose functions doesn't return anything because they describe
     * the desired screen state instead of constructing UI widgets
     * compose functions is fast, doesn't change value, doesn't have side-effects
     */

    /**
     * Composable functions can execute in any order
     * Composable functions can run in parallel
     * Recomposition skips as much as possible
     * Recomposition is optimistic
     * Composable functions might run quite frequently
     */

    @Composable
    fun FirstScreen() {

        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    /**
                     * call compose function again with new data is recomposed (setter function)
                     * compose call lambda by Text function, it called recomposed
                     * functions not belong data are not recomposed like Title()

                     * recompose only call when data change (this happens only when function's input change)
                     * compose skip functions that don't depend on data or data don't have change

                     * recompose is optimistic (kha quan):
                     * finish recompose before parameters change again
                     * if parameter change before recomposition finishes, compose might cancel and restart with new parameter

                     * recompose might run quite frequently (thuong xuyen)
                     * when read those settings
                     */

                    Title()

                    //counter
                    var counter by remember { mutableStateOf(Click(0)) }
                    Counter(clicks = counter) {
                        counter = Click(1)
                    }
//                    Counter(clicks = Click(0)) {
//                        counter.intValue = 1
//                    }
                }
            }
        }
    }


    @Composable
    fun Title() {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = "First Screen",
            color = Color.Black,
        )
    }

    /**
     * Recompose when parameter changes
     *
     * SideEffect
     */

    class Click(val clicks: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return (other as Click).clicks == clicks
        }

        override fun hashCode(): Int {
            return clicks
        }
    }

    @Composable
    fun Counter(clicks: Click, onClick: () -> Unit) {
        Log.d("Counter", "Outer Count is: ${clicks.clicks}")
//        SideEffect {
//            Log.d("Counter", "Outer Count is: $clicks")
//        }
        Button(
            modifier = Modifier.wrapContentSize(), onClick = onClick
        ) {
//            SideEffect {
//                Log.d("Counter", "Inner Count is: $clicks")
//            }
            Text(text = "I've been clicked ${clicks.clicks} times")
        }
    }

    @Composable
    fun SecondScreen(viewModel: MainViewModel) {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                val isLoading = remember { mutableStateOf(false) }
                val data = remember { mutableStateOf(listOf<String>()) }

                // Define a LaunchedEffect to perform a long-running operation asynchronously
                // `LaunchedEffect` will cancel and re-launch if
                // `isLoading.value` changes
                LaunchedEffect(isLoading.value) {
                    if (isLoading.value) {
                        val newData = viewModel.getData()
                        delay(2000)
                        data.value = newData
                        isLoading.value = false
                    }
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
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Black,
                    )
                    Button(modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                        onClick = { isLoading.value = true }) {
                        Text("Fetch Data")
                    }
//                    if (isLoading.value) {
//                        // Show a loading indicator
//                        CircularProgressIndicator()
//                    } else {
                    //ListComposable(myList = data.value)
                    ListWithBug(myList = data.value)
//                    }
                }
            }
        }
    }

    /**
     * composable functions can run in parallel
     */
    @Composable
    fun ListComposable(myList: List<String>) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            //
            Column {
                for (item in myList) {
                    Text("Item: $item")
                }
            }
            //
            Text("Count: ${myList.size}")
        }
    }

    @Composable
    @Deprecated("Example with bug")
    fun ListWithBug(myList: List<String>) {
        var items = 0

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                for (item in myList) {
                    Text("Item: $item")
                    items++ // Avoid! Side-effect of the column recomposing.
                }
            }
            Text("Count: $items")
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
//                    val data = remember { mutableStateOf(listOf<String>()) }
//
//                    LaunchedEffect(viewModel.listName) {
//                        // Update the state with the new data
//                        viewModel.listName.collect { newData ->
//                            data.value = newData
//                        }
//                    }

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        NamePicker(
                            viewModel = viewModel,
                            header = "Third Screen",
//                            names = arrayListOf(),  onNameClicked = {
//                                viewModel.updateName(it)
//                            }
                                                )
                    }
                }
            }
        }
    }

    /**
     * Display a list of names the user can click with a header
     */
    /**
     * recompose skips as much as possible
     */
    @Composable
    fun NamePicker(
        viewModel: MainViewModel,
        header: String,
//        names: List<String>,
//        onNameClicked: (String) -> Unit
    ) {
        /**
         * Skipping if the inputs haven't changed
         */

        val data = remember { mutableStateOf(listOf<String>()) }

        LaunchedEffect(viewModel.listName) {
            // Update the state with the new data
            viewModel.listName.collect { newData ->
                data.value = newData
            }
        }

        Log.d("NamePicker", "NamePicker recomposing")

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /**
             * this will recompose when [header] changes, but not when [names] changes
             */
            Text(
                text = header,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Black,
                fontSize = 20.sp,
            )
            HorizontalDivider()

            /**
             * LazyColumn is the Compose version of a RecyclerView.
             * The lambda passed to items() is similar to a RecyclerView.ViewHolder.
             */

            val listState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), state = listState
            ) {
                Log.d("NamePicker", "NamePicker recomposing column")
                itemsIndexed(data.value,
                    /**
                     * Add extra information to help smart recompositions
                     */
                    key = { index, name ->
                        Log.d("KEY", "Key for $name is $index")
                        index
                    }) { index, name ->
                    /**
                     * When an item's [name] updates, the adapter for that item
                     * will recompose. This will not recompose when [header] changes
                     */
                    NamePickerItem(
                        name = name,
//                        onNameClicked = onNameClicked
                        onNameClicked = {
                            viewModel.updateName(it)
                        }
                    )
                }
            }

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

    /**
     * Display a single name the user can click.
     */
    @Composable
    private fun NamePickerItem(name: String, onNameClicked: (String) -> Unit) {
        Log.d("NamePickerItem", "NamePickerItem recomposing for $name")
        Text(text = name,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNameClicked(name) }
                .padding(16.dp),
            textAlign = TextAlign.Center)
    }

    /**
     * DisposableEffect
     */
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

    /**
     *State and Composition
     */
    @Composable
    fun FifthScreen() {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Hello!",
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    /**
                     * compose is declarative and only way to update is calling
                     * the same composable function with new argument
                     * TextField don't automatically update like they do in imperative XML
                     * because it doesn't update itself - it updates when its value parameter changes
                     */

                    OutlinedTextField(value = "", onValueChange = { }, label = { Text("Name") })

                    /**
                     * remember store objects and forgets when compose functions call
                     * remember is remove from composition

                     * remember have 3 ways to call
                     * val mutableState = remember { mutableStateOf(default) }
                     * var value by remember { mutableStateOf(default) }
                     * val (value, setValue) = remember { mutableStateOf(default) }
                     */

                    val name = remember {
                        mutableStateOf("World")
                    }

                    val defaultName = rememberUpdatedState(newValue = name)

                    val saveableName = rememberSaveable { name }

                    if (name.value.isNotEmpty()) {
                        Text(
                            text = "Hello, ${name.value}!",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    OutlinedTextField(value = name.value, onValueChange = {
                        name.value = it
                        Log.d("Default Name", defaultName.value.value)
                        Log.d("Saveable Name", saveableName.value)
                    }, label = { Text("Name") })

                    Text(
                        modifier = Modifier.padding(8.dp), text = saveableName.value
                    )
                }
            }
        }
    }
}