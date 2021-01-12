package com.affirm.baller.utils

import android.util.Log

class Console() : ConsoleInterface {
    override fun log(s: String) {
        Log.d("baller", s);
    }
}
