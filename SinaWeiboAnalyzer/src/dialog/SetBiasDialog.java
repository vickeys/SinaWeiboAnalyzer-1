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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import root.AnalyzerWindow;

public class SetBiasDialog extends Dialog {
	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;

	private Text yellow_alarm_limit_local, red_alarm_limit_local;
	private AnalyzerWindow analyzerWindow;

	public SetBiasDialog(Shell parentShell, AnalyzerWindow analyzerWindow) {
		super(parentShell);
		this.analyzerWindow = analyzerWindow;
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		Label winLabel = new Label(comp, SWT.CENTER);
		winLabel.setText("设置预警阈值(大于0的整数)");
		Display disp = Display.getCurrent();
		FontData fontdata = new FontData("Arial", 11, SWT.BOLD);
		Font font = new Font(disp, fontdata);
		winLabel.setFont(font);

		Group c = new Group(comp, SWT.SHADOW_ETCHED_IN);
		c.setLayout(new GridLayout(2, false));

		Label yellow_label = new Label(c, SWT.LEFT);
		yellow_label.setText("黄色预警阈值：");
		// spaceAnno.setBounds(0, 18, 55, 15);
		yellow_alarm_limit_local = new Text(c, SWT.SINGLE);

		Label red_label = new Label(c, SWT.LEFT);
		red_label.setText("红色预警阈值：");
		red_alarm_limit_local = new Text(c, SWT.SINGLE);

		return comp;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, OK, "确定", false);
		createButton(parent, CANCEL, "取消", false);
		createButton(parent, RESET_ID, "重设", false);
	}

	protected void buttonPressed(int buttonId) {
		// System.out.println(buttonId);
		if (buttonId == OK) // 如果点击了确定按钮，先检查是否为合法路径
		{
			String yellowValueString = yellow_alarm_limit_local.getText();
			String redValueString = red_alarm_limit_local.getText();
			try {
				int yellowValue = Integer.parseInt(yellowValueString);
				int redValue = Integer.parseInt(redValueString);
				if (yellowValue <= 0 || redValue <= 0)
					throw new Exception();
				if (yellowValue >= redValue) {
					MessageBox m = new MessageBox(new Shell(
							Display.getCurrent()), SWT.NO);
					m.setText("错误");
					m.setMessage("红色预警阈值必须大于黄色预警阈值！");
					m.open();
				} else {
					// 刷新主界面中微博预警表格
					analyzerWindow.resetAlarmBias(yellowValue, redValue);
					super.buttonPressed(buttonId);
				}

			} catch (Exception e) {
				e.printStackTrace();
				MessageBox m = new MessageBox(new Shell(Display.getCurrent()),
						SWT.NO);
				m.setText("错误");
				m.setMessage("预警值必须是大于0的整数！");
				m.open();
				
			}

		} else if (buttonId == RESET_ID) // 如果是重设按钮
		{
			yellow_alarm_limit_local.setText("");
			red_alarm_limit_local.setText("");
		} else
			// 取消按钮
			super.buttonPressed(buttonId);
	}

}