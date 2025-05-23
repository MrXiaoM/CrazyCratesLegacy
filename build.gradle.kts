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

        listOf(
            "de.tr7zw.changeme.nbtapi",
            "org.bstats",
            "dev.triumphteam.cmd"
        ).forEach { relocate(it, "${rootProject.group}.library.$it") }
    }
}
