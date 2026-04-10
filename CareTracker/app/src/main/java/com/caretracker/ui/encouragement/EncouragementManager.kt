package com.caretracker.ui.encouragement

object EncouragementManager {
    private val messages = listOf(
        "You are doing meaningful work every day.",
        "Take one quiet minute for yourself today.",
        "Your care matters, and so does your health.",
        "Small steps count for caregivers too.",
        "Drink some water and take a deep breath.",
        "Caring for yourself helps you care for others.",
        "Rest is part of responsible caregiving.",
        "You do not have to do everything perfectly.",
        "A gentle reminder: your needs matter too.",
        "One healthy choice today is enough to start."
    )

    fun getRandom(): String = messages.random()
}
