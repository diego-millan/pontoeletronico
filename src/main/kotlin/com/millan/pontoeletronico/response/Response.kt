package com.millan.pontoeletronico.response

data class Response<T> (
    val errors:ArrayList<String> = arrayListOf(),
    var data: T? = null
)