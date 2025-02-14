package com.example.student_project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.student_project.DepContainer
import com.example.student_project.ui.screen.LearningScreen
import com.example.student_project.ui.screen.details.CourseDetailsScreen
import com.example.student_project.ui.screen.details.course.CourseLessonScreen
import com.example.student_project.ui.screen.home.content.HomeScreen
import com.example.student_project.ui.screen.home.filtering.filteration.CourseFilterScreen
import com.example.student_project.ui.screen.home.filtering.filteration.MentorFilterScreen
import com.example.student_project.ui.screen.home.filtering.filterationresult.CourseFilterResultScreen
import com.example.student_project.ui.screen.home.filtering.filterationresult.MentorFilterResultScreen
import com.example.student_project.ui.screen.SplashScreen
import com.example.student_project.ui.screen.details.MentorDetailsScreen
import com.example.student_project.ui.screen.log.forgetpassword.EmailAndPhoneScreen
import com.example.student_project.ui.screen.log.forgetpassword.NewPasswordScreen
import com.example.student_project.ui.screen.log.forgetpassword.OtpTokenScreen
import com.example.student_project.ui.screen.log.login.LoginScreen
import com.example.student_project.ui.screen.log.signup.AdditionalInfoScreen
import com.example.student_project.ui.screen.log.signup.SignUpScreen
import com.example.student_project.ui.screen.profile.ProfileScreen
import com.example.student_project.ui.screen.profile.editprofile.EditProfileScreen
import com.example.student_project.ui.screen.profile.helpcenter.HelpCenterScreen
import com.example.student_project.ui.screen.profile.invitefriends.InviteFriendsScreen
import com.example.student_project.ui.screen.profile.notification.NotificationScreen
import com.example.student_project.ui.screen.profile.payment.PaymentScreen
import com.example.student_project.ui.screen.profile.privacypolicy.PrivacyPolicyScreen
import com.example.student_project.ui.screen.profile.security.SecurityScreen

