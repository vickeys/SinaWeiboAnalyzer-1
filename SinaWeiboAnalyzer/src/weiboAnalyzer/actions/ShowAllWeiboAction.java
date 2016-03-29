/*动作类，调用对话框ShowAllWeiboDialog，切换显示所有微博还是仅显示负面微博
*/

package weiboAnalyzer.actions;

import java.io.File;

import help.PathManager;
import help.WeiboConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import root.AnalyzerWindow;
import dialog.NewProjectDialog;
import dialog.OpenProjectDialog;
import dialog.ShowAllWeiboDialog;

public class ShowAllWeiboAction extends Action{
	AnalyzerWindow analyzerWindow;
	

	public ShowAllWeiboAction() {
		super();
		setText("显示所有微博/仅显示负面微博");
		
	}

	public void setAnalyzerWindow(AnalyzerWindow analyzerWindow) {
		this.analyzerWindow = analyzerWindow;
	}

	public void run(){
		ShowAllWeiboDialog dialog = new ShowAllWeiboDialog(new Shell(Display.getCurrent()),analyzerWindow);
		int openValue = dialog.open();

	}

}
