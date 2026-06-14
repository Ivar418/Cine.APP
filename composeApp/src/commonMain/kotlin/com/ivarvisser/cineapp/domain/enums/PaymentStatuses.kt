package com.ivarvisser.cineapp.domain.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PaymentStatusesSerializer::class)
enum class PaymentStatuses(val selector: Int) {
    Pending(1),
    Paid(2),
    Cancelled(3),
    Failed(4),
    Expired(5)
}

object PaymentStatusesSerializer : KSerializer<PaymentStatuses> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PaymentStatuses", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: PaymentStatuses) {
        encoder.encodeInt(value.selector)
    }

    override fun deserialize(decoder: Decoder): PaymentStatuses {
        val value = decoder.decodeInt()
        return PaymentStatuses.entries.first { it.selector == value }
    }
}