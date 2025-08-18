package me.leondorus.promote

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import me.leondorus.data.TOPIC
import me.leondorus.repos.MessageRepo
import me.leondorus.repos.ReactRepo

private val topicMap = mapOf(
    TOPIC.SPAM to TOPIC.SOK,
    TOPIC.SOK to TOPIC.ARCH,
)

class Promoter(
    private val reactRepo: ReactRepo,
    private val messageRepo: MessageRepo,
    private val checker: MessageChecker,
    private val forwarder: Forwarder,
) {
    suspend fun startCollectingFlow() {
        reactRepo.getReactsAddUpdates().collect { react ->
            val mid = react.messageId
            val messFlow = messageRepo.getMessage(mid)
            val reactsFlow = reactRepo.getReactsForMessage(mid)

            val (mess, reacts) = combine(messFlow, reactsFlow) { mess, reacts ->
                (mess!! to reacts)
            }.first()
            assert(mid == mess.id)

            val shouldPromote = checker.checkMessage(mess, reacts)
            if (!shouldPromote)
                return@collect

            val wasPromotedByOther = !messageRepo.promoteMessage(mid)
            if (wasPromotedByOther)
                return@collect

            val nextTopic = topicMap[mess.topic]
            if (nextTopic != null) {
                val newMess = forwarder.forwardMessageToTopic(mid, nextTopic)
                messageRepo.addMessage(newMess)
            }
        }
    }
}