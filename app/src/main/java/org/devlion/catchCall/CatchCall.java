package org.devlion.catchCall;

import android.content.Context;
import android.provider.CallLog;
import android.util.Log;

import org.devlion.util.cmn.HttpHelper;
import org.devlion.util.db.CallLogVo;
import org.devlion.util.db.DBHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatchCall implements HttpHelper.HttpListener {

    final String TAG = "CATCH_CALL";
    private DBHelper dbHelper;
    Context context;

    CatchCall(Context context){
        this.context = context;
        init();
    }

    void init(){
        dbHelper = new DBHelper(context);
        dbHelper.open();
        dbHelper.create();
    }

    public void check(String phoneNumber){
        Log.d(TAG, "check():" + phoneNumber);
        List<String> numberList = getTwoNumberFromBack(phoneNumber);
        if(numberList.size() < 3) return;

        // API 호출
        Map<String, String> req = new HashMap<String, String>();
        req.put("enumber",numberList.get(0));
        req.put("cnumber",numberList.get(1));

        String serverUrl = SharedPrefHelper.getInstance(context).getString(SharedPrefHelper.SERVER_URL);
        String serverKey = SharedPrefHelper.getInstance(context).getString(SharedPrefHelper.SERVER_KEY);

        serverUrl = "https://" + serverUrl;
        serverUrl += "/monitoring.app/api/getUser";

        Log.d(TAG, "SERVER_URL:" + serverUrl);
        Log.d(TAG, "SERVER_KEY:" + serverKey);

        // API 리스너 정의
        HttpHelper httpHelper = new HttpHelper(serverUrl, serverKey);
        httpHelper.setHttpListener(this);

        httpHelper.doGet(req);
    }

    public void check(String cnumber, String enumber){

        Map<String, String> req = new HashMap<String, String>();
        req.put("enumber", enumber);
        req.put("cnumber", cnumber);

        String serverUrl = SharedPrefHelper.getInstance(context).getString(SharedPrefHelper.SERVER_URL);
        String serverKey = SharedPrefHelper.getInstance(context).getString(SharedPrefHelper.SERVER_KEY);

        serverUrl = "https://" + serverUrl;
        serverUrl += "/monitoring.app/api/getUser";

        Log.d(TAG, "SERVER_URL:" + serverUrl);
        Log.d(TAG, "SERVER_KEY:" + serverKey);

        // API 리스너 정의
        HttpHelper httpHelper = new HttpHelper(serverUrl, serverKey);
        httpHelper.setHttpListener(this);

        httpHelper.doGet(req);

    }

    void deleteCallLog(){
        dbHelper.deleteAll();
    }

    List<String> getTwoNumberFromBack(String phoneNumber){

        List<String> numberList = new ArrayList<String>();
        String[] numberAry = phoneNumber.split("-");

        for(int i = numberAry.length - 1 ; 0 <= i ; i--) {
            numberList.add(numberAry[i]);
        }

        return numberList;
    }



    @Override
    public void onResponse(int resCode, JSONObject res) {
        Log.d(TAG, "resCode: " + resCode + ", res: " + res);

        try {
            if(res != null){

                String cellPhone = (String)res.get("cellPhone");
                String companyPhone = (String)res.get("companyPhone");
                String companyCode = (String)res.get("companyCode");
                String addisplayName = (String)res.get("addisplayName");
                String chargeJob = (String)res.get("chargeJob");
                String mainDeptCode = (String)res.get("mainDeptCode");
                String companyName = (String)res.get("companyName");
                String deptName = (String)res.get("deptName");

                // 팝업 오픈
                Popup p = Popup.getInstance(context);
                Log.d(TAG, "open p: " + p);
                p.open(companyName, addisplayName, deptName, chargeJob);

                // DB 저장
                CallLogVo callLogVo = new CallLogVo();
                callLogVo.setCellPhone(cellPhone);
                callLogVo.setCompanyPhone(companyPhone);
                callLogVo.setCompanyCode(companyCode);
                callLogVo.setAddisplayName(addisplayName);
                callLogVo.setChargeJob(chargeJob);
                callLogVo.setMainDeptCode(mainDeptCode);
                callLogVo.setCompanyName(companyName);
                callLogVo.setDeptName(deptName);
                dbHelper.insert(callLogVo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
