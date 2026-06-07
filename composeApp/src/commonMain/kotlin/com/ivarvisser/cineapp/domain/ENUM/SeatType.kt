package com.ivarvisser.cineapp.domain.ENUM

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SeatTypeSerializer::class)

enum class SeatType {
    Normal,
    Wheelchair,
}

object SeatTypeSerializer : KSerializer<SeatType> {
    override val descriptor =
        PrimitiveSerialDescriptor("SeatType", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): SeatType {
        return when (decoder.decodeInt()) {
            0 -> SeatType.Normal
            1 -> SeatType.Wheelchair
            else -> throw SerializationException("Unknown SeatType")
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: SeatType
    ) {
        encoder.encodeInt(
            when (value) {
                SeatType.Normal -> 0
                SeatType.Wheelchair -> 1
            }
        )
    }
}