package me.leondorus.memespeaker.data

enum class TOPIC(val id: Long?) {
    GENERAL(null),
    SPAM(6),
    SOK(4),
    ARCH(3),
}

private val INT_TO_TOPIC = mapOf(
    null to TOPIC.GENERAL,
    6L to TOPIC.SPAM,
    4L to TOPIC.SOK,
    3L to TOPIC.ARCH,
)

val (Long?).tpc: TOPIC?
    get() = INT_TO_TOPIC[this]