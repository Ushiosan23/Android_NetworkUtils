// Build configuration
buildscript {
	/* Library properties */
	extra.set("group_id", "com.github.ushiosan23")
	extra.set("artifact_id", "androidnetworkutils")
	extra.set("library_description", "Network utilities for Android platform")
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
	/* url */
	extra.set("project_url", "https://github.com/Ushiosan23/Android_NetworkUtils.git")
	extra.set("scm_url", "scm:git:git://github.com/Ushiosan23/Android_NetworkUtils.git")
	extra.set("scm_url_developer", "scm:git:ssh://github.com/Ushiosan23/Android_NetworkUtils.git")

	/* licence */
	extra.set("license_name", "MIT LICENSE")
	extra.set("license_url", "https://raw.githubusercontent.com/Ushiosan23/Android_NetworkUtils/main/LICENSE")

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
if (tasks.findByName("clean") == null) {
	tasks.register("clean", Delete::class) {
		delete(rootProject.buildDir)
	}
}

/* ---------------------------------------------------------
 *
 * Local functions
 *
 * --------------------------------------------------------- */

// Local functions
inline fun <reified T> prjExtra(property: String, default: T): T = try {
	rootProject.extra.get(property) as T ?: default
} catch (err: Exception) {
	default
}
