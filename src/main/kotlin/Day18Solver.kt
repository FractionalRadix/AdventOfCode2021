import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay18() {
    val inputList = Path("""inputFiles\AoCDay18.txt""").readLines()
    println("The magnitude of the sum of the snailfish homework is ${magnitudeOfSummation(inputList)}") // 4173.
    println("The largest magnitude of the sum of any two snailfish numbers is ${largestMagnitudeOfAnySumOfTwo(inputList)}") // 4706.
}

fun largestMagnitudeOfAnySumOfTwo(input: List<String>): Long {
    val parser = SnailFishNumberParser()
    val listOfTrees1 = input.map { parser.parse(it) }
    val listOfTrees2 = listOfTrees1.map { it.copy() }

    val listOfMagnitudes = mutableListOf<Long>()
    for (tree1 in listOfTrees1) {
        for (tree2 in listOfTrees2) {

            println("$tree1 + $tree2")
            val sumOfPair = add(tree1.copy(), tree2.copy())
            println("$sumOfPair")
            val magnitude = sumOfPair.magnitude()
            println("$magnitude")
            listOfMagnitudes.add(magnitude)
        }
    }

    //println()
    //listOfMagnitudes.forEach { print(" $it") }
    //println()
    //listOfMagnitudes.sortedDescending().forEach { print(" $it") }

    return listOfMagnitudes.maxOf { it }
}

fun magnitudeOfSummation(input: List<String>): Long {
    val parser = SnailFishNumberParser()
    return summation(input.map { parser.parse(it) }).magnitude()
}

fun reduce(tree: Node): Node {

    var moreOperationsAvailable = true
    while (moreOperationsAvailable) {
        // Is there a node that explodes?
        // In other words, what is the leftmost pair at depth 4?
        val explodingNode = findExplodingNode(tree)
        if (explodingNode != null) {
            // If there is a node that explodes, perform the explosion.
            explode(explodingNode)
        } else {
            // If there is no node that explodes, check if there is a number that splits.
            // In other words, find the leftmost leaf node whose number is 10 or greater.
            var splitNode = findSplittableNode(tree)
            if (splitNode != null) {
                // If there is a node that splits, perform the split.
                splitNode.split()
            } else {
                moreOperationsAvailable = false
            }
        }
    }

    return tree
}

abstract class Node {
    var parent: Node? = null    // When set to "protected" then the constructor in NonLeafNode cannot see it...??
    abstract fun write(indentation: Int)
    abstract fun leftNeighbour(): Node?
    abstract fun rightNeighbour(): Node?
    abstract fun rightmostLeaf(): Node
    abstract fun leftmostLeaf(): Node
    abstract fun descendantsAtDepth(depth: Int): List<Node>
    abstract fun leavesInOrder(): List<LeafNode>
    abstract fun root(): Node
    abstract fun magnitude(): Long
    abstract fun copy(): Node
}

class LeafNode(var number: Int): Node() {
    override fun toString() = "$number"
    override fun write(indentation: Int) {
        println()
        print(" ".repeat(indentation))
        print(number)
    }

    override fun rightmostLeaf() = this

    override fun leftmostLeaf() = this

    override fun leftNeighbour(): Node? {
        return root()
            .leavesInOrder()
            .zipWithNext()
            .firstOrNull { it.second == this }
            ?.first
    }

    override fun rightNeighbour(): Node? {
        return root()
            .leavesInOrder()
            .zipWithNext()
            .firstOrNull { it.first == this }
            ?.second
    }

    override fun descendantsAtDepth(depth: Int): List<Node> {
        return if (depth == 0) {
            listOf(this)
        } else {
            listOf()
        }
    }

    override fun leavesInOrder() = listOf(this)

    override fun root() = if (parent == null) this else parent!!.root() //TODO!~ Use null coalesching operator

    override fun magnitude() = number.toLong()

    override fun copy() = LeafNode(number)
}

class NonLeafNode(): Node() {
    lateinit var lhs: Node
    lateinit var rhs: Node

    constructor(lhs: Node, rhs: Node) : this() {
        this.lhs = lhs
        lhs.parent = this
        this.rhs = rhs
        rhs.parent = this
    }

    override fun toString() = "[${lhs},${rhs}]"

    override fun write(indentation: Int) {
        println()
        print(" ".repeat(indentation))
        print("[")
        lhs.write(indentation + 2)
        rhs.write(indentation+2)
        println()
        print(" ".repeat(indentation))
        print("]")
    }

    fun isLefthandNode(): Boolean {
        if (parent == null) {
            return false
        }
        return ((parent as NonLeafNode).lhs === this)
    }

    fun isRighthandNode(): Boolean {
        if (parent == null) {
            return false
        }
        return ((parent as NonLeafNode).rhs === this)
    }

    override fun rightmostLeaf() = rhs.rightmostLeaf()

    override fun leftmostLeaf() = lhs.leftmostLeaf()

    override fun leftNeighbour(): Node? {
        TODO()
    }

