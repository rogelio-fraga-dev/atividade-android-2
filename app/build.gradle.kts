plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "br.com.faculdade.imepac"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "br.com.faculdade.imepac"
        minSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // RecyclerView para listas paginadas
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView para cards de equipamentos e manutenções
    implementation("androidx.cardview:cardview:1.0.0")

    // SwipeRefreshLayout para pull-to-refresh nas listas
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Import do Firebase BoM (Bill of Materials) para gerenciar versões automaticamente
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Dependência do Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    // Dependência do Firestore (Banco de dados NoSQL)
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Gráficos Pro
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}