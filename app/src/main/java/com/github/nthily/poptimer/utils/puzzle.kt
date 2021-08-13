package com.github.nthily.poptimer.utils

import org.worldcubeassociation.tnoodle.puzzle.ClockPuzzle
import org.worldcubeassociation.tnoodle.puzzle.CubePuzzle
import org.worldcubeassociation.tnoodle.puzzle.FourByFourCubePuzzle
import org.worldcubeassociation.tnoodle.puzzle.MegaminxPuzzle
import org.worldcubeassociation.tnoodle.puzzle.PyraminxPuzzle
import org.worldcubeassociation.tnoodle.puzzle.SkewbPuzzle
import org.worldcubeassociation.tnoodle.puzzle.SquareOnePuzzle
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubePuzzle
import org.worldcubeassociation.tnoodle.puzzle.TwoByTwoCubePuzzle
import org.worldcubeassociation.tnoodle.scrambles.Puzzle

enum class Puzzles(val puzzle: Puzzle){

    TWO(TwoByTwoCubePuzzle()),
    THREE(ThreeByThreeCubePuzzle()),
    FOUR(FourByFourCubePuzzle()),
    FIVE(CubePuzzle(5)),
    SIX(CubePuzzle(6)),
    SEVEN(CubePuzzle(7)),
    PYRA(PyraminxPuzzle()),
    SQ1(SquareOnePuzzle()),
    MEGA(MegaminxPuzzle()),
    CLOCK(ClockPuzzle()),
    SKEWB(SkewbPuzzle())
}