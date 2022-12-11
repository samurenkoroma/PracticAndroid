package com.tiun.composite.data

import com.tiun.composite.domain.entity.GameSettings
import com.tiun.composite.domain.entity.Level
import com.tiun.composite.domain.entity.Question
import com.tiun.composite.domain.repository.GameRepository
import kotlin.random.Random
import kotlin.math.max

object GameRepositoryImpl : GameRepository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)

        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        val to = max(maxSumValue, rightAnswer + countOfOptions)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(10, 3, 50, 20)
            Level.EASY -> GameSettings(10, 10, 70, 60)
            Level.NORMAL -> GameSettings(20, 20, 80, 45)
            Level.HARD -> GameSettings(30, 30, 90, 45)
        }
    }
}