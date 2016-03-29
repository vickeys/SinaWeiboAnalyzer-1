package actions;

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
	  msg.setMessage("本软件由常州信息职业技术学院One Piece团队开发\n\n"+"\n\t队长:    李鹏程"+"\n\n\t队员:    马首群   闫凯伦"+"\n\n\t指导老师:    钱银中\n");
		msg.open();
		
	}
}
