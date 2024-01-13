package com.example.composeornot.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import com.example.composeornot.R
import com.example.composeornot.Screen

@Composable
fun DetailScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.clickable {
//                navController.popBackStack()   ==  the same as following:
                navController.navigate(route = Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            },
            text = "Details",
            color = Color.Red,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.clickable {
                navController.navigate(route = Screen.Detail.route)
            },
            text = "Home",
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmbeddedComponent(
    bigText: Int,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit,
) {
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )

    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CustomUiComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var value by remember { mutableIntStateOf(0) }

        CustomComponent(indicatorValue = value)

        TextField(
            value = value.toString(),
            onValueChange = { it: String ->
                value = if (it.isNotEmpty()) it.toInt() else 0
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    foregroundIndicatorStrokeWidth: Float = 100f,
    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bigTextSuffix: String = "KM",
    smallText: String = "Remaining",
    smallTextFontSize: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    smallTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
) {
    var allowedIndicatorValue by remember {
        mutableIntStateOf(maxIndicatorValue)
    }

    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }


    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000),
        label = "sweepAngle"
    )
    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000),
        label = "receivedValue"
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue =
        if (allowedIndicatorValue == 0)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000),
        label = "animatedBigTextColor"
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                    indicatorStrokeCap = indicatorStrokeCap
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                    indicatorStrokeCap = indicatorStrokeCap
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedComponent(
            bigText = receivedValue,
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    indicatorStrokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = indicatorStrokeCap
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2,
            y = (size.height - componentSize.height) / 2
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    indicatorStrokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = indicatorStrokeCap
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2,
            y = (size.height - componentSize.height) / 2
        )
    )
}

//@Composable
//fun CustomUiComponent() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        var value by remember { mutableIntStateOf(0) }
//
//        TextField(value = value.toString(), onValueChange = { it: String ->
//            value = if (it.isNotEmpty()) it.toInt() else 0
//        }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
//
//        Indicator(
//            progress = value.toFloat(),
//            modifier = Modifier.width(50.dp)
//        )}
//
//}

@Composable
fun PinCode() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by remember { mutableStateOf("") }
        val numberOfDigits = 4

        TextField(
            value = name,
            onValueChange = { it: String -> if (it.length <= numberOfDigits) name = it },
            label = { Text(text = "PIN") },
            placeholder = { Text(text = "Enter your PIN") },
            maxLines = 1
        )
    }
}

@Composable
fun PasswordFields() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val icon = if (passwordVisibility) {
            painterResource(id = R.drawable.ic_launcher_foreground)
        } else {
            painterResource(id = R.drawable.ic_launcher_background)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { it: String -> password = it },
            placeholder = { Text(text = "Password") },
            label = { Text(text = "Password") },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        contentDescription = "visibility icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val painter = rememberImagePainter(
            data = "https://img.freepik.com/premium-photo/a-blue-iguana-is-shown-in-this-image_9493-5826.jpg",
            builder = {
                placeholder(R.drawable.ic_launcher_background)
                crossfade(300)
                transformations(GrayscaleTransformation(), CircleCropTransformation())

            }
        )
        val painterState = painter.state
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = "iguana image"
        )
//        if (painterState is ImagePainter.State.Loading) {
//            CircularProgressIndicator()
//        }
    }
}

@Composable
fun MyButton() {
    var clicked by remember {
        mutableStateOf(false)
    }

    Surface(
        onClick = { clicked = !clicked },
        shape = ShapeDefaults.Medium,
        border = BorderStroke(width = 1.dp, color = Color.DarkGray),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "my button",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (!clicked) "Sing up with this button" else "Loading")
            if (clicked) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(8.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun InputText() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        var text by remember {
            mutableStateOf("Type here...")

        }
        OutlinedTextField(value = text, onValueChange = { newText ->
            text = newText
        },
            label = {
                Text(text = "title")
            },
            singleLine = false,
            maxLines = 3,
            leadingIcon = {
                IconButton(onClick = { Log.d("leading icon", "clicked") }) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "email icon")
                }
            },
            trailingIcon = {
                IconButton(onClick = { Log.d("trailing icon", "clicked") }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "check icon")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    Log.d("imeAction", "done")
                }
            )
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(title: String, description: String) {
    var expandedState by remember {
        mutableStateOf(false)
    }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = "label"
    )

    Card(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        shape = ShapeDefaults.Medium,
        onClick = { expandedState = !expandedState }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(200f)
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = { expandedState = !expandedState }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "expand"
                    )
                }
            }
            if (expandedState) {
                Text(text = description)
            }
        }
    }
}
