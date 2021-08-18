/*
package com.github.nthily.poptimer.utils

import kotlin.random.Random

// This file is no longer used

// A simple 3x3x3 Cube Expanded Chart
// 0, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 1, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 2, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 3, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 4, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 5, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 6, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 7, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11
// 8, 1, 2 | 3, 4, 5 | 6, 7, 8 | 9, 10, 11

// 0,         1,               2,                      white(0, 3),  white(0, 4),  white(0, 5),     6,         7,          8,           9,          10,          11
// 1,         1,               2,                      white(1, 3),  white(1, 4),  white(1, 5),     6,         7,          8,           9,          10,          11
// 2,         1,               2,                      white(2, 3),  white(2, 4),  white(2, 5),     6,         7,          8,           9,          10,          11
// orange(3, 0), orange(3, 1), orange(3, 2),           green(3, 3),  green(3, 4),  green(3, 5),     red(3, 6), red(3, 7), red(3, 8),    blue(3, 9), blue(3, 10), blue(3, 11)
// orange(4, 0), orange(4, 1), orange(4, 2),           green(4, 3),  green(4, 4),  green(4, 5),     red(4, 6), red(4, 7), red(4, 8),    blue(4, 9), blue(4, 10), blue(4, 11)
// orange(5, 0), orange(5, 1), orange(5, 2),           green(5, 3),  green(5, 4),  green(5, 5),     red(5, 6), red(5, 7), red(5, 8),    blue(5, 9), blue(5, 10), blue(5, 11)
// 6,         1,               2,                      yellow(6, 3), yellow(6, 4), yellow(6, 5),    6,         7,         8,            9,          10,          11
// 7,         1,               2,                      yellow(7, 3), yellow(7, 4), yellow(7, 5),    6,         7,         8,            9,          10,          11
// 8,         1,               2,                      yellow(8, 3), yellow(8, 4), yellow(8, 5),    6,         7,         8,            9,          10,          11


// U' -> (3, 0) -> (3, 3) | (3, 3) -> (3, 6) | (3, 6) -> (3, 9) | (3, 9) -> (3, 0) Formula: ((x/3) + 1) % 4 ) * 3
// U2 -> (3, 0) -> (3, 6) | (3, 3) -> (3, 9) | (3, 6) -> (3, 0) | (3, 9) -> (3, 3) Formula: ((x/3) + 2) % 4) * 3
// U -> (3, 0) -> (3, 9) | (3, 3) -> (3, 0) | (3, 6) -> (3, 3) | (3, 9) -> (3, 6) Formula: ((x/3) + 3) % 4) * 3

class Cube(private val degree: Int = 3) {

    var cube: MutableList<Array<String>> = mutableListOf()

    init {
        cube = MutableList(degree * 3) { Array(degree * 4) { "" } } // init a cube
        generateNbyNbyNCube()
    }

    fun generate3x3x3CubeScramble(): String {
        val suffix = arrayOf("\'", "2")
        val layer = arrayOf("F", "R", "L", "B", "U", "D")
        var temp: String
        val scramble = mutableListOf<String>()
        val scrambleLength = Random.nextInt(20, 25)
        scramble.add(getOneStep(layer, suffix))

        for(index in 1 until scrambleLength) {
            do {
                temp = getOneStep(layer, suffix)
            } while (temp[0] == scramble[index - 1][0]) // generate a disruption that is different from the previous step
            scramble.add(temp)
        }
        return scramble
            .toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
    }

    private fun getOneStep(layer: Array<String>, suffix: Array<String>):String {
        return layer.random() + suffix.random() + " "
    }

    private fun generateNbyNbyNCubeChart():MutableList<Array<String>>  {
        var currentColumnLayer = 0 // Calculate the current group, from top to bottom
        var currentRowLayer: Int // Calculate the current group, from left to right


        for(column in 0 until degree * 3) {
            if(column % degree == 0) currentColumnLayer += 1
            currentRowLayer = 0
            for(row in 0 until degree * 4) {
                if(row % degree == 0) currentRowLayer += 1
                when(currentColumnLayer) {
                    1 -> if(currentRowLayer == 2) cube[column][row] = "white"
                    2 -> {
                        when(currentRowLayer) {
                            1 -> cube[column][row] = "orange"
                            2 -> cube[column][row] = "green"
                            3 -> cube[column][row] = "red"
                            4 -> cube[column][row] = "blue"
                        }
                    }
                    3 -> if(currentRowLayer == 2) cube[column][row] = "yellow"
                }
            }
        }
        return cube
    }

    fun rotateU(step: String) {

        val type = step[0]
        val subDegree = if(type == 'U') degree else (degree * 2 - 1)

        fun targetAxisY(str: String, index: Int): Int {
            var result = 0
            when (str) {
                "U'", "D" -> result = ((index + degree) % (degree * 4))
                "U2", "D2" -> result = ((index + (degree * 2)) % (degree * 4))
                "U", "D'" -> result = ((index + (degree * 3)) % (degree * 4))
            }
            return result
        }

        val backUpAllSideColor = mutableListOf<String>()

        for(index in 0 until (degree * 4)) {
            backUpAllSideColor.add(cube[subDegree][index])
        }

        for(index in 0..3) {

            val currentLayer = index * degree

            for(layer in 0 until degree) {
                cube[subDegree][targetAxisY(step, layer + currentLayer)] = backUpAllSideColor[layer + currentLayer]
            }
        }

    }

    fun rotateR() {

    }

    fun rotateL() {

    }

    fun rotateB() {

    }

    fun rotateF() {

    }

    fun rotateD() {

    }
}


 */