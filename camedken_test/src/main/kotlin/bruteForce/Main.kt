package bruteForce

import java.io.File

data class Point(val x: Int, val y: Int)

data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {
	val topLeft = Point(x, y)
	val bottomRight = Point(x + width, y + height)
}

fun main(args: Array<String>) {
	val rectangles = readRectanglesFromFile("/Volumes/workplace/camedken_test/src/main/resources/rectangles.txt")
	val allIntersections = findMaximalIntersections(rectangles)

	val maximalIntersections = allIntersections.filter { outerSet ->
		allIntersections.none { innerSet -> innerSet != outerSet && innerSet.containsAll(outerSet) }
	}

	maximalIntersections.forEach { println(it.map { index -> index + 1 }) }
}

/**
 * Checks whether two rectangles intersect.
 *
 * @param r1 The first rectangle.
 * @param r2 The second rectangle.
 * @return true if the rectangles intersect, false otherwise.
 */
fun isRectangleIntersecting(r1: Rectangle, r2: Rectangle): Boolean {
	return r1.topLeft.x < r2.bottomRight.x &&
		r2.topLeft.x < r1.bottomRight.x &&
		r1.topLeft.y < r2.bottomRight.y &&
		r2.topLeft.y < r1.bottomRight.y
}

/**
 * Checks if all rectangles in the given list intersect with each other.
 *
 * @param rectangles List of rectangles to check.
 * @return true if all rectangles intersect with each other, false otherwise.
 */
fun allIntersect(rectangles: List<Rectangle>): Boolean {
	for (i in rectangles.indices) {
		for (j in i + 1 until rectangles.size) {
			if (!isRectangleIntersecting(rectangles[i], rectangles[j])) {
				return false
			}
		}
	}
	return true
}

/**
 * Finds maximal intersecting subsets of rectangles where every rectangle in the set intersects each other.
 *
 * @param rectangles List of rectangles to process.
 * @param subset Optional parameter representing the current subset being processed.
 * @param index Optional parameter representing the index of the rectangle being considered.
 * @return List of maximal intersecting subsets, where each subset contains indices of rectangles that intersect with each other.
 */
fun findMaximalIntersections(rectangles: List<Rectangle>, subset: List<Int> = emptyList(), index: Int = 0): Set<Set<Int>> {
	return if (index == rectangles.size) {
		if (allIntersect(subset.map { rectangles[it] })) setOf(subset.toSet()) else emptySet()
	} else {
		findMaximalIntersections(rectangles, subset, index + 1) +
			findMaximalIntersections(rectangles, subset + index, index + 1)
	}
}

/**
 * Reads rectangles from a file and returns a list of rectangles.
 *
 * @param filename The path to the file containing rectangle data.
 * @return A list of rectangles read from the file.
 * @throws IllegalArgumentException If the input data format is incorrect.
 */
fun readRectanglesFromFile(filename: String): List<Rectangle> {
	return File(filename).readLines().map { line ->
		val tokens = line.split(" ")
		require(tokens.size == 4) { "Need 4 arguments to create a rectangle" }
		val (x, y, width, height) = tokens
		Rectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt())
	}
}
