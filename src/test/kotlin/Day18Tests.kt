import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Day18Tests {

    @Test
    fun testParse() {
        val expected = "[[[[7,3],[7,9]],[8,[6,2]]],[[8,[4,5]],[[6,4],[6,7]]]]"
        val actual = SnailFishNumberParser().parse("[[[[7,3],[7,9]],[8,[6,2]]],[[8,[4,5]],[[6,4],[6,7]]]]")
        //actual.write(2)
        assertEquals(expected, actual.toString())
    }

    @Test
    fun testLeavesInOrder() {
        val tree = SnailFishNumberParser().parse("[[[[7,3],[7,9]],[8,[6,2]]],[[8,[4,5]],[[6,4],[6,7]]]]")
        val expectedNumbers = listOf(7,3,7,9,8,6,2,8,4,5,6,4,6,7)
        val leafNodes = tree.leavesInOrder()
        assertContentEquals(expectedNumbers, leafNodes.map { it -> it.number })
    }

    @Test
    fun testExplode1() {
        assertEquals("[[[[0,9],2],3],4]", explodeString("[[[[[9,8],1],2],3],4]"))
        // The 9 has no regular number to its left, so it is not added to any regular number.
    }

    @Test
    fun testExplode2() {
        assertEquals( "[7,[6,[5,[7,0]]]]", explodeString("[7,[6,[5,[4,[3,2]]]]]"))
        // The 2 has no regular number to its right, and so it is not added to any regular number.
    }

    @Test
    fun testExplode3() {
        assertEquals("[[6,[5,[7,0]]],3]", explodeString("[[6,[5,[4,[3,2]]]],1]"))
    }

    @Test
    fun testExplode4() {
        assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", explodeString("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"))
        // The pair [3,2] is unaffected because the pair [7,3] is further to the left; [3,2] would explode on the next action.
    }

    @Test
    fun testExplode5() {
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", explodeString("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun testSplit1() {
        assertEquals("[5,5]", LeafNode(10).calculateSplit().toString())
    }

    @Test
    fun testSplit2() {
        assertEquals("[5,6]", LeafNode(11).calculateSplit().toString())
    }

    @Test
    fun testSplit3() {
        assertEquals("[6,6]", LeafNode(12).calculateSplit().toString())
    }

    @Test
    fun testReduce() {
        val input = SnailFishNumberParser().parse("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")
        reduce(input)
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", input.toString())
    }

    @Test
    fun testMagnitude1() {
        assertEquals(29, magnitudeOfString("[9,1]"))
        assertEquals(21, magnitudeOfString("[1,9]"))
        assertEquals(129, magnitudeOfString("[[9,1],[1,9]]"))
        assertEquals(143, magnitudeOfString("[[1,2],[[3,4],5]]"))
        assertEquals(1384, magnitudeOfString("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"))
        assertEquals(445, magnitudeOfString("[[[[1,1],[2,2]],[3,3]],[4,4]]"))
        assertEquals(791, magnitudeOfString("[[[[3,0],[5,3]],[4,4]],[5,5]]"))
        assertEquals(1137, magnitudeOfString("[[[[5,0],[7,4]],[5,5]],[6,6]]"))
        assertEquals(3488, magnitudeOfString("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"))
    }

    @Test
    fun testSummation() {
        val parser = SnailFishNumberParser()
        val input = listOf(
            parser.parse("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]"),
            parser.parse("[[[5,[2,8]],4],[5,[[9,9],0]]]"),
            parser.parse("[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]"),
            parser.parse("[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]"),
            parser.parse("[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]"),
            parser.parse("[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]"),
            parser.parse("[[[[5,4],[7,7]],8],[[8,3],8]]"),
            parser.parse("[[9,3],[[9,9],[6,[4,9]]]]"),
            parser.parse("[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]"),
            parser.parse("[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"),
        )

        val actualSum = summation(input)
        val expectedSum = parser.parse("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]")

        //TODO?~ Create an equals function for the Tree...? Right now we're parsing the expected value and returning it to String again...
        assertEquals(expectedSum.toString(), actualSum.toString())
    }

    @Test
    fun testMagnitudeOfSummation() {
        val input = listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
            "[[[5,[2,8]],4],[5,[[9,9],0]]]",
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
            "[[[[5,4],[7,7]],8],[[8,3],8]]",
            "[[9,3],[[9,9],[6,[4,9]]]]",
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
        )
        assertEquals(4140, magnitudeOfSummation(input))
    }

    @Test
    fun testLargestMagnitudeOfAnySumOfTwo() {
        val input = listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
            "[[[5,[2,8]],4],[5,[[9,9],0]]]",
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
            "[[[[5,4],[7,7]],8],[[8,3],8]]",
            "[[9,3],[[9,9],[6,[4,9]]]]",
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
        )

        assertEquals(3993, largestMagnitudeOfAnySumOfTwo(input))
    }
}