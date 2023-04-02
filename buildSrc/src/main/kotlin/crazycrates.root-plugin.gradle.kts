plugins {
    `java-library`

    `maven-publish`

    id("com.github.hierynomus.license")

    id("com.github.johnrengelman.shadow")
}

repositories {
    maven("https://repo.triumphteam.dev/snapshots/")

    maven("https://libraries.minecraft.net/")

    maven("https://repo.crazycrew.us/api/")

    maven("https://jitpack.io/")

    mavenCentral()
    mavenLocal()
}

val javaVersion = 8

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

tasks {
    compileJava {
        options.release.set(javaVersion)
    }
}

license {
    header = rootProject.file("LICENSE")
    encoding = "UTF-8"

    mapping("java", "JAVADOC_STYLE")

    include("**/*.java")
}