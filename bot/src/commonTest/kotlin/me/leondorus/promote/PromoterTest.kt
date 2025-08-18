package me.leondorus.promote

import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.leondorus.data.*
import me.leondorus.repos.MessageRepo
import me.leondorus.repos.ReactRepo
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
        val forwarder = mockk<Forwarder>(relaxed = true)

        val reacts = listOf(
            React("a", 5.mid, 0.uid),
            React("a", 6.mid, 0.uid),
            React("a", 20.mid, 0.uid),
            React("a", 127.mid, 0.uid),
        )
        val messages = listOf(
            Message(5.mid, TOPIC.SPAM, false),
            Message(6.mid, TOPIC.SOK, false),
            Message(20.mid, TOPIC.ARCH, false),
            Message(127.mid, TOPIC.GENERAL, false),
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
            forwarder.forwardMessageToTopic(5.mid, TOPIC.SOK)
            forwarder.forwardMessageToTopic(6.mid, TOPIC.ARCH)
        }
        coVerify {
            messRepo.promoteMessage(5.mid)
            messRepo.promoteMessage(6.mid)
            messRepo.promoteMessage(20.mid)
            messRepo.promoteMessage(127.mid)
        }
    }

    @Test
    fun `Does nothing if checker says no`() = runTest {
        val checker = FalseChecker()
        val forwarder = mockk<Forwarder>(relaxed = true)

        val reacts = listOf(
            React("a", 5.mid, 0.uid),
            React("a", 6.mid, 0.uid),
            React("a", 20.mid, 0.uid),
            React("a", 127.mid, 0.uid),
        )
        val messages = listOf(
            Message(5.mid, TOPIC.SPAM, false),
            Message(6.mid, TOPIC.SOK, true),
            Message(20.mid, TOPIC.ARCH, false),
            Message(127.mid, TOPIC.GENERAL, false),
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
    }

    @Test
    fun `Does not promote already promoted`() = runTest {
        val checker = TrueChecker()
        val forwarder = mockk<Forwarder>(relaxed = true)

        val react = React("a", 5.mid, 0.uid)
        val reactRepo = mockk<ReactRepo>()
        coEvery { reactRepo.getReactsForMessage(react.messageId) } returns flowOf(listOf(react))
        coEvery { reactRepo.getReactsAddUpdates() } returns flowOf(react)

        val message = Message(5.mid, TOPIC.SPAM, false)
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