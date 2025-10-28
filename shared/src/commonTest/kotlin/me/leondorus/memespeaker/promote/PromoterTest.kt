package me.leondorus.memespeaker.promote

import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.leondorus.memespeaker.data.*
import me.leondorus.memespeaker.repos.MessageRepo
import me.leondorus.memespeaker.repos.ReactRepo
import kotlin.test.Test

class PromoterTest {
    class TrueChecker : MessageChecker {
        override fun checkMessage(message: Message, reacts: List<React>) = true
    }

    class FalseChecker : MessageChecker {
        override fun checkMessage(message: Message, reacts: List<React>): Boolean = false
    }

    @Test
    fun `Promotes messages to correct topics when checker says true`() = runTest {
        val checker = TrueChecker()
        val forwarder = mockk<Forwarder>()

        val reacts = listOf(
            React("a", 5L.mid, 0L.uid),
            React("a", 6L.mid, 0L.uid),
            React("a", 20L.mid, 0L.uid),
            React("a", 127L.mid, 0L.uid),
        )
        val messages = listOf(
            Message(5L.mid, TOPIC.SPAM, false),
            Message(6L.mid, TOPIC.SOK, false),
            Message(20L.mid, TOPIC.ARCH, false),
            Message(127L.mid, TOPIC.GENERAL, false),
        )
        val newMessages = listOf(
            Message(1005L.mid, TOPIC.SOK, false),
            Message(1006L.mid, TOPIC.ARCH, false)
        )

        val reactRepo = mockk<ReactRepo>()
        for (i in 0..<reacts.size) {
            coEvery { reactRepo.getReactsForMessage(reacts[i].messageId) } returns flow { emit(listOf(reacts[i])) }
        }
        coEvery { reactRepo.getReactsAddUpdates() } returns flow { for (r in reacts) emit(r) }

        val messRepo = mockk<MessageRepo>()
        for (i in 0..<messages.size) {
            coEvery { messRepo.getMessage(messages[i].id) } returns flow { emit(messages[i]) }
            coEvery { messRepo.promoteMessage(messages[i].id) } returns true
        }
        coEvery { messRepo.addMessage(newMessages[0]) } returns Unit
        coEvery { messRepo.addMessage(newMessages[1]) } returns Unit
        coEvery { messRepo.getAllMessages() } returns flow { emit(messages) }

        coEvery { forwarder.forwardMessageToTopic(5L.mid, TOPIC.SOK) } returns newMessages[0]
        coEvery { forwarder.forwardMessageToTopic(6L.mid, TOPIC.ARCH) } returns newMessages[1]


        val promoter = Promoter(reactRepo, messRepo, checker, forwarder)
        promoter.startCollectingFlow()

        coVerify {
            forwarder.forwardMessageToTopic(5L.mid, TOPIC.SOK)
            forwarder.forwardMessageToTopic(6L.mid, TOPIC.ARCH)
        }
        coVerify {
            messRepo.promoteMessage(5L.mid)
            messRepo.promoteMessage(6L.mid)
            messRepo.promoteMessage(20L.mid)
            messRepo.promoteMessage(127L.mid)
        }
        coVerify {
            messRepo.addMessage(newMessages[0])
            messRepo.addMessage(newMessages[1])
        }
    }

    @Test
    fun `Does nothing if checker says no`() = runTest {
        val checker = FalseChecker()
        val forwarder = mockk<Forwarder>()

        val reacts = listOf(
            React("a", 5L.mid, 0L.uid),
            React("a", 6L.mid, 0L.uid),
            React("a", 20L.mid, 0L.uid),
            React("a", 127L.mid, 0L.uid),
        )
        val messages = listOf(
            Message(5L.mid, TOPIC.SPAM, false),
            Message(6L.mid, TOPIC.SOK, true),
            Message(20L.mid, TOPIC.ARCH, false),
            Message(127L.mid, TOPIC.GENERAL, false),
        )

        val reactRepo = mockk<ReactRepo>()
        for (i in 0..<reacts.size) {
            coEvery { reactRepo.getReactsForMessage(reacts[i].messageId) } returns flow { emit(listOf(reacts[i])) }
        }
        coEvery { reactRepo.getReactsAddUpdates() } returns flow { for (r in reacts) emit(r) }

        val messRepo = mockk<MessageRepo>()
        for (i in 0..<messages.size) {
            coEvery { messRepo.getMessage(messages[i].id) } returns flow { emit(messages[i]) }
            coEvery { messRepo.promoteMessage(messages[i].id) } returns true
        }
        coEvery { messRepo.getAllMessages() } returns flow { emit(messages) }

        val promoter = Promoter(reactRepo, messRepo, checker, forwarder)
        promoter.startCollectingFlow()

        coVerify {
            forwarder wasNot Called
        }
        coVerify(exactly = 0) {
            messRepo.promoteMessage(any())
        }
        coVerify(exactly = 0) {
            messRepo.addMessage(any())
        }
    }

    @Test
    fun `Does not promote already promoted`() = runTest {
        val checker = TrueChecker()
        val forwarder = mockk<Forwarder>(relaxed = true)

        val react = React("a", 5L.mid, 0L.uid)
        val reactRepo = mockk<ReactRepo>()
        coEvery { reactRepo.getReactsForMessage(react.messageId) } returns flowOf(listOf(react))
        coEvery { reactRepo.getReactsAddUpdates() } returns flowOf(react)

        val message = Message(5L.mid, TOPIC.SPAM, false)
        val messRepo = mockk<MessageRepo>()
        coEvery { messRepo.getMessage(message.id) } returns flowOf(message)
        coEvery { messRepo.promoteMessage(message.id) } returns false
        coEvery { messRepo.getAllMessages() } returns flowOf(listOf(message))

        val promoter = Promoter(reactRepo, messRepo, checker, forwarder)
        promoter.startCollectingFlow()

        coVerify {
            messRepo.promoteMessage(message.id)
        }
        coVerify {
            forwarder wasNot Called
        }
    }
}