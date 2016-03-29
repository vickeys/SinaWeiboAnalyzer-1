/*
 * 用于打开项目并设置主界面中的项目名称和项目路径
 * 
 * */

package dialog;
import help.PathManager;
import help.WeiboConstants;
import java.io.File;
import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import root.AnalyzerWindow;

public class OpenProjectDialog extends Dialog {
	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;
	private Combo existedProj;
	private Text proj_name, proj_path;  // 保存从主界面传来的文本框
	private PathManager workspace, projDirectory;
	TableViewer viewer;
	AnalyzerWindow analyzerWindow;

	public OpenProjectDialog(Shell parentShell, AnalyzerWindow analyzerWindow) {
		super(parentShell);
		this.analyzerWindow = analyzerWindow;
		this.workspace = analyzerWindow.getWorkspace();
		this.projDirectory = analyzerWindow.getProjDirectory();
		this.proj_name = analyzerWindow.getProj_name();
		this.proj_path = analyzerWindow.getProj_path();
		this.viewer = analyzerWindow.getViewer();
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		Label winLabel = new Label(comp, SWT.CENTER);
		winLabel.setText("选择已创建的项目");
		Display disp = Display.getCurrent();
		FontData fontdata = new FontData("Arial", 11, SWT.BOLD);
		Font font = new Font(disp, fontdata);
		winLabel.setFont(font);
		Group c = new Group(comp, SWT.SHADOW_ETCHED_IN);
		Label spaceAnno = new Label(c, SWT.LEFT);
		spaceAnno.setText("可选项目：");
		spaceAnno.setBounds(0, 18, 55, 15);
		existedProj = new Combo(c, SWT.READ_ONLY);
		existedProj.setBounds(60, 15, 200, 20);
		// System.out.println("Testing from OpenProjectDialog :"+workspace.getPath());
		// //测试语句
		File workspaceDir = new File(workspace.getPath());
		String projDir[] = workspaceDir.list();
		for (int i = 0; i < projDir.length; i++) {
			existedProj.add(projDir[i]);

			// System.out.println("Testing from OpenProjectDialog :"+projDir[i]);
			// //测试语句
		}
		return comp;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, OK, "确定", false);
		createButton(parent, CANCEL, "取消", false);
		// createButton(parent, RESET_ID, "重设", false);
	}

	protected void buttonPressed(int buttonId) {
		// System.out.println(buttonId); //测试语句
		if (buttonId == OK) // 如果点击了确定按钮，先检查是否为合法路径
		{
			projDirectory.setPath("\\" + existedProj.getText() + "\\");
			proj_name.setText(existedProj.getText()); // 设置主界面中的项目名称
			proj_path.setText(workspace.getPath() + "\\"
					+ existedProj.getText()); // 设置主界面中的项目路径

/*			System.out.println("Testing from OpenProjectDialog123 :"
					+ workspace.getPath() + projDirectory.getPath()
					+ WeiboConstants.OBJ_XML_FILE+"END"); // 测试语句
*/
			File xmlFile = new File(workspace.getPath() + projDirectory.getPath()+ WeiboConstants.OBJ_XML_FILE);
			//File file = new File("C:\\eclipse_plugIns\\workspace\\writeXml\\weiboData.xml");
		
			List<Element> weibos = null;
			if (xmlFile.exists()) {
			//	System.out.println("Testing !!!!");
				System.out.println(xmlFile.getName());
				try {
					SAXBuilder builder = new SAXBuilder();
					Document document = builder.build(xmlFile);// 获得文档对象
					Element root = document.getRootElement();// 获得根节点
					weibos = XPath.selectNodes(root,"//WeiboList/微博");///账号		
				/*	for (int i = 0; i < weibos.size(); i++) {
						System.out.println("用户名："+weibos.get(i).getChildText("账号")+ "\n"+
                                           "内容：" +weibos.get(i).getChildText("内容")+ "\n"+
                                           "转发数:" +weibos.get(i).getChildText("转发"));
					}*/
				} catch (Exception e) {
					System.out.println("XML 解析异常！");
				}
			}
			//清空主界面关键字文本框和微博文本框
        	analyzerWindow.getKeyWord_text().setText("");
        	analyzerWindow.getWeibo_text().setText("");
			// 向主界面的微博预警表中填写数据
			analyzerWindow.callInput(weibos);
			super.buttonPressed(buttonId);
		} else
			// 取消按钮
			super.buttonPressed(buttonId);
	}

}