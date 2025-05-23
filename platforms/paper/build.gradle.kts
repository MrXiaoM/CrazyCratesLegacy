@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("crazycrates.paper-plugin")
}

repositories {
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
}

dependencies {
    api(project(":crazycrates-api"))

    compileOnly(libs.papermc)

    implementation(libs.triumph.cmds)

    implementation(libs.nbt.api)
    implementation(libs.bstats.bukkit)

    compileOnly(libs.holographic.displays)
    compileOnly(libs.decent.holograms)
    compileOnly(libs.cmi.lib)
    compileOnly(fileTree("libs"))

    compileOnly(libs.placeholder.api)

    compileOnly(libs.itemsadder.api)

    compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")
    compileOnly("net.Indyuce:MMOItems-API:6.10-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}+Paper+${rootProject.version}.jar")

        listOf(
            "de.tr7zw.changeme.nbtapi",
            "org.bstats",
            "dev.triumphteam.cmd"
        ).forEach { relocate(it, "${rootProject.group}.library.$it") }
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().resources.srcDirs) {
            expand(
                "name" to rootProject.name,
                "group" to rootProject.group,
                "version" to rootProject.version,
                "description" to rootProject.description,
                "website" to "https://www.mcio.dev"
            )
            include("plugin.yml")
        }
    }
}
