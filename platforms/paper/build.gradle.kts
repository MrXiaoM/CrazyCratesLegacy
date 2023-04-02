@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("crazycrates.paper-plugin")

    alias(settings.plugins.run.paper)
}

repositories {
    /**
     * PAPI Team
     */
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    /**
     * NBT Team
     */
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    api(project(":crazycrates-api"))

    compileOnly(libs.papermc)

    implementation(libs.triumph.cmds)

    implementation(libs.nbt.api)
    implementation(libs.bstats.bukkit)

    compileOnly(libs.holographic.displays)
    compileOnly(libs.decent.holograms)
    compileOnly(libs.cmi.api)
    compileOnly(libs.cmi.lib)

    compileOnly(libs.placeholder.api)

    compileOnly(libs.itemsadder.api)
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

    runServer {
        minecraftVersion("1.16.5")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "name" to rootProject.name,
                "group" to rootProject.group,
                "version" to rootProject.version,
                "description" to rootProject.description,
                "website" to "https://pds.ink"
            )
        }
    }
}
