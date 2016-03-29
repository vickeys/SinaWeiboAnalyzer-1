/*
 * 这个类用于调用SinaWeibo工具类实现微博抓取
 */
package grasp;

import org.eclipse.swt.widgets.Display;


import dialog.GraspWeiboDialog;

public class GraspProcess extends Thread {
	private GraspWeiboDialog guipr; // 前台界面对象

	/**取得前台界面**/
	public GraspProcess(GraspWeiboDialog taskGUI) {
		this.guipr = taskGUI;
	}
	
	/**登录微博账号**/
	public void login(String username ,String password){		
		String loginvalidator =SinaWeibo.login(username, password);
		intsertConsoleText(loginvalidator);
		if(loginvalidator.equals("登陆成功！\n")){
			setGraspGUIButtonState(true);
		}
	}
	
	/**退出微博账号**/
	public void logout(){
		SinaWeibo.close();	
	}
	
	/**检查是否登录新浪微博账号**/
	public void checkLogin(){
		boolean flag;
		try {
			flag = SinaWeibo.whetherLogin();
			setGraspGUIButtonState(flag);
			if(!flag) intsertConsoleText("请登录\n"); 
			else intsertConsoleText("已登录，可以进行抓取\n"); 
		} catch (Exception e) {
			intsertConsoleText("请检查网络！\n");
		}

	}
	
	/**这个方法用于抓取微博、保存微博 变量包括关键字、微博类型、开始时间、结束时间、保存地址	**/
	public void startGrasp(String keyword,boolean sort,
			String starttime, String endtime, String path){
		String url = null;
		int page = 0; // 网页总页数
		int countpage = 0; // 当前页数
		SinaWeibo.wbcount = 0; // 计数清零
		// 登录微博，返回登录结果 
			url = SinaWeibo.URLEncode(keyword,sort,starttime, endtime); // 获得构造的网址
			intsertConsoleText("开始搜索！\n");
			page = SinaWeibo.getPage(url + "1"); // 获得共计有多少页微博
			maxProgressBar(page - 1); // 设置进度条最大值
			
			// 这个循环用于抓取每页微博
			for (int i = 1; i <= page; i++) {
					intsertConsoleText(SinaWeibo.catchWeibo(url, path, i)
							+ "\n");
					countpage++;
				moveProgressBar(i);// 进度条+1
			}
			intsertConsoleText("共抓取" + SinaWeibo.wbcount + "条微博,生成" + countpage
					+ "个xml文件!\n");
			 setGraspGUIGraspButtonState(true);//刷新界面“抓取”按钮状态
	}

	/**设置界面按钮状态**/
	private void setGraspGUIButtonState(final boolean bFlag) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				guipr.setButtonState(bFlag);
			}
		});
	}
	
    /**设置界面“抓取”按钮状态**/
	private void setGraspGUIGraspButtonState(final boolean bFlag) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				guipr.setGraspButtonState(bFlag);
			}
		});
	}
	
	/**移动进度条**/
	private void moveProgressBar(final int progress) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				guipr.getProgressBar().setSelection(progress);
			}
		});
	}

	/**这个方法用于设置进度条最大值**/	 
	private void maxProgressBar(final int progress) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				guipr.getProgressBar().setMaximum(progress);
				;

			}
		});
	}

	/**这个方法用于信息框显示信息**/
	private void intsertConsoleText(final String str) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				guipr.getConsoleText().insert(str);

			}
		});
	}
}