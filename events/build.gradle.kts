plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.egt.defaults)
    alias(libs.plugins.egt.loom)
}

dependencies {
    modCompileOnly(libs.flk)
}