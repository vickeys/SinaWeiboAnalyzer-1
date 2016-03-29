
/*
 * NewProjectDialog类用于项目创建时执行初始化操作
 * 
 * */

package dialog;

import help.PathManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.*;

import root.AnalyzerWindow;

public class NewProjectDialog extends Dialog
{
  private static final int RESET_ID = 
                  IDialogConstants.NO_TO_ALL_ID + 1;
  
  private Text projName_local;
  private Text proj_name, proj_path;  //保存从主界面传来的文本框
  private PathManager workspace,projDirectory;
  AnalyzerWindow analyzerWindow;
  
  public NewProjectDialog(Shell parentShell,AnalyzerWindow analyzerWindow)
  {
    super(parentShell);
	this.analyzerWindow = analyzerWindow;
	this.proj_name = analyzerWindow.getProj_name();
	this.proj_path = analyzerWindow.getProj_path();
	this.workspace = analyzerWindow.getWorkspace();
	this.projDirectory = analyzerWindow.getProjDirectory();
  }

  protected Control createDialogArea(Composite parent)
  {
    Composite comp = (Composite)super.createDialogArea(parent);
    Label winLabel = new Label(comp, SWT.CENTER);
    winLabel.setText("新建项目");
    Display disp = Display.getCurrent();
    FontData fontdata = new FontData("Arial", 11, SWT.BOLD);
    Font font = new Font(disp,fontdata);
    winLabel.setFont(font);
    
    Group c = new Group(comp,SWT.SHADOW_ETCHED_IN);
    
    Label spaceAnno = new Label(c, SWT.LEFT);
    spaceAnno.setText("项目名称：");
    spaceAnno.setBounds(0, 18, 55, 15);
    projName_local = new Text(c, SWT.SINGLE);
    projName_local.setBounds(60, 15, 200, 20);
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    projName_local.setText(sf.format(new Date()));

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
	  //System.out.println(buttonId);
    if(buttonId == OK)    //如果点击了确定按钮，先检查是否为合法路径
    {
        
        if(projName_local.getText().trim().equals("")){
        	MessageBox m = new MessageBox(new Shell(Display.getCurrent()),SWT.NO);
      	  m.setText("错误");
      	  m.setMessage("请输入合法的路径！");
      	  m.open();
        }
        else{
        	projDirectory.setPath("\\"+projName_local.getText()+"\\");
        	proj_name.setText(projName_local.getText());       //设置主界面中的项目名称
        	proj_path.setText(workspace.getPath()+"\\"+projName_local.getText());  //设置主界面中的项目路径
        	//System.out.println(projDirectory.getPath());   //测试语句
        	analyzerWindow.getKeyWord_text().setText("");
        	analyzerWindow.getWeibo_text().setText("");
        	analyzerWindow.callInput(null);
        	super.buttonPressed(buttonId);
        }
        

    }
    else if(buttonId == RESET_ID)      //如果是重设按钮
    	projName_local.setText("");
    else             //取消按钮
      super.buttonPressed(buttonId);
  }
  
  

}