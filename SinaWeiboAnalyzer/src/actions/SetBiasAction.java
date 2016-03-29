/*执行新建项目
通过对话框获取项目名称，连同工作空间路径，创建项目文件夹，并在其中创建weiboSrc文件夹
*/

package actions;

import java.io.File;

import help.PathManager;
import help.WeiboConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import root.AnalyzerWindow;
import dialog.NewProjectDialog;
import dialog.SetBiasDialog;

public class SetBiasAction extends Action{
	private AnalyzerWindow analyzerWindow;
	public SetBiasAction() {
		super();
		setText("设置预警阈值");
	}

	public void setAnalyzerWindow(AnalyzerWindow analyzerWindow) {
		this.analyzerWindow = analyzerWindow;
	}

	public void run(){
		SetBiasDialog dialog = new SetBiasDialog(new Shell(Display.getCurrent()),analyzerWindow);
		int openValue = dialog.open();
		//System.out.println("Class NewProjectAction get directory:"+analyzerWindow.getProjDirectory().getPath());     //测试语句
		//System.out.println("Class NewProjectAction get workspace:"+analyzerWindow.getWorkspace().getPath());    //测试语句
/*		String absPath = analyzerWindow.getWorkspace().getPath()+analyzerWindow.getProjDirectory().getPath(); 
		File projDir = new File(absPath);
		if(!projDir.exists())
		    projDir.mkdir();
		File weiboSrcDir = new File(absPath+WeiboConstants.WEIBO_SRC_DIR);
		if(!weiboSrcDir.exists())
			weiboSrcDir.mkdir();*/
	}

}
