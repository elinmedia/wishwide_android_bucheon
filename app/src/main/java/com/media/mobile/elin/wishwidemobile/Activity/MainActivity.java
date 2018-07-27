package com.media.mobile.elin.wishwidemobile.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.media.mobile.elin.wishwidemobile.Model.WideCustomerVO;
import com.media.mobile.elin.wishwidemobile.PermissionConstant;
import com.media.mobile.elin.wishwidemobile.R;
import com.media.mobile.elin.wishwidemobile.SharedPreferencesConstant;
import com.media.mobile.elin.wishwidemobile.WebUrlConstance;
import com.tsengvn.typekit.TypekitContextWrapper;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        WebUrlConstance,
        SharedPreferencesConstant,
        PermissionConstant,
        Button.OnClickListener {

    private static final String TAG = "MainActivity";

    private final Context mContext = this;

    private SharedPreferences mSharedPreferences;
    private WideCustomerVO wideCustomerVO;

    //TopBar 관련 멤버변수
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5;
    private LinearLayout mLlTabs;
    private ImageView mImgTopLogo;
    private TextView mTvTopTilte;

    //WebView 관련 멤버변수
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefresh;
    private WebAndAppBridge mWebAndAppBridge;

    private AppCompatDialog progressDialog;
    private AlertDialog mDialog;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(TypekitContextWrapper.wrap(base));
    }


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSharedPreferences = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);


        //View 초기화
        initializeView();


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                View dialogView = LayoutInflater.from(view.getContext())
                        .inflate(R.layout.dialog_alert, null);

                TextView mTvReceivedBenefitGuide = (TextView) dialogView.findViewById(R.id.tv_received_benefit_guide);
                Button mBtnOK = (Button) dialogView.findViewById(R.id.btn_ok);


                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        view.getContext());

                mTvReceivedBenefitGuide.setText(message);

                mBtnOK.setText(android.R.string.ok);
                mBtnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.confirm();
                        mDialog.dismiss();
                    }
                });


                //다이아로그박스 출력
                mDialog = builder
                        .setView(dialogView)
                        .create();
                mDialog.show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result.confirm();
                        mDialog.dismiss();
                    }
                }, 4500);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                View dialogView = LayoutInflater.from(view.getContext())
                        .inflate(R.layout.dialog_confirm, null);

                TextView mTvReceivedBenefitGuide = (TextView) dialogView.findViewById(R.id.tv_received_benefit_guide);
                Button mBtnCase1 = (Button) dialogView.findViewById(R.id.btn_case_1);
                Button mBtnCase2 = (Button) dialogView.findViewById(R.id.btn_case_2);


                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        view.getContext());

                mTvReceivedBenefitGuide.setText(message);

                mBtnCase1.setText(android.R.string.ok);
                mBtnCase1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.confirm();
                        mDialog.dismiss();
                    }
                });


                mBtnCase2.setText(android.R.string.cancel);
                mBtnCase2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.cancel();
                        mDialog.dismiss();
                    }
                });

                //다이아로그박스 출력
                mDialog = builder
                        .setView(dialogView)
                        .create();
                mDialog.show();


                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                Log.d(TAG, "onPageStarted()..." + url);
//                progressON(MainActivity.this, "로딩 중...");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished()..." + url);
                super.onPageFinished(view, url);

                mSwipeRefresh.setRefreshing(false);
                mSwipeRefresh.setEnabled(true);

                mLlTabs.setVisibility(View.VISIBLE);
                mToolbar.setVisibility(View.VISIBLE);
                mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);

                mImgTopLogo.setVisibility(View.GONE);
                mTvTopTilte.setVisibility(View.GONE);


                //AR 게임 실행 버튼 visible
                if (url.equals(DOMAIN_NAME)) {
                    mToolbar.setVisibility(View.GONE);
                    mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);    //menu(navigation) gone setting
                    mLlTabs.setVisibility(View.GONE);

//                        setWebViewScrollable(false);
                    mSwipeRefresh.setEnabled(false);

//                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

