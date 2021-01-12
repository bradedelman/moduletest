package com.affirm.baller.service

import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class NativeStore {

    var _data: HashMap<String, JSONObject> = HashMap();

    fun set(key: String, value: String)
    {
        // has to be stringified to cross the boundary, but we store it as JSON, since usually read using getFromJSON
        _data.set(key, JSONObject(value));
    }

    fun get(key: String):String {
        // has to be stringified to cross the boundary
        var o:JSONObject? = _data.get(key);
        if (o != null) {
            return o?.toString(4);
        }
        return "__ERROR__";
    }

    fun setRaw(key: String, value: JSONObject) {
        // way to pass in non-Stringified from native code
        _data.set(key, value);
    }

    fun getArrayCount(key: String, payloadStr: String):Int {

        var o:Any? = getCore(key, payloadStr);
        var aa:JSONArray = o as JSONArray;
        return o.length();
    }

    fun getFromJSON(key: String, payloadStr: String):String {

        var o:Any? = getCore(key, payloadStr);

        if (o != null) {
            try {
                var oo: JSONObject = o as JSONObject;
                return oo.toString(4);
            } catch (e:Exception) {
                // leaf node
                if (o is kotlin.String) {
                    return "\"" + (o as String) + "\"";
                } else if (o is kotlin.Int) {
                    return "" + (o as kotlin.Int);
                } else if (o is kotlin.Boolean) {
                    return "" + (o as kotlin.Boolean);
                } else if (o is kotlin.Double) {
                    return "" + (o as kotlin.Double);
                }
            }
        }
        return "__ERROR__";



    }

    fun getCore(key: String, payloadStr: String):Any? {

        // TODO this is GROSS! make it better
        var payload = JSONObject(payloadStr);
        var path:String = payload["_path"] as String;
        var args:JSONArray = payload["_args"] as JSONArray;

        var o:Any? = _data.get(key);

        var dot:Int = 0;
        while (dot != -1 && o != null) { // keep going if more dots or found nothing
            dot = path.indexOf(".");

            var c = path;
            if (dot != -1) {
                c = path.substring(0, dot);
            }
            var j = c.indexOf("[");
            if (j != -1) {
                // indexing into array
                var k = c.indexOf("]");
                var index = c.substring(j+1, k);
                var c = c.substring(0, j);
                index = index.trim();
                var n:Int = 0;
                if (index[0] == '$') {
                    // it's a variable!
                    var v:Int = index.substring(1).toInt();
                    n = args[v-1] as Int;
                } else {
                    n = index.toInt();
                }
                var oo:JSONObject = o as JSONObject;
                var aa:JSONArray = oo[c] as JSONArray;
                o = aa[n];
            } else {
                // indexing to non-array
                var oo:JSONObject = o as JSONObject;
                o = oo[c];
            }
            path = path.substring(dot+1);
        }

        return o;
    }

}
