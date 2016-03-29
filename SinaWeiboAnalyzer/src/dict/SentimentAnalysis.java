/*  情感分析类
  计算抓取到的所有微博情感分
  */
package dict;
import help.WeiboConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.DocumentHelper;
import org.eclipse.swt.widgets.Display;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.eclipse.swt.widgets.Display;

import dialog.ComputeSentimentScoreDialog;


public class SentimentAnalysis {
	List<Element> weibos;
	private ComputeSentimentScoreDialog cssd;
	public List<Element> getWeibos() {
		return weibos;
	}

	String pathname;
	String outpath;
	boolean onlyNeg;  //是否只计算负面情感词

	public SentimentAnalysis(ComputeSentimentScoreDialog cssd,String pathname, String outpath) {
		super();
		this.cssd=cssd;
		this.pathname = pathname;
		this.outpath = outpath;
	}

/*这个方法用于返回是否只计算负面情感词*/
	public void setOnlyNeg(boolean onlyNeg) {
		this.onlyNeg = onlyNeg;
	}

	/*这个方法用于微博文本情感分析计算*/
	public void analysis() throws Exception {
		

		/* ..................加载分词工具.............................. */
		String argu = "";
		String system_charset = "UTF-8";
		int charset_type = 1;
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			//System.err.println("初始化失败！原因是： " + nativeBytes);
			intsertConsoleText("\n分词工具初始化失败！\n请检查许可证是否到期 ");
			return;
		}
	
		/* ...................创建情感词典................................. */
		Map dictionary = Dictionary.getDictionary("nlpir/newWords.xlsx");

		/* ....................情感计算................................ */
		Element outroot = new Element("WeiboList");
		Document Doc = new Document(outroot);
		Element elements;
		SAXBuilder builder = new SAXBuilder();
		int count = 0;
		File f = new File(pathname);
		File files[] = f.listFiles();
		long startMili=System.currentTimeMillis();// 计时开始
		int fileNum=0;
		//每次循环处理一个XML文件内所有微博
		for ( fileNum = 0; fileNum < files.length; fileNum++) {
			if (files[fileNum].isFile()
					&& files[fileNum].getName().endsWith(".xml")) {
				Document document = builder.build(files[fileNum]);// 获得文档对象
				Element root = document.getRootElement();// 获得根节点
				List<Element> account = XPath.selectNodes(root, "//微博/item/账号");
				List<Element> content = XPath.selectNodes(root, "//微博/item/内容");
				List<Element> repeat = XPath.selectNodes(root, "//微博/item/转发");
				List<Element> comment = XPath.selectNodes(root, "//微博/item/评论");
				List<Element> priase = XPath.selectNodes(root, "//微博/item/点赞");
				List<Element> times = XPath.selectNodes(root, "//微博/item/时间");
				//每次循环处理一个XML文件内一条微博
				for (int i = 0; i < account.size(); i++) {
					if (repeat.get(i).getText().equals(""))
						repeat.get(i).setText("0");
					if (comment.get(i).getText().equals(""))
						comment.get(i).setText("0");
					if (priase.get(i).getText().equals(""))
						priase.get(i).setText("0");
					String sInput = content.get(i).getText();
					nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(
							sInput, 1);
					int nCountKey = 0;
					String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(
							sInput, 30, true);   //提取关键字
					StringTokenizer st = new StringTokenizer(nativeByte, "#");
					int scoreOne = 0;// 只计算负面词
					int scoreTwo = 0;// 只计算正面词
					int scoreThree =0; //纯正面词
					StringBuffer sb = new StringBuffer();
					//每次循环处理一个关键字，根据关键字查询情感词典获得极性和得分
					while (st.hasMoreTokens()) {
						String token = st.nextToken().trim();
						String[] arr = token.split("/");
						if (dictionary.containsKey(arr[0])) {
							int keyNum = Integer.parseInt(arr[3]);
							int polar = ((SeWord) dictionary.get(arr[0]))
									.getPolar();
							int strength = ((SeWord) dictionary.get(arr[0]))
									.getStrength();
							int strengthed = strength * keyNum;
							sb.append(arr[0]);
							sb.append("[");
							if (polar == 2) {
								scoreOne -= strengthed;
								scoreTwo -= strengthed;
								if (keyNum == 1)
									sb.append(-strength);
								else {
									sb.append(-strength);
									sb.append("*");
									sb.append(keyNum);
								}
							} else if (polar == 1) {
								scoreTwo += strengthed;
								scoreThree+= strengthed;
								if (keyNum == 1){
									sb.append(strength);
								}
								else {
									sb.append(strength);
									sb.append("*");
									sb.append(keyNum);
								}
							}
							sb.append("]");
							sb.append("  ,   ");
						}
					}
					/******* 生成总的xml文件 ***********/
					elements = new Element("微博");
					elements.addContent(new Element("账号").setText(account
							.get(i).getText()));
					elements.addContent(new Element("内容").setText(content
							.get(i).getText()));
					elements.addContent(new Element("转发").setText(repeat.get(i)
							.getText()));
					elements.addContent(new Element("评论").setText(comment
							.get(i).getText()));
					elements.addContent(new Element("点赞").setText(priase.get(i)
							.getText()));
					elements.addContent(new Element("时间").setText(times.get(i)
							.getText()));
					if (onlyNeg) {
						if(scoreOne==0)
							elements.addContent(new Element("分值").setText(scoreThree
									+ ""));
						else 
						    elements.addContent(new Element("分值").setText(scoreOne
								    + "")); // 根据单选按钮生成得分数据
					} else {
						elements.addContent(new Element("分值").setText(scoreTwo
								+ ""));
					}
					elements.addContent(new Element("关键字").setText(sb
							.toString()));
					outroot.addContent(elements);
					
					count++;
				}
			}
			moveProgressBar(fileNum);
			intsertConsoleText("处理完"+(fileNum+1)+"个XML文件\n");
		}
		long endMili=System.currentTimeMillis();//计时结束
		intsertConsoleText("\n------------------------------\n");
		intsertConsoleText("共计处理"+(fileNum)+"个XML文件\n");
		intsertConsoleText("共计处理"+(count)+"条微博文本\n");
		intsertConsoleText("总消耗时间为"+(endMili-startMili)+"毫秒\n");
		XMLOutputter XMLOut = new XMLOutputter();
		XMLOut.output(Doc, new FileOutputStream(outpath));
		CLibrary.Instance.NLPIR_Exit();
		File file= new File(outpath);
		Document document = builder.build(file);// 获得文档对象
		Element root = document.getRootElement();// 获得根节点
		List<Element> weibos = XPath.selectNodes(root, "//微博");
		
		this.weibos = weibos;

	}
	/**
     * 用于显示对话框进度条
     * @param progress
     */
	
   private void moveProgressBar(int progress){
        cssd.getDisp().asyncExec(new Runnable() {
            
            @Override
            public void run() {
            	//if(cssd.getpBar().isDisposed()) return ;
                cssd.getpBar().setSelection(progress);
                
            }
        });
    } 
   
    /**
    * 用于计算情感的过程中显示动态信息
    * @param str
    */
   private void intsertConsoleText(final String str) {
	   cssd.getDisp().syncExec(new Runnable() {
           
           @Override
           public void run() {
               cssd.getConsoleText().insert(str);
               
           }
       });
   }
}