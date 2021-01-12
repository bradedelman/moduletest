package com.affirm.baller.service

import com.affirm.baller.platform.Native
import org.json.JSONObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result;

class NativeHttp constructor(native: Native) {

    var _native: Native = native;

    fun send(dataStr: String):String {

        var data = JSONObject(dataStr);
        var url:String = data["_url"] as String;
        var _onError:String = data["_onError"] as String;
        var _onSuccess:String = data["_onSuccess"] as String;
        var _viewId:String = data["_viewId"] as String;
        var _storeId:String = data["_storeId"] as String;
        var _verb:String = data["_verb"] as String;

        // select GET or POST - add POST body if provided
        val httpAsync =  if (_verb.toUpperCase().equals("POST")) url.httpPost() else url.httpGet();
        if (data.has("_body")) {
            httpAsync.body(data["_body"] as String);
        }

        // add headers
        if (data.has("_headers")) {
            var headers = data["_headers"] as JSONObject;
            for (header in headers.keys()) {
                httpAsync.header(header, headers[header] as String);
            }
        }

        var request = httpAsync.responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        this._native.jsCall(_viewId, _onError, ex.toString())
                    }
                    is Result.Success -> {
                        val data = result.get()

                        var store:NativeStore? = this._native._services["NativeStore"] as NativeStore;
                        store?.setRaw(_storeId, JSONObject(data));
                        this._native.jsCall(_viewId, _onSuccess)
                    }
                }
            }

        request.join()

        return "";
    }

}
