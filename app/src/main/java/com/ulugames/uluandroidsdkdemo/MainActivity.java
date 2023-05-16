package com.ulugames.uluandroidsdkdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.ulusdk.Bean.ULUOrder;
import com.ulusdk.Bean.ULURole;
import com.ulusdk.Bean.ULUUser;
import com.ulusdk.ULUManager;
import com.ulusdk.uluinterface.ULUListener;
import com.ulusdk.uluinterface.ULUOnCustomerServiceNewMessageListener;
import com.ulusdk.uluinterface.ULUPayListenter;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ULUListener uluListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uluListener = new ULUListener() {
            @Override
            public void onInitSuccess() {
                //初始化成功回调，执行接下来的操作，如登录等
                login();//此处仅做参考，请按照实际需求处理回调
            }

            @Override
            public void onInitFail(String s) {
                //初始化失败，将无法正常登录，请做适当处理，在收到失败回调之前请不要重复调用初始化
            }

            @Override
            public void onLogoutSuccess() {
                //登出成功，常见于玩家打开sdk用户中心之后退出登录，请安排用户重新登录或者进行其他操作
            }

            @Override
            public void onLogoutFail(String s) {
                //登出失败，一般不需要在这里添加操作
            }

            @Override
            public void onLoginSuccess(ULUUser uluUser) {
                pay();//此处仅做参考，请按照实际需求处理回调
            }

            @Override
            public void onLoginFail(String s) {
                //登录失败
            }
        };
        init();
    }

    /**
     * 初始化，一般于打开app后调用，其他大部分接口正常使用的前提为初始化成功，请尽早调用并确保初始化成功
     */
    private void init(){
        ULUManager.getInstance().init(this, "参数表中的gameId", uluListener);
    }

    /**
     * 唤起登录框，需要在初始化完成之后调用，否则无法登录
     */
    private void login(){
        ULUManager.getInstance().login();
    }

    /**
     * 唤起支付，在不同渠道表现不同，所有的参数有就填，尽量填，没有的时候可以不传
     */
    private void pay(){
        ULUOrder uluOrder = new ULUOrder();
        uluOrder.setExtraData("透传参数");
        uluOrder.setUluProductId("ulu_商品id");
        ULURole uluRole = new ULURole();
        uluRole.setRoleName("RoleName");
        uluRole.setServerName("ServerName");
        uluRole.setServerId("ServerId");
        uluRole.setVipLevel(1);
        uluRole.setRoleId("RoleId");
        ULUManager.getInstance().pay(uluOrder, uluRole, new ULUPayListenter() {
            @Override
            public void onPaySuccess(String orderId, String extraData) {
                runOnUiThread( () -> Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show());
                //此处仅做参考，请按照实际需求处理回调
            }

            @Override
            public void onPayFail(String orderId, String errorMsg, String extraData) {
                runOnUiThread( () -> Toast.makeText(MainActivity.this, "支付失败" + errorMsg, Toast.LENGTH_SHORT).show());
                //此处仅做参考，请按照实际需求处理回调
            }
        });
    }

    /**
     * 开启用户中心界面，包含账号绑定和切换账号功能，请不要忘记处理初始化中的logout回调
     */
    private void openUserCenter(){
        ULURole uluRole = new ULURole();
        uluRole.setRoleLevel("角色等级");
        uluRole.setRoleName("角色名称");
        uluRole.setServerName("服务器名称");
        uluRole.setServerId("服务器ID");
        uluRole.setVipLevel(1); //vip等级，如没有VIP等级机制可传0
        uluRole.setRoleId("角色ID");
        //↑如果需要在没有角色信息的时候打开用户中心，请在上面每一项都传入空字符串而不是null
        ULUManager.getInstance().openUserCenter(uluRole, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//示例用方向为竖屏，横屏一般为SCREEN_ORIENTATION_SENSOR_LANDSCAPE，可按照自身需求修改
    }

    /**
     * 打开客服界面
     */
    private void openCustomerService(){
        ULURole uluRole = new ULURole();
        uluRole.setRoleLevel("角色等级");
        uluRole.setRoleName("角色名称");
        uluRole.setServerName("服务器名称");
        uluRole.setServerId("服务器ID");
        uluRole.setVipLevel(1); //vip等级，如没有VIP等级机制可传0
        uluRole.setRoleId("角色ID");
        //↑如果需要在没有角色信息的时候打开客服，请在上面每一项都传入空字符串而不是null
        ULUManager.getInstance().openCustomerService(uluRole, new ULUOnCustomerServiceNewMessageListener() {
            @Override
            public void onCustomerServiceNewMessage() {
                //提示用户客服有新消息了，如在客服按钮上添加红点等。
            }
        });
//        ULUManager.getInstance().openCustomerService(uluRole);//当然不需要接受新消息回调也可以
    }

    /**
     * 打点用接口，包含af fb firebase等第三方渠道，全部使用这个接口就行
     */
    private void trackEvent(){
        ULURole uluRole = new ULURole();
        uluRole.setRoleLevel("角色等级");
        uluRole.setRoleName("角色名称");
        uluRole.setServerName("服务器名称");
        uluRole.setServerId("服务器ID");
        uluRole.setVipLevel(1); //vip等级，如没有VIP等级机制可传0
        uluRole.setRoleId("角色ID");
        //↑如果需要在没有角色信息的时候打点，请在上面每一项都传入空字符串而不是null
        Map<String, Object> map = new HashMap<>();
        map.put("回传键","回传值");//如果有额外需求的参数在此处添加
        ULUManager.getInstance().uluTrackEvent(uluRole,"打点名称",map);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ULUManager.getInstance().ULUOnActivityResult(requestCode, resultCode, data);
    }
}