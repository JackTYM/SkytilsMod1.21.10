import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.egt.defaults)
    alias(libs.plugins.egt.loom)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.hypixel.net/repository/Hypixel")
}

val relocated: Configuration by configurations.creating

dependencies {
    modImplementation(libs.flk)
    // hack to return non null dependency
    include(implementation(libs.elementa.asProvider().get())!!)
    relocated(implementation(libs.elementa.layoutdsl.get())!!)
    include(implementation(libs.vigilance.get())!!)
    include(modImplementation(libs.universalcraft.get())!!)
    implementation(project(":events", configuration = "namedElements"))
    relocated(project(":events")) {
        isTransitive = false
    }
    implementation(libs.hypixelmodapi)
    libs.bundles.cloud.get().forEach { include(modImplementation(it)!!) }
    modImplementation(libs.bundles.fabricapi)
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "version" to project.version,
                "flk" to ">=${libs.versions.flk.get()}"
            ))
        }
    }

    remapJar {
        archiveBaseName.set(shadowJar.flatMap(ShadowJar::getArchiveBaseName))
        inputFile.set(shadowJar.flatMap(ShadowJar::getArchiveFile))
    }

    shadowJar {
        archiveBaseName.set("Skytils-1.21.10")
        archiveClassifier.set("dev")
        configurations = listOf(relocated)

        relocate("gg.essential.elementa.unstable", "gg.skytils.elementa.unstable")
    }
}