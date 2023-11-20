import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
/* Do Not Modify */
/* ------------------------------------------------------ */



var inputs: List<Any> = listOf("r",2)
/* Inputs will be parsed here in order */
/* Example: input 1 is a string: use * inputs[0] * to reference it */
/* Write your Evaluation code here */
/* use Kotlin code */
/* Output needs to be in a String Variable inside the printOutput function at the end of the document */
putOutput("Output")




/* ------------------------------------------------------ */
/* Do Not Modify */
fun putOutput(output: String) {
    var outString=""
    File("runtime/execution.kts").readLines().toMutableList().forEach{outString+="$it\n"}
    File("runtime/execution.kts").writeText("$outString$output\n")
}


