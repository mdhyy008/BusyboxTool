package com.install.busybox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.support.design.widget.*;
import android.content.*;
import android.net.*;
import android.net.Uri;
import java.io.*;
import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity
{
	CheckBox cb;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
		

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            }
			else{
				dow();
			}
        }
		
		
		



	}
	//菜单 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.Begin:
				cb = (CheckBox)findViewById(R.id.mainCheckBox1);	
				if (cb.isChecked())
				{
					Intent i = new Intent(MainActivity.this, InstallBusybox.class);
					startActivity(i);
				}
				else
				{	
					Snackbar.make(getWindow().getDecorView(), "你没有勾选使用协议,请认真阅读并勾选", Snackbar.LENGTH_SHORT).show();
				}


				break;
        }
        return super.onOptionsItemSelected(item);
    }



	//判断文件存在
	public boolean fileIsExists(String fi)
	{
		try
		{
			File f=new File(fi);
			if (!f.exists())
			{
				return false;
			}
		}
		catch (Exception e)
		{
// TODO: handle exception
			return false;
		}
		return true;
	}


public void dow(){
	
	if (fileIsExists("sdcard/busybox") == false)
	{
		Snackbar.make(getWindow().getDecorView(), "缺少文件,正在从服务器下载...", Snackbar.LENGTH_LONG).show();
		String link = "http://bmob-cdn-15690.b0.upaiyun.com/2018/01/25/d82701ca4072b03080529c012e3996ca.zip";
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		request.setTitle("busybox文件");
		request.setDescription("正在下载busybox文件...");
		request.setDestinationInExternalPublicDir("", "busybox");
		DownloadManager downloadManager = (DownloadManager)getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManager.enqueue(request);
	}
	
}


    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

	
	
    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
			.setTitle("存储权限不可用")
			.setMessage("请在-应用设置-权限-中，允许使用存储权限来保存数据")
			.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 跳转到应用设置界面
					goToAppSetting();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).setCancelable(false).show();
    }

	
	
    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
					
                    if (dialog != null && dialog.isShowing()) {
						
                        dialog.dismiss();
                    }
                   	Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
               
				
					
					}
            }
        }
    }
	








}
