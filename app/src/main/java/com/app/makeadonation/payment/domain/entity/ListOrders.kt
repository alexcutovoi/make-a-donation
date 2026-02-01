package com.app.makeadonation.payment.domain.entity

data class ListOrders (
    val currentPage: Int,
    val orders: List<Success>,
    val totalOrdersNum: Int,
    val totalPagesNum: Int
)
