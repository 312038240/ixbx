package com.jim.ixbx.view.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jim.ixbx.IxbxApplication;
import com.jim.ixbx.utils.SpUtils;

/**
 *
 *
 *
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private IxbxApplication mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (IxbxApplication) getApplication();
        mApplication.addActivity(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.removeActivity(this);
        mProgressDialog.dismiss();
    }
    public void saveUser(String username,String pwd){
       SpUtils.put(this,"username",username);
       SpUtils.put(this,"pwd",pwd);
    }

    public void startActivity(Class clazz,boolean isFinish){
        startActivity(clazz,isFinish,null);
    }

    public void startActivity(Class clazz, boolean isFinish, String contact) {
        Intent intent = new Intent(this,clazz);
        if (contact!=null){
            intent.putExtra("username",contact);
        }
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }
    public String getUserName(){
        return (String) SpUtils.get(this,"username","");
    }
    public String getPwd(){
        return (String) SpUtils.get(this,"pwd","");
    }

    public void showDialog(String msg){
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    public void hideDialog(){
        mProgressDialog.hide();
    }


}
