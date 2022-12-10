package com.tiun.composite.domain.usecase

import com.tiun.composite.domain.entity.GameSettings
import com.tiun.composite.domain.entity.Level
import com.tiun.composite.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}