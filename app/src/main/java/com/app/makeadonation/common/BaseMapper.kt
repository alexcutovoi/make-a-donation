package com.app.makeadonation.common

interface BaseMapper<INPUT, OUTPUT> {
    fun generate(input: INPUT): OUTPUT
}