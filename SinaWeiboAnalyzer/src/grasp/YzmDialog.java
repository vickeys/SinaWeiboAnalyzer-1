package grasp;


import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class YzmDialog extends Dialog {

	protected String yzmcode;
	protected Shell shell;
	private Text text;
	private Button button;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public YzmDialog(Shell parent, int style) {
		super(parent, style);
		setText("请输入大写验证码");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public void open(String path) {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		//显示验证码图片
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(20, 10, 100, 30);
		lblNewLabel.setImage(new Image(display,path));
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(134, 17, 80, 23);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				yzmcode = text.getText();
				shell.close();
			}
		});
		btnNewButton.setBounds(30, 46, 80, 27);
		btnNewButton.setText("确 定");
		
		button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button.setBounds(134, 46, 80, 27);
		button.setText("\u53D6 \u6D88");
		

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(240, 110);
		shell.setText(getText());

	}
	
	public static void main(String args[]){
		
		  YzmDialog y= new YzmDialog(new Shell(new Display()), SWT.CLOSE);
		  y.open("");
	}
}
