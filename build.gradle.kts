plugins {
    `java-library`
    application
    kotlin("jvm") version "2.2.0"

    id("com.google.protobuf") version "0.9.4"
}

group = "org.mzverse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1:${osdetector.classifier}"
    }
}

dependencies {
    api("com.google.protobuf:protobuf-java:3.25.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("mz.genshincode.Main")
}