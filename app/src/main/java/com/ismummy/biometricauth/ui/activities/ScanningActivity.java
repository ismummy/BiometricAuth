package com.ismummy.biometricauth.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ismummy.biometricauth.R;
import com.ismummy.biometricauth.ui.activities.HEROFUN.HAPI;
import com.ismummy.biometricauth.ui.activities.HEROFUN.LAPI;
import com.ismummy.biometricauth.ui.bases.BaseActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanningActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    @BindView(R.id.btn_scanner_continue)
    Button btnCont;
    @BindView(R.id.tv_error)
    TextView tvMessage;
    @BindView(R.id.iv_finger)
    ImageView viewFinger;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private LAPI m_cLAPI = null;
    private int m_hDevice = 0;
    private int[] RGBbits = new int[256 * 360];

    //for HAPI
    private boolean option = true;        //ANSI-true, ISO-false

    private String[] mListString = null;
    private String[] mFiletString = null;

    public static final int MESSAGE_SHOW_TEXT = 101;
    public static final int MESSAGE_ID_SETTEXT = 404;

    private HAPI m_cHAPI = null;
    private volatile boolean bContinue = false;

    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;

    private Intent intent;
    private int operation;
    private String id;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        openDevice();
    }

    @Override
    protected void onPause() {
        m_cHAPI.DoCancel();
        bContinue = false;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (m_hDevice != 0) closeDevice();
        super.onDestroy();
    }

    private void registerListener() {
        if (mContext != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mContext.registerReceiver(mScreenReceiver, filter);
        }
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                onDestroy();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        intent = getIntent();

        if (intent == null) {
            finish();
        }

        operation = intent.getIntExtra("OPERATION", 0);
        id = intent.getStringExtra("ACCOUNT_NUMBER");


        m_cLAPI = new LAPI(this);
        m_cHAPI = new HAPI(this, m_fpsdkHandle);
        mContext = this;
        mScreenReceiver = new ScreenBroadcastReceiver();
        registerListener();


    }

    @OnClick(R.id.btn_scanner_continue)
    void continueClicked() {
        if (bContinue) {
            m_cHAPI.DoCancel();
            bContinue = false;
            m_appHandle.obtainMessage(MESSAGE_ID_SETTEXT, R.id.btn_scanner_continue, R.string.TEXT_CONTINUE).sendToTarget();
            return;
        }

        if (btnCont.getText().toString().equalsIgnoreCase("Continue")) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

    protected void openDevice() {
        Runnable r = new Runnable() {
            public void run() {
                String msg;
                m_hDevice = m_cLAPI.OpenDeviceEx();
                if (m_hDevice == 0) msg = "Can't open device !";
                else {
                    msg = "OpenDevice() = OK";
                }
                m_cHAPI.m_hDev = m_hDevice;
                m_cHAPI.DBRefresh();
                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, msg));

                progressBar.setVisibility(View.VISIBLE);
                m_appHandle.obtainMessage(MESSAGE_ID_SETTEXT, R.id.btn_scanner_continue, R.string.TEXT_CANCEL).sendToTarget();
                bContinue = true;
                if (operation == 1) {
                    verify(id);
                } else {
                    enroll(id);
                }
            }
        };
        Thread s = new Thread(r);
        s.start();
    }

    protected void closeDevice() {
        Runnable r = new Runnable() {
            public void run() {
                String msg;
                m_cHAPI.DoCancel();
                if (m_hDevice != 0) {
                    //m_cLAPI.CtrlLed(m_hDevice, 0);	//for Optical
                    m_cLAPI.CloseDeviceEx(m_hDevice);
                }
                msg = "CloseDevice() = OK";
                m_hDevice = 0;
                m_cHAPI.m_hDev = m_hDevice;
                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, msg));
            }
        };
        Thread s = new Thread(r);
        s.start();
    }


    protected void enroll(String id) {
        String msg;
        Resources res = getResources();
        if (id.isEmpty()) {
            msg = res.getString(R.string.Insert_ID);
            m_fpsdkHandle.obtainMessage(HAPI.MSG_SHOW_TEXT, 0, 0, msg).sendToTarget();
            m_appHandle.obtainMessage(MESSAGE_ID_SETTEXT, R.id.btn_scanner_continue, R.string.TEXT_CONTINUE).sendToTarget();
            bContinue = false;
            return;
        }

        boolean ret = m_cHAPI.Enroll(id, option);
        if (ret) {
            msg = String.format("Enroll OK (ID=%s)", id);
            m_cHAPI.DBRefresh();
        } else {
            msg = String.format("Enroll : False : %s", errorMessage(m_cHAPI.GetErrorCode()));
        }
        bContinue = false;
        m_fpsdkHandle.obtainMessage(HAPI.MSG_SHOW_TEXT, 0, 0, msg).sendToTarget();
        m_appHandle.obtainMessage(MESSAGE_ID_SETTEXT, R.id.btn_scanner_continue, R.string.TEXT_CONTINUE).sendToTarget();
        progressBar.setVisibility(View.GONE);
    }

    protected void verify(String id) {
        int retry;
        String msg = "";
        String o_id = id;
        Resources res = getResources();

        if (id.isEmpty()) {
            msg = res.getString(R.string.Insert_ID);
            m_fpsdkHandle.obtainMessage(HAPI.MSG_SHOW_TEXT, 0, 0, msg).sendToTarget();
            m_appHandle.obtainMessage(MESSAGE_ID_SETTEXT, R.id.btn_scanner_continue, R.string.TEXT_CONTINUE).sendToTarget();
            bContinue = false;
            return;
        }
        id = o_id + "_LEFT";
        for (retry = 0; retry < 10; retry++) {
            boolean ret = m_cHAPI.Verify(id, option);
            if (ret) {
                msg = String.format(Locale.getDefault(), "Verify OK (ID=%s) : Time(Capture=%dms,Create=%dms,Match=%dms)",
                        id, m_cHAPI.GetProcessTime(0), m_cHAPI.GetProcessTime(1), m_cHAPI.GetProcessTime(2));
                break;
            } else {
                int errCode = m_cHAPI.GetErrorCode();
                if (errCode != HAPI.ERROR_NONE && errCode != HAPI.ERROR_LOW_QUALITY) {
                    msg = String.format("Verify : False : %s", errorMessage(m_cHAPI.GetErrorCode()));
                    break;
                }
            }
            SLEEP();
        }
        id = o_id + "_RIGHT";
        for (retry = 0; retry < 10; retry++) {
            boolean ret = m_cHAPI.Verify(id, option);
            if (ret) {
                msg = String.format(Locale.getDefault(), "Verify OK (ID=%s) : Time(Capture=%dms,Create=%dms,Match=%dms)",
                        id, m_cHAPI.GetProcessTime(0), m_cHAPI.GetProcessTime(1), m_cHAPI.GetProcessTime(2));
                break;
            } else {
                int errCode = m_cHAPI.GetErrorCode();
                if (errCode != HAPI.ERROR_NONE && errCode != HAPI.ERROR_LOW_QUALITY) {
                    msg = String.format("Verify : False : %s", errorMessage(m_cHAPI.GetErrorCode()));
                    break;
                }
            }
            SLEEP();
        }

        if (retry == 10) {
            msg = "Verify : False : Exceed retry limit";
        }
        bContinue = false;
        m_fpsdkHandle.obtainMessage(HAPI.MSG_SHOW_TEXT, 0, 0, msg).sendToTarget();
        m_appHandle.obtainMessage(MESSAGE_ID_SETTEXT, R.id.btn_scanner_continue, R.string.TEXT_CONTINUE).sendToTarget();
        progressBar.setVisibility(View.GONE);
    }


    protected void SLEEP() {
        int startTime, passTime = 0;
        startTime = (int) System.currentTimeMillis();
        while (passTime < 300) {
            passTime = (int) System.currentTimeMillis();
            passTime = passTime - startTime;
        }
    }


    @SuppressLint("HandlerLeak")
    private final Handler m_fEvent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_TEXT:
                    tvMessage.setText((String) msg.obj);
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private final Handler m_appHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_TEXT:
                    tvMessage.setText((String) msg.obj);
                    break;
                case MESSAGE_ID_SETTEXT:
                    Button btn = findViewById(msg.arg1);
                    btn.setText(msg.arg2);
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private final Handler m_fpsdkHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str;
            Resources res;
            switch (msg.what) {
                case 0xff:
                    break;
                case HAPI.MSG_SHOW_TEXT:
                    tvMessage.setText((String) msg.obj);
                    break;
                case HAPI.MSG_PUT_FINGER:
                    res = getResources();
                    str = res.getString(R.string.Put_your_finger);
                    if (msg.arg1 > 0) {
                        str += (" (" + String.valueOf(msg.arg1) + "/" + String.valueOf(msg.arg2) + ")");
                    }
                    str += " ! ";
                    str += (String) msg.obj;
                    tvMessage.setText(str);
                    break;
                case HAPI.MSG_RETRY_FINGER:
                    res = getResources();
                    str = res.getString(R.string.Retry_your_finger);
                    str += " !";
                    tvMessage.setText(str);
                    break;
                case HAPI.MSG_TAKEOFF_FINGER:
                    res = getResources();
                    str = res.getString(R.string.Takeoff_your_finger);
                    str += " !";
                    tvMessage.setText(str);
                    break;
                case HAPI.MSG_ON_SEARCHING:
                    res = getResources();
                    str = res.getString(R.string.TEXT_ON_SEARCHING);
                    if (msg.arg1 > 0) {
                        str += (" (quality=" + String.valueOf(msg.arg1) + ")");
                    }
                    str += "  ...  ";
                    tvMessage.setText(str);
                    break;
                case HAPI.MSG_FINGER_CAPTURED:
                    ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
                    break;
                case HAPI.MSG_DBRECORD_START:
                    mListString = new String[msg.arg1];
                    mFiletString = new String[msg.arg1];
                    break;
                case HAPI.MSG_DBRECORD_NEXT:
                    mListString[msg.arg2] = String.format(Locale.getDefault(), "No = %d : ID = %s", msg.arg2, msg.obj);
                    mFiletString[msg.arg2] = (String) msg.obj;
                    break;
                case HAPI.MSG_DBRECORD_END:
                    //return total number of record in db
                    String txt = String.format(Locale.getDefault(), "Record Count = %d", msg.arg1);
                    tvMessage.setText(txt);
                    break;
            }
        }
    };

    public String errorMessage(int errCode) {
        Resources res;
        res = getResources();
        switch (errCode) {
            case HAPI.ERROR_NONE:
                return res.getString(R.string.ERROR_NONE);
            case HAPI.ERROR_ARGUMENTS:
                return res.getString(R.string.ERROR_ARGUMENTS);
            case HAPI.ERROR_LOW_QUALITY:
                return res.getString(R.string.ERROR_LOW_QUALITY);
            case HAPI.ERROR_NEG_ACCESS:
                return res.getString(R.string.ERROR_NEG_ACCESS);
            case HAPI.ERROR_NEG_FIND:
                return res.getString(R.string.ERROR_NEG_FIND);
            case HAPI.ERROR_NEG_DELETE:
                return res.getString(R.string.ERROR_NEG_DELETE);
            case HAPI.ERROR_INITIALIZE:
                return res.getString(R.string.ERROR_INITIALIZE);
            case HAPI.ERROR_CANT_GENERATE:
                return res.getString(R.string.ERROR_CANT_GENERATE);
            case HAPI.ERROR_OVERFLOW_RECORD:
                return res.getString(R.string.ERROR_OVERFLOW_RECORD);
            case HAPI.ERROR_NEG_ADDNEW:
                return res.getString(R.string.ERROR_NEG_ADDNEW);
            case HAPI.ERROR_NEG_CLEAR:
                return res.getString(R.string.ERROR_NEG_CLEAR);
            case HAPI.ERROR_NONE_ACTIVITY:
                return res.getString(R.string.ERROR_NONE_ACTIVITY);
            case HAPI.ERROR_NONE_CAPIMAGE:
                return res.getString(R.string.ERROR_NONE_CAPIMAGE);
            case HAPI.ERROR_NOT_CALIBRATED:
                return res.getString(R.string.ERROR_NOT_CALIBRATED);
            case HAPI.ERROR_NONE_DEVICE:
                return res.getString(R.string.ERROR_NONE_DEVICE);
            case HAPI.ERROR_TIMEOUT_OVER:
                return res.getString(R.string.ERROR_TIMEOUT_OVER);
            case HAPI.ERROR_DO_CANCELED:
                return res.getString(R.string.ERROR_DOCANCELED);
            case HAPI.ERROR_EMPTY_DADABASE:
                return res.getString(R.string.ERROR_EMPTY_DADABASE);
            default:
                return String.format(Locale.getDefault(), "errCode=%d", errCode);
        }
    }

    private void ShowFingerBitmap(byte[] image, int width, int height) {
        if (width == 0) return;
        if (height == 0) return;
        for (int i = 0; i < width * height; i++) {
            int v;
            if (image != null) v = image[i] & 0xff;
            else v = 0;
            RGBbits[i] = Color.rgb(v, v, v);
        }
        Bitmap bmp = Bitmap.createBitmap(RGBbits, width, height, Config.RGB_565);
        intent.putExtra("IMAGE", bmp);
        viewFinger.setImageBitmap(bmp);
    }
}