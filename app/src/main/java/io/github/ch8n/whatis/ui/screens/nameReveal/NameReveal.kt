package io.github.ch8n.whatis.ui.screens.nameReveal


import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.ch8n.whatis.ui.screens.home.safeRandomIndex
import io.github.ch8n.whatis.ui.screens.shareName.ShareActivity
import kotlinx.coroutines.launch
import java.util.*


@ExperimentalMaterialApi
@Composable
fun NameRevealScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val firstName by remember { mutableStateOf("Pooja") }
    val lastName by remember { mutableStateOf("Srivastav") }
    var firstRandomIndex by remember { mutableStateOf(firstName.safeRandomIndex()) }
    var secondRandomIndex by remember { mutableStateOf(lastName.safeRandomIndex()) }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(text = data.message)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { _innerPadding ->

        Box(
            modifier = Modifier
                .padding(_innerPadding)
                .fillMaxSize()
                .wrapContentSize()
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    if (firstName.length >= 7 || lastName.length >= 7) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                firstName.forEachIndexed { index, _char ->
                                    Text(
                                        text = if (index == 0) _char.uppercaseChar()
                                            .toString() else _char.toString(),
                                        style = MaterialTheme.typography.h1,
                                        fontSize = 56.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = if (index == firstRandomIndex || index == firstRandomIndex + 1) {
                                            Modifier.border(width = 1.dp, color = Color.Red)
                                        } else {
                                            Modifier
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                lastName.forEachIndexed { index, _char ->
                                    Text(
                                        text = _char.toString(),
                                        style = MaterialTheme.typography.h1,
                                        fontSize = 56.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = if (index == secondRandomIndex || index == secondRandomIndex + 1) {
                                            Modifier.border(width = 1.dp, color = Color.Red)
                                        } else {
                                            Modifier
                                        }
                                    )
                                }
                            }

                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            firstName.forEachIndexed { index, _char ->
                                Text(
                                    text = _char.toString(),
                                    style = MaterialTheme.typography.h1,
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = if (index == firstRandomIndex || index == firstRandomIndex + 1) {
                                        Modifier.border(width = 1.dp, color = Color.Red)
                                    } else {
                                        Modifier
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            lastName.forEachIndexed { index, _char ->
                                Text(
                                    text = _char.toString(),
                                    style = MaterialTheme.typography.h1,
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = if (index == secondRandomIndex || index == secondRandomIndex + 1) {
                                        Modifier.border(width = 1.dp, color = Color.Red)
                                    } else {
                                        Modifier
                                    }
                                )
                            }
                        }
                    }



                    Text(
                        text = "Is",
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Medium,
                        fontSize = 42.sp,
                    )

                    val name = "${
                        firstName.get(firstRandomIndex)
                    }${
                        firstName.get(firstRandomIndex + 1)
                    }${
                        lastName.get(secondRandomIndex)
                    }${
                        lastName.get(secondRandomIndex + 1)
                    }".lowercase(Locale.getDefault()).run {
                        take(1).uppercase(Locale.getDefault()) + drop(1)
                    }

                    Text(
                        text = name,
                        style = MaterialTheme.typography.h1,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {

                        IconButton(onClick = {
                            firstRandomIndex = firstName.safeRandomIndex()
                            secondRandomIndex = lastName.safeRandomIndex()
                        }) {
                            Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "")
                        }

                        val clipboardManager = LocalClipboardManager.current
                        IconButton(onClick = {
                            clipboardManager.setText(buildAnnotatedString {
                                append("$firstName $lastName is $name")
                            })
                            scope.launch {
                                scaffoldState.snackbarHostState
                                    .showSnackbar(
                                        message = "Copied to Clipboard...",
                                        duration = SnackbarDuration.Short
                                    )
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = ""
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            val currentContext = LocalContext.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-36).dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                OutlinedButton(onClick = {
                    currentContext.startActivity(
                        Intent(currentContext, ShareActivity::class.java)
                            .also {
                                it.putExtra("firstName", firstName)
                                it.putExtra("lastName", lastName)
                                it.putExtra("firstIndex", firstRandomIndex)
                                it.putExtra("secondIndex", secondRandomIndex)
                            }
                    )
                }) {
                    Text(
                        text = "Share?",
                        style = MaterialTheme.typography.h2,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

    }
}


@ExperimentalMaterialApi
@Preview(
    showBackground = true,
    device = Devices.NEXUS_6P
)
@Composable
fun PreviewNameRevealScreen() {
    NameRevealScreen(NavHostController(LocalContext.current))
}
