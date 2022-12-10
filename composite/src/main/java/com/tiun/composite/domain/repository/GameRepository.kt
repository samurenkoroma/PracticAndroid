package com.tiun.composite.domain.repository

import com.tiun.composite.domain.entity.GameSettings
import com.tiun.composite.domain.entity.Level
import com.tiun.composite.domain.entity.Question

interface GameRepository {
    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question
    fun getGameSettings(level: Level): GameSettings
}