package com.example.student_project.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.student_project.R

val RobotoRegular = FontFamily(Font(R.font.roboto_regular))
val RobotoBold = FontFamily(Font(R.font.roboto_bold))
val RobotoThin = FontFamily(Font(R.font.roboto_thin))
// not used yet
val RobotLight = FontFamily(Font(R.font.roboto_light))
val EncodeSens = FontFamily(Font(R.font.encode_sans_bold))
val AROneSansRegular = FontFamily(Font(R.font.ar_one_sans_regular))
val SFProDisplay = FontFamily(Font(R.font.sf_pro_display_medium))
// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        headlineLarge = TextStyle(fontFamily = RobotoBold),
        headlineSmall = TextStyle(fontFamily = RobotoThin),
        titleMedium = TextStyle(fontFamily = RobotoRegular),

        /* Other default text styles to override
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
        */
    )
