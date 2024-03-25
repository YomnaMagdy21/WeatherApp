plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding =true

    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")


    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
  //  annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
//Retrofit
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//Room
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
//Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
//ViewModel
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    //lottiefiles
    implementation ("com.airbnb.android:lottie:3.4.0")
    //openStreetMap
    implementation ("org.osmdroid:osmdroid-android:6.1.14")
//    implementation ("org.osmdroid:osmdroid-android:7.0.1")
//    implementation ("org.osmdroid:osmdroid-wms:7.0.1")
//    implementation ("org.osmdroid:osmdroid-mapsforge:7.0.1")
//    implementation ("org.osmdroid:osmdroid-geopackage:7.0.1")
//    implementation ("org.osmdroid:osmdroid-third-party:6.0.1")
//    implementation ("org.mapsforge:mapsforge-map-android:0.20.0")
//    implementation ("org.mapsforge:mapsforge-map:0.20.0")
//    implementation ("org.mapsforge:mapsforge-themes:0.20.0")
//
//
//    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
//    implementation (project(":osmdroid-android"))
//    implementation (project(":osmdroid-geopackage"))
//    implementation (project(":osmdroid-mapsforge"))
//    implementation (project(":osmdroid-wms"))
//    implementation (project(":osmdroid-shape"))

//location
    implementation("com.google.android.gms:play-services-location:21.1.0")

//
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$2.7.0")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$2.7.0")
//    kapt ("androidx.lifecycle:lifecycle-compiler:$2.7.0")
//
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$2.7.0")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$2.7.0")

//
//    // Dependencies for local unit tests
//    testImplementation ("junit:junit:$2.2.0")
//    testImplementation ("org.hamcrest:hamcrest-all:$2.2.0")
//    testImplementation ("androidx.arch.core:core-testing:$2.2.0")
//    testImplementation ("org.robolectric:robolectric:$2.2.0")
//
//    // AndroidX Test - JVM testing
//    testImplementation ("androidx.test:core-ktx:$2.6.1")
//
//    androidTestImplementation ("androidx.arch.core:core-testing:$2.2.0")
//
//
//    // AndroidX and Robolectric
//    testImplementation ("androidx.test.ext:junit-ktx:$2.6.1")
//    testImplementation ("androidx.test:core-ktx:$2.6.1")
//    testImplementation ("org.robolectric:robolectric:4.8")

    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

    //kotlinx-coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$1.7.3")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$1.7.3")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$1.7.3")

    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}