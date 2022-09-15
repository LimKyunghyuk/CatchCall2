package org.devlion.catchCall;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageButton;
import android.widget.TextView;

public class Popup {

    final String TAG = "CATCH_CALL";
    Context context;
    WindowManager windowManager;
    View rootView;

    Boolean isOpen; // 전화거절/통화밸 종료/전화 수신 전에 팝업을 먼저 종료할 경우, removeView() 두 번 호출 방지

    private static Popup singleton;

    private Popup(Context context){
        this.context = context;
    }

    public static Popup getInstance(Context context){
        if(singleton == null) singleton = new Popup(context);
        return singleton;
    }

    public void open(String team, String name){
        Log.d(TAG, "Popup.open()");
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int width = (int) (getScreenWidth() * 0.9);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                width,                                      // 너비
                WindowManager.LayoutParams.WRAP_CONTENT,    // 높이
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE           // Home, Back 버튼 무시
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.call_popup, null);

        TextView tvTopDescription = rootView.findViewById(R.id.tv_top_description);
        TextView tvBottomDescription = rootView.findViewById(R.id.tv_bottom_description);
        ImageButton btnClose = rootView.findViewById(R.id.btn_close);

        if (!TextUtils.isEmpty(team)) {
            tvTopDescription.setText(team);
        }

        if (!TextUtils.isEmpty(name)) {
            tvBottomDescription.setText(name);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        windowManager.addView(rootView, layoutParams);
        isOpen = true;

        Log.d(TAG, "rootView: " + rootView);
        Log.d(TAG, "windowManager: " + windowManager);
        Log.d(TAG, "isOpen: " + isOpen);
    }

    public void close() {
        Log.d(TAG, "Popup.close()");
        Log.d(TAG, "rootView: " + rootView);
        Log.d(TAG, "windowManager: " + windowManager);
        Log.d(TAG, "isOpen: " + isOpen);

        if (rootView != null && windowManager != null && isOpen == true){
            windowManager.removeView(rootView);
            isOpen = false;
        }
    }

    // 화면의 가로 사이즈를 구한다. getDefaultDisplay()가 deprecated 되었음
    public int getScreenWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();

            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }
}
