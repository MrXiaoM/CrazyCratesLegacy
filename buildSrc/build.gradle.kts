plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin:0.16.1")
    implementation("com.github.johnrengelman:shadow:8.1.0")
}