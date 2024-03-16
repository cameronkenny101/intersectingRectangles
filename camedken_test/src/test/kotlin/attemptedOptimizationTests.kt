import attemptedOptimization.Rectangle
import attemptedOptimization.findMaximalIntersectingSubsets
import attemptedOptimization.isRectangleIntersecting
import attemptedOptimization.readRectanglesFromFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class AttemptedOptimizationTests {

	@Test
	fun testIsRectangleIntersecting() {
		val rectangle1 = Rectangle(0, 0, 2, 2)
		val rectangle2 = Rectangle(1, 1, 2, 2)
		val rectangle3 = Rectangle(3, 3, 2, 2)

		// Test intersecting rectangles
		assertEquals(true, isRectangleIntersecting(rectangle1, rectangle2))
		assertEquals(true, isRectangleIntersecting(rectangle2, rectangle1))

		// Test non-intersecting rectangles
		assertEquals(false, isRectangleIntersecting(rectangle1, rectangle3))
		assertEquals(false, isRectangleIntersecting(rectangle3, rectangle1))
	}

	@Test
	fun testFindMaximalIntersectingSubsets() {
		val rectangles = listOf(
			Rectangle(0, 0, 2, 2),
			Rectangle(1, 1, 2, 2),
			Rectangle(4, 4, 2, 2)
		)

		val expected = listOf(
			setOf(0, 1),
			setOf(2)
		)

		assertEquals(expected, findMaximalIntersectingSubsets(rectangles))
	}

	@Test
	fun testReadRectanglesFromFile() {
		val filename = "test_rectangles.txt"
		val content = """
            0 0 2 2
            1 1 2 2
            4 4 2 2
        """.trimIndent()

		File(filename).writeText(content)

		val expected = listOf(
			Rectangle(0, 0, 2, 2),
			Rectangle(1, 1, 2, 2),
			Rectangle(4, 4, 2, 2)
		)

		assertEquals(expected, readRectanglesFromFile(filename))

		// Cleanup
		File(filename).delete()
	}
}
