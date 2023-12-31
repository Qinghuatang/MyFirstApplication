plugins {
    id("com.android.application")
}

android {
    namespace = "com.jnu.student"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jnu.student"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            // 设置支持的SO库架构（开发者可以根据需要，选择一个或多个平台的so）
            abiFilters.add("x86")
            abiFilters.add("x86_64")
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

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}


dependencies {
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.tencent.map:tencent-map-vector-sdk:4.3.4")
    implementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test:runner:1.5.2")

    // AndroidX Test Core
    androidTestImplementation("androidx.test:core:1.5.0")

    // AndroidX Test Runner
    androidTestImplementation("androidx.test:runner:1.5.2")

    // AndroidX Test Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



    implementation("de.hdodenhof:circleimageview:3.0.0")



}