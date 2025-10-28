package me.leondorus.savers

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import me.leondorus.data.Message
import me.leondorus.data.TOPIC
import me.leondorus.data.mid
import me.leondorus.repos.MessageRepo
import kotlin.test.Test

class MessageSaverTest {
    @Test
    fun `Saves new messages`() = runTest {
        val messages = listOf(
            Message(5L.mid, TOPIC.SPAM, false),
            Message(6L.mid, TOPIC.SOK, false),
            Message(20L.mid, TOPIC.ARCH, false),
            Message(127L.mid, TOPIC.GENERAL, false),
        )
        val messageUpdateRepo = mockk<MessageUpdateRepo>()
        every { messageUpdateRepo.getMessageUpdates() } returns flow { for (m in messages) emit(m) }

        val messageRepo = mockk<MessageRepo>()
        for (m in messages) {
            coEvery { messageRepo.addMessage(m) } returns Unit
        }

        val messageSaver = MessageSaver(messageUpdateRepo, messageRepo)
        messageSaver.startSaving()

        coVerify {
            for (m in messages)
                messageRepo.addMessage(m)
        }
    }
}