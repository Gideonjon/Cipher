plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.cypher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cypher"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Navigation Dependency
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Circle Image View
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Dots Indicators
    implementation("com.tbuonomo:dotsindicator:5.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Updated to latest stable version
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Updated to latest stable version

    // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Updated to latest stable version


    //Progress Bar
    implementation("com.jpardogo.googleprogressbar:library:1.2.0")
}