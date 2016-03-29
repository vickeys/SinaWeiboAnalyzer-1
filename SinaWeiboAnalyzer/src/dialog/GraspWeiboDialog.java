/*
 * 微博抓取界面
 * 用于抓取微博前设置参数，包括微博账号，密码，关键字，微博发表时间范围
 * 
 */

package dialog;



import grasp.GraspProcess;

import help.PathManager;
import help.WeiboConstants;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.RowData;


public class GraspWeiboDialog extends Dialog {
//	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;
	private Text accountText;
	private Text passwordText;
	private Text keyText;
	private Text fromText;  //抓取微博时间段的开始时间
	private Text toText;    //抓取微博时间段的结束时间
	private PathManager workspace, projDirectory;
	private Button graspButton;        //抓取按钮
	private Button loginButton;        //登录按钮
	private Button exitButton;         //退出按钮
	private ProgressBar progressBar;  //进度条
	private Text consoleText;         //抓取微博实时信息显示框
	private GraspProcess task = new GraspProcess(this);           

	public GraspWeiboDialog(Shell parentShell, PathManager workspace,
			PathManager projDirectory) {
		super(parentShell);
		this.workspace = workspace;
		this.projDirectory = projDirectory;
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);

		Label winLabel = new Label(comp, SWT.CENTER);
		winLabel.setText("设置抓取参数");
		Display disp = Display.getCurrent();
		FontData fontdata = new FontData("Arial", 11, SWT.BOLD);
		Font font = new Font(disp, fontdata);
		winLabel.setFont(font);

		Group c = new Group(comp, SWT.SHADOW_ETCHED_IN);
		c.setLayout(new GridLayout(1, true));
		Group r1 = new Group(c, SWT.SHADOW_NONE);
		r1.setLayout(new GridLayout(3, true));
		Label accountLabel = new Label(r1, SWT.CENTER);
		accountLabel.setText("微博账号：");
		accountText = new Text(r1, SWT.SINGLE);
		GridData gd_accountText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accountText.widthHint = 90;
		accountText.setLayoutData(gd_accountText);
	    //登录
		loginButton = new Button(r1,SWT.RIGHT);
	    GridData gd_loginButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_loginButton.widthHint = 66;
	    loginButton.setLayoutData(gd_loginButton);
	    loginButton.setAlignment(SWT.CENTER);
	    
		Label passwordLabel = new Label(r1, SWT.CENTER);
		passwordLabel.setText("密码：");
		passwordText = new Text(r1, SWT.SINGLE);
		GridData gd_passwordText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_passwordText.widthHint = 90;
		passwordText.setLayoutData(gd_passwordText);
		passwordText.setEchoChar('*');
	    //退出 
		exitButton = new Button(r1,SWT.RIGHT);	     
	    GridData gd_exitButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_exitButton.widthHint = 66;
	    exitButton.setLayoutData(gd_exitButton);
	    exitButton.setAlignment(SWT.CENTER);
	    exitButton.setText("注销");
		
	    
		Group r2 = new Group(c, SWT.SHADOW_NONE);
		r2.setLayout(new GridLayout(1, true));
		
		Button keyBotton = new Button(r2, SWT.CHECK);
		keyBotton.setText("设置关键字：");

		Composite r21 = new Composite(r2, SWT.EMBEDDED);
		r21.setLayout(new RowLayout(SWT.HORIZONTAL));

		Label keyLabel = new Label(r21, SWT.CENTER);
		keyLabel.setText("关键字：");
		keyText = new Text(r21, SWT.SINGLE);
		keyText.setLayoutData(new RowData(100, SWT.DEFAULT));
		keyText.setEditable(false);
		// 设置默认值
		accountText.setText("15189735221");  //设置默认账号
		passwordText.setText("qqaazz");//设置默认密码
		keyText.setText("民航 旅游");           //设置默认关键字

