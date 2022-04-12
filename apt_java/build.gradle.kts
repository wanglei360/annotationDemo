plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":annotation"))
    implementation("com.squareup:javapoet:1.11.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


