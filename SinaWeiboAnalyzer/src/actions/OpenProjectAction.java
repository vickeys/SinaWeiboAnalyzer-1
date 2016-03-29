package actions;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


import root.AnalyzerWindow;
import dialog.OpenProjectDialog;

public class OpenProjectAction extends Action{
	
	AnalyzerWindow analyzerWindow;

	public OpenProjectAction() {
		super();
		// TODO Auto-generated constructor stub
		setText("打开项目");
		setToolTipText("打开项目");
	    setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(),"openPro20.png"));
		
	}

	public void setAnalyzerWindow(AnalyzerWindow analyzerWindow) {
		this.analyzerWindow = analyzerWindow;
		
	}

	public void run(){
		//System.out.println("Testing from OpenProjectAction :"+workspace.getPath()); //测试语句
		OpenProjectDialog dialog = new OpenProjectDialog(new Shell(Display.getCurrent()),analyzerWindow);
		int openValue = dialog.open();
		
		
	}

}
