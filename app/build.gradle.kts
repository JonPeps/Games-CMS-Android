
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jonpeps.gamescms"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    defaultConfig {
        applicationId = "com.jonpeps.gamescms"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        android.buildFeatures.buildConfig = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "DYNAMO_DB_ACCESS_KEY", "\"" + project.properties["dynamo_db_access_key"] + "\"")
        buildConfigField("String", "DYNAMO_DB_SECRET_KEY", "\"" + project.properties["dynamo_db_secret_key"] + "\"")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    kapt {
        correctErrorTypes = true
    }

    packaging {
        resources {
            resources.excludes += "**/*"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(platform(libs.bom.v2313))
    implementation(libs.dynamodb.enhanced)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    kotlin("kapt")
    implementation(libs.runtime)
    testImplementation(libs.junit.v412)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.core.v540)
    testImplementation(libs.mockito.junit.jupiter)

    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose.v182)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.moshi.kotlin.codegen)
    implementation(libs.hilt.android.v256)
    implementation(libs.androidx.room.runtime.v270)
    implementation(libs.androidx.room.ktx.v270)
    testImplementation(libs.androidx.room.testing)
    kapt(libs.hilt.compiler.v256)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.androidx.hilt.compiler.v110)
    implementation(libs.hilt.android.gradle.plugin.v256)
}