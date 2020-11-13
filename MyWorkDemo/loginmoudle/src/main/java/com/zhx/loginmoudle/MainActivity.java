package com.zhx.loginmoudle;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wanmei.show.fans.R;

import java.util.ArrayList;
import java.util.List;

import cn.com.chinatelecom.account.sdk.AuthPageConfig;
import cn.emay.ql.LoginCallback;
import cn.emay.ql.UniSDK;
import cn.emay.ql.utils.DeviceUtil;
import cn.emay.ql.utils.LoginUiConfig;

/**
 * Created by duoku_tech on 2017/4/18.
 */

public class MainActivity extends AppCompatActivity {

    private PermissionListener mListener;
    private static final int PERMISSION_REQUESTCODE = 100;
    Button tv_login;
    TextView tv_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_login = (Button) findViewById(R.id.tv_login);
        tv_result = (TextView) findViewById(R.id.tv_result);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UniSDK.getInstance().login(MainActivity.this,"daa96efb17864d0b9cabaefa29b1dbdc","a7517ac91cf5412e", new LoginCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_result.setText("SDK版本号 ： " + UniSDK.getInstance().getVersion() +  "  登录结果 " + msg);
                                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailed(String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_result.setText("SDK版本号 ： " + UniSDK.getInstance().getVersion() +  "  登录结果 " + msg);
                                Toast.makeText(MainActivity.this,"登录失败" + msg,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },getLoginUiConfig(),false);
            }
        });
        requestPermission(this);
    }

    private LoginUiConfig getLoginUiConfig(){
        LoginUiConfig uiConfig = new LoginUiConfig();
        LoginUiConfig.YiDongLoginConfig yidongConfig = uiConfig.new YiDongLoginConfig();

        yidongConfig.setStatusBarColor(0xff0086d0);
        yidongConfig.setAuthNavTransparent(false);//授权页head是否隐藏
        yidongConfig.setAuthBGImgPath("");//设置背景图
        yidongConfig.setNavColor(0xff0086d0);//导航栏颜色
        yidongConfig.setNavReturnImgPath("");//导航返回图标
        yidongConfig.setNavReturnSize(30);//返回图标大小
        yidongConfig.setNavText("登录");//导航栏标题
        yidongConfig.setNavTextColor(0xffffffff);//导航栏字体颜色
        yidongConfig.setNavTextSize(17);

        yidongConfig.setLoginLogo("ic_launcher");//logo图片
        yidongConfig.setLogoWidthDip(70);//图片宽度
        yidongConfig.setLogoHeightDip(70);//图片高度
        yidongConfig.setLogoOffsetY(100);//图片Y偏移量
        yidongConfig.setLogoHidden(false);//logo图片隐藏

        yidongConfig.setNumberColor(0xff333333);//手机号码字体颜色
        yidongConfig.setNumberSize(18);////手机号码字体大小
        yidongConfig.setNumFieldOffsetY(170);//号码栏Y偏移量

        yidongConfig.setSloganTextColor(0xff999999);//slogan文字颜色
        yidongConfig.setSloganTextSize(10);//slogan文字大小
        yidongConfig.setSloganOffsetY(230);//slogan声明标语Y偏移量

        yidongConfig.setLogBtnText("本机号码一键登录");//登录按钮文本
        yidongConfig.setLogBtnTextColor(0xffffffff);//登录按钮文本颜色
        yidongConfig.setLogBtnImgPath("");//登录按钮背景
        yidongConfig.setLogBtnSize(15);
        yidongConfig.setLogBtnOffsetY(254);//登录按钮Y偏移量

        yidongConfig.setSwitchAccTextColor(0xff329af3);//切换账号字体颜色
        yidongConfig.setShowOtherLogin(true);//切换账号是否隐藏
        yidongConfig.setSwitchAccTextSize(14);//切换账号字体大小
        yidongConfig.setSwitchOffsetY(310);//切换账号偏移量

        yidongConfig.setUncheckedImgPath("umcsdk_uncheck_image");//chebox未被勾选图片
        yidongConfig.setCheckedImgPath("umcsdk_check_image");//chebox被勾选图片
        yidongConfig.setCheckBoxImgPathSize(9);//勾选check大小
        yidongConfig.setPrivacyState(true);//授权页check
        yidongConfig.setPrivacyAlignment1("登录即同意");
        yidongConfig.setPrivacyAlignment2("应用自定义服务条款一");
        yidongConfig.setPrivacyAlignment3("https://www.baidu.com");
        yidongConfig.setPrivacyAlignment4("应用自定义服务条款二");
        yidongConfig.setPrivacyAlignment5("https://www.hao123.com");
        yidongConfig.setPrivacyAlignment6("并使用本机号码登录");
        yidongConfig.setPrivacyTextSize(10);
        yidongConfig.setPrivacyTextColor1(0xff666666);//文字颜色
        yidongConfig.setPrivacyTextColor2(0xff0085d0);//条款颜色
        yidongConfig.setPrivacyOffsetY_B(30);//隐私条款Y偏移量
        yidongConfig.setPrivacyMargin(50);
        uiConfig.setYiDongLoginConfig(yidongConfig);

        LoginUiConfig.LianTongLoginConfig lianTongLoginConfig = uiConfig.new LianTongLoginConfig();
        lianTongLoginConfig.setShowProtocolBox(false);//不展示协议的勾选框
        //注意，当setShowProtocolBox = false时，只能通过代码来设置按钮文字
        lianTongLoginConfig.setLoginButtonText("快捷登录");//按钮文字内容
        lianTongLoginConfig.setLoginButtonWidth(500);//按钮宽度
        lianTongLoginConfig.setLoginButtonHeight(100);//按钮高度
        lianTongLoginConfig.setOffsetY(100);//按钮Y轴距离
        lianTongLoginConfig.setProtocolCheckRes(R.drawable.selector_button_cucc);//按钮点击背景
        lianTongLoginConfig.setProtocolUnCheckRes(R.drawable.selector_button_ctc);//按钮未点击背景
        lianTongLoginConfig.setProtocolID("protocol_1");//xml布局中定义的控件id
        lianTongLoginConfig.setProtocolUrl("https://www.baidu.com");
        lianTongLoginConfig.setProtocolID1("protocol_2");//xml布局中定义的控件id
        uiConfig.setLianTongLoginConfig(lianTongLoginConfig);


        //隐私协议文本,其中配置说明如下
        // 1、$OAT 为运营商协议标题占位符，SDK程序默认替换为《天翼账号服务与隐私协议》，若有其它运营商协议配置需求，可添加配置；
        // 2、$CAT 为自定义协议标题占位符，SDK程序会替换为自定义标题字段的值；
        // 3、[应用名] ：修改为您应用的名称
        LoginUiConfig.DianXinLoginConfig dianXinLoginConfig = uiConfig.new DianXinLoginConfig();
        dianXinLoginConfig.setPrivacyText("登录即同意$OAT与$CAT并授权[本demo]获取本机号码");
        dianXinLoginConfig.setPrivacyTextColor(0xFF000000);//隐私协议文本的字体颜色
        dianXinLoginConfig.setPrivacyTextSize(12);//隐私协议文本的字体大小
        dianXinLoginConfig.setOperatorAgreementTitleColor(0xFF0090FF);//运营商协议标题的字体颜色
        dianXinLoginConfig.setCustomAgreementTitle("《我的自定义协议》");//自定义协议标题
        dianXinLoginConfig.setCustomAgreementLink("https://www.baidu.com");//自定义协议wap页面地址
        dianXinLoginConfig.setCustomAgreementTitleColor(0xFF0090FF);//自定义协议标题的字体颜色

        //弹窗登录设置弹窗大小，单位为px
        dianXinLoginConfig.setDialogHeight(1000);
        dianXinLoginConfig.setDialogWidth(DeviceUtil.getScreenWidth(this));
        //弹窗弹出位置AuthPageConfig.BOTTOM,AuthPageConfig.CENTER
        dianXinLoginConfig.setLocation(AuthPageConfig.BOTTOM);

        uiConfig.setDianXinLoginConfig(dianXinLoginConfig);
        return uiConfig;
    }
    private void requestPermission(Activity activity) {
        requestRunPermisssion(new String[]{ Manifest.permission.READ_PHONE_STATE},new PermissionListener(){
            @Override
            public void onGranted() {
                //表示所有权限都授权了

            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                for(String permission : deniedPermission){
                    Toast.makeText(MainActivity.this, "被拒绝的权限：" + permission, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void requestRunPermisssion(String[] permissions, PermissionListener listener){
        mListener = listener;
        List<String> permissionLists = new ArrayList<>();
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionLists.add(permission);
            }
        }

        if(!permissionLists.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        }else{
            //表示全都授权了
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUESTCODE:
                if(grantResults.length > 0){
                    //存放没授权的权限
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i = 0; i < grantResults.length; i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        //说明都授权了
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 已授权、未授权的接口回调
     */
    public interface PermissionListener {

        void onGranted();//已授权

        void onDenied(List<String> deniedPermission);//未授权

    }
}
