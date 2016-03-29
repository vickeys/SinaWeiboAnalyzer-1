package root;
import org.eclipse.swt.widgets.Display;
public class WeiboAnalyzer {
	public static void main(String args[]) {
		try {
			AnalyzerWindow window = new AnalyzerWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
