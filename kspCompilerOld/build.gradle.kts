import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.5.10-1.0.0-beta01"
}

dependencies {
    implementation(project(":annotation"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.20-RC-1.0.4")
    implementation("com.squareup:kotlinpoet:1.7.2")

    //一定要加这个  否则会出问题
    implementation("com.google.auto.service:auto-service-annotations:1.0-rc7")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:0.3.2")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}