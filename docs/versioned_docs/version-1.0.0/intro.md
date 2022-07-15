---
sidebar_position: 1
---

# Getting Started

## Installation

> Only Jitpack is supported in the alpha phase.

### Jitpack
#### Kotlin DSL
Add Jitpack to repositories:
```kotlin
//build.gradle.kts
repositories {  
  mavenCentral()  
  maven { url = uri("https://jitpack.io") }  
}
```
Add the dependency:
```kotlin
//build.gradle.kts
dependencies {
  implementation("com.github.robinohs:totp-kt:v1.0.0")
}
```
#### Maven
Add Jitpack to repositories:
```xml
<!--pom.xml-->
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Add the dependency:
```xml
<!--pom.xml-->
<dependency>
  <groupId>com.github.robinohs</groupId>
    <artifactId>totp-kt</artifactId>
    <version>v1.0.0</version>
</dependency>
```
#### Gradle
Add Jitpack to repositories:
```groovy
//build.gradle
allprojects {
  repositories {
    //...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency:
```groovy
//build.gradle
dependencies {
  implementation 'com.github.robinohs:totp-kt:v1.0.0'
}
```
