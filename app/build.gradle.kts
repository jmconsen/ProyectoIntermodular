plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //AÑADIMOS ESTA LINEA PARA QUE FUNCIONE EL PLUGIN DE FIREBASE
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyectointermodular"

    //CAMBIADO DE 34 A 35
    //compileSdk = 34
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyectointermodular"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    //AÑADIMOS ESTAS DOS LINEAS PARA QUE FUNCIONE EL PLUGIN DE FIREBASE
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //AÑADIMOS ESTA LINEA PARA QUE FUNCIONE EL ICONO DE LA APP
    implementation ("androidx.compose.material:material-icons-extended:1.0.0")

    //COPIADO
    //AÑADIMOS ESTAS LINEAS PARA QUE FUNCIONE EL PLUGIN DE FIREBASE
    //implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    //implementation("com.google.firebase:firebase-analytics")
    //implementation ("com.google.firebase:firebase-auth-ktx")
    //implementation ("com.google.firebase:firebase-firestore-ktx")


    // AÑADIMOS ESTAS LINEAS PARA QUE FUNCIONE EL PLUGIN DE FIREBASE
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    //AÑADIMOS ESTAS LINEAS PARA QUE FUNCIONE EL PLUGIN DE GOOGLE
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.android.gms:play-services-basement:18.2.")
    //implementation ("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")
    implementation("androidx.compose.foundation:foundation:<latest_version>")
    implementation("androidx.compose.material3:material3:<latest_version>")
    implementation("androidx.navigation:navigation-compose:<latest_version>")
}