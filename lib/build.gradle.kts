description = ""
group = "dev.robinohs"
version = "1.0.1-SNAPSHOT"

val sonatypeUsername: String? = System.getenv("SONATYPE_USERNAME")
val sonatypePassword: String? = System.getenv("SONATYPE_PASSWORD")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("jacoco")
    id("org.sonarqube") version "3.3"
    id("java-library")
    id("maven-publish")
    id("signing")
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
    withJavadocJar()
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
        create<MavenPublication>("mavenJava") {
            artifactId = "totp-kt"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("totp-kt")
                description.set("Native Kotlin library for time-based TOTP and HMAC-based HOTP one-time passwords.")
                url.set("https://github.com/robinohs/totp-kt")
                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://mit-license.org/")
                    }
                }
                developers {
                    developer {
                        id.set("Roboh97")
                        name.set("Robin Ohs")
                        email.set("info@robinohs.dev")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/robinohs/totp-kt.git")
                    developerConnection.set("scm:git:https://github.com/robinohs/totp-kt.git")
                    url.set("https://github.com/robinohs/totp-kt")
                }
            }
        }
    }
    repositories {
        maven {
            // change URLs to point to your repos, e.g. http://my.org/repo
            name="oss"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}