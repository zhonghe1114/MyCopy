/**
 * 
 */
package com.zhl.myaidlseriver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author dehoo-ZhongHeliang 2013-3-28下午4:43:51
 * @version jdk 1.6; sdk 4.2.0
 */
public class MyRemoteService extends Service
{
	int age;
	int age2 = 88;
	String name;

	@Override
	public IBinder onBind(Intent intent)
	{
		return new IPersonsub();
	}
	class IPersonsub extends IPerson.Stub
	{
		
		public void setAge(int page) throws RemoteException
		{
			age = page;
		}

		public void setName(String pname) throws RemoteException
		{
			name = pname;
		}

		public String display() throws RemoteException
		{
			String string = "黄东：22"+"后来加的name："+name + "" +age;
			return string;
		}

		public int getAge() throws RemoteException
		{
			return age2;
		}
		
	}
	
}
