plugins {
    id("java")
}

group = "com.xiaoyu.mcsl"
version = "0.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.xiaoyu.mcsl.MinecraftServerLuncher"
    }
}