    override fun rightNeighbour(): Node? {
        TODO()

        /*
        if (parent == null) {
            return null
        } else {
            // Is the parent a righthand node itself?
            if ((parent as NonLeafNode).isRighthandNode()) {
                // If yes, go up a level in the hierarchy, and try from there.
                return parent!!.rightNeighbour()
            } else {
                // If no, go to the grandparent node.
                // If there is no grandparent node, you're done: no right-hand side neighbours.
                // If there IS a grandparent node, find its *leftmost* leaf.
                val grandParent = parent!!.parent
                if (grandParent == null) {
                    return null
                } else {
                    return grandParent.leftmostLeaf()
                }
            }
        }
         */
    }

    override fun descendantsAtDepth(depth: Int): List<Node> {
        if (depth == 0) {
            return listOf(this)
        } else {
            val result = mutableListOf<Node>()
            result.addAll(lhs.descendantsAtDepth(depth - 1))
            result.addAll(rhs.descendantsAtDepth(depth - 1))
            return result
        }
    }

    override fun leavesInOrder(): List<LeafNode> {
        val result = mutableListOf<LeafNode>()
        result.addAll(lhs.leavesInOrder())
        result.addAll(rhs.leavesInOrder())
        return result
    }

    override fun root() = if (parent == null) this else parent!!.root() //TODO!~ Use null coalescing operator.

    override fun magnitude(): Long = 3 * lhs.magnitude() + 2 * rhs.magnitude()

    override fun copy() = NonLeafNode(lhs.copy(), rhs.copy())
}

fun add(lhs: Node, rhs:Node): Node {
    val rawSum = NonLeafNode(lhs, rhs)
    return reduce(rawSum)
}

fun explodeString(input: String): String {
    val tree = SnailFishNumberParser().parse(input)
    // Find the leftmost pair that is nested inside 4 pairs.
    // In other words, the leftmost pair at depth 4.
    val explodingNode = findExplodingNode(tree)
    if (explodingNode != null) {
        explode(explodingNode)
    }
    return tree.toString()
}

fun magnitudeOfString(input: String): Long {
    val tree = SnailFishNumberParser().parse(input)
    return tree.magnitude()
}

private fun findExplodingNode(tree: Node): NonLeafNode? {
    return tree
        .descendantsAtDepth(4)
        .filterIsInstance<NonLeafNode>()
        .firstOrNull()
}

private fun findSplittableNode(tree: Node): LeafNode? {
    return tree
        .leavesInOrder()
        .firstOrNull { it.number >= 10 }
}

private fun explode(explodingNode: NonLeafNode) {
    val lhsChildLeaf = explodingNode.lhs as LeafNode
    val rhsChildLeaf = explodingNode.rhs as LeafNode

    val leftNeighbour = lhsChildLeaf.leftNeighbour()
    if (leftNeighbour != null) {
        if (leftNeighbour is LeafNode) { // ... and it SHOULD be ...
            leftNeighbour.number += lhsChildLeaf.number
        }
    }

    val rightNeighbour = rhsChildLeaf.rightNeighbour()
    if (rightNeighbour != null) {
        if (rightNeighbour is LeafNode) { // ... and it SHOULD be ...
            rightNeighbour.number += rhsChildLeaf.number
        }
    }

    // Replace the exploding node with '0'
    val parentOfExplosion = explodingNode.parent as NonLeafNode?
    if (parentOfExplosion != null) {
        val newNode = LeafNode(0)
        if (explodingNode.isLefthandNode()) {
            parentOfExplosion.lhs = newNode
            newNode.parent = parentOfExplosion
        } else if (explodingNode.isRighthandNode()) {
            parentOfExplosion.rhs = newNode
            newNode.parent = parentOfExplosion
        }
    }
}

fun LeafNode.calculateSplit(): NonLeafNode {
    val lhsDigit = number / 2
    val rhsDigit = lhsDigit + (number % 2)
    return NonLeafNode(LeafNode(lhsDigit), LeafNode(rhsDigit))
}

fun NonLeafNode.replaceLhs(newLhs: Node) {
    lhs = newLhs
    newLhs.parent = this
}

fun NonLeafNode.replaceRhs(newRhs: Node) {
    rhs = newRhs
    newRhs.parent = this
}

fun LeafNode.split() {
    val replacement = calculateSplit()
    if (parent != null) {
        val t = parent as NonLeafNode
        if (t.lhs == this) {
            t.replaceLhs(replacement)
        } else if (t.rhs == this) {
            t.replaceRhs(replacement)
        }
    }
}

fun summation(input: List<Node>): Node {
    //TODO?~ Handle empty lists?
    var sum = input[0]
    val tail = input.drop(1)
    for (node in tail) {
        sum = add(sum, node)
    }
    return sum
}

class SnailFishNumberParser {
    private var sfnIndex = 0

    fun parse(input: String): Node {
        sfnIndex = 0
        return doParse(input)
    }

    private fun doParse(input: String): Node {
        if (input[sfnIndex] == '[') {
            sfnIndex++ // Skip the "["
            val lhs = doParse(input)
            sfnIndex += 1  // skip the ","
            val rhs = doParse(input)
            sfnIndex += 1  // skip the "]"
            return NonLeafNode(lhs, rhs)
        } else { // input is a single digit
            val number = "${input[sfnIndex]}".toInt()
            sfnIndex += 1
            return LeafNode(number)
        }
    }
}