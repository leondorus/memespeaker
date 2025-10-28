package me.leondorus.memespeaker.promote

import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.React

interface MessageChecker {
    fun checkMessage(message: Message, reacts: List<React>): Boolean
}