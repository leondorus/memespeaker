@file:Suppress("unused")

package me.leondorus.checkers

import me.leondorus.data.Message
import me.leondorus.data.React
import me.leondorus.promote.MessageChecker

class NonEmptyChecker : MessageChecker {
    override fun checkMessage(
        message: Message,
        reacts: List<React>
    ): Boolean {
        return !reacts.isEmpty()
    }
}