package com.ivarvisser.cineapp.domain.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = OrderTypesSerializer::class)
enum class OrderTypes(val selector: Int) {
    Reservation(1),
    Online(2),
    Touch(3),
    Website(4),
    Cashier(5),
    Payment(6)
}

object OrderTypesSerializer : KSerializer<OrderTypes> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OrderTypes", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: OrderTypes) {
        encoder.encodeInt(value.selector)
    }

    override fun deserialize(decoder: Decoder): OrderTypes {
        val value = decoder.decodeInt()
        return OrderTypes.entries.first { it.selector == value }
    }
}