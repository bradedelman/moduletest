package com.affirm.baller.utils

interface NativeInterface {

    fun call6(id: String, method: String, a: Any, b: Any, c: Any, d: Any, e: Any, f: Any):Any?
    fun call5(id: String, method: String, a: Any, b: Any, c: Any, d: Any, e: Any):Any?
    fun call4(id: String, method: String, a: Any, b: Any, c: Any, d: Any):Any?
    fun call3(id: String, method: String, a: Any, b: Any, c: Any):Any?
    fun call2(id: String, method: String, a: Any, b: Any):Any?
    fun call1(id: String, method: String, a: Any):Any?
    fun call0(id: String, method: String):Any?

    fun callAPI2(apiName: String, method: String, a: Any, b: Any):Any?
    fun callAPI1(apiName: String, method: String, a: Any):Any?

}