@Composable
fun Navigation(depContainer: DepContainer) {
    val navController = rememberNavController()
    // start from splash
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.SplashScreen.route) { SplashScreen(navController) }
        composable(Screens.SignupScreen.route) { SignUpScreen(navController) }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController, depContainer.studentRepo)
        }
        composable(
            Screens.AdditionalInfoScreen.route + "/{email}" + "/{password}",
            arguments =
            listOf(
                navArgument(name = "email") { type = NavType.StringType },
                navArgument(name = "password") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            AdditionalInfoScreen(
                navController = navController,
                backStackEntry.arguments?.getString("email"),
                backStackEntry.arguments?.getString("password"),
                depContainer.studentRepo
            )
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                navController,
                depContainer.courseRepo,
                depContainer.studentRepo,
                depContainer.instructorRepo
            )
        }
        composable(
            Screens.CourseDetailScreen.route + "/{course_title}",
            arguments = (listOf(navArgument(name = "course_title") { type = NavType.StringType })),
        ) { backStackEntry ->
            CourseDetailsScreen(
                navController = navController,
                backStackEntry.arguments?.getString("course_title"),
                depContainer.courseRepo,
            )
        }
        composable(Screens.LearningScreen.route) { LearningScreen(navController = navController) }
        composable(Screens.ProfileScreen.route) { ProfileScreen(navController = navController) }
        composable(Screens.MentorFilterScreen.route) {
            // filled parcel object "FiltrationResult" and send it to search result screen
            MentorFilterScreen(navController)
        }
        composable(
            Screens.MentorDetailsScreen.route + "/{instructor_id}", arguments = listOf(
                navArgument(name = "instructor_id") { type = NavType.StringType })
        ) { backStackEntry ->
            MentorDetailsScreen(
                navController = navController,
                instructorId = backStackEntry.arguments?.getString("instructor_id")
            )

        }
        composable(
            Screens.MentorFilterResultScreen.route +
                    "/{jop_title}" +
                    "/{rating}" +
                    "/{hourly_rate}",
            arguments =
            (listOf(
                navArgument(name = "jop_title") { type = NavType.StringType },
                navArgument(name = "rating") { type = NavType.FloatType },
                navArgument(name = "hourly_rate") { type = NavType.FloatType },
            )),
        ) { backStackEntry ->
            MentorFilterResultScreen(
                navController = navController,
                backStackEntry.arguments?.getString("jop_title"),
                backStackEntry.arguments?.getFloat("rating"),
                backStackEntry.arguments?.getFloat("hourly_rate"),
                depContainer.instructorRepo
            )
        }
        // when we click
        // we will send this data to detailsScreen
        // here will be course details screen

        composable(Screens.CourseFilterScreen.route) {
            // filled parcel object "FiltrationResult" and send it to search result screen
            CourseFilterScreen(navController)
        }
        //
        composable(
            Screens.CourseFilterResultScreen.route +
                    "/{course_category}" +
                    "/{difficulty_level}" +
                    "/{released_date}" +
                    "/{rating}" +
                    "/{hourly_rate}",
            arguments =
            (listOf(
                navArgument(name = "course_category") { type = NavType.StringType },
                navArgument(name = "difficulty_level") { type = NavType.StringType },
                navArgument(name = "released_date") { type = NavType.StringType },
                navArgument(name = "rating") { type = NavType.FloatType },
                navArgument(name = "hourly_rate") { type = NavType.FloatType },
            )),
        ) { backStackEntry ->
            CourseFilterResultScreen(
                navController,
                backStackEntry.arguments?.getString("course_category"),
                backStackEntry.arguments?.getString("difficulty_level"),
                backStackEntry.arguments?.getString("released_date"),
                backStackEntry.arguments?.getFloat("rating"),
                backStackEntry.arguments?.getFloat("hourly_rate"),
                depContainer.courseRepo,
            )
        }
        // here we will send this info to course result screen
        // and then we will send this info to course details screen
        // there will be 2 fun one for courseResultScreen
        // and other one will be for courseDetailsScreen
        // will activate when we click on a course

        composable(
            Screens.EmailAndPhoneScreen.route + "/{user_email}",
            arguments = listOf(navArgument(name = "user_email") { type = NavType.StringType }),
        ) { backStackEntry ->
            EmailAndPhoneScreen(
                navController = navController,
                backStackEntry.arguments?.getString("user_email"),
            )
        }
        composable(Screens.NewPasswordScreen.route) {
            NewPasswordScreen(navController = navController)
        }
        composable(
            Screens.OtpTokenScreen.route + "/{user_email}",
            arguments = listOf(navArgument(name = "user_email") { type = NavType.StringType }),
        ) { backStackEntry ->
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            OtpTokenScreen(
                navController = navController,
                backStackEntry.arguments?.getString("user_email"),
            )
        }
        composable(Screens.EditProfileScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            EditProfileScreen(navController = navController, depContainer.studentRepo)
        }
        composable(Screens.NotificationScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            NotificationScreen(navController = navController)
        }
        composable(Screens.PaymentScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            PaymentScreen(navController = navController)
        }
        composable(Screens.SecurityScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            SecurityScreen(navController = navController)
        }
        composable(Screens.PrivacyPolicyScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            PrivacyPolicyScreen(navController = navController)
        }
        composable(Screens.HelpCenterScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            HelpCenterScreen(navController = navController)
        }
        composable(Screens.InviteFriendsScreen.route) {
            // we should take a phone or email from previous screen
            // then send it to backend and get otp code
            // then pass it if true move to next screen
            InviteFriendsScreen(navController = navController)
        }
        composable(
            Screens.CourseLessonScreen.route + "/{video_url}",
            arguments = listOf(navArgument(name = "video_url") { type = NavType.StringType }),
        ) { backStackEntry ->
            CourseLessonScreen(
                navController = navController,
                backStackEntry.arguments?.getString("video_url"),
            )
        }
    }
}
