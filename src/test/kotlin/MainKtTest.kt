import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class MainKtTest {

    private val pathToTestResources = "src/test/resources/testSources/"

    private fun runTest(filename: String?, expected: String) {
        val result = ByteArrayOutputStream()
        PrintStream(BufferedOutputStream(result)).use { printer ->
            System.setOut(printer)
            if (filename == null) {
                main(arrayOf())
            } else {
                main(arrayOf(pathToTestResources + filename))
            }
        }

        assertEquals(expected, result.toString())
    }

    @Test
    fun testSingleLineWithoutSpaces() {
        val filename = "testSingleLineWithoutSpaces"
        val expected = "root"

        runTest(filename, expected)
    }

    @Test
    fun testSingleLineWithSpaces() {
        val filename = "testSingleLineWithSpaces"
        val expected = "root"

        runTest(filename, expected)
    }

    @Test
    fun testAllPossibleLeftConnections() {
        val filename = "testAllPossibleLeftConnections"
        val expected =
            "firstLevel secondLevel thirdLevel fourthLevel fifthLevel sixthLevel seventhLevel eighthLevel nineLevel tenthLevel"

        runTest(filename, expected)
    }

    @Test
    fun testAllPossibleRightConnections() {
        val filename = "testAllPossibleRightConnections"
        val expected =
            "firstLevel secondLevel thirdLevel fourthLevel fifthLevel sixthLevel seventhLevel eighthLevel nineLevel tenthLevel"

        runTest(filename, expected)
    }

    @Test
    fun testCompleteTree() {
        val filename = "testCompleteTree"
        val expected = "rooot a b c d e f g h i j k l m n"

        runTest(filename, expected)
    }

    @Test
    fun testAllPossibleLeftMultilineConnections() {
        val filename = "testAllPossibleLeftMultilineConnections"
        val expected =
            "firstLevel secondLevel thirdLevel fourthLevel fifthLevel sixthLevel seventhLevel eighthLevel nineLevel tenthLevel"

        runTest(filename, expected)
    }

    @Test
    fun testAllPossibleRightMultilineConnections() {
        val filename = "testAllPossibleRightMultilineConnections"
        val expected =
            "firstLevel secondLevel thirdLevel fourthLevel fifthLevel sixthLevel seventhLevel eighthLevel nineLevel tenthLevel"

        runTest(filename, expected)
    }

    @Test
    fun testMultilineTree() {
        val filename = "testMultilineTree"
        val expected = "root leftFirst leftThird rightSecond rightFourth lf llf rf rrf"

        runTest(filename, expected)
    }

    @Test
    fun testEmptyInput() {
        val filename = "testEmptyInput"
        val expected = ""

        runTest(filename, expected)
    }

    @Test
    fun testFileNotFound() {
        runTest("", "File not found: $pathToTestResources\n")
    }

    @Test
    fun testNoFilename() {
        runTest(null, "First argument should be filename\n")
    }
}