import java.time.format.DateTimeFormatter
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED

logger.lifecycle("""
*******************************************
 You are building FastAsyncWorldEdit!

 If you encounter trouble:
 1) Read COMPILING.md if you haven't yet
 2) Try running 'build' in a separate Gradle run
 3) Use gradlew and not gradle
 4) If you still need help, ask on Discord! https://discord.gg/intellectualsites

 Output files will be in [subproject]/build/libs
*******************************************
""")

var rootVersion by extra("1.16")
var revision: String by extra("")
var buildNumber by extra("")
var date: String by extra("")

version = String.format("%s-%s", rootVersion, buildNumber)

allprojects {
    gradle.projectsEvaluated {
        tasks.withType(JavaCompile::class) {
            options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "1000"))
        }
        tasks.withType(Test::class) {
            testLogging {
                events(FAILED)
                exceptionFormat = FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
            }
        }
    }
}

applyCommonConfiguration()
