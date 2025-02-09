plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "banquemisr.challenge05.movie"
    compileSdk = 35

    defaultConfig {
        applicationId = "banquemisr.challenge05.movie"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // UI testing
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.4.3")
    androidTestImplementation ("androidx.compose.ui:ui-tooling:1.4.3")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation ("org.mockito:mockito-core:4.6.1")
    androidTestImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // testing
    testImplementation( "junit:junit:4.13.2")
    testImplementation( "org.mockito:mockito-core:2.25.0")
    testImplementation( "org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation( "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")


    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.4")

    //coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // dagger hilt
    api("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // room db
    val roomVersion = "2.5.2"
    api("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    api("androidx.room:room-ktx:$roomVersion")

    // gson
    api ("com.google.code.gson:gson:2.10")

}

kapt {
    correctErrorTypes = true
}
