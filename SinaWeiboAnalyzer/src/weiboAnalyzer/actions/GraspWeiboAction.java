/*执行抓取微博
先检查有无创建项目，然后通过对话框GraspWeiboDialog设置抓取参数，把抓取到的XML文件集中放入项目文件夹下的一个专门文件夹中
*/
package weiboAnalyzer.actions;
import help.PathManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import dialog.GraspWeiboDialog;

public class GraspWeiboAction extends Action {
	private PathManager workspace, projDirectory;

	public GraspWeiboAction() {
		super();
		setText("抓取微博");
		setToolTipText("抓取微博");
		setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(),
				"grasp20.png"));
	}

	public void setWorkspace(PathManager workspace) {
		this.workspace = workspace;
	}

	public void setProjDirectory(PathManager projDirectory) {
		this.projDirectory = projDirectory;
	}

	public void run() {
		if (projDirectory.getPath() == null) {
	    	  MessageBox m = new MessageBox(new Shell(Display.getCurrent()),SWT.NO);
	    	  m.setText("错误");
	    	  m.setMessage("抓取微博前必须创建项目！");
	    	  m.open();

		} else {
			GraspWeiboDialog dialog = new GraspWeiboDialog(new Shell(
					Display.getCurrent()),workspace, projDirectory);
			int openValue = dialog.open();
			
			//System.out.println("GraspWeiboAction中显示项目路径"+workspace.getPath()+projDirectory.getPath());  //测试语句

		}

	}
}
