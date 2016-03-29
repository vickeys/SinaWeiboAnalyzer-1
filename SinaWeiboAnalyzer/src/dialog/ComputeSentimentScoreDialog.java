/*
 * 情感计算对话框
 * 用于计算情感前设置参数，选择只考虑负面情感词，还是正负均考虑
 * 
 * */

package dialog;

import help.PathManager;
import help.WeiboConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import dict.SentimentAnalysis;
import root.*;
public class ComputeSentimentScoreDialog extends Dialog {
	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;
	private Button[] radios = new Button[2];
	private PathManager workspace, projDirectory;
	private AnalyzerWindow analyzerWindow;
	private Display disp;
	private ProgressBar pBar;   //进度条
	private Text consoleText;  //显示计算过程的动态信息
	private boolean onlyNeg = true;  // 只考虑负面情感词 
	
	SentimentAnalysis sa;  //情感分析类

	
	
	public ComputeSentimentScoreDialog(Shell parentShell,
			AnalyzerWindow analyzerWindow) {
		super(parentShell);
		this.workspace = analyzerWindow.getWorkspace();
		this.projDirectory = analyzerWindow.getProjDirectory();
		this.analyzerWindow = analyzerWindow;
	}

	//创建对话框界面
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		comp.setSize(600, 800);
		Label winLabel = new Label(comp, SWT.CENTER);
		winLabel.setText("选择含负面情感词的微博情感计算方式");
		//Display disp = Display.getCurrent();,
	    disp = Display.getDefault();
		FontData fontdata = new FontData("Arial", 11, SWT.BOLD);
		Font font = new Font(disp, fontdata);
		winLabel.setFont(font);

		Group c = new Group(comp, SWT.SHADOW_ETCHED_IN);
		c.setLayout(new GridLayout(1, false));
		Group r1 = new Group(c, SWT.SHADOW_NONE);
		r1.setLayout(new GridLayout(1, false));
		radios[0] = new Button(r1, SWT.RADIO);
		radios[0].setText("只计算负面情感词的累加");
		radios[0].setSelection(true);
		radios[0].pack();
		radios[1] = new Button(r1, SWT.RADIO);
		radios[1].setText("计算正面和负面情感词的累加");
		radios[1].pack();
		Button b =new Button(r1, SWT.NONE);
		 b.setText("开始计算"); 
		
		Group r2 = new Group(c, SWT.SHADOW_NONE);
		r2.setLayout( new FormLayout());
	    pBar = new ProgressBar(r2, SWT.HORIZONTAL);
		FormData fd_bar = new FormData();
		fd_bar.top = new FormAttachment(0);
		fd_bar.right = new FormAttachment(100, -10);
		fd_bar.bottom = new FormAttachment(0, 20);
		fd_bar.left = new FormAttachment(100, -200);
		pBar.setLayoutData(fd_bar);
	    
		consoleText = new Text(r2, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(pBar,5,SWT.BOTTOM);
		fd_text.right = new FormAttachment(pBar,0, SWT.RIGHT);
		fd_text.bottom = new FormAttachment(pBar,100,SWT.BOTTOM);
		fd_text.left = new FormAttachment(pBar,0, SWT.LEFT);
		consoleText.setLayoutData(fd_text);
 

			
			String srcPath = workspace.getPath() + projDirectory.getPath()
					+ WeiboConstants.WEIBO_SRC_DIR; // 抓取到的微博所在路径
			/****************************马首群添加*********************************************/
			String outPath = workspace.getPath() + projDirectory.getPath()
					+WeiboConstants.OBJ_XML_FILE;//生成xml文件所在路径  
			/**********************************************************************************/
		 sa = new SentimentAnalysis(this,srcPath,outPath);
			File srcDir = new File(srcPath);
			String xmlFiles[] = srcDir.list();
			int taskCount=xmlFiles.length;
			pBar.setMaximum(taskCount-1);
		 b.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				boolean onlyNeg = true;
				if (radios[1].getSelection())
					onlyNeg = false;
				sa.setOnlyNeg(onlyNeg);
				
				// 下面编写计算情感的代码（方法调用） 马 编写代码
				/****************************马首群添加*********************************************/

				//int taskCount=xmlFiles.length;
				 consoleText.insert("开始进行微博分析\n");
	             new Thread(){             //为微博情感分析创建一个线程
	                 public void run(){
	                     try {
							sa.analysis();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                 }
	             }.start();

				/**********************************************************************************/
			//		System.out.println("OK");
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
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
		if (buttonId == OK) // 如果点击了确定按钮
		{
		    onlyNeg = true;
			if (radios[1].getSelection())
				onlyNeg = false;
			    
			analyzerWindow.getKeyWord_text().setText("");
			analyzerWindow.getWeibo_text().setText("");
			analyzerWindow.callInput(sa.getWeibos());
			
		//		System.out.println("OK");
		}

		super.buttonPressed(buttonId);

	}

	public Display getDisp() {
		return disp;
	}

	public ProgressBar getpBar() {
		return pBar;
	}

	public void setpBar(ProgressBar pBar) {
		this.pBar = pBar;
	}

	public Text getConsoleText() {
		return consoleText;
	}

	public void setConsoleText(Text consoleText) {
		this.consoleText = consoleText;
	}

	

}