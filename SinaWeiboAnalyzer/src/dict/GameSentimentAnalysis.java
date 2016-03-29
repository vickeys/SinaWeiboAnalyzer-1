package dict;

import help.WeiboConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import dialog.ComputeSentimentScoreDialog;
import dialog.GameSentimentScoreDialog;

public class GameSentimentAnalysis {
	List<Element> weibos;
	private GameSentimentScoreDialog cssd;
	public List<Element> getWeibos() {
		return weibos;
	}

	String pathname;
	String outpath;
	boolean onlyNeg;

	public GameSentimentAnalysis(GameSentimentScoreDialog cssd,String pathname, String outpath) {
		super();
		this.cssd=cssd;
		this.pathname = pathname;
		this.outpath = outpath;
	}


	public void setOnlyNeg(boolean onlyNeg) {
		this.onlyNeg = onlyNeg;
	}


	public void analysis() throws Exception {

		/* ................................................................ */
		String argu = "";
		String system_charset = "UTF-8";
		int charset_type = 1;
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！原因是： " + nativeBytes);
			return;
		}
	
		/* ................................................................ */
		Map dictionary = Dictionary.getDictionary("nlpir/newWords.xlsx");
		Element outroot = new Element("WeiboList");
		Document Doc = new Document(outroot);
		Element elements;
		/* ................................................................ */
		SAXBuilder builder = new SAXBuilder();
		int count = 0;
		double loseRate=0;//丢失率
		double mistakeRate=0;//出错率
		double markFalseNum=0;//标注为负的微博总数
		double markFalseandScoreFalse=0;//标注为负且分值为负的总数
		double afterCompMinusNum=0;//计算后分值小于0的总数
		File f = new File(pathname);
		File files[] = f.listFiles();
		long startMili=System.currentTimeMillis();// 计时开始
		int fileNum=0;
	
		for ( fileNum = 0; fileNum < files.length; fileNum++) {
			if (files[fileNum].isFile()
					&& files[fileNum].getName().endsWith(".xml")) {
				Document document = builder.build(files[fileNum]);// 获得文档对象
				Element root = document.getRootElement();// 获得根节点
				List<Element> content = XPath.selectNodes(root, "//WeiboList/微博/内容");
				List<Element> mark = XPath.selectNodes(root, "//WeiboList/微博/标注");
				for (int i = 0; i < content.size(); i++) {
					String sInput = content.get(i).getText();
					nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(
							sInput, 1);
					int nCountKey = 0;
					String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(
							sInput, 30, true);
					StringTokenizer st = new StringTokenizer(nativeByte, "#");
					int score=0;
					int scoreOne = 0;// 只计算负面词
					int scoreTwo = 0;// 只计算正面词
					int scoreThree =0; //纯正面词
					StringBuffer sb = new StringBuffer();
					while (st.hasMoreTokens()) {
						String token = st.nextToken().trim();
						String[] arr = token.split("/");
						if (dictionary.containsKey(arr[0])) {
							// System.out.print(((SeWord)dictionary.get(arr[0])).toString());
							// System.out.print("出现次数 "+arr[3]+"||");
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
					elements.addContent(new Element("内容").setText(content
							.get(i).getText()));
					elements.addContent(new Element("标注").setText(mark
							.get(i).getText()));
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
					if (onlyNeg) {
						score=scoreOne;
					}else {
						score=scoreTwo;
					}
					if(mark.get(i).getText().equals(WeiboConstants.WORD_FOR_NEGATIVE)){
						markFalseNum++;//标注为负的微博总数
						if (score<0){
							System.out.println("weibo"+sInput);
							markFalseandScoreFalse++;
						}
					}
					if (score<0) {
						afterCompMinusNum++;
					}
					
					count++;
				}
			}
			moveProgressBar(fileNum);
			intsertConsoleText("处理完"+(fileNum+1)+"个XML文件\n");
		}
		long endMili=System.currentTimeMillis();//计时结束
		loseRate=1-(markFalseandScoreFalse/markFalseNum);
		mistakeRate=1-(markFalseandScoreFalse/afterCompMinusNum);
		intsertConsoleText("\n------------------------------\n");
		intsertConsoleText("共计处理"+(fileNum)+"个XML文件\n");
		intsertConsoleText("共计处理"+(count)+"条微博文本\n");
		intsertConsoleText("总消耗时间为"+(endMili-startMili)+"毫秒\n");
		intsertConsoleText("丢失率为"+(String.format("%.2f", loseRate*100))+"%\n");
		intsertConsoleText("出错率为"+(String.format("%.2f", mistakeRate*100))+"%\n");
		XMLOutputter XMLOut = new XMLOutputter();
		// 输出 weiboData.xml 文件；
		XMLOut.output(Doc, new FileOutputStream(outpath));
		CLibrary.Instance.NLPIR_Exit();
		File file= new File(outpath);
		Document document = builder.build(file);// 获得文档对象
		Element root = document.getRootElement();// 获得根节点
		List<Element> weibos = XPath.selectNodes(root, "//微博");
		
		this.weibos = weibos;

	}
	/**
     * 显示进度条
     * @param progress
     */
	
   private void moveProgressBar(int progress){
        cssd.getDisp().asyncExec(new Runnable() {
            
            @Override
            public void run() {
                cssd.getpBar().setSelection(progress+1);
                
            }
        });
    }    
	/*
    * 文本插入
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