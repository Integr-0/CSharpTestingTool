import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
/* Do Not Modify */
/* ------------------------------------------------------ */



var inputs: List<Any> = listOf("r",1)
val BASE_PRICE: Double = 50.0
val HOURLY_PRICE: Double = 10.0

/* Tire change prices */
val TIRE_SUMMER_TO_WINTER_MATERIAL: Double = 30.0
val TIRE_SUMMER_TO_WINTER_HOURS: Double = 1.0

val TIRE_WINTER_TO_SUMMER_MATERIAL: Double = 20.0
val TIRE_WINTER_TO_SUMMER_HOURS: Double = 0.5

val TIRE_EXTERNAL_MATERIAL: Double = 10.0
val TIRE_EXTERNAL_HOURS: Double = 0.5

/* Oil change prices */
val OIL_SMALL_MATERIAL: Double = 40.0
val OIL_SMALL_HOURS: Double = 0.5

val OIL_SUV_MATERIAL: Double = 60.0
val OIL_SUV_HOURS: Double = 1.0

val OIL_MOTORBIKE_MATERIAL: Double = 30.0
val OIL_MOTORBIKE_HOURS: Double = 1.5

if (inputs[0] as String == "r") {
	when(inputs[1] as Int) {
        1 -> {
            printPrices(TIRE_SUMMER_TO_WINTER_MATERIAL, TIRE_SUMMER_TO_WINTER_HOURS)
        }
        2 -> {
            printPrices(TIRE_WINTER_TO_SUMMER_MATERIAL, TIRE_WINTER_TO_SUMMER_HOURS)
        }
        3 -> {
            printPrices(TIRE_EXTERNAL_MATERIAL, TIRE_EXTERNAL_HOURS)
        }
    }
} else {
	when(inputs[1] as Int) {
        1 -> {
            printPrices(OIL_SMALL_MATERIAL, OIL_SMALL_HOURS)
        }
        2 -> {
            printPrices(OIL_SUV_MATERIAL, OIL_SUV_HOURS)
        }
        3 -> {
            printPrices(OIL_MOTORBIKE_MATERIAL, OIL_MOTORBIKE_HOURS)
        }
    }
}

fun printPrices(materialCost: Double, hours: Double) {
    putOutput("Price: ${BASE_PRICE+materialCost+hours*HOURLY_PRICE}, Donation: ${materialCost/10}");
}




/* ------------------------------------------------------ */
/* Do Not Modify */
fun putOutput(output: String) {
    var outString=""
    File("runtime/execution.kts").readLines().toMutableList().forEach{outString+="$it\n"}
    File("runtime/execution.kts").writeText("$outString$output\n")
}





































































































