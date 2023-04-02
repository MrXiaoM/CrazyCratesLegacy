plugins {
    id("crazycrates.root-plugin")
}
tasks {
    shadowJar {
        archiveClassifier.set("legacy")
    }
}