//                        String localPhoneNum = telephonyManager.getLine1Number();
//                        if (localPhoneNum != null) {
//                            localPhoneNum = localPhoneNum.replace("+82", "0");
//                            Log.d(TAG, "현재 디바이스의 전화번호 확인: " + localPhoneNum);
//
//                            mWebView.loadUrl("javascript:getDevicePhoneNum(" + localPhoneNum + ")");
//                        }
                    mWebView.clearHistory();
                }
                if (url.contains(DOMAIN_NAME + "gift/")) {   //선물가게
                    mTvTopTilte.setVisibility(View.VISIBLE);
                    mTvTopTilte.setText("선물가게");
                }
                else if (url.contains(DOMAIN_NAME + HOME_PATH)) { //홈
                    mImgTopLogo.setVisibility(View.VISIBLE);

                    mWebView.clearHistory();

                    //권한 안내 띄우기
                    if (!mSharedPreferences.getBoolean(WHETHER_PERMISSION_GUIDE_SHOW_KEY, false)) {
                        Log.d(TAG, "권한 안내 띄우기 시도");
                        startActivityForResult(new Intent(MainActivity.this, PermissionGuideActivity.class), 00);
                    }


                    //매장명 set
                    String wideCustomerName = mSharedPreferences.getString(WIDE_CUSTOMER_NAME_KEY, "GUEST");
                    if (wideCustomerName == null) {
                        wideCustomerName = "";
                    }
                    Log.d(TAG, "고객명 확인: " + wideCustomerName);
                }
                else if (url.contains(DOMAIN_NAME + STAMP_AND_POINT_LIST_DETAIL_PATH) || url.contains(DOMAIN_NAME + STAMP_AND_POINT_LIST_HISTORY_PATH)) {
                    //도장/포인트 내역
                    mTvTopTilte.setVisibility(View.VISIBLE);
                    mTvTopTilte.setText("도장/포인트");
                }
                else if (url.contains(DOMAIN_NAME + JOIN_PATH)) {
                    mToolbar.setVisibility(View.GONE);
                    mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);    //menu(navigation) gone
                    mLlTabs.setVisibility(View.GONE);
                }
                else if (url.contains(DOMAIN_NAME + COUPON_LIST_PATH) || url.contains(DOMAIN_NAME + COUPON_DETAIL_PATH)) {
                    mTvTopTilte.setVisibility(View.VISIBLE);
                    mTvTopTilte.setText("쿠폰");
                }
                else if (url.contains(DOMAIN_NAME + RECEIVED_GIFT_LIST_PATH) || url.contains(DOMAIN_NAME + RECEIVED_GIFT_DETIL_PATH)) {
                    mTvTopTilte.setVisibility(View.VISIBLE);
                    mTvTopTilte.setText("받은 선물함");
                }

                progressOFF();
            }

        });


        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });


        String responseCode = getIntent().getStringExtra("responseCode");


        if (responseCode.equals("AUTO")) {
            JSONObject objRoot = new JSONObject();
            try {
                Log.d(TAG, "전화번호 확인: " + getIntent().getStringExtra("wideCustomerPhone"));
                objRoot.put("wideCustomerPhone", getIntent().getStringExtra("wideCustomerPhone"));

                mWebView.postUrl(DOMAIN_NAME, EncodingUtils.getBytes(objRoot.toString(), "UTF-8"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (responseCode.equals("LOGIN")) {
            //로그인 url 이동
            mWebView.loadUrl(DOMAIN_NAME);
        }
    }


    //View 초기화
    private void initializeView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mLlTabs.setVisibility(View.VISIBLE);
                } else {
                    mLlTabs.setVisibility(View.GONE);
                }
            }
        };


        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);    //menu(navigation) visible/gone setting

        mImgTopLogo = (ImageView) findViewById(R.id.img_top_logo);
        mTvTopTilte = (TextView) findViewById(R.id.tv_top_title);

        mLlTabs = (LinearLayout) findViewById(R.id.ll_tabs);

        mBtn1 = (Button) findViewById(R.id.btn_1);
        mBtn2 = (Button) findViewById(R.id.btn_2);
        mBtn3 = (Button) findViewById(R.id.btn_3);
        mBtn4 = (Button) findViewById(R.id.btn_4);
        mBtn5 = (Button) findViewById(R.id.btn_5);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_web_loading);
        mProgressBar.setMax(100);

        mWebView = (WebView) findViewById(R.id.web_view);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebAndAppBridge = new WebAndAppBridge(mWebView);
        mWebView.addJavascriptInterface(mWebAndAppBridge, "WebAndAppBridge");

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mToolbar 클릭 이벤트!");
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_1:
//                if (isLocationUpdateNextTime) {
//                    //위치 서비스 켜기 "다음에" 누르면 매장 전체 검색
                    mWebView.loadUrl(DOMAIN_NAME + HOME_PATH + "?lat=0&lng=0");
