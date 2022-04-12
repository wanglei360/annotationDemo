plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.annotation"
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {

        val release = getByName("release")
        release.apply {
            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/release/kotlin")) // 指定ksp生成目录，否则编译器不会生成代码
                }
            }
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        val debug = getByName("debug")
        debug.apply {
            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/debug/kotlin")) // 指定ksp生成目录，否则编译器不会生成代码
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
//    ksp {
//
//    }
}
dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")

    annotationProcessor(project(":apt_java"))
    kapt(project(":apt_kotlin"))
    ksp(project(":ksp_kotlin"))
    implementation(project(":annotation"))


}