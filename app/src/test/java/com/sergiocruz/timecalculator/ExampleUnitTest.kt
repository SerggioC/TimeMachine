package com.sergiocruz.timecalculator

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class   ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun safeTypecast() {

        val y = emptyArray<Int>()
        val yy: String? = y as? String? ?: "nothing"
        //print(yy)

        val name: Pair<Pair<String, String>, Pair<String, String>> = ("Sergio" to "Cruz") to ("home" to "here")
        //println(name)

        val triplet = Triple("ya", "meu", "tasse")

        val (arraya, cena, cena2, cena3) = arrayOf("ya", "meu", "tasse", "bem")
        //assertEquals(arraya, "ya")

        val lista = mutableListOf<Int>()
        lista.add(101)

        for (i in 0 until lista.size) {
            println(lista[i])
        }

    }
}
