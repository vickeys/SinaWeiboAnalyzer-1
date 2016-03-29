package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InstructionForUseAction extends Action{
	
	public InstructionForUseAction(){
		super();
		setText("使用说明");
	}
	
	public void run(){
		Shell s=new Shell(Display.getCurrent(),SWT.CLOSE | SWT.MIN);
		s.setSize(610,500);
	
		s.setText("“网络评论情感分析器”使用说明");
		Text t=new Text(s,SWT.V_SCROLL|SWT.WRAP|SWT.READ_ONLY);
		t.setBackground(new Color(Display.getDefault(), new RGB(224,
				238, 224)));

	   String s1="1. 运行软件\n软件运行后要求选择工作空间，工作空间是保存项目数据的文件夹\n2. 新建项目\n主界面打开后，点击工具栏上的“新建项目”按钮，或者选择“项目”菜单中“新\n建项目”将新建项目，成功后主界面中将出现项目名称和项目路径，同时，工作\n空间中出现刚创建的项目文件夹；\n3. 抓取微博\n选择“抓取和计算”菜单中“抓取微博”的子菜单，或者点击工具栏上“抓取微博”按钮，在弹出的窗口中，可以输入新浪微博账号和密码，默认抓取的微博关键字是：“民航  旅游”，默认时间为到当前时间为止，上述参数均可以设置。设置后点击“抓取”按钮即可实现抓取功能，抓取完成后点击“关闭窗口”按钮。\n";
	  String s2="4. 情感计算\n选择“抓取和计算”菜单中“情感计算与分析“子菜单，或者点击工具栏“情感计算与分析”按钮，在弹出窗口，选择含负面情感词的文本情感计算方式，默认为只计算负面情感词的累加。单击“开始计算”按钮即可开始情感分析，情感分析完成后点击”关闭窗口”按钮。情感计算后，预警信息表中显示负面微博预警信息。\n";
       String s3="5. 设置预警阈值\n单击“设置”菜单中“设置预警阈值”菜单项，在弹出的对话框中即可设置黄色预警和红色预警的范围。如果预警信息表中有内容，将按照新设置的阈值显示。\n6.显示所有微博\n单击“设置”菜单中“显示所有微博/仅显示负面微博”子菜单，在弹出的对话框中选中“显示所有微博”复选框，预警信息表显示所有的微博的预警信息。\n"  ;
	  String s4="7.比赛数据转换为XML格式\n项目新建后，选择“比赛专用”菜单中“比赛数据转化为XML”的子菜单，可以把比赛用的测试文本和标注文本转换成xml文档，存储在项目中。\n8. 比赛文本情感计算\n选择“比赛专用”菜单中的“比赛文本情感计算“子菜单，在弹出窗口，选择含负面情感词的文本情感计算方式，默认为只计算负面情感词的累加。点击“开始计算”按钮，计算比赛数据的情感分值。计算结束后，对话框下部显示计算时间、负面微博的丢失率和负面微博的报错率。关闭对话框，预警信息表中推送计算结果，表中的备注栏显示文本的标注，计算结果与标注不一致的文本以红色字体显示。";
	  t.setText(s1+s2+s3+s4);
       t.setFont(new Font(null, "黑体", 12, SWT.NORMAL));
	 t.setSize(605, 450);
		s.setLocation(Display.getCurrent().getClientArea().width / 2 - s.getShell().getSize().x/2, Display.getCurrent()  
                .getClientArea().height / 2 - s.getSize().y/2); 
		s.open();
	}

}
