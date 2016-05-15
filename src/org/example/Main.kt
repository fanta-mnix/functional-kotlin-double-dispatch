package org.example

data class Point(val x: Double, val y: Double) {
    companion object {
        @JvmField val ZERO: Point = Point(0.0, 0.0)
    }
}

interface Shape {
    fun collidesWith(s: Shape): Boolean
}

data class Square(val center: Point, val length: Double) : Shape {

    private val collidesWithDispatch = FunctionTable(
            FunctionEntry.of { shape: Shape -> false },
            FunctionEntry.of { square: Square -> true },
            FunctionEntry.of { s: Circle -> false },
            FunctionEntry.of(Square::collidesWithTriangle.curry(this))
    )

    override fun collidesWith(s: Shape): Boolean = collidesWithDispatch(s)

    private fun collidesWithTriangle(t: Triangle): Boolean = true
}

data class Circle(val center: Point, val radius: Double) : Shape {

    private val collidesWithDispatch = FunctionTable(
            FunctionEntry.of { shape: Shape -> false },
            FunctionEntry.of { square: Square -> false },
            FunctionEntry.of { s: Circle -> true }
    )

    override fun collidesWith(s: Shape): Boolean = collidesWithDispatch(s)
}

data class Triangle(val center: Point, val length: Double): Shape {

    override fun collidesWith(s: Shape): Boolean {
        throw UnsupportedOperationException()
    }
}

fun main(args: Array<String>) {

    val dispatch = FunctionTable(
            FunctionEntry.of { text: CharSequence -> "CharSequence($text)" },
            FunctionEntry.of { text: String -> "String($text)" },
            FunctionEntry.of { text: StringBuilder -> "StringBuilder($text)" }
    )

    println(dispatch("I'm a String"))
    println(dispatch(StringBuilder("I'm a StringBuilder")))
    println(dispatch(StringBuffer("I'm a StringBuffer")))

    val circle = Circle(Point.ZERO, 1.0)
    val square = Square(Point.ZERO, 1.0)
    val triangle  = Triangle(Point.ZERO, 1.0)

    println("Circle collides with Square? ${circle.collidesWith(square)}")
    println("Square collides with Triangle? ${square.collidesWith(triangle)}")

    println("fin~")

}