//                } else {
//                    requestLocationUpdate();
//                }

                break;
            case R.id.btn_2:    //받은선물함
                mWebView.loadUrl(DOMAIN_NAME + RECEIVED_GIFT_LIST_PATH);
                break;
            case R.id.btn_3:    //선물가게
                mWebView.loadUrl(DOMAIN_NAME + GIFT_STORE_LIST_PATH);
                break;
            case R.id.btn_4:    //도장/포인트
                mWebView.loadUrl(DOMAIN_NAME + STAMP_AND_POINT_LIST_DETAIL_PATH);
                break;
            case R.id.btn_5:    //쿠폰
                mWebView.loadUrl(DOMAIN_NAME + COUPON_LIST_PATH);
                break;
        }
//        progressON(this, "로딩 중...");
    }


    //Web의 javascript와 앱을 연결해주는 클래스
    private class WebAndAppBridge {
        private static final String TAG = "WebAndAppBridge";

        public static final String REQUEST_EVENT = "request";
        public static final String PERMISSION_DENIED_EVENT = "denied";
        public static final String PERMISSION_GRANTED_EVENT = "granted";

        private final WebView mWebView;

        public WebAndAppBridge(WebView webView) {
            mWebView = webView;
        }


        @JavascriptInterface
        public void callStore(String tel) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel)));
        }


        @JavascriptInterface
        public void callBrowser(String url) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }


        @JavascriptInterface
        public void callClearWebViewHistory() {
            mWebView.clearHistory();
        }


        @JavascriptInterface
        public void getAndroidContactList(String event) {
            Log.d(TAG, "연락처 가져오기");

            JSONObject objRoot = new JSONObject();

            try {
                switch (event) {
                    case REQUEST_EVENT:
                        List<String> deniedPermissions = getDeniedPermissions(Manifest.permission.READ_CONTACTS);

//                        mGiftProductNo = giftProductNo;

                        if (deniedPermissions.size() > 0) {
                            requestPermission(deniedPermissions.toArray(new String[deniedPermissions.size()]), CONTACT_PERMISSION);
                        } else {
                            mWebView.post(new Runnable() {
                                @Override
                                public void run() {
                                    getAndroidContactList(PERMISSION_GRANTED_EVENT);
                                }
                            });

                        }

                        objRoot.put("responseCode", "HOLD");
                        break;
                    case PERMISSION_GRANTED_EVENT:
                        objRoot.put("contacts", getContactAll());
//                        objRoot.put("giftProductNo", mGiftProductNo);
                        objRoot.put("responseCode", "SUCCESS");

                        Log.d(TAG, "연락처 확인: " + objRoot.toString());

                        mWebView.postUrl(DOMAIN_NAME + CONTACT_LIST_PATH, EncodingUtils.getBytes(objRoot.toString(), "UTF-8"));
                        break;
                    case PERMISSION_DENIED_EVENT:
                        objRoot.put("responseCode", "DENIED");
//                        objRoot.put("giftProductNo", mGiftProductNo);

                        mWebView.postUrl(DOMAIN_NAME + CONTACT_LIST_PATH, EncodingUtils.getBytes(objRoot.toString(), "UTF-8"));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @JavascriptInterface
        public void requestCurrentLocation(String event) {
            switch (event) {
                case REQUEST_EVENT:
//                    requestLocationUpdate();
                    break;
            }
        }

        @JavascriptInterface
        public void callQRCodeScanner(String type) {
            Log.d(TAG, "enter callQRCodeScanner()...");
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
            intentIntegrator.initiateScan();
        }

        private JSONArray getContactAll() {
            Cursor cursor = null;

            JSONArray arrContacts = new JSONArray();

            try {
                cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, null);

                int contactIdIdx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                cursor.moveToFirst();

                do {
                    JSONObject objContact = new JSONObject();

                    String idContact = cursor.getString(contactIdIdx);
                    String name = cursor.getString(nameIdx);
                    String phoneNumber = cursor.getString(phoneNumberIdx);

//                    Log.d(TAG, "getContactAll's Contact ID: " + idContact);
//                    Log.d(TAG, "getContactAll's Name: " + name);
//                    Log.d(TAG, "getContactAll's Phone Number: " + phoneNumber);

                    if ((phoneNumber != null) && Pattern.matches("^(010)-(\\d{3,4})-(\\d{4})", phoneNumber)) {
                        objContact.put("contactId", idContact);
                        objContact.put("contactName", name);
                        objContact.put("contactPhone", phoneNumber);

                        arrContacts.put(objContact);
                    }
                }
                while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return arrContacts;
        }


        @JavascriptInterface
        public void setCustomerInfo(
                String wideCustomerPhone,
                int wideCustomerNo,
                String wideCustomerEmail,
                String wideCustomerBirth,
                String wideCustomerName,
                int wideCustomerSex) {

            if (wideCustomerVO == null) {
                wideCustomerVO = new WideCustomerVO();
            }

            wideCustomerVO.setWideCustomerNo(wideCustomerNo);
            wideCustomerVO.setWideCustomerPhone(wideCustomerPhone);
            wideCustomerVO.setWideCustomerBirth(wideCustomerBirth);
            wideCustomerVO.setWideCustomerSex(wideCustomerSex);
            wideCustomerVO.setWideCustomerEmail(wideCustomerEmail);
            wideCustomerVO.setWideCustomerName(wideCustomerName);

            Log.d(TAG, wideCustomerVO.toString());
//
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(WIDE_CUSTOMER_PHONE_KEY, wideCustomerPhone);
            editor.putInt(WIDE_CUSTOMER_NO_KEY, wideCustomerNo);
            editor.putString(WIDE_CUSTOMER_BIRTH_KEY, wideCustomerBirth);
            editor.putString(WIDE_CUSTOMER_SEX_KEY, wideCustomerSex + "");
            editor.putString(WIDE_CUSTOMER_EMAIL_KEY, wideCustomerEmail);
            editor.putString(WIDE_CUSTOMER_NAME_KEY, wideCustomerName);
            editor.commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (mWebView.canGoBack())) {
            mWebView.goBack();

//            WebBackForwardList list = mWebView.copyBackForwardList();

            if (mWebView.getUrl().contains(DOMAIN_NAME + GIFT_ORDER_PATH)) {
                mWebView.goBackOrForward(-1);
                // history 삭제
                mWebView.clearHistory();
            }

            return true;
        } else {
            View dialogView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.dialog_confirm, null);

            TextView mTvReceivedBenefitGuide = (TextView) dialogView.findViewById(R.id.tv_received_benefit_guide);
            Button mBtnCase1 = (Button) dialogView.findViewById(R.id.btn_case_1);
            Button mBtnCase2 = (Button) dialogView.findViewById(R.id.btn_case_2);


            // Generates an Alert Dialog to show the error message
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);

            mTvReceivedBenefitGuide.setText("Wishwide 앱을 종료하시겠습니까?");

            mBtnCase1.setText("예");
            mBtnCase1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    mDialog.dismiss();
                }
            });


            mBtnCase2.setText("아니요");
            mBtnCase2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            //다이아로그박스 출력
            mDialog = builder
                    .setView(dialogView)
                    .create();
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        return super.onKeyDown(keyCode, event);
    }

    //왼쪽 네비게이션바 아이템 선택 리스너
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here...

        int id = item.getItemId();