		keyBotton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection()) {
					keyText.setEditable(true);
					keyText.setText("");
				} else {
					keyText.setEditable(false);
					keyText.setText("民航 旅游");
				}
			}
		});

		Group r22 = new Group(c, SWT.SHADOW_NONE);
		r22.setLayout(new GridLayout(2, true));
		Button btnRadioButton1 = new Button(r22, SWT.RADIO);
		btnRadioButton1.setSelection(true);
		btnRadioButton1.setText("实时微博");
		Button btnRadioButton2 = new Button(r22, SWT.RADIO);
		btnRadioButton2.setText("热门微博");
		Group r3 = new Group(c, SWT.SHADOW_NONE);
		r3.setLayout(new GridLayout(1, true));

		Button timeBotton = new Button(r3, SWT.CHECK);
		timeBotton.setText("设置微博发表的时间范围：");
		timeBotton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection()) {
					fromText.setEditable(true);
					toText.setEditable(true);
				} else {
					fromText.setText("");
					toText.setText("");
					fromText.setEditable(false);
					toText.setEditable(false);
				}
			}
		});

		Composite r31 = new Composite(r3, SWT.EMBEDDED);
		r31.setLayout(new RowLayout(SWT.HORIZONTAL));

		Label fromLabel = new Label(r31, SWT.CENTER);
		fromLabel.setText("开始时间：");
		fromText = new Text(r31, SWT.SINGLE);
		fromText.setEditable(false);

		Label toLabel = new Label(r31, SWT.CENTER);
		toLabel.setText("结束时间：");
		toText = new Text(r31, SWT.SINGLE);
		toText.setEditable(false);
		
		Label timeFormat = new Label(r3, SWT.CENTER);
		timeFormat.setText("时间格式举例：2016年5月1日应输入20160101");

		Group r4 = new Group(c, SWT.SHADOW_NONE);
		r4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        r4.setLayout(new GridLayout(1,false));
        graspButton = new Button(r4, SWT.PUSH);                //抓取按钮
        GridData gd_openButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_openButton.widthHint = 80;
        graspButton.setLayoutData(gd_openButton);
        graspButton.setText("抓取");
		Label graspTip = new Label(r4, SWT.CENTER);
		graspTip.setText("为防止文件覆盖，一分钟内请勿多次抓取");
        
        progressBar = new ProgressBar(r4, SWT.NONE);          //进度条
        progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        consoleText = new Text(r4, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
        consoleText.setEditable(false);
        GridData gd_consoleText = new GridData(GridData.FILL_BOTH);
        gd_consoleText.heightHint = 80;
        consoleText.setLayoutData(gd_consoleText);
    
        //判断登录状态  
        consoleText.insert("检查登录状态中,如长时间无提示，请检查网络\n");
        loginButton.setText("登录");
        loginButton.setEnabled(false);
    	exitButton.setEnabled(false);
    	graspButton.setEnabled(false);
    	new Thread(){   //为微博登录登录创建一个新的线程
            public void run(){           
            	task.checkLogin();
            }
        }.start(); 		
       //回车响应按钮
        getShell().setDefaultButton(loginButton);
        loginButton.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent e) {	    		
	    		String username=accountText.getText();
                String password=passwordText.getText();                         
        		if(username.equals("")||password.equals(""))
        		{
        			consoleText.insert("用户名或者密码不能为空\n");
        		}
        		else
        		{
        			
        			consoleText.insert("正在登录...\n");
        		new Thread(){   //为微博登录登录创建一个新的线程
	                 public void run(){           
	                     task.login(username,password);               
	                 }
	             }.start(); 
        		}
	    	}
	    });
        
		 exitButton.addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent e) {
		    		task.logout();; //关闭浏览器
		    		setButtonState(false);
		    		accountText.setText("");
		    		passwordText.setText("");
		    		consoleText.insert("已注销.\n");
		    	}
		    });
		 
        graspButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String keyword= keyText.getText().toString(); 
                String starttime=fromText.getText().toString();
                String endtime=toText.getText().toString();
                boolean sort = btnRadioButton1.getSelection();
            
                //构造抓取微博内容保存的地址
                String path =workspace.getPath()
        				+ projDirectory.getPath() + WeiboConstants.WEIBO_SRC_DIR+"\\";          	
            	consoleText.insert("准备中......\n");
            	graspButton.setEnabled(false);   //让抓取按钮处于无法点击状态                   
        		new Thread(){   //为微博登录登录创建一个新的线程
	                 public void run(){           
	                    		try {               	
	                    			task.startGrasp(keyword,sort,starttime,endtime,path);
	                    		} catch (Exception e) {
									e.printStackTrace();
								} 
	                 }
	             }.start(); 		  
            }
        });	
		return comp;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, OK, "关闭窗口", false);
		//createButton(parent, CANCEL, "取消", false);
		// createButton(parent, RESET_ID, "重设", false);
	}

	protected void buttonPressed(int buttonId) {
			super.buttonPressed(buttonId);

	}
	
	public void setButtonState(boolean bFlag){
        if(bFlag){ 
	    	loginButton.setText("已登录");
	        loginButton.setEnabled(false);
	    	exitButton.setEnabled(true);
	    	graspButton.setEnabled(true); 
	    	accountText.setEditable(false);
	    	passwordText.setEditable(false);
	    	}
	    else { 
	        loginButton.setText("登录");
	        loginButton.setEnabled(true);
	    	exitButton.setEnabled(false);
	    	graspButton.setEnabled(false); 
	    	accountText.setEditable(true);
	    	passwordText.setEditable(true);
	    }  
    }
	
	public void setGraspButtonState(boolean bFlag){
		graspButton.setEnabled(bFlag); 
	}
    public Text getConsoleText(){
        return consoleText;
    }
    public ProgressBar getProgressBar(){
        return progressBar;
    }
}