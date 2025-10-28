package me.leondorus.memespeaker.promote

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import me.leondorus.memespeaker.data.TOPIC
import me.leondorus.memespeaker.repos.MessageRepo
import me.leondorus.memespeaker.repos.ReactRepo

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
                (mess to reacts)
            }.first()
            if (mess == null)
                // Message is probably older than we have at db, ignore
                return@collect
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