
/*把比赛用的文本数据和标注文本转换为一个XML文件*/

package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import dialog.Txt2XmlDialog;
import root.AnalyzerWindow;

public class TransformDataAction extends Action{
	private AnalyzerWindow analyzerWindow;
	public TransformDataAction(){
		super();
		setText("比赛数据转换为XML");
	}
	
	public void run(){
		System.out.println("TransformDataAction输出，该类作用是比赛数据转换为XML");    //   测试语句
		if (analyzerWindow.getProjDirectory().getPath() == null) {
	    	  MessageBox m = new MessageBox(new Shell(Display.getCurrent()),SWT.NO);
	    	  m.setText("错误");
	    	  m.setMessage("抓取微博前必须创建项目！");
	    	  m.open();

		} else {
		Txt2XmlDialog t = new Txt2XmlDialog(getAnalyzerWindow());		
		t.open();
		}
	}
	public void setAnalyzerWindow(AnalyzerWindow analyzerWindow) {
		this.analyzerWindow = analyzerWindow;
	}
	public AnalyzerWindow getAnalyzerWindow() {
		return analyzerWindow;
	}

}
