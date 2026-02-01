plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.egt.defaults)
    alias(libs.plugins.egt.loom)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    modImplementation(libs.flk)
    // hack to return non null dependency
    include(implementation(libs.elementa.asProvider().get())!!)
    include(modImplementation(libs.universalcraft.get())!!)
    implementation(project(":events"))
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
        archiveBaseName.set("Skytils-1.21.10")
    }
}