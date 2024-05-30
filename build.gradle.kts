plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight)
    alias(libs.plugins.run.paper)
}

group = "gg.flyte"
version = "1.0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.flyte.gg/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    implementation(libs.twilight)
//    implementation(libs.keyforge)
    implementation(libs.paperlib)

    implementation("cloud.keyforge:keyforge:0.0.4")

    implementation(libs.lamp.common)
    implementation(libs.lamp.bukkit)
}

tasks {
    build { dependsOn(shadowJar) }
    runServer { minecraftVersion("1.20.4") }
    compileKotlin { kotlinOptions.jvmTarget = "17" }

    shadowJar {
        relocate("kotlin", "gg.flyte.kotlin")
        relocate("io.papermc.lib", "gg.flyte.paperlib")
        relocate("org.jetbrains.annotations", "gg.flyte.jetbrains.annotations")
        relocate("org.intellij.lang.annotations", "gg.flyte.intellij.lang.annotations")
    }
}