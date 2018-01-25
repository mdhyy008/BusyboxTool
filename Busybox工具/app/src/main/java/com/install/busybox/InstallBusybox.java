package com.install.busybox;
import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.content.res.*;
import android.app.*;

public class InstallBusybox extends AppCompatActivity
{
	TextView te1;
	TextView te2;
	private static String TAG = "InstallBusybox";

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install);
		init();



	}


	public void init()
	{
		//检测设备信息
		String xin;
		te1 = (TextView)findViewById(R.id.installTextView1);
		te2 = (TextView)findViewById(R.id.installTextView2);
		if (fileIsExists("/system/xbin/busybox"))
		{
			xin = "设备机型:" + Build.MODEL + "\n\n设备型号:" + Build.DEVICE + "\n\nAndroid版本:" + Build.VERSION.RELEASE + "\n\nBusybox:已安装";
			te1.setText(xin);
		}
		else
		{
			xin = "设备机型:" + Build.MODEL + "\n\n设备型号:" + Build.DEVICE + "\n\nAndroid版本:" + Build.VERSION.RELEASE + "\n\nBusybox:未安装";
			te1.setText(xin);
		}

		
		if(fileIsExists("sdcard/busybox")){
			te2.setText("Busybox文件:已下载完成,可以安装");
			te2.setTextColor(0xFF3F51B5);
		}
		else{
			te2.setText("Busybox文件:文件下载失败，请检查网络重新打开本软件");
			te2.setTextColor(0xFFE91E63);
			
		}
	}


	public void install(View view)
	{
		//安装

		
		if(fileIsExists("sdcard/busybox")){
			Snackbar.make(view, "确认安装？", Snackbar.LENGTH_LONG)
				.setAction("确认", new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						inbusy();
					}
				}).show();	
		}
		else{
			
			Snackbar.make(getWindow().getDecorView(), "缺少文件,请重启软件下载", Snackbar.LENGTH_SHORT).show();
			
		}
		
		
		
			
				
				
	}

	public void uninstall(View view)
	{
		//卸载
		Snackbar.make(view, "确认卸载？", Snackbar.LENGTH_LONG)
			.setAction("确认", new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					unbusy();
				}
			}).show();
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




	public void inbusy()
	{
		//安装busy
		String cmd[]={"mount -o rw,remount /system",
			"cp /sdcard/busybox /system/xbin/busybox"};
		new shell().execCommand(cmd, true);
		init();
	}

	public void unbusy()
	{
		//卸载busy
		String cmd[]={"mount -o rw,remount /system",
			"rm -f /system/xbin/busybox"};
		new shell().execCommand(cmd, true);
		init();

	}






	




}
