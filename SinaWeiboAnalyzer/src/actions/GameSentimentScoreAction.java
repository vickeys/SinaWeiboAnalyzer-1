
/*计算比赛文本情感，然后与标注的情感比较，计算情感分析的准确率*/

package actions;

import help.WeiboConstants;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import root.AnalyzerWindow;
import dialog.ComputeSentimentScoreDialog;
import dialog.GameSentimentScoreDialog;

public class GameSentimentScoreAction extends Action{
	private AnalyzerWindow analyzerWindow;
	public GameSentimentScoreAction(){
		super();
		setText("比赛文本情感计算");
	}
	public void setAnalyzerWindow(AnalyzerWindow analyzerWindow) {
		this.analyzerWindow = analyzerWindow;
	}
	
	public void run(){
		System.out.println("GameSentimentScoreAction输出，该类作用是计算比赛文本情感，然后与标注的情感比较，计算情感分析的准确率");    //   测试语句
		if (analyzerWindow.getProjDirectory().getPath() == null) {
			MessageBox m = new MessageBox(new Shell(Display.getCurrent()),
					SWT.NO);
			m.setText("错误");
			m.setMessage("请先创建比赛数据文件夹并将比赛数据转化为XML文件");
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
				m.setMessage("请将赛数据转化为XML文件");
				m.open();
			}
			else{
				GameSentimentScoreDialog dialog = new GameSentimentScoreDialog(new Shell(
						Display.getCurrent()),analyzerWindow);
				int openValue = dialog.open();

			}

		}
	}

}
