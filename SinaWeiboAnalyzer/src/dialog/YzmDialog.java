/*
 * 这个类用于显示验证码提示框
 */

package dialog;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageReader;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.gargoylesoftware.htmlunit.html.HtmlImage;

public class YzmDialog extends Dialog {

	private String yzmcode;
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
	 * @throws IOException 
	 */
	public String open(HtmlImage yzmImage) throws IOException {
		createContents();
		
		//获取屏幕高度和宽度
        int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
        int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
        //获取对象窗口高度和宽度
        int shellH = shell.getBounds().height;
        int shellW = shell.getBounds().width;        
        //定位对象窗口坐标
        shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 3));
	
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		//显示验证码图片
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(20, 10, 100, 30);
		ImageReader imageReader = yzmImage.getImageReader();
	    yzmImage.getImageReader().getStreamMetadata();
        BufferedImage bufferedImage = imageReader.read(0);     
        lblNewLabel.setImage(new Image(display,  convertToSWT(bufferedImage)));

		
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
		shell.setDefaultButton(btnNewButton); //回车键确定
		
		button.setBounds(134, 46, 80, 27);
		button.setText("\u53D6 \u6D88");
		

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return yzmcode;
		
	}
	//AWT image 转换为SWT image
	private static ImageData convertToSWT(BufferedImage bufferedImage) {
		  if (bufferedImage.getColorModel() instanceof DirectColorModel) {
		   DirectColorModel colorModel = (DirectColorModel) bufferedImage
		     .getColorModel();
		   PaletteData palette = new PaletteData(colorModel.getRedMask(),
		     colorModel.getGreenMask(), colorModel.getBlueMask());
		   ImageData data = new ImageData(bufferedImage.getWidth(),
		     bufferedImage.getHeight(), colorModel.getPixelSize(),
		     palette);
		   WritableRaster raster = bufferedImage.getRaster();
		   int[] pixelArray = new int[3];
		   for (int y = 0; y < data.height; y++) {
		    for (int x = 0; x < data.width; x++) {
		     raster.getPixel(x, y, pixelArray);
		     int pixel = palette.getPixel(new RGB(pixelArray[0],
		       pixelArray[1], pixelArray[2]));
		     data.setPixel(x, y, pixel);
		    }
		   }
		   return data;
		  } else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
		   IndexColorModel colorModel = (IndexColorModel) bufferedImage
		     .getColorModel();
		   int size = colorModel.getMapSize();
		   byte[] reds = new byte[size];
		   byte[] greens = new byte[size];
		   byte[] blues = new byte[size];
		   colorModel.getReds(reds);
		   colorModel.getGreens(greens);
		   colorModel.getBlues(blues);
		   RGB[] rgbs = new RGB[size];
		   for (int i = 0; i < rgbs.length; i++) {
		    rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
		      blues[i] & 0xFF);
		   }
		   PaletteData palette = new PaletteData(rgbs);
		   ImageData data = new ImageData(bufferedImage.getWidth(),
		     bufferedImage.getHeight(), colorModel.getPixelSize(),
		     palette);
		   data.transparentPixel = colorModel.getTransparentPixel();
		   WritableRaster raster = bufferedImage.getRaster();
		   int[] pixelArray = new int[1];
		   for (int y = 0; y < data.height; y++) {
		    for (int x = 0; x < data.width; x++) {
		     raster.getPixel(x, y, pixelArray);
		     data.setPixel(x, y, pixelArray[0]);
		    }
		   }
		   return data;
		  }
		  return null;
		 }

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(240, 110);
		shell.setText(getText());

	}
	
/*	public static void main(String args[]){
		
		  YzmDialog y= new YzmDialog(new Shell(new Display()), SWT.CLOSE);
		  y.open("");
	}*/
}
