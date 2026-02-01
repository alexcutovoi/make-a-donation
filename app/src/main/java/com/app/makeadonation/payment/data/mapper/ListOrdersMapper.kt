package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.ListOrdersResponse
import com.app.makeadonation.payment.domain.entity.ListOrders

class ListOrdersMapper : BaseMapper<ListOrdersResponse, ListOrders> {
    override fun generate(input: ListOrdersResponse): ListOrders {
        return ListOrders(
            currentPage = input.currentPage,
            orders = input.orders.map {
                SuccessResponseMapper().generate(it)
            },
            totalOrdersNum = input.totalOrdersNum,
            totalPagesNum = input.totalPagesNum
        )
    }
}