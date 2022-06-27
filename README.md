# totp-kt - Kotlin OTP Library
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/summary/new_code?id=robinohs_totp-kt)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![CircleCI](https://dl.circleci.com/status-badge/img/gh/robinohs/totp-kt/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/robinohs/totp-kt/tree/main) [![codecov](https://codecov.io/gh/robinohs/totp-kt/branch/main/graph/badge.svg?token=2OT80TLHK9)](https://codecov.io/gh/robinohs/totp-kt) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=robinohs_totp-kt&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=robinohs_totp-kt) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=robinohs_totp-kt&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=robinohs_totp-kt) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=robinohs_totp-kt&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=robinohs_totp-kt) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=robinohs_totp-kt&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=robinohs_totp-kt)


Native Kotlin library for time-based (TOTP) and HMAC-based (HOTP) one-time passwords.
Enables the developer to:
- validate and generate TOTP and HOTP one-time passwords,
- generate randomly secure secrets to use with authenticators,
- generate randomly secure recovery codes.

# Navigation
 - [Installation](#installation)
	- [Jitpack](#jitpack)
		- [Kotlin DSL](#kotlin-dsl)
		- [Maven](#maven)
		- [Gradle](#gradle)
 - [Usage](#usage)
    - [Example](#example)
    - [Spring Boot](#spring-boot)
  - [License](#license)

# Installation
## Jitpack
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
	implementation("com.github.robinohs:totp-kt:v1.0.2-alpha")
}
```
#### Maven
Add Jitpack to repositories:
```kotlin
//pom.xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency:
```kotlin
//pom.xml
<dependency>
    <groupId>com.github.robinohs</groupId>
    <artifactId>totp-kt</artifactId>
    <version>v1.0.2-alpha</version>
</dependency>
```
#### Gradle
Add Jitpack to repositories:
```kotlin
//build.gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency:
```kotlin
//build.gradle
dependencies {
	implementation 'com.github.robinohs:totp-kt:v1.0.2-alpha'
}
```
# Usage
## Example
## Spring Boot
# License
