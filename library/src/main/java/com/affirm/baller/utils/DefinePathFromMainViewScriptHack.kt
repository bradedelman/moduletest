package com.affirm.baller.utils

class DefinePathFromMainViewScriptHack {

    companion object {
        fun getPath(script: String):String
        {
            var result = "_path_not_found_";

            // find last define("
            var token = "define(\"";
            var last = script.lastIndexOf(token);
            if (last != -1) {
                var script2 = script.substring(last+token.length);
                var next = script2.indexOf("\"");
                if (next != -1) {
                    result = script2.substring(0, next);
                }
            }

            return result;
        }
    }
}