//        progressON(this, "로딩 중...");
        switch (id) {
            case R.id.nav_nearby_stores:    //홈
                mBtn1.performClick();
                break;
            case R.id.nav_gift_store:   //선물가게
                mBtn3.performClick();
                break;
            case R.id.nav_received_gift:    //받은선물내역(선물함)
                mWebView.loadUrl(DOMAIN_NAME + RECEIVED_GIFT_LIST_PATH);
                break;
            case R.id.nav_send_gift:    //보낸선물내역
                mWebView.loadUrl(DOMAIN_NAME + SEND_GIFT_LIST_PATH);
                break;
            case R.id.nav_coupon:   //쿠폰함내역(쿠폰함)
                mBtn5.performClick();
                break;
            case R.id.nav_point_and_stamp:  //도장/포인트내역
                mBtn4.performClick();
                break;
            case R.id.nav_setting:  //환경설정
                //setting activity 이동
                startActivityForResult(new Intent(MainActivity.this, SettingActivity.class), 11);
                break;
            default:
//                progressOFF();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    //권한 허용 안 된 리스트 가져오기
    private List<String> getDeniedPermissions(String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();

        for (String permission : permissions) {
            boolean isDeniedPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            Log.d(TAG, permission + "의 권한 허용 여부: " + isDeniedPermission);

            if (!isDeniedPermission) {
                //권한 없음
                deniedPermissions.add(permission);
            }
        }

        return deniedPermissions;
    }


    //권한 요청
    private void requestPermission(String[] deniedPermissions, int requestCode) {
        Log.d(TAG, "권한 요청: " + deniedPermissions.length);
        ActivityCompat.requestPermissions(
                this,
                deniedPermissions,
                requestCode);
    }


    //권한 허용 메시지
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                //저장소
                break;
            case CONTACT_PERMISSION:
                //전화부
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mWebAndAppBridge.getAndroidContactList(WebAndAppBridge.PERMISSION_GRANTED_EVENT);
                } else {
                    mWebAndAppBridge.getAndroidContactList(WebAndAppBridge.PERMISSION_DENIED_EVENT);
                }
                break;
            case CALL_PERMISSION:
                //전화

                break;
            case GAME_START_PERMISSION:
                //카메라, 저장소
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @JavascriptInterface
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "enter onActivityResult()...");
        switch (requestCode) {
            case 00:
                if (resultCode == 1) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(WHETHER_PERMISSION_GUIDE_SHOW_KEY, true);
                    editor.commit();
                }
                break;
            case 11:
                if (resultCode == 1) {
                    //로그아웃
                    Log.d(TAG, "로그아웃 시도");
                    mWebView.loadUrl(DOMAIN_NAME);
                } else if (resultCode == 2) {
                    //설정 변경
                    Log.d(TAG, "설정 변경");
                    progressOFF();
                }
            default:
                break;
        }

        // QR코드/ 바코드를 스캔한 결과
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            // result.getFormatName() : 바코드 종류
            // result.getContents() : 바코드 값
            Log.d(TAG, resultCode + "QR 코드 스캔 값: " + result.getContents());

            mWebView.loadUrl(DOMAIN_NAME + COUPON_DETAIL_PATH + "?qrVal=" + result.getContents());
        }
        else {
            mWebView.loadUrl(DOMAIN_NAME + COUPON_DETAIL_PATH);
        }
    }


    //prograss message setting and show progress
    public void progressON(Activity activity, String message) {
        if (activity == null || activity.isFinishing()) {
            Log.d(TAG, "액티비티 return");
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            Log.d(TAG, "progressDialog is not null");
            progressSET(message);
        } else {
            Log.d(TAG, "progressDialog is null");
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }


    //progress message setting
    private void progressSET(String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }


        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }


    //progress off
    private void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}