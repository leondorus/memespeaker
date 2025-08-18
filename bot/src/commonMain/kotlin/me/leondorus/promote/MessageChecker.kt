package me.leondorus.promote

import me.leondorus.data.Message
import me.leondorus.data.React

interface MessageChecker {
    fun checkMessage(message: Message, reacts: List<React>): Boolean
}