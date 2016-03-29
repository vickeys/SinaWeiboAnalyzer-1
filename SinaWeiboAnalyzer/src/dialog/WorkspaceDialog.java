
/*
 * WorkspaceDialog类用于系统初启时设定工作空间路径
 * 
 * */

package dialog;

import help.PathManager;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class WorkspaceDialog extends Dialog
{
  private static final int RESET_ID = 
                  IDialogConstants.NO_TO_ALL_ID + 1;
  
  private Text path;
  PathManager workspace;
  public WorkspaceDialog(Shell parentShell,PathManager workspace)
  {
    super(parentShell);
    this.workspace = workspace;
    
  }

  protected Control createDialogArea(Composite parent)
  {
    Composite comp = (Composite)super.createDialogArea(parent);
    FillLayout layout = new FillLayout(SWT.VERTICAL);
    layout.spacing = 5;
    Label winLabel = new Label(comp, SWT.CENTER);
    winLabel.setText("选择工作空间");
    Display disp = Display.getCurrent();
    FontData fontdata = new FontData("Arial", 11, SWT.BOLD);
    Font font = new Font(disp,fontdata);
    winLabel.setFont(font);
    
    Label annotation = new Label(comp, SWT.CENTER);
    annotation.setText("（工作空间是保存项目资源的文件夹）");
    
    Group c = new Group(comp,SWT.SHADOW_ETCHED_IN);
  
    Label spaceAnno = new Label(c, SWT.LEFT);
    spaceAnno.setText("工作空间：");
    spaceAnno.setBounds(0, 18, 55, 15);
    path = new Text(c, SWT.SINGLE);
    path.setText("E:\\weiboSpace");        //设置默认工作空间
    path.setBounds(60, 15, 200, 20);
    Button b = new Button(c, SWT.PUSH);
    b.setText("浏览...");
    b.setBounds(270, 10, 80, 30);
    b.addSelectionListener(new SelectionAdapter(){
    	public void widgetSelected(SelectionEvent e) {
    		Shell shell = new Shell(disp);
    		DirectoryDialog d = new DirectoryDialog(shell);
    		d.setMessage("选择文件夹");
    		String selectPath = d.open();
    		try {
				path.setText(selectPath);
				System.out.println(path.getText());
			} catch (IllegalArgumentException e1) {
				
			}
    		
    		
    	}
    });
    return comp;
  }
  
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, OK, "确定", false);
    createButton(parent, CANCEL, "取消", false);
    createButton(parent, RESET_ID, "重设", false);
  }
  
  protected void buttonPressed(int buttonId)
  {
	  System.out.println(buttonId);
    if(buttonId == OK)    //如果点击了确定按钮，先检查是否为合法路径
    {
    	File f = new File(path.getText().trim());
      if (!f.isDirectory())
      {  
    	   if(f.mkdirs())
    	   {
    	  MessageBox m = new MessageBox(new Shell(Display.getCurrent()),SWT.NO);
    	  m.setText("友情提醒");
    	  m.setMessage("路径"+path.getText().trim()+"\n   已经创建");
    	  m.open();
    	  workspace.setPath(path.getText());
          super.buttonPressed(buttonId);
    	   }
    	   else
    	   {
    		   MessageBox m = new MessageBox(new Shell(Display.getCurrent()),SWT.NO);
    	    	  m.setText("友情提醒");
    	    	  m.setMessage("请输入合法路径");
    	    	  m.open();
    	   }
      }
      else{
    	  //System.out.println(path.getText()+"....from OK");	  //测试语句
    	  workspace.setPath(path.getText());
          super.buttonPressed(buttonId);
      }
      
    }
    else if(buttonId == RESET_ID)      //如果是重设按钮
    	path.setText("");
    else             //取消按钮
    {
    	System.exit(0);
    }
  }
  
  

}