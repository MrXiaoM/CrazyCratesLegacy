plugins {
    id("crazycrates.root-plugin")
}

dependencies {
    api(project(":crazycrates-api"))
    api(project(":crazycrates-paper"))
}
tasks {
    shadowJar {
        archiveClassifier.set("legacy")
    }
}
