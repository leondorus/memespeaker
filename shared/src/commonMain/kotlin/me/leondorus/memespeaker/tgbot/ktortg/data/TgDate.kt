@file:OptIn(ExperimentalTime::class)
@file:UseSerializers(MyInstantSerializer::class)

package me.leondorus.memespeaker.tgbot.ktortg.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@JvmInline
@Serializable
value class TgDate(
    val date: Instant
)

private object MyInstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("kotlin.time.Instant_as_epoch", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) =
        encoder.encodeString(value.toEpochMilliseconds().toString())

    override fun deserialize(decoder: Decoder): Instant = Instant.fromEpochSeconds(decoder.decodeLong())
}