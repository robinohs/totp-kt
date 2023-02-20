---
sidebar_position: 1
---

# Getting Started

## Installation

### Maven

#### Kotlin DSL
Add the dependency:
```kotlin
//build.gradle.kts
dependencies {
  implementation("dev.robinohs:totp-kt:v1.0.1")
}
```
#### Maven
Add the dependency:
```xml
<!--pom.xml-->
<dependency>
    <groupId>dev.robinohs</groupId>
    <artifactId>totp-kt</artifactId>
    <version>1.0.1</version>
</dependency>
```
#### Gradle
Add the dependency:
```groovy
//build.gradle
dependencies {
  implementation 'dev.robinohs:totp-kt:v1.0.1'
}
```

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
  implementation("com.github.robinohs:totp-kt:v1.0.1")
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
    <version>1.0.1</version>
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
  implementation 'com.github.robinohs:totp-kt:v1.0.1'
}
```
