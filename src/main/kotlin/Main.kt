import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Scanner

private class Node(val value: String, var leftChild: Node? = null, var rightChild: Node? = null)

private const val RIGHT_VERTEX = '\\'
private const val LEFT_VERTEX = '/'

fun main(argc: Array<String>) {
    if (argc.isEmpty()) {
        println("First argument should be filename")
        return
    }

    try {
        val reader = Scanner(BufferedInputStream(FileInputStream(argc[0])))
        reader.use {
            solve(reader)
        }
    } catch (e: FileNotFoundException) {
        println("File not found: ${argc[0]}")
    }
}

private fun solve(reader: Scanner) {
    val root = createGraph(reader)
    if (root != null) {
        printPreOrderDfs(root, true)
    }
}

/**
 * Creates the graph, i.e. finds for each node it's left and right children (if existing)
 *
 * Solution works well for large files, because it does not store all input in the memory, only the current and the previous
 * processing lines.
 *
 * @return root of the graph
 */
private fun createGraph(reader: Scanner): Node? {
    var rootNode: Node? = null

    /*
    if line[index] is:
      a space symbol or not the first letter in the word, then node at this index is null.
      a first letter in the word, then node at this index is a reference to corresponding Node of this word.
      an edge ('\' or '/'), then node at this index is a reference to corresponding Node at the top of this edge.
     */
    val nodesOnPreviousLine = mutableListOf<Node?>()
    val nodesOnCurrentLine = mutableListOf<Node?>()
    var previousLine = ""
    var currentLine: String

    while (reader.hasNextLine()) {
        currentLine = reader.nextLine()

        while (nodesOnPreviousLine.size < currentLine.length) {
            nodesOnPreviousLine.add(null)
            nodesOnCurrentLine.add(null)
        }

        for ((index, char) in currentLine.withIndex()) {
            if (index > 0 && char.isLetter() && currentLine[index - 1].isLetter()) {
                //already processed this word
                continue
            }

            when {
                char.isLetter() -> {
                    val newWordBuilder = StringBuilder()
                    for (newWordIndex in index until currentLine.length) {
                        if (currentLine[newWordIndex].isLetter()) {
                            newWordBuilder.append(currentLine[newWordIndex])
                        } else {
                            break
                        }
                    }
                    val newWord = newWordBuilder.toString()
                    val endIndex = index + newWord.length - 1

                    val newNode = Node(newWord)

                    if (previousLine == "") {
                        //first line in input
                        nodesOnCurrentLine[index] = newNode
                        rootNode = newNode
                    } else {
                        var parentNodeIndex = -1

                        for (i in index - 1..endIndex + 1) {
                            if (i < 0 || i >= previousLine.length) {
                                continue
                            }

                            if (i > index - 1 && previousLine[i] == LEFT_VERTEX) {
                                parentNodeIndex = i
                                nodesOnPreviousLine[i]?.leftChild = newNode
                                break
                            } else if (i < endIndex + 1 && previousLine[i] == RIGHT_VERTEX) {
                                parentNodeIndex = i
                                nodesOnPreviousLine[i]?.rightChild = newNode
                                break
                            }
                        }

                        if (parentNodeIndex != -1) {
                            nodesOnCurrentLine[index] = newNode
                        }
                    }
                }

                char == LEFT_VERTEX || char == RIGHT_VERTEX -> {
                    var indexOfLeftmostParentLetterOnPreviousLine =
                        if (previousLine.safeIsLetter(index)) {
                            index
                        } else if (char == LEFT_VERTEX && previousLine.safeIsLetter(index + 1)) {
                            index + 1
                        } else if (char == RIGHT_VERTEX && previousLine.safeIsLetter(index - 1)) {
                            index - 1
                        } else {
                            -1
                        }

                    if (indexOfLeftmostParentLetterOnPreviousLine >= 0) {
                        while (indexOfLeftmostParentLetterOnPreviousLine > 0 && previousLine[indexOfLeftmostParentLetterOnPreviousLine - 1].isLetter()) {
                            indexOfLeftmostParentLetterOnPreviousLine -= 1
                        }
                    }

                    val parentIndex =
                        when {
                            indexOfLeftmostParentLetterOnPreviousLine >= 0 -> {
                                indexOfLeftmostParentLetterOnPreviousLine
                            }
                            char == LEFT_VERTEX -> {
                                index + 1
                            }
                            else -> {
                                index - 1
                            }
                        }

                    nodesOnCurrentLine[index] = nodesOnPreviousLine[parentIndex]
                }
            }
        }

        previousLine = currentLine
        nodesOnPreviousLine.clear()
        nodesOnPreviousLine.addAll(nodesOnCurrentLine)
    }

    return rootNode
}

private fun printPreOrderDfs(root: Node?, isRoot: Boolean = false) {
    if (root == null) {
        return
    }

    if (!isRoot) {
        print(' ')
    }
    print(root.value)

    printPreOrderDfs(root.leftChild)
    printPreOrderDfs(root.rightChild)
}

private fun String.safeIsLetter(index: Int): Boolean {
    return index >= 0 && index < this.length && this[index].isLetter()
}
