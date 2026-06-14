package com.ivarvisser.cineapp.domain.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PaymentMethodsSerializer::class)
enum class PaymentMethods(val selector: Int, val displayName: String) {
    CreditCard(1, "Creditcard"),
    Pin(2, "PIN"),
    iDEAL(3, "iDEAL"),
    Reservation(4, "Reservering"),
    Unknown(5, "Onbekend"),
    CreditCardOnline(6, "Creditcard (online)"),
    Giftcard(7, "Cadeaubon"),
}

object PaymentMethodsSerializer : KSerializer<PaymentMethods> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PaymentMethods", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: PaymentMethods) {
        encoder.encodeInt(value.selector)
    }

    override fun deserialize(decoder: Decoder): PaymentMethods {
        val value = decoder.decodeInt()
        return PaymentMethods.entries.first { it.selector == value }
    }
}