package code.scripting

import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.CompoundDependenciesResolver
import kotlin.script.experimental.dependencies.DependsOn
import kotlin.script.experimental.dependencies.FileSystemDependenciesResolver
import kotlin.script.experimental.dependencies.maven.MavenDependenciesResolver
import kotlin.script.experimental.dependencies.resolveFromScriptSourceAnnotations
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

object ScriptWithMavenDepsConfiguration : ScriptCompilationConfiguration(
    {
        // Implicit imports for all scripts of this type
        defaultImports(DependsOn::class)
        jvm {
            // Extract the whole classpath from context classloader and use it as dependencies
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        // Callbacks
        refineConfiguration {
            // Process specified annotations with the provided handler
            onAnnotations(DependsOn::class, handler = ::configureMavenDepsOnAnnotations)
        }
    }
) {
    private fun readResolve(): Any = ScriptWithMavenDepsConfiguration
}

@KotlinScript(
    // File extension for the script type
    fileExtension = "scriptwithdeps.kts",
    // Compilation configuration for the script type
    compilationConfiguration = ScriptWithMavenDepsConfiguration::class
)
abstract class ScriptWithMavenDeps


class ScriptingSetup {
    private fun evalFile(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<ScriptWithMavenDeps>()
        return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
    }

    fun runScript() {
        val path = Paths.get("").toAbsolutePath().toString()
        val scriptFile = File("$path/runtime/execution.kts")
        evalFile(scriptFile)
    }

    fun genScript(content: String) {
        val path = Paths.get("").toAbsolutePath().toString()
        if (!Files.exists(Path.of("$path/runtime"))) {
            Files.createDirectory(Path.of("$path/runtime"))
            if (Files.exists(Path.of("$path/runtime/execution.kts"))) {
                Files.createFile(Path.of("$path/runtime/execution.kts"))
            }
        }
        val scriptFile = File("$path/runtime/execution.kts")
        println("Generating Script: $scriptFile")
        File("runtime/execution.kts").writeText(content)
    }

    fun parseInputs(inputs: List<String>) {
        val path = Paths.get("").toAbsolutePath().toString()
        if (Files.exists(Path.of("$path/runtime/execution.kts"))) {
            var lines: MutableList<String> = File("runtime/execution.kts").readLines().toMutableList()
            var line1 = "var inputs: List<Any> = listOf("
            line1 += unifyString(inputs)
            line1 += ")"
            lines[getIndexOfInput(lines)] = line1
            File("runtime/execution.kts").writeText(combineNewLine(lines))
        }
    }

    private fun getIndexOfInput(list: MutableList<String>): Int {
        list.forEach {
            if (it.contains("var inputs: List<Any>")) {
                return list.indexOf(it)
            }
        }
        return -1
    }

    fun readOutput(): String {
        var string = readLastLine()
        if (string.startsWith("}")) {
            string = "ERROR WHILE FETCHING OUTPUT [NO OUTPUT VALUE WAS SET] [DEBUG YOUR CODE]"
        } else {
            delLastLine()
        }
        return string
    }

    private fun delLastLine() {
        val path = Paths.get("").toAbsolutePath().toString()
        if (Files.exists(Path.of("$path/runtime/execution.kts"))) {
            var lines: MutableList<String> = File("runtime/execution.kts").readLines().toMutableList()
            lines[lines.count()-1] = ""
            File("runtime/execution.kts").writeText(combineNewLine(lines))
        }
    }

    private fun unifyString(input: List<String>): String {
        var output = ""
        for (s in input) {
            if (input.indexOf(s) != input.count()-1) {
                output += "$s,"
            } else {
                output += s
            }
        }
        return output
    }

    private fun combineNewLine(input: List<String>): String {
        var output = ""
        for (s in input) {
            output += "$s\n"
        }
        return output
    }

    private fun readLastLine(): String {
        val path = Paths.get("").toAbsolutePath().toString()
        if (Files.exists(Path.of("$path/runtime/execution.kts"))) {
            var lines: List<String> = File("runtime/execution.kts").readLines()
            lines = lines.reversed()
            return lines[0]
        }
        return ""
    }

    fun putScript(ownCode: String): String {
        return "import java.io.File\n" +
                "import java.nio.file.Files\n" +
                "import java.nio.file.Path\n" +
                "import java.nio.file.Paths\n" +
                "/* Do Not Modify */\n" +
                "/* ------------------------------------------------------ */\n" +
                "\n" +
                "\n" +
                "\n" +
                ownCode+"\n" +
                "\n" +
                "\n" +
                "\n" +
                "/* ------------------------------------------------------ */\n" +
                "/* Do Not Modify */\n" +
                "fun putOutput(output: String) {\n" +
                "    var outString=\"\"\n" +
                "    File(\"runtime/execution.kts\").readLines().toMutableList().forEach{outString+=\"${'$'}it\\n\"}\n" +
                "    File(\"runtime/execution.kts\").writeText(\"${'$'}outString${'$'}output\\n\")\n" +
                "}"
    }

    private fun readFirstLine(): String {
        val path = Paths.get("").toAbsolutePath().toString()
        if (Files.exists(Path.of("$path/runtime/execution.kts"))) {
            var lines: MutableList<String> = File("runtime/execution.kts").readLines().toMutableList()
            return lines[getIndexOfInput(lines)]
        }
        return ""
    }
}

// Handler that reconfigures the compilation on the fly
fun configureMavenDepsOnAnnotations(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
    val annotations = context.collectedData?.get(ScriptCollectedData.collectedAnnotations)?.takeIf { it.isNotEmpty() }
        ?: return context.compilationConfiguration.asSuccess()
    return runBlocking {
        resolver.resolveFromScriptSourceAnnotations(annotations)
    }.onSuccess {
        context.compilationConfiguration.with {
            dependencies.append(JvmDependency(it))
        }.asSuccess()
    }
}

private val resolver = CompoundDependenciesResolver(FileSystemDependenciesResolver(), MavenDependenciesResolver())
