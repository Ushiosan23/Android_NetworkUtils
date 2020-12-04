// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:4.1.1")
		classpath("com.google.gms:google-services:4.3.4")
		classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.10")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
		classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
	}
}

allprojects {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
}

tasks.create("delete", Delete::class.java) {
	delete(rootProject.buildDir)
}
