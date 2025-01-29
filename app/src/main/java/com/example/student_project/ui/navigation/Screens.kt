package com.example.student_project.ui.navigation

sealed class Screens(val route: String) {
    data object SplashScreen : Screens("splash_screen")

    data object LoginScreen : Screens("login_screen")

    data object SignupScreen : Screens("signup_screen")

    data object AdditionalInfoScreen : Screens("additional_info_screen")

    data object EmailAndPhoneScreen : Screens("email_and_phone_screen")

    data object NewPasswordScreen : Screens("new_password_screen")

    data object OtpScreen : Screens("otp_screen")

    data object HomeScreen : Screens("home_screen")

    data object CourseFilterResultScreen : Screens("course_filter_result_screen")

    data object CourseDetailScreen : Screens("course_detail_screen")

    data object MentorFilterResultScreen : Screens("mentor_filter_result_screen")

    data object MentorFilterScreen : Screens("mentor_filter_screen")

    data object CourseFilterScreen : Screens("course_filter_screen")

    data object LearningScreen : Screens("learning_screen")

    data object ProfileScreen : Screens("profile_screen")
}
