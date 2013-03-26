package com.example.myfilemanager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class IconifiedTextView extends LinearLayout
{
	private TextView mTextView = null;
	private ImageView mIcon = null;
	
	public IconifiedTextView(Context context, IconifiedText aIcon)
	{
		super(context);
		// 设置布局方式
		this.setOrientation(HORIZONTAL);
		// 设置ImageView为文件图标
		mIcon = new ImageView(context);
		mIcon.setImageDrawable(aIcon.getIcon());
		// 设置图标的填充位置
		mIcon.setPadding(8, 12, 6, 12);
		
		addView(mIcon,new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT));
		mTextView = new TextView(context);
		mTextView.setText(aIcon.getText());
		mTextView.setPadding(8, 6, 6, 10);
		mTextView.setTextSize(26);
		addView(mTextView,new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 
				LayoutParams.FILL_PARENT));
	}
	
	public void setText(String words)
	{
		mTextView.setText(words);
	}
	
	public void setIcon(Drawable bullet)
	{
		mIcon.setImageDrawable(bullet);
	}

	
}
