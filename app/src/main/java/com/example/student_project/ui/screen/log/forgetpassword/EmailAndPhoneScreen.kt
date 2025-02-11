package com.example.student_project.ui.screen.log.forgetpassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.PopBackStackEntry
import com.example.student_project.ui.theme.borderButton
import com.example.student_project.ui.theme.cardColor
import com.example.student_project.ui.theme.darkerGrayColor
import com.example.student_project.ui.theme.headLineColor

@Composable
fun EmailAndPhoneScreen(navController: NavController, userEmail: String?) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    //  val scope = rememberCoroutineScope()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.padding(top = 50.dp, start = 10.dp, bottom = 50.dp)) {
            PopBackStackEntry(navController)
            Text(
                text = "Forgot Password",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp,
                color = headLineColor,
                modifier = Modifier.padding(top = 30.dp, start = 10.dp),
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                modifier =
                Modifier
                    .width(screenWidth * 64 / 100)
                    .height(screenHeight * 28 / 100)
                    .align(alignment = Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.forgot_password),
                contentDescription = null,
            )
        }
        Text(
            modifier =
            Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 50.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
            text = "Select which contact details should we use to reset your password",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 20 / 100)
                        .padding(10.dp)
                        .clickable {
                            // here we call backend and give email and back will response with
                            // phone number
                            // will navigate to otp code screen and we will pass phone with it
                            // to give it to back to give me code
                            navController.navigate(Screens.OtpTokenScreen.route )
                        },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(width = 3.dp, color = borderButton),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Card(
                            modifier =
                            Modifier
                                .padding(10.dp)
                                .height(screenHeight * 9 / 100)
                                .width(screenWidth * 19 / 100),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                        ) {
                            Image(
                                modifier =
                                Modifier
                                    .padding(15.dp)
                                    .align(alignment = Alignment.CenterHorizontally)
                                    .size(30.dp),
                                painter = painterResource(id = R.drawable.sms),
                                contentDescription = null,
                            )
                        }
                        Column(modifier = Modifier.padding(start = 15.dp)) {
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = "via SMS:",
                                style = MaterialTheme.typography.headlineSmall,
                                color = darkerGrayColor,
                                fontSize = 14.sp,
                            )
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = "here we will take phone from backend",
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
                Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 20 / 100)
                        .padding(10.dp)
                        .clickable {
                            // will navigate to otp code screen and we will pass email with it
                            // to give it to backend to give me code
                            navController.navigate(Screens.OtpTokenScreen.route + "/$userEmail")
                        },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(width = 3.dp, color = borderButton),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Card(
                            modifier =
                            Modifier
                                .padding(10.dp)
                                .height(screenHeight * 9 / 100)
                                .width(screenWidth * 19 / 100),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                        ) {
                            Image(
                                modifier =
                                Modifier
                                    .padding(15.dp)
                                    .align(alignment = Alignment.CenterHorizontally)
                                    .size(30.dp),
                                painter = painterResource(id = R.drawable.gmail_otp),
                                contentDescription = null,
                            )
                        }
                        Column(modifier = Modifier.padding(start = 15.dp)) {
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = "via Email:",
                                style = MaterialTheme.typography.headlineSmall,
                                color = darkerGrayColor,
                                fontSize = 14.sp,
                            )
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = userEmail.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
