plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

group = "com.xiaoyu.mcsl"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.13.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
    shadowJar {
        archiveBaseName.set("MinecraftServerLauncher")
        archiveClassifier.set("")
        archiveVersion.set("0.1.1")
        manifest {
            attributes["Main-Class"] = "com.xiaoyu.mcsl.MinecraftServerLuncher"
        }
    }
    build {
        dependsOn(shadowJar)
    }
}