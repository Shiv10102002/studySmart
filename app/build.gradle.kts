plugins {
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    id("com.android.application")
    id ("kotlin-android")
    id ("dagger.hilt.android.plugin")
    id ("com.google.devtools.ksp")
}

android {
    namespace = "com.shiv.studysmart"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shiv.studysmart"
        minSdk = 26
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
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
    //dagger hilt
    val hilt = "2.56.2"
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:$hilt")
    ksp("com.google.dagger:hilt-android-compiler:$hilt")

    // Room Database
    val room_version = "2.7.2"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // compose destination
    val destinationVersion = "1.9.52"
    implementation("io.github.raamcosta.compose-destinations:core:$destinationVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$destinationVersion")

    // fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.3")

    implementation(libs.androidx.compose.bom)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
   //desuger jdk
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
}
