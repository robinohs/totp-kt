description = ""
group = "dev.robinohs"
version = "1.0.3"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
    id("jacoco")
    id("org.sonarqube") version "3.3"
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    implementation("commons-codec:commons-codec:1.15")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

java {
    withSourcesJar()
}

jacoco {
    toolVersion = "0.8.8"
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }
    jacocoTestReport {
        reports {
            xml.required.set(true)
            xml.outputLocation.set(layout.buildDirectory.file("$buildDir/reports/jacoco/test/jacocoTestReport.xml"))
        }
        dependsOn(test)
    }
//    coverallsJacoco {
//        reportPath = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
//    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "totpkt"
            version = project.version as String

            from(components["java"])
        }
    }
}