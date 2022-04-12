pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.buildFileName = "build.gradle.kts" // 指定脚本为build.gradle.kts，下一步介绍
rootProject.name="annotationDemo" // 项目名称，自己取

include("app")
include("annotation")
include("apt_java")
include("apt_kotlin")
include("ksp_kotlin")
