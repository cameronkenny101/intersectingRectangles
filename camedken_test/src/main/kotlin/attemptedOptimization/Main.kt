package attemptedOptimization

import java.io.File

data class Point(val x: Int, val y: Int)

data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {
	val topLeft = Point(x, y)
	val bottomRight = Point(x + width, y + height)
}

fun main(args: Array<String>) {
	val rectangles = readRectanglesFromFile("/Volumes/workplace/camedken_test/src/main/resources/rectangles.txt")
	val maximalIntersections = findMaximalIntersectingSubsets(rectangles)

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
 * Finds maximal intersecting subsets of rectangles where every rectangle in the set intersects each other.
 *
 * @param rectangles List of rectangles to process.
 * @return List of maximal intersecting subsets, where each subset contains indices of rectangles that intersect with each other.
 */
fun findMaximalIntersectingSubsets(rectangles: List<Rectangle>): List<Set<Int>> {
	// Number of rectangles
	val n = rectangles.size

	// Matrix to store intersection information between rectangles
	val intersections = Array(n) { IntArray(n) }

	// Fill the intersection matrix
	for (i in 0 until n) {
		for (j in i + 1 until n) {
			if (isRectangleIntersecting(rectangles[i], rectangles[j])) {
				intersections[i][j] = 1
				intersections[j][i] = 1
			}
		}
	}

	// List to store maximal intersecting subsets
	val maximalGroups = mutableListOf<Set<Int>>()

	// Function to recursively find maximal intersecting subsets
	fun findMaximalIntersectingSubset(currentGroup: Set<Int>, remainingRectangles: Set<Int>) {
		val intersectsAll = currentGroup.all { rect1 ->
			currentGroup.all { rect2 -> rect1 == rect2 || intersections[rect1][rect2] == 1 }
		}
		if (intersectsAll) {
			maximalGroups.add(currentGroup.sorted().toSet())
			for (nextRectangle in remainingRectangles) {
				findMaximalIntersectingSubset(currentGroup + nextRectangle, remainingRectangles - nextRectangle)
			}
		}
	}

	// Iterate over all rectangles to find maximal intersecting subsets
	for (i in 0 until n) {
		val initialGroup = setOf(i)
		val remainingRectangles = (0 until n).toSet() - i
		findMaximalIntersectingSubset(initialGroup, remainingRectangles)
	}

	val allGroups = maximalGroups.distinct() // Remove duplicates

	return allGroups.filter { outerSet ->
		allGroups.none { innerSet -> innerSet != outerSet && innerSet.containsAll(outerSet) }
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

