plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}
dependencies {
    implementation("androidx.annotation:annotation:1.3.0")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}