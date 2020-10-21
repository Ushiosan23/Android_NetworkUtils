// Build configuration
buildscript {
    /* Library properties */
    extra.set("kotlin_version", "1.4.10")
    /* app */
    extra.set("app_version", 1)
    extra.set("app_version_name", "0.0.1")
    /* jdk */
    extra.set("jvm_version", 11)
    /* android */
    extra.set("sdk_version", 30)
    extra.set("min_sdk_version", 23)
    extra.set("build_tools_version", "30.0.1")

    /* Repositories */
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    /* Dependencies */
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.10")
    }
}

// All project configuration
allprojects {

    /* Repositories */
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

// Create clean task
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}