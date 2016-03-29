/*执行情感计算
先检查有无创建项目，从项目文件夹中保存XML的文件夹中获取每个XML文件，计算处理后输出到一个XML文件
*/


package weiboAnalyzer.actions;

import java.io.File;

import help.PathManager;
import help.WeiboConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import root.AnalyzerWindow;
import dialog.ComputeSentimentScoreDialog;

public class ComputeSentimentScoreAction extends Action {
	//private PathManager workspace, projDirectory;
	private AnalyzerWindow analyzerWindow;

	public ComputeSentimentScoreAction() {
		super();
		setText("情感计算与分析");
		setToolTipText("情感计算与分析");
		setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(),
				"comp2.png"));
	}
	

	public void setAnalyzerWindow(AnalyzerWindow analyzerWindow) {
		this.analyzerWindow = analyzerWindow;
	}


/*	public void setWorkspace(PathManager workspace) {
		this.workspace = workspace;
	}

	public void setProjDirectory(PathManager projDirectory) {
		this.projDirectory = projDirectory;
	}*/

	public void run() {
		if (analyzerWindow.getProjDirectory().getPath() == null) {
			MessageBox m = new MessageBox(new Shell(Display.getCurrent()),
					SWT.NO);
			m.setText("错误");
			m.setMessage("情感计算前必须创建项目并抓取微博！");
			m.open();
		} else {
			String srcPath = analyzerWindow.getWorkspace().getPath() + analyzerWindow.getProjDirectory().getPath()
					+ WeiboConstants.WEIBO_SRC_DIR;      //抓取到的微博所在路径
			System.out.println(srcPath);               //测试语句
			File srcDir = new File(srcPath);
			String xmlFiles[] = srcDir.list();
			if (xmlFiles.length == 0) {
				MessageBox m = new MessageBox(new Shell(Display.getCurrent()),
						SWT.NO);
				m.setText("错误");
				m.setMessage("情感计算前必须抓取微博！");
				m.open();
			}
			else{
				ComputeSentimentScoreDialog dialog = new ComputeSentimentScoreDialog(new Shell(
						Display.getCurrent()),analyzerWindow);
				int openValue = dialog.open();

			}

		}

	}
}
