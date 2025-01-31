import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.example.student_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.student_project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ktfmt {
    kotlinLangStyle()
}
tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)

    implementation(libs.androidx.window)
    implementation(libs.androidx.window.v110)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.material:material:1.7.7")
    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //life cycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    //navigation
    implementation("androidx.navigation:navigation-compose:2.8.6")
    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    //splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    //coil
    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.accompanist:accompanist-coil:0.10.0")
    //
    implementation("androidx.concurrent:concurrent-futures:1.2.0")

    // Kotlin
    implementation("androidx.concurrent:concurrent-futures-ktx:1.2.0")
}