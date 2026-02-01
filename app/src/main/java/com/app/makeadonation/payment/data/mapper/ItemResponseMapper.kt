package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.ItemResponse
import com.app.makeadonation.payment.domain.entity.Item

class ItemResponseMapper : BaseMapper<ItemResponse, Item> {
    override fun generate(input: ItemResponse): Item {
        return Item(
            description = input.description,
            details = input.details,
            id = input.id,
            name = input.name,
            quantity = input.quantity,
            reference = input.reference,
            sku = input.sku,
            unitOfMeasure = input.unitOfMeasure,
            unitPrice = input.unitPrice
        )
    }
}