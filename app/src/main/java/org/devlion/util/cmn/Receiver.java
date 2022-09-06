package org.devlion.util.cmn;

import org.json.JSONObject;

public interface Receiver {
    void onResponse(int resCode, JSONObject res);
}
