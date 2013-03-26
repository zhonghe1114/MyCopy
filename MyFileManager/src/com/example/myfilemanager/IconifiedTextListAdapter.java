package com.example.myfilemanager;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class IconifiedTextListAdapter extends BaseAdapter
{
	private Context mContext = null;
	private List<IconifiedText> mItems = new ArrayList<IconifiedText>();
	
	public IconifiedTextListAdapter(Context context)
	{
		this.mContext = context;
	}

	/**
	 * Function: addItem
	 * 添加一个文件
	 * @author dehoo-ZhongHeliang 2013-3-25下午1:56:58
	 * @param it
	 */
	public void addItem(IconifiedText it)
	{
		mItems.add(it);
	}
	
	/**
	 * Function: setListItems
	 * 设置文件列表
	 * @author dehoo-ZhongHeliang 2013-3-25下午1:57:15
	 * @param lit
	 */
	public void setListItems(List<IconifiedText> lit)
	{
		mItems = lit;
	}
	
	/**
	 * Function: areAllItemsSelectable
	 * 能否全部选中
	 * @author dehoo-ZhongHeliang 2013-3-25下午1:59:30
	 * @return
	 */
	public boolean areAllItemsSelectable()
	{
		return false;
	}
	
	/**
	 * Function: isSelectable
	 * 判断指定文件是否被选中
	 * @author dehoo-ZhongHeliang 2013-3-25下午2:01:00
	 * @param position
	 * @return
	 */
	public boolean isSelectable(int position)
	{
		return mItems.get(position).isSelectable();
	}
	
	@Override
	public int getCount()
	{
		return mItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		IconifiedTextView btv;
		if (convertView == null)
		{
			btv = new IconifiedTextView(mContext, mItems.get(position));
		}
		else {
			btv = (IconifiedTextView) convertView;
			btv.setText(mItems.get(position).getText());
			btv.setIcon(mItems.get(position).getIcon());
		}
		return btv;
	}

}
