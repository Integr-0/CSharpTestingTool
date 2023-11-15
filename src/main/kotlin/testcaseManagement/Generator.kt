package testcaseManagement

import WindowView.sm
import androidx.compose.runtime.MutableState
import kotlin.random.Random


fun generateAmount(generationArgs: List<generationArg>, amount: Int, code: MutableState<String>): List<TestCase> {
    sm.genScript(sm.putScript(code.value))
    val cases: MutableList<TestCase> = mutableListOf()
    for (num in 0..amount) {
        cases += generateRandomArgSet(generationArgs)
    }
    return cases
}

fun generateRandomArgSet(generationArgs: List<generationArg>): TestCase {
    val args: MutableList<String> = mutableListOf()
    for (arg in generationArgs) {
        when (arg) {
            is RandomIntValue -> args += RandomIntValue().value.toString()
            is RandomIntValueFromRange -> args += RandomIntValueFromRange(arg.min, arg.max).value.toString()
            is RandomDoubleValueFromRange -> args += RandomDoubleValueFromRange(arg.min, arg.max).value.toString()
            is StringValueFromSet -> args += StringValueFromSet(arg.strings).value
        }
    }

    var case = TestCase(args)
    case.generateOutput()
    return case
}

open class generationArg

class RandomIntValue : generationArg() {
    val value = Random.nextInt()
}

class RandomIntValueFromRange(var min: Int, var max: Int) : generationArg() {
    val value = Random.nextInt((max + 1) - min) + min
}

class RandomDoubleValueFromRange(var min: Double, var max: Double) : generationArg() {
    val value = Random.nextDouble((max + 1) - min) + min
}

class StringValueFromSet(var strings: List<String>) : generationArg() {
    val value = strings.random()
}