package me.leondorus.data

enum class TOPIC(val id: Int?) {
    GENERAL(null),
    SPAM(6),
    SOK(4),
    ARCH(3),
}

val INT_TO_TOPIC = mapOf(
    null to TOPIC.GENERAL,
    6 to TOPIC.SPAM,
    4 to TOPIC.SOK,
    3 to TOPIC.ARCH,
)