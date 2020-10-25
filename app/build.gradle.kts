// Imports
import com.android.builder.core.DefaultApiVersion

// Library plugins
plugins {
	signing
	id("maven-publish")
	id("com.android.library")
	id("kotlin-android")
	id("kotlin-android-extensions")
	id("org.jetbrains.dokka")
	kotlin("plugin.serialization") version "1.4.0"
}

// Local functions
inline fun <reified T> prjExtra(property: String, default: T): T = try {
	rootProject.extra.get(property) as T ?: default
} catch (err: Exception) {
	default
}

// Local properties
val appSdkVersion = DefaultApiVersion(prjExtra("sdk_version", 30))
val appMinSdkVersion = DefaultApiVersion(prjExtra("min_sdk_version", 23))

// Android configuration
android {
	/* app sdk configuration */
	compileSdkVersion(appSdkVersion.apiLevel)
	buildToolsVersion = prjExtra("build_tools_version", "30.0.1")

	/* default configuration */
	defaultConfig {
		minSdkVersion = appMinSdkVersion
		targetSdkVersion = appSdkVersion
		versionCode = prjExtra("app_version", 1)
		versionName = prjExtra("app_version_name", "1.0")
	}

	/* build types configuration */
	buildTypes {
		/* release type */
		getByName("release") {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				file("proguard-rules.pro")
			)
		}
		/* debug type */
		getByName("debug") {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				file("proguard-rules.pro")
			)
		}
	}

	/* kotlin configuration */
	kotlinOptions {
		jvmTarget = prjExtra("jvm_version", "11")
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
			jdkVersion.set(prjExtra("jvm_version", 11))
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

/* app dependencies */
dependencies {
	/* file tree */
	implementation(fileTree(fileTreeImplementation))

	// Kotlin implementation
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.9")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")

	// Android implementation (no change this)
	implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
	implementation("androidx.navigation:navigation-ui-ktx:2.3.1")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.core:core-ktx:1.3.2")
	implementation("androidx.constraintlayout:constraintlayout:2.0.2")
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("com.google.android.material:material:1.2.1")
	implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
	implementation("androidx.navigation:navigation-ui-ktx:2.3.1")
	implementation("androidx.media:media:1.2.0")

	// Volley
	implementation("com.android.volley:volley:1.1.1")

	// Test framework implementation (no change this)
	testImplementation("junit:junit:4.13.1")
	androidTestImplementation("androidx.test.ext:junit:1.1.2")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

/* ---------------------------------------------------------
 *
 * Publish section
 *
 * --------------------------------------------------------- */

fun getUrlServer(version: String): String = if (version.endsWith("snapshot", true))
	"https://oss.sonatype.org/content/repositories/snapshots"
else
	"https://oss.sonatype.org/service/local/staging/deploy/maven2"

// Call this after evaluation project
afterEvaluate {
	// Publishing configuration extension
	publishing {
		/* base configuration vars */
		val projectName = rootProject.name.toLowerCase()
		val stringVersion = prjExtra("app_version_name", "1.0")
		val versionName = stringVersion.replace(".", "_")
		val urlServer = getUrlServer(stringVersion)

		/* configure publications */
		publications {

			/* maven publication */
			create<MavenPublication>("MavenAndroid") {
				groupId = prjExtra("group_id", "")
				artifactId = prjExtra("artifact_id", "")
				version = stringVersion

				/* Set components */
				from(components["release"])

				/* POM document */
				pom {
					name.set("${projectName}_$versionName")
					description.set(prjExtra("library_description", ""))
					url.set(prjExtra("project_url", ""))

					/* license */
					licenses {
						license {
							name.set(prjExtra("license_name", ""))
							url.set(prjExtra("license_url", ""))
						}
					}

					/* developers */
					developers {
						developer {
							id.set("Ushiosan23")
							name.set("Ushiosan23")
							email.set("haloleyendee@outlook.com")
						}
					}

					/* scm */
					scm {
						connection.set(prjExtra("scm_url", ""))
						developerConnection.set(prjExtra("scm_url_developer", ""))
						url.set(prjExtra("project_url", ""))
					}
				}

				/* dependencies */
				versionMapping {
					usage("java-api") {
						fromResolutionOf("runtimeClasspath")
					}
					usage("java-runtime") {
						fromResolutionResult()
					}
				}
			}


		}

		/* define publishing repositories */
		repositories {
			/* maven repository */
			maven {
				name = "MavenCentralRepository" // Optional
				url = uri(urlServer)

				/* credentials */
				credentials {
					username = "Ushiosan23"
					password = rootProject.properties["mavenPass"] as String? ?: ""
				}
			}
		}

	}

	// Signing documents
	signing {
		sign(publishing.publications["MavenAndroid"])
	}
}
