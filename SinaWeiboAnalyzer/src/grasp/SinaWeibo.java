/* 
 * 微博抓取工具类
 * 需要的参数有微博账号,密码,关键词（可选）,时间（可选）,地址
 * 
 */

package grasp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import dialog.YzmDialog;

public class SinaWeibo {

	protected static int wbcount = 0; // 计数，用于统计微博抓取的总量
	private final static WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24); // 设置默认浏览器为FIREFOX_24

	/** 这个方法用于登录新浪微博，返回登录结果 **/
	protected static String login(String username, String password) {
		
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page;
		try {
			page = webClient.getPage("https://login.weibo.cn/login/");
			
			//XPATH方式，获得账号、密码输入框，由于密码输入框name值为password_xxxx，后四位为随机数，所以采用XPATH方法
			List<?> inputList1 = page
					.getByXPath("/html/body/div[position()=2]/form/div/input[position()=1]");
			HtmlInput input1 = (HtmlInput) inputList1.get(0);
			input1.setValueAttribute(username); // 填入账号

			List<?> inputList2 = page
					.getByXPath("/html/body/div[position()=2]/form/div/input[position()=2]");
			HtmlInput input2 = (HtmlInput) inputList2.get(0);
			input2.setValueAttribute(password); // 填入密码

			// 获取sumbit提交按钮;
			final List<HtmlForm> formList = page.getForms(); // 因为新浪微博form表单没有name值，所以无法用getFormByName("form")方法获得;
			final HtmlForm form = formList.get(0);
			final HtmlSubmitInput button = form.getInputByName("submit");
			HtmlPage newPage = null;
			try {
				newPage = button.click(); // 模拟点击获得新页面
			//	System.out.println(newPage.asText());
			} catch (RuntimeException e) { // 如果产生RuntimeException异常，说明网络不通
				e.printStackTrace();
				return "请确保网络畅通！\n";
			}

			// 用于获得登录后情况，登录后分为：登录成功、登录名或密码错误、请输入验证码三种情况
			List<?> balanceList3 = newPage
					.getByXPath("/html/body/div[position()=2]/text()");
			// System.out.println(balanceList3.get(0).toString());
			if (balanceList3.get(0).toString().trim().equals("登录名或密码错误")) {
				return "微博账号和密码不正确！\n";
			}
			if (balanceList3.get(0).toString().trim().equals("验证码错误")) {
				// 获得验证码的网页地址
				HtmlImage yzmImage=  (HtmlImage)newPage.getElementsByTagName("img").get(0);

				// 创建验证码提示框
				YzmDialog yzmDialog = new YzmDialog(new Shell(new Display()),
						SWT.CLOSE | SWT.ON_TOP);
			
				//进行带验证码的登录并返回登录结果
				return codeLogin(newPage, username, password, yzmDialog.open(yzmImage));
			}
			if (balanceList3.get(0).toString().trim().equals("欢迎访问微博")){
				return "请输入账号密码\n";
			}
			else {
				return "登陆成功！\n";
			}
		} catch (FailingHttpStatusCodeException | IOException e) {
			return "请确保网络畅通！\n";
		}
	}

	/**这个方法用于带验证码登录新浪微博，返回登录结果**/
	private static String codeLogin(HtmlPage page, String username,
			String password, String code) throws IOException {

		// XPATH 方式，获得账号和密码，验证码
		List<?> inputList1 = page
				.getByXPath("/html/body/div[position()=3]/form/div/input[position()=1]");

		HtmlInput input1 = (HtmlInput) inputList1.get(0);
		input1.setValueAttribute(username);

		List<?> inputList2 = page
				.getByXPath("/html/body/div[position()=3]/form/div/input[position()=2]");
		HtmlInput input2 = (HtmlInput) inputList2.get(0);
		input2.setValueAttribute(password);

		List<?> inputList3 = page
				.getByXPath("/html/body/div[position()=3]/form/div/input[position()=3]");
		HtmlInput input3 = (HtmlInput) inputList3.get(0);
		try{
		input3.setValueAttribute(code);
		}catch(NullPointerException e){
			return "请输入验证码！\n";
		}
		List<HtmlForm> formList = page.getForms();
		HtmlForm form = formList.get(0);
		HtmlSubmitInput button = form.getInputByName("submit");
		HtmlPage newPage = null;
		newPage = button.click();// 模拟点击
	//	System.out.println(newPage.asText());

		List<?> balanceList3 = null;
		try {
			balanceList3 = newPage
					.getByXPath("/html/body/div[position()=2]/text()");
		} catch (IndexOutOfBoundsException e) {
			return "请确保网络畅通！\n";
		}  
		if (balanceList3.get(0).toString().trim().equals("验证码错误")
				|| balanceList3.get(0).toString().trim().equals("请输入验证码")) {
			return "验证码错误！请重新登录！\n";
		} else {
			return "登陆成功！\n";
		}

	}
  /**判断是否登录新浪微博账号**/
  protected static boolean whetherLogin() throws Exception{	

		HtmlPage page = webClient.getPage("https://weibo.cn");	
	//	page.refresh();
		List<?> markList = page
				.getByXPath("/html/body/div[position()=2]/div/a/text()");
	//	System.out.println(page.asText());
		if(markList.size()==2)  return false ;
		else return true;

}
	/**构造网址**/
	protected static String URLEncode(String keyword, boolean sort,String starttime,
			String endtime) {
		String strUrl = "http://weibo.cn/search/mblog?hideSearchFrame=&keyword=";
		String strKeyword = null;   // %E8%88%AA%E7%A9%BA+%E6%97%85%E6%B8%B8
		String strTime = null;
		String strSort = null;

		if (keyword.equals("") || keyword.equals(null)) {
			keyword = "民航 旅游";
		}
		try {
			strKeyword = URLEncoder.encode(keyword, "UTF-8");//encode对关键字加密
		} catch (UnsupportedEncodingException e) {	
			e.printStackTrace();
		} //encode对关键字加密
		if (starttime.equals("") || endtime.equals("")) {
			strTime = "&filter=hasori";
		} else {
			strTime = "&advancedfilter=1&hasori=1&starttime=" + starttime
					+ "&endtime=" + endtime;
		}
		if(sort) strSort = "&sort=time&page=";
		else strSort = "&sort=hot&page=";
		String strURL = strUrl + strKeyword + strTime + strSort;
	 //   System.out.println(strURL+"1");
		return strURL;
	}
    /**获取搜索到多少页微博内容**/
	protected static int getPage(String URL){
		// 得到搜索出多少条微博数据
		HtmlPage page = null;
		try {
			page = webClient.getPage(URL + "1");
		} catch (FailingHttpStatusCodeException | IOException e1) {
			e1.printStackTrace();
		}

		List<?> pagelist = null;
		// 这个循环获取共计有多少页
		for (int i = 2; i <= 6; i++) {
			if (page.getByXPath(
					"/html/body/div[position()=" + i + "]/span/text()").size() == 0) {
				continue;
			} else {
				pagelist = page.getByXPath("/html/body/div[position()=" + i
						+ "]/span/text()");
				// / System.out.println(pagelist.get(0));
				break;
			}
		}
		// 正则表达式匹配出数字
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		int n = 100;
		try {
			Matcher m = p.matcher(pagelist.get(0).toString());
			int pagenum = Integer.parseInt(m.replaceAll("").trim());
			if (pagenum >= 1000) {
				n = 100;
			} else {
				if (pagenum % 10 == 0) {
					n = pagenum / 10;
				} else {
					n = (pagenum / 10) + 1;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			n = 100;
			// / System.out.println("未获得多少页,默认抓取100页");
		}
		return n;
	}

	/**输出xml文件**/
	protected static void outputXml(Document doc, String path) throws IOException {
		try {
			Format format = Format.getPrettyFormat();
			XMLOutputter XMLOut = new XMLOutputter(format);
			XMLOut.output(doc, new FileOutputStream(path));
		//	System.out.println("输出XML成功！"+path);
		} catch (FileNotFoundException e) {
		//	System.out.println("指定的路径不存在！");
		}
	}

	/**统一时间格式**/
/*	private static String formatTime(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		
		return df.format(new Date());// new Date()为获取当前系统时间
	}
*/	
	
    /**这个方法用来抓取微博，生成xml文件保存，返回文件名
     * @throws IOException **/
	protected static String catchWeibo(String strUrl, String path, int i){
		
		HtmlPage page3=null;
		try {
			page3 = webClient.getPage(strUrl + i);
		} catch (FailingHttpStatusCodeException | IOException e1) {
			return "请确保网络畅通！";
		}

		List<Integer> listX = new ArrayList<Integer>();
		 //这个循环用于找到有微博内容的DIV行，用list存储
		for (int j = 3; j <= 30; j++) {
			String strX = "/html/body/div[position()=" + j + "]/div";
			if (page3.getByXPath(strX).size() == 1
					|| page3.getByXPath(strX).size() == 2)
				listX.add(j);
		}
		Iterator<Integer> lx = listX.iterator();

		// 创建根节点 并设置它的属性 ;
		Element root = new Element("微博").setAttribute("count",
				String.valueOf(listX.size())); // 这一页有多少条微博
		// if(listX.size()==0) return false; //判断页面是否有微博内容，如果没有就停止抓取;
		// 将根节点添加到文档中;
		while (lx.hasNext()) {
			wbcount++;
			String strX = "/html/body/div[position()=" + lx.next() + "]/div";
			String sbWb = null; // 包含账号微博的部分
			String sbData = null;// 包含数据的部分

			// 不带图片的DIV里只包含1个DIV要根据“ 赞\\[”来切割
			if (page3.getByXPath(strX).size() == 1) {
				HtmlDivision wb = (HtmlDivision) page3.getByXPath(strX).get(0);
				String sss[] = wb.asText().split(" 赞\\[");
				sss[1] = "赞[" + sss[1];
				sbWb = sss[0];
				sbData = sss[1];
			}
			// 带图片的包含2个DIV
			else {
				HtmlDivision wb = (HtmlDivision) page3.getByXPath(strX).get(0);// 账号和内容部分的DIV
				HtmlDivision data = (HtmlDivision) page3.getByXPath(strX)
						.get(1);// 数据部分的DIV
				sbWb = wb.asText();
				sbData = data.asText();
			}
			String w[] = sbWb.split(":", 2); // 切出账号和微博内容
			// System.out.println(w[0].toString());
			// System.out.println(w[1].toString());
			String s[] = sbData.split("收藏"); // 把“数据”切成2段
			// 正则表达式匹配提取数据
			String re1 = ".*?"; // Non-greedy match on filler
			String re2 = "(\\d+)"; // Integer Number 1
			Pattern p = Pattern.compile(re1 + re2 + re1 + re2 + re1 + re2,
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(s[0]); // s[0]
			String zf = null, pl = null, dz = null;
			if (m.find()) {
				dz = m.group(1);
				zf = m.group(2);
				pl = m.group(3);
				// System.out.println(dz);
			}
			String ss[] = s[1].split("来自"); //得到时间
			// System.out.println(ss[0].trim()); // 去掉首位空格
//
			// 调用保存XML
			// 创建节点 item;
			Element elements = new Element("item");
			// 给item节点添加子节点并赋值；
			elements.addContent(new Element("账号").setText(w[0].toString()));
			elements.addContent(new Element("内容").setText(w[1].toString()));
			elements.addContent(new Element("时间").setText(ss[0].trim()
					.toString()));
			elements.addContent(new Element("转发").setText(zf));
			elements.addContent(new Element("评论").setText(pl));
			elements.addContent(new Element("点赞").setText(dz));
			root.addContent(elements);
		}
		Document doc = new Document(root);
		// System.out.println("抓取微博成功！");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		// 调用outputXml()输出XML文件，以系统当前时间用来命名

		String fileName = df.format(new Date()) + "-" + i + ".xml"; // 文件命名
		try {
			outputXml(doc, path + fileName);
		} catch (IOException e) {
			return e.getMessage();
		}

		return fileName;
	}

	/**关闭浏览器**/
	protected static void close() {
	
        webClient.getCookieManager().clearCookies();
		webClient.closeAllWindows(); // 关闭
	}

}
