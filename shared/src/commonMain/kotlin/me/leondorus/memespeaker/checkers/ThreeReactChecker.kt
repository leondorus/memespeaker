package me.leondorus.memespeaker.checkers

import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.React
import me.leondorus.memespeaker.promote.MessageChecker

private val reactPoints = mapOf(
    "🏆" to 9,
    "🔥" to 8,
)
private const val DEFAULT_POINTS = 6
private fun getPoints(emoji: String) = reactPoints.getOrDefault(emoji, DEFAULT_POINTS)
private const val THRESHOLD = 24

class ThreeReactChecker : MessageChecker {
    override fun checkMessage(
        message: Message,
        reacts: List<React>
    ): Boolean {
        if (message.wasPromoted)
            return false

        val reactSum = reacts.sumOf { (emoji, _, _) -> getPoints(emoji) }
        return reactSum >= THRESHOLD
    }
}