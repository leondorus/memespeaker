import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import me.leondorus.tgbot.ktortg.KtorBot
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class KtorBotTest {
    @Test
    fun `Send message`() = runTest {
        val bot = KtorBot("7965726034:AAE9mQKm-ic_quvNv_h2rO4ePVIUBDT3lxs", this)
        bot.forwardMessage(200082971L, 200082971L, 16, null)
        assert(true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get updates`() = runBlocking {
        val bot = KtorBot("7965726034:AAE9mQKm-ic_quvNv_h2rO4ePVIUBDT3lxs", this)
        bot.getUpdates().collect { update ->
            println(update)
        }
    }
}