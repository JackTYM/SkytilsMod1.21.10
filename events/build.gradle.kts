plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.egt.defaults)
    alias(libs.plugins.egt.loom)
}

dependencies {
    modCompileOnly(libs.flk)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))