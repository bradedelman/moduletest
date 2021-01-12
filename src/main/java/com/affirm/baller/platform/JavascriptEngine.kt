package com.affirm.baller.platform

import com.squareup.duktape.Duktape

class JavascriptEngine {

    var _duktape: Duktape = Duktape.create();

    fun<T> set(name: String, clazz: Class<T>, native: T)
    {
        _duktape.set(name, clazz, native);
    }

    fun evaluate(s: String):Any
    {
        var result = _duktape.evaluate(s);
        if (result == null) {
            return "";
        } else {
            return result }

    }

}