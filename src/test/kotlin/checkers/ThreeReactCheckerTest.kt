package checkers

import me.leondorus.checkers.ThreeReactChecker
import me.leondorus.data.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ThreeReactCheckerTest {
    private val checker = ThreeReactChecker()
    private val emojis = object {
        val trophy = "🏆"
        val fire = "🔥"
        val heart = "❤"
        val explosion = "🤯"
    }

    @Test
    fun `Test number of stupid reacts`() {
        val ans = listOf(false, false, false, true, true, true)
        val message = Message(3.mid, TOPIC.SPAM, false)
        val react1 = emojis.heart
        val react2 = emojis.explosion

        for (i in 0..<ans.size) {
            val reacts1 = List(i + 1) { React(react1, 3.mid, it.uid) }
            val reacts2 = List(i + 1) { React(react2, 3.mid, it.uid) }
            assertEquals(ans[i], checker.checkMessage(message, reacts1), message = "For $reacts1")
            assertEquals(ans[i], checker.checkMessage(message, reacts2), message = "For $reacts2")
        }
    }

    @Test
    fun `Test number of fire reacts`() {
        val ans = listOf(false, false, true, true, true, true)
        val message = Message(3.mid, TOPIC.SPAM, false)
        val react = emojis.fire

        for (i in 0..<ans.size) {
            val reacts = List(i + 1) { React(react, 3.mid, it.uid) }
            assertEquals(ans[i], checker.checkMessage(message, reacts), message = "For $reacts")
        }
    }

    @Test
    fun `Test number of trophy reacts`() {
        val ans = listOf(false, false, true, true, true, true)
        val message = Message(3.mid, TOPIC.SPAM, false)
        val react = emojis.trophy

        for (i in 0..<ans.size) {
            val reacts = List(i + 1) { React(react, 3.mid, it.uid) }
            assertEquals(ans[i], checker.checkMessage(message, reacts), message = "For $reacts")
        }
    }


    @Test
    fun `Test different reacts`() {
        val anses = mapOf(
            listOf(emojis.trophy, emojis.fire, emojis.heart) to false,
            listOf(emojis.fire,   emojis.fire, emojis.heart) to false,
            listOf(emojis.trophy, emojis.fire, emojis.heart, emojis.explosion) to true,
            listOf(emojis.trophy, emojis.trophy, emojis.heart) to true,
            listOf(emojis.trophy, emojis.trophy, emojis.trophy, emojis.heart) to true,
        )
        val message = Message(3.mid, TOPIC.SPAM, false)

        for ((reacts, ans) in anses) {
            assertEquals(
                ans,
                checker.checkMessage(message, reacts.mapIndexed { i, emoji -> React(emoji, 3.mid, i.uid) }),
                message = "For $reacts"
            )
        }
    }

    @Test
    fun `Does not promote if was promoted`() {
        val reacts = listOf(emojis.trophy, emojis.trophy, emojis.trophy, emojis.heart).mapIndexed { i, emoji -> React(emoji, 3.mid, i.uid) }
        val message = Message(3.mid, TOPIC.SPAM, true)

        assert(!checker.checkMessage(message, reacts))
    }

    //TODO reacts from the same user
}