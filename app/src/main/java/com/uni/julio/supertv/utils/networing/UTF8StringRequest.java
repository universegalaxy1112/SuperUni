package com.uni.julio.supertv.utils.networing;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

public class UTF8StringRequest extends StringRequest {

    public UTF8StringRequest(int type, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(type, url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, "UTF-8");
            //;//Log.d("liveTV","aaaaaaaaaaaa "+response.headers);
            //defautl Charset is ISO-8859-1, the response should contain the charset as UTF-8 in Content-Type but it's returning application/xml
            //;//Log.d("liveTV","aaaaaaaaaaaa "+HttpHeaderParser.parseCharset(response.headers));
            //parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
