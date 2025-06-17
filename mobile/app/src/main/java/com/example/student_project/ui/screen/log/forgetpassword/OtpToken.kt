package com.example.student_project.ui.screen.log.forgetpassword

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.PopBackStackEntry
import com.example.student_project.ui.theme.ambientShadowColor
import com.example.student_project.ui.theme.borderOfTextFieldColor
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.spotShadowColor
import com.example.student_project.ui.theme.textFieldColor
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpTokenScreen(navController: NavController, userEmail: String?, studentRepo: StudentRepo) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
//    val otpTextString = "123456"
    val focusRequester = remember {
        FocusRequester()
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var otpText by remember {
        mutableStateOf("")
    }
    var verifyButtonVisibility by remember {
        mutableStateOf(true)
    }
    var resendButtonVisibility by remember {
        mutableStateOf(false)
    }

    var date by rememberSaveable {
        mutableIntStateOf(60)
    }
    var tokenState by remember {
        mutableStateOf<Result<String>?>(null)
    }
    LaunchedEffect(date) {
        if (date >= 1) {
            delay(1000)
            date--
        } else {
            resendButtonVisibility = true
            verifyButtonVisibility = false
        }
    }
    LaunchedEffect(scope) {
        tokenState = studentRepo.resetPasswordToken(userEmail.toString())

    }
    Column(
        modifier = Modifier
            .padding(
                start = Constant.normalPadding,
                end = Constant.normalPadding,
                top = 50.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Constant.normalPadding)
        ) {
            PopBackStackEntry(navController)
            Text(
                text = "Forgot Password",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight(700),
                fontSize = 24.sp,
                color = headLineColor,
                modifier = Modifier.padding(top = 18.dp),
            )
        }

        tokenState?.onSuccess {
            Text(text = "this code $it")
        }?.onFailure {
            Toast.makeText(context, "failed to load token", Toast.LENGTH_SHORT).show()
        }


        Text(
            text = "Code has send to " + userEmail.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = buttonColor,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(bottom = Constant.veryLargePadding)
        )

        OtpInputField(
            modifier = Modifier.focusRequester(focusRequester),
            otpText = otpText,
            otpLength = otpText.length,
            shouldShowCursor = true,
            shouldCursorBlink = true,
            onOtpModified = { value, otpFilled ->
                otpText = value
                if (otpFilled) {
                    Toast.makeText(context, "otp is filled", Toast.LENGTH_SHORT).show()
                }
            })

        Text(
            text = "Resend code in $date",
            color = buttonColor,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(top = Constant.veryLargePadding)
        )

        AnimatedVisibility(visible = verifyButtonVisibility) {

            Button(
                onClick = {
                    tokenState?.onSuccess {

                        if (otpText == it) {

                            //here we pass email to make sure that the changed password will be this email
                            navController.navigate(Screens.NewPasswordScreen.route + "/$otpText" + "/$userEmail")
                        } else {
                            Toast.makeText(context, "otp is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }?.onFailure {
                        Toast.makeText(context, "failed to load token", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(Constant.buttonRadios),
                modifier =
                Modifier
                    .padding(top = 40.dp, bottom = Constant.veryLargePadding)
                    .align(alignment = Alignment.CenterHorizontally)
                    .width(screenWidth * 90 / 100)
                    .shadow(
                        elevation = 10.dp,
                        RoundedCornerShape(Constant.buttonRadios),
                        spotColor = spotShadowColor.copy(alpha = 0.4f),
                        ambientColor = ambientShadowColor.copy(alpha = 0.35f),
                    ),
//                .height(screenHeight * 6 / 100),

                colors =
                ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_color)
                ),
            ) {
                Text(
                    text = "Verify",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(Constant.smallPadding),
                    fontWeight = FontWeight(700),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )
            }
        }
        AnimatedVisibility(visible = resendButtonVisibility) {
            Button(
                onClick = {
                    //resend to backend to get the new code
                    CoroutineScope(Dispatchers.IO).launch {
                        tokenState = studentRepo.resetPasswordToken(userEmail.toString())
                    }
                    date = 60
                    resendButtonVisibility = false
                    verifyButtonVisibility = true
                },
                shape = RoundedCornerShape(Constant.buttonRadios),
                modifier =
                Modifier
                    .padding(top = 40.dp, bottom = Constant.veryLargePadding)
                    .align(alignment = Alignment.CenterHorizontally)
                    .width(screenWidth * 90 / 100)
                    .shadow(
                        elevation = 10.dp,
                        RoundedCornerShape(Constant.buttonRadios),
                        spotColor = spotShadowColor.copy(alpha = 0.4f),
                        ambientColor = ambientShadowColor.copy(alpha = 0.35f),
                    ),
//                .height(screenHeight * 6 / 100),

                colors =
                ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_color)
                ),
            ) {
                Text(
                    text = "Resend",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(Constant.smallPadding),
                    fontWeight = FontWeight(700),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )
            }
        }
    }

}


@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
    onOtpModified: (String, Boolean) -> Unit
) {


    //we can make it read only and just filled the code once the request done
    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= 6) {
                onOtpModified(it.text, it.text.length == otpText.length)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(6) { index ->
                    CharacterContainer(
                        index = index,
                        otpText = otpText,
                        shouldShowCursor,
                        shouldCursorBlink
                    )
                }
            }
        }
    )

}

@Composable
fun CharacterContainer(
    index: Int,//1
    otpText: String,
    shouldShowCursor: Boolean,//true
    shouldCursorBlink: Boolean
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val isFocused = otpText.length == index

    var cursorVisibility by remember {
        mutableStateOf(shouldShowCursor)
    }
    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(500)
                cursorVisibility = !cursorVisibility
            }
        }
    }
    Box(
        modifier = Modifier
            .width(screenWidth * 12 / 100)
            .height(screenHeight * 7 / 100)
            .clip(RoundedCornerShape(Constant.buttonRadios))
            .border(1.dp, borderOfTextFieldColor, RoundedCornerShape(Constant.buttonRadios))
            .background(color = textFieldColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier,
            text = otpText.getOrNull(index)?.toString() ?: "",
            color = buttonColor,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
        )
        AnimatedVisibility(visible = isFocused && cursorVisibility) {

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .height(25.dp)
                    .background(buttonColor)
            )
        }
    }

}
