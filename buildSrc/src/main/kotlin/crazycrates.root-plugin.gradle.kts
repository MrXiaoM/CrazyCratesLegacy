plugins {
    `java-library`

    id("com.github.hierynomus.license")

    id("com.gradleup.shadow")
}

repositories {
    mavenLocal()
    if (java.util.Locale.getDefault().country == "CN") {
        maven("https://mirrors.huaweicloud.com/repository/maven/")
    }
    mavenCentral()
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://repo.helpch.at/releases/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
    maven("https://jitpack.io/")
}

val javaVersion = JavaVersion.VERSION_17

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

license {
    header = rootProject.file("LICENSE")
    encoding = "UTF-8"

    mapping("java", "JAVADOC_STYLE")

    include("**/*.java")
}