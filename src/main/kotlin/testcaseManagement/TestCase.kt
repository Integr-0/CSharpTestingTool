package testcaseManagement

import WindowView.sm

class TestCase(var inputs: List<String>) {
    var output: String = ""

    fun generateOutput() {
        sm.parseInputs(inputs)
        sm.runScript()
        output = sm.readOutput()
    }
}