plugins {
    `java-library`

    id("com.github.hierynomus.license")

    id("com.github.johnrengelman.shadow")
}

repositories {
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.crazycrew.us/api/")
    maven("https://jitpack.io/")
    mavenCentral()
    mavenLocal()
}

val javaVersion = JavaVersion.VERSION_1_8

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
}

license {
    header = rootProject.file("LICENSE")
    encoding = "UTF-8"

    mapping("java", "JAVADOC_STYLE")

    include("**/*.java")
}