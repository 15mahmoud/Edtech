package com.example.student_project.screen.login.forgetpassword

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.navigation.Screens
import com.example.student_project.screen.uiconstant.PopBackStackEntry
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.textFieldColor

@Composable
fun OtpCodeScreen(
    state: OtpState,
    focusRequester: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.padding(top = 50.dp, start = 20.dp)) {
                PopBackStackEntry(navController)
                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    color = headLineColor,
                    modifier = Modifier.padding(top = 30.dp),
                )
            }

            // it should be text that say
            // code has been send to + phone number or email
            // we will get these phone number and email from previous screen by nav
            Row(
                modifier = Modifier.padding(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, CenterHorizontally),
            ) {
                state.code.forEachIndexed { index, number ->
                    OtpInputField(
                        number = number,
                        focusRequester = focusRequester[index],
                        onFocusChanged = { isFocused ->
                            if (isFocused) {
                                onAction(OtpAction.OnChangeFieldFocus(index))
                            }
                        },
                        onNumberChanged = { newNumber ->
                            onAction(OtpAction.OnEnterNumber(newNumber, index))
                        },
                        onKeyboardBack = { onAction(OtpAction.OnKeyboardBack) },
                        modifier =
                            Modifier.weight(1f)
                                .aspectRatio(1f)
                                .shadow(2.dp, RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp,
                                    color = textFieldColor,
                                    shape = RoundedCornerShape(12.dp),
                                ),
                    )
                }
            }
        }
        // here we need to make counter 1 min
        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 300.dp)) {
            Button(
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_color)
                    ),
                shape = RoundedCornerShape(100.dp),
                onClick = {
                    if (state.isValid == true) {
                        // here we need to see what we will do with this code
                        navController.navigate(Screens.NewPasswordScreen.route)
                    }
                },
            ) {
                Text(
                    text = "Verify",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun OtpInputField(
    number: Int?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    // when ever we change the number we will recreate the state again
    // which mean ui will recompose
    val text by
        remember(number) {
            mutableStateOf(
                TextFieldValue(
                    text = number?.toString().orEmpty(),
                    selection = TextRange(index = if (number != null) 1 else 0),
                )
            )
        }
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier =
            modifier
                .background(Color.White)
                .height(screenHeight * 6 / 100)
                .width(screenWidth / 19 / 100),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                // i can use toIntOrNull to change it to int for comparesion
                // with what i get from backend
                val newNumber = newText.text
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            cursorBrush = SolidColor(Color.Transparent),
            singleLine = true,
            textStyle =
                TextStyle(
                    textAlign = TextAlign.Center,
                    color = buttonColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                ),
            keyboardOptions =
                KeyboardOptions(
                    // this only allow digit keyboard
                    keyboardType = KeyboardType.NumberPassword
                ),
            modifier =
                Modifier.padding(10.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        // note here
                        // first isFocus is our var that can change
                        // second one is function
                        isFocused = it.isFocused
                        onFocusChanged(it.isFocused)
                    }
                    .onKeyEvent { event ->
                        val didPressDelete = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                        // this should be !=
                        if (didPressDelete && number == null) {
                            onKeyboardBack()
                        }
                        false
                    },
        )
    }
}
