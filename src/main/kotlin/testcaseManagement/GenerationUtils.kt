package testcaseManagement

import androidx.compose.runtime.MutableState
import code.scripting.ScriptingSetup

class GenerationUtils(val sm: ScriptingSetup) {
    fun gen(code: MutableState<String>, args: List<generationArg>, amount: MutableState<Int>): List<TestCase> {
        return generateAmount(generationArgs = args, amount = amount.value, code = code)
    }

    fun printAll(cases: List<TestCase>) {
        cases.forEach { testCase ->
            testCase.inputs.forEach {
                if (testCase.inputs.indexOf(it) < testCase.inputs.count()-1) {
                    print("$it, ")
                }
                else print(it)
            }
            println(" -> "+testCase.output+" ")
        }
    }

    fun test(code: MutableState<String>, args: List<generationArg>) {
        sm.genScript(sm.putScript(code.value))
        sm.parseInputs(generateRandomArgSet(args).inputs)
        sm.runScript()
        println(sm.readOutput())
    }
}