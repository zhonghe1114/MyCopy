package com.zhl.myaidl;

import com.zhl.myaidlseriver.IPerson;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;

public class MainActivity extends Activity
{
	private IPerson iPerson;
	private Button btn = null;
	private ServiceConnection connection = new ServiceConnection()
	{
		
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			iPerson = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			iPerson = IPerson.Stub.asInterface(service);
			Log.d("onServiceConnected", "iPerson---:"+iPerson);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initValue();
		Intent intent = new Intent("com.zhl.myaidlseriver.action.MY_REMOTE_SERVICE");
		// 设置Intent Action属性
		bindService(intent, connection, BIND_AUTO_CREATE);
		
	}
	/**
	 * Function:
	 * 
	 * @author dehoo-ZhongHeliang 2013-3-28下午4:57:42
	 */
	private void initValue()
	{
		btn = (Button)findViewById(R.id.button1);
		
		btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					try
					{
						iPerson.setName("jiangmeiq");
						iPerson.setAge(24);
					}
					catch (RemoteException e)
					{
						e.printStackTrace();
					}
					String msg = iPerson.display();
					int age = iPerson.getAge();
					Log.d("MainActivity", "Info: " + msg);
					Log.d("MainActivity", "age = :" + "" +age);
				}
				catch (RemoteException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	protected void onDestroy()
	{
		unbindService(connection);
		super.onDestroy();
	}
	

}
