package com.example.myfilemanager;

import android.graphics.drawable.Drawable;

/**
 * 显示每一行的信息
 * @author dehoo-ZhongHeliang 2013-3-24下午22:41:24
 * @version jdk 1.6; sdk 4.2.0
 */
public class IconifiedText implements Comparable<IconifiedText>
{
	private String mText = "";
	private boolean mSelectable = true;
	private Drawable mIcon = null;
	
	public IconifiedText(String text,Drawable bullet)
	{
		this.mText = text;
		this.mIcon = bullet;
	}
	
	public boolean isSelectable()
	{
		return mSelectable;
	}
	
	public void setSelectable(boolean selectalbe)
	{
		this.mSelectable = selectalbe;
	}

	public void setIcon(Drawable drawable)
	{
		this.mIcon = drawable;
	}
	
	public Drawable getIcon()
	{
		return mIcon ;
	}
	
	public String getText()
	{
		return mText;
	}
	
	public void setText(String text)
	{
		this.mText = text;
	}

	@Override
	public int compareTo(IconifiedText another)
	{
		if (this.mText != null)
		{
			return this.mText.compareTo(another.getText());
		}else {
			throw new IllegalArgumentException();
		}
	}
}
