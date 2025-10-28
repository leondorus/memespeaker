@file:Suppress("unused")

package me.leondorus.memespeaker.checkers

import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.React
import me.leondorus.memespeaker.promote.MessageChecker

class NonEmptyChecker : MessageChecker {
    override fun checkMessage(
        message: Message,
        reacts: List<React>
    ): Boolean {
        return !reacts.isEmpty()
    }
}