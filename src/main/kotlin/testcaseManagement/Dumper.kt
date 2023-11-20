package testcaseManagement

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Dumper {
    fun dumpCases(cases: List<TestCase>, name: String) {
        var content = combineNewLine(caseContents(cases))
        genDumpFile(content, "${safeSave(name)}.cases")
    }

    private fun safeSave(string: String): String {
        return string.replace(" ", "_")
    }

    private fun caseContents(cases: List<TestCase>): List<String> {
        var outList: MutableList<String> = mutableListOf()
        cases.forEach { testCase ->
            var str = ""
            testCase.inputs.forEach {
                if (testCase.inputs.indexOf(it) < testCase.inputs.count()-1) {
                    str += "$it,"
                }
                else str += it
            }
            outList += str+" -> "+testCase.output+" "
        }
        return outList
    }

    private fun combineNewLine(input: List<String>): String {
        var output = ""
        for (s in input) {
            output += "$s\n"
        }
        return output
    }

    fun dumpGenFunc(func: String, name: String) {
        genDumpFile(func, "${safeSave(name)}.func")
    }

    private fun genDumpFile(content: String, name: String) {
        val path = Paths.get("").toAbsolutePath().toString()
        if (!Files.exists(Path.of("$path/runtime/dumps"))) {
            Files.createDirectory(Path.of("$path/runtime/dumps"))
        }
        if (!Files.exists(Path.of("$path/runtime/dumps/$name.dump"))) {
            Files.createFile(Path.of("$path/runtime/dumps/$name.dump"))
        }
        val dumpFile = File("$path/runtime/dumps/$name.dump")
        println("Generating Dump: $dumpFile")
        File("$path/runtime/dumps/$name.dump").writeText(content)
    }
}
