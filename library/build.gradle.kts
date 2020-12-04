plugins {
	id("com.android.library")
	id("kotlin-android")
	id("kotlin-android-extensions")
	id("org.jetbrains.dokka")
	kotlin("plugin.serialization") version "1.4.0"
}

android {
	setCompileSdkVersion(30)
	buildToolsVersion("30.0.2")

	defaultConfig {
		versionCode = 1
		versionName = "1.0.0"

		minSdkVersion(23)
		targetSdkVersion(30)

		testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}

	/* kotlin configuration */
	kotlinOptions {
		jvmTarget = "11"
	}
}

/* dokka configuration */
tasks.dokkaHtml.configure {
	outputDirectory.set(buildDir.resolve("dokka"))
	/* configure source documentation */
	dokkaSourceSets {
		named("main") {
			suppress.set(false)
			includeNonPublic.set(true)
			skipDeprecated.set(false)
			skipEmptyPackages.set(false)
			sourceRoots.from(file("src"))
			jdkVersion.set(11)
			noStdlibLink.set(false)
			noJdkLink.set(false)
			noAndroidSdkLink.set(false)
		}
	}
}

/* var to use in file tree implementation */
val fileTreeImplementation = mapOf(
	"dir" to "libs",
	"include" to listOf("*.jar")
)

dependencies {
	/* file tree */
	implementation(fileTree(fileTreeImplementation))

	// Kotlin implementation
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1-native-mt")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.1-native-mt")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

	// Volley
	implementation("com.android.volley:volley:1.1.1")

	// Test framework implementation (no change this)
	testImplementation("junit:junit:4.13.1")
	androidTestImplementation("androidx.test.ext:junit:1.1.2")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

/* ---------------------------------------------------------
 *
 * Publication
 *
 * --------------------------------------------------------- */

ext.set("bintrayRepo", "AndroidMaven")
ext.set("bintrayName", "android_networkutils")

ext.set("publishedGroupId", "com.github.ushiosan23")
ext.set("libraryName", "android_networkutils")
ext.set("artifact", "android_networkutils")
ext.set("libraryDescription", "Network utilities for android platform.")

ext.set("siteUrl", "https://github.com/Ushiosan23/Android_NetworkUtils")
ext.set("gitUrl", "https://github.com/Ushiosan23/Android_NetworkUtils.git")

ext.set("libraryVersion", "1.0.0")

ext.set("developerId", "Ushiosan23")
ext.set("developerName", "Ushiosan23")
ext.set("developerEmail", "haloleyendee@outlook.com")

ext.set("licenseName", "MIT License")
ext.set("licenseUrl", "https://github.com/Ushiosan23/Android_NetworkUtils/blob/main/LICENCE")
ext.set("allLicenses", arrayOf("MIT License"))

apply(from = "install.gradle")
apply(from = "bintray.gradle")
