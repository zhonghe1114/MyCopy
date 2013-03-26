package com.example.myfilemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

public class MainActivity extends ListActivity
{
	private List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
	private File currentDirectory = new File("/");
	private File myTmpFile = null;
	private int myTmpOpt = -1;
	private String TAG = "MainAcitivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		browsetoRoot();
		this.setSelection(0);
	}

	/**
	 * Function: browsetoRoot
	 * 浏览文件系统的根目录
	 * @author ZhongHeliang 2013-3-24 下午10:18:17
	 */
	private void browsetoRoot()
	{
		browseTo(new File("/mnt/sdcard"));
	}
	
	/**
	 * Function: upOneLevel
	 * 返回上一级目录
	 * @author ZhongHeliang 2013-3-24 下午10:20:11
	 */
	private void upOneLevel()
	{
		if (this.currentDirectory.getParent() != null)
		{
			this.browseTo(this.currentDirectory.getParentFile());
		}
	}
	
	/**
	 * Function: browseTo
	 * 浏览指定目录，如果是文件，这进行打开操作
	 * @author ZhongHeliang 2013-3-24 下午10:21:16
	 * @param file
	 */
	private void browseTo(final File file)
	{
		this.setTitle(file.getAbsolutePath());
		if (file.isDirectory())
		{
			this.currentDirectory = file;
			fill(file.listFiles());
		}
		else {
			fileOptMenu(file);
		}
	}
	
	/**
	 * Function: openFile
	 * 打开指定目录
	 * @author ZhongHeliang 2013-3-24 下午10:22:46
	 * @param aFile
	 */
	protected void openFile(File aFile)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(aFile.getAbsolutePath());
		// 取得文件名
		String fileName = file.getName();
		// 根据不同文件类型打开文件
		if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
		{
			intent.setDataAndType(Uri.fromFile(file), "audio/*");
		}
		else if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
		{
			intent.setDataAndType(Uri.fromFile(file), "image/*");
		}
		else if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
		{
			intent.setDataAndType(Uri.fromFile(file), "video/*");
		}
		startActivity(intent);
	}
	
	/**
	 * Function: fill
	 * 为ListActivity添加内容
	 * @author ZhongHeliang 2013-3-24 下午10:23:37
	 * @param files
	 */
	private void fill(File[] files)
	{
		// 清空列表
		this.directoryEntries.clear();
		// 添加一个当前目录选项
		this.directoryEntries.add(new IconifiedText(getString(R.string.current_dir), getResources().getDrawable(R.drawable.folder)));
		// 如果不是根目录
		if (this.currentDirectory.getParent() != null)
		{
			this.directoryEntries.add(new IconifiedText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.uponelevel)));
		}
		
		Drawable currentIcon = null;
		for (File currentFile : files)
		{
			// 判断是文件还是文件夹
			if (currentFile.isDirectory())
			{
				currentIcon = getResources().getDrawable(R.drawable.folder);
			}
			else {
				// 取得文件名
				String fileName = currentFile.getName();
				// 根据文件名判断文件类型，设置不同图标
				if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
				{
					currentIcon = getResources().getDrawable(R.drawable.image);
				}
				else if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWebText)))
				{
					currentIcon = getResources().getDrawable(R.drawable.webtext);
				}
				else if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage)))
				{
					currentIcon = getResources().getDrawable(R.drawable.packed);
				}
				else if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
				{
					currentIcon = getResources().getDrawable(R.drawable.audio);
				}
				else if (checkEndWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
				{
					currentIcon = getResources().getDrawable(R.drawable.video);
				}
				else
				{
					currentIcon = getResources().getDrawable(R.drawable.text);
				}
				// 确保知先生文件名，不显示路径
				long currentPathLong = this.currentDirectory.getAbsoluteFile().length();
				this.directoryEntries.add(new IconifiedText(currentFile.getAbsolutePath().substring((int)currentPathLong), currentIcon));
			}
			Collections.sort(this.directoryEntries);
			IconifiedTextListAdapter itlaAdapter = new IconifiedTextListAdapter(this);
			itlaAdapter.setListItems(this.directoryEntries);
			this.setListAdapter(itlaAdapter);
		}
	}
	
	/**
	 * Function: checkEndWithInStringArray
	 * 判断文件是什么类型
	 * @author ZhongHeliang 2013-3-24 下午10:26:59
	 * @param checkItsEnd
	 * @param fileEndings
	 * @return
	 */
	private boolean checkEndWithInStringArray(String checkItsEnd,String[] fileEndings)
	{
		for (String aEnd : fileEndings)
		{
			if (checkItsEnd.endsWith(aEnd))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Function: MyPaste
	 * 粘贴操作
	 * @author ZhongHeliang 2013-3-24 下午10:34:04
	 */
	public void MyPaste()
	{
		if (myTmpFile == null)
		{
			Builder builder = new Builder(MainActivity.this);
			builder.setTitle("提示");
			builder.setMessage("没有复制或剪切操作");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
				
			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		else {
			if (myTmpOpt == 0) // 复制
			{
				if (new File(GetCurDirectory() + "/" + myTmpFile.getName()).exists())
				{
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle("粘贴提示");
					builder.setMessage("该目录有相同文件，是否需要覆盖？");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									copyFile(myTmpFile, new File(GetCurDirectory()+"/" +myTmpFile.getName()));
									browseTo(new File(GetCurDirectory()));
								}
						
					});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}
				else
				{
					copyFile(myTmpFile, new File(GetCurDirectory()+ "/" + myTmpFile.getName()));
					browseTo(new File(GetCurDirectory()));
				}
			}
			else if (myTmpOpt == 1) // 粘贴
			{
				if (new File(GetCurDirectory() + "/" + myTmpFile.getName()).exists())
				{
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle("粘贴提示");
					builder.setMessage("该目录有相同的文件，是否需要覆盖？");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									moveFile(myTmpFile.getAbsolutePath(), GetCurDirectory()+"/"+myTmpFile.getName());
									browseTo(new File(GetCurDirectory()));
								}
						
					});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}
				else {
					moveFile(myTmpFile.getAbsolutePath(), GetCurDirectory() + "/" + myTmpFile.getName());
					browseTo(new File(GetCurDirectory()));
				}
			}
		}
	}
	
	/**
	 * Function: MyDelete
	 * 删除整个文件夹
	 * @author ZhongHeliang 2013-3-24 下午10:35:14
	 */
	private void MyDelete()
	{
		File tmp = new File(this.currentDirectory.getAbsolutePath());
		// 跳到上一级目录
		this.upOneLevel();
		// 删除取得的目录
		if (deleteFolder(tmp))
		{
			Builder builder = new Builder(MainActivity.this);
			builder.setTitle("提示");
			builder.setMessage("删除成功");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
				
			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		else
		{
			Builder builder = new Builder(MainActivity.this);
			builder.setTitle("提示");
			builder.setMessage("删除失败");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
				
			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		this.browseTo(this.currentDirectory);
	}
	
	/**
	 * Function: MyNew
	 * 新建文件夹
	 * @author ZhongHeliang 2013-3-24 下午10:36:33
	 */
	private void MyNew()
	{
		final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		final View dialogview = factory.inflate(R.layout.dialog, null);
		// 设置
		((TextView)dialogview.findViewById(R.id.TextView_PROM)).setText("请输入文件夹名称");
		((EditText)dialogview.findViewById(R.id.EditText_PROM)).setText("文件夹名称···");
		
		Builder builder = new Builder(MainActivity.this);
		builder.setTitle("新建文件夹");
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok,
				new  AlertDialog.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						String value = ((EditText)dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
						if (newFolder(value))
						{
							Builder builder = new Builder(MainActivity.this);
							builder.setTitle("提示");
							builder.setMessage("新建文件夹成功");
							builder.setPositiveButton(android.R.string.ok,
									new AlertDialog.OnClickListener(){

										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											dialog.cancel();
										}
								
							});
							builder.setCancelable(false);
							builder.create();
							builder.show();
						}
						else
						{
							Builder builder = new Builder(MainActivity.this);
							builder.setTitle("提示");
							builder.setMessage("新建文件夹失败");
							builder.setPositiveButton(android.R.string.ok,
									new AlertDialog.OnClickListener(){

										@Override
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											dialog.cancel();
										}
								
							});
							builder.setCancelable(false);
							builder.create();
							builder.show();
						}
					}
			
		});
		builder.setNegativeButton(android.R.string.cancel, 
				new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			
			@Override
			public void onCancel(DialogInterface dialog)
			{
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	/**
	 * Function: newFolder
	 * 新建文件夹
	 * @author ZhongHeliang 2013-3-24 下午10:38:34
	 * @param file
	 * @return
	 */
	private boolean newFolder(String file)
	{
		File dirFile = new File(this.currentDirectory.getAbsolutePath()+ "/" +file);
		try
		{
			if (!(dirFile.exists())&& !(dirFile.isDirectory()))
			{
				boolean creatok = dirFile.mkdirs();
				if (creatok)
				{
					this.browseTo(this.currentDirectory);
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.d(TAG, e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * Function: deleteFile
	 * 删除文件
	 * @author ZhongHeliang 2013-3-24 下午10:39:35
	 * @param file
	 * @return
	 */
	private boolean deleteFile(File file)
	{
		boolean result = false;
		if (file != null)
		{
			try
			{
				File file2 = file;
				file2.delete();
				result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Function: deleteFolder
	 * 删除文件夹
	 * @author ZhongHeliang 2013-3-24 下午10:40:34
	 * @param folder
	 * @return
	 */
	private boolean deleteFolder(File folder)
	{
		boolean result = false;
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				if (folder.delete())
				{
					result = true;
				}
			}
			else
			{
				for (int i = 0; i < childs.length; i++)
				{
					String childName = childs[i];
					String childPath = folder.getPath() + File.separator +childName;
					File filePath = new File(childPath);
					if (filePath.exists() && filePath.isFile())
					{
						if (filePath.delete())
						{
							result = true;
						}
						else
						{
							result = false;
							break;
						}
					}
					else if (filePath.exists() && filePath.isDirectory())
					{
						if (deleteFolder(filePath))
						{
							result = true;
						}else {
							result = false;
							break;
						}
					}
				}
				folder.delete();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	/**
	 * Function: fileOptMenu
	 * 处理文件，包括打开，重命名等操作
	 * @author ZhongHeliang 2013-3-24 下午10:41:41
	 * @param file
	 */
	private void fileOptMenu(final File file)
	{
		OnClickListener listener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (which == 0)
				{
					openFile(file);
				}
				else if (which == 1)
				{
					// 自定义一个带输入框都由TextView和EditText构成
					final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
					final View dialogview = factory.inflate(R.layout.rename, null);
					// 设置TextView的提示信息
					((TextView) dialogview.findViewById(R.id.TextView01)).setText("重命名");
					// 设置EditView输入框值
					((EditText) dialogview.findViewById(R.id.EditText01)).setText(file.getName());
					
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle("重命名");
					builder.setView(dialogview);
					builder.setPositiveButton(android.R.string.ok, 
							new AlertDialog.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									String value = GetCurDirectory()+"/" +((EditText)dialogview.findViewById(R.id.EditText01)).getText().toString();
									if (new File(value).exists())
									{
										Builder builder = new Builder(MainActivity.this);
										builder.setTitle("重命名");
										builder.setMessage("文件名重复，是否需要覆盖？");
										builder.setPositiveButton(android.R.string.ok, 
												new AlertDialog.OnClickListener(){

													@Override
													public void onClick(
															DialogInterface dialog,
															int which)
													{
														String str2 = GetCurDirectory() + "/" + ((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
														file.renameTo(new File(str2));
														browseTo(new File(GetCurDirectory()));
													}
											
										});
										builder.setNegativeButton(android.R.string.cancel,
												new DialogInterface.OnClickListener()
												{
													
													@Override
													public void onClick(DialogInterface dialog, int which)
													{
														dialog.cancel();
													}
												});
										builder.setCancelable(false);
										builder.create();
										builder.show();
										
									}
									else {
										// 重命名
										file.renameTo(new File(value));
										browseTo(new File(GetCurDirectory()));
									}
								}
						
					});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.cancel();
								}
							});
					builder.show();
					
				}
				else if (which == 2) {
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle("删除文件");
					builder.setMessage("确定删除"+file.getName()+"?");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									if (deleteFile(file))
									{
										Builder builder = new Builder(MainActivity.this);
										builder.setTitle("提示对话框");
										builder.setMessage("删除成功");
										builder.setPositiveButton(android.R.string.ok,
												new AlertDialog.OnClickListener(){

													@Override
													public void onClick(
															DialogInterface dialog,
															int which)
													{
														dialog.cancel();
														browseTo(new File(GetCurDirectory()));
													}
											
										});
										builder.setCancelable(false);
										builder.create();
										builder.show();
									}
									else
									{
										Builder builder = new Builder(MainActivity.this);
										builder.setTitle("提示对话框");
										builder.setMessage("删除失败");
										builder.setPositiveButton(android.R.string.ok,
												new AlertDialog.OnClickListener(){

													@Override
													public void onClick(
															DialogInterface dialog,
															int which)
													{
														dialog.cancel();
													}
											
										});
										builder.setCancelable(false);
										builder.create();
										builder.show();
									}
								}
						
					});
					builder.setNegativeButton(android.R.string.ok,
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}
				else if (which == 3) {// 复制
					// 保存外面复制的文件目录
					myTmpFile = file;
					myTmpOpt = 0;  // 0表示复制
				}
				else if (which == 4) {// 剪切
					// 保存外面复制的文件目录
					myTmpFile = file;
					// 1表示剪切
					myTmpOpt = 1;
					
				}
			}
		};
		// 显示操作菜单
		String[] menu = {"打开","重命名","删除","复制","剪切"};
		new AlertDialog.Builder(MainActivity.this)
			.setTitle("请选择要进行的操作")
			.setItems(menu, listener)
			.show();
	}
	
	/**
	 * Function: GetCurDirectory
	 * 得到当前路径
	 * @author ZhongHeliang 2013-3-24 下午10:44:55
	 * @return
	 */
	private String GetCurDirectory()
	{
		return this.currentDirectory.getAbsolutePath();
	}
	
	/**
	 * Function: moveFile
	 * 移动文件
	 * @author ZhongHeliang 2013-3-24 下午10:45:07
	 * @param source
	 * @param destination
	 */
	private void moveFile(String source,String destination)
	{
		new File(source).renameTo(new File(destination));
	}
	
	/**
	 * Function: copyFile
	 * 复制文件
	 * @author ZhongHeliang 2013-3-24 下午10:45:03
	 * @param src
	 * @param target
	 */
	private void copyFile(File src,File target)
	{
		InputStream in = null;
		OutputStream out = null;
		
		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);
			
			byte[] b = new byte[8192];
			int len = bin.read(b);
			while(len != -1)
			{
				bout.write(b,0,len);
				len = bin.read(b);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (bin != null)
				{
					bin.close();
				}
				if (bout != null)
				{
					bout.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	


	// 创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "新建目录").setIcon(R.drawable.addfolderr);
		menu.add(0, 1, 0, "删除目录").setIcon(R.drawable.delete);
		menu.add(0, 2, 0, "粘贴文件").setIcon(R.drawable.paste);
		menu.add(0, 3, 0, "跟目录").setIcon(R.drawable.goroot);
		menu.add(0, 4, 0, "上一级").setIcon(R.drawable.uponelevel);
		
		return true; 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
		case 0:
			MyNew();
			break;
		case 1:
			MyDelete();
			break;
		case 2:
			MyPaste();
			break;
		case 3:
			this.browsetoRoot();
			break;
		case 4:
			this.upOneLevel();
			break;
		}
		
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		String selectFileString = this.directoryEntries.get(position).getText();
		if (selectFileString.equals(getString(R.string.current_dir)))
		{
			// 如果选择的刷新
			this.browseTo(this.currentDirectory);
		}
		else if (selectFileString.equals(getString(R.string.up_one_level)))
		{
			// 返回上一级
			this.upOneLevel();
		}
		else {
			File clickedFile = null;
			clickedFile = new File(this.currentDirectory.getAbsoluteFile() + this.directoryEntries.get(position).getText());
			if (clickedFile != null)
			{
				this.browseTo(clickedFile);
			}
		}
	}
	
	
	
}
