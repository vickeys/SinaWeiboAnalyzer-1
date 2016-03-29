package weiboAnalyzer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class CopyRightOwner extends Action {
    public CopyRightOwner()
    {
    	this.setText("关于软件");
    	this.setToolTipText("关于软件");
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	 Shell  s=new Shell(Display.getCurrent());
	 MessageBox msg=new MessageBox(s);
	  msg.setText("关于软件");
	  msg.setMessage("所有者:常州信息职业技术学院"+'\n'+"指导老师:钱银中"+'\n'+"学生:李鹏程 马首群 闫凯伦"+"\n  禁止恶意传播！！！！");
		msg.open();
		
	}
}
