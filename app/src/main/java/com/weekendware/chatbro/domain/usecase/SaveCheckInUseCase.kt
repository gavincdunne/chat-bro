
package com.weekendware.chatbro.domain.usecase

import com.weekendware.chatbro.domain.model.CheckIn
import com.weekendware.chatbro.data.repository.CheckInRepository

class SaveCheckInUseCase(private val repository: CheckInRepository) {
    suspend operator fun invoke(mood: String, journal: String) {
        val checkIn = CheckIn(mood, journal, System.currentTimeMillis())
        repository.save(checkIn)
    }
}
