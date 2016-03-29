/*
 *比赛专用类，txt文件转换为xml文件
 */
package grasp;

import help.WeiboConstants;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



public class Txt2Xml {


	//输出xml文件
	public static String outputXml(List<String> content,List<String> mark,String path){
		String news = null;			
	//	List<String> content = txt2xml.readFile("D:\\a.txt");
	//	List<String> mark = txt2xml.readMark("D:\\b.txt");

		System.out.println(content.size());
		System.out.println(mark.size());
		if(mark.size()==content.size()){
			Element root = new Element("WeiboList");			
		for(int i=0;i<content.size();i++){
			Element elements = new Element("微博");
			elements.addContent(new Element("内容").setText(content.get(i)));			
			elements.addContent(new Element("标注").setText(mark.get(i)));
			root.addContent(elements);
		}		
		Document Doc = new Document(root);
		Format format = Format.getPrettyFormat();
		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			XMLOut.output(Doc, new FileOutputStream(path+"Game-Weibo.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  news = "输出XML成功！共计"+mark.size()+"条微博。";
///		 System.out.println("输出XML成功！");
		
		}else{	
			news = "微博内容和标注数量不一致！";
///			System.out.println("微博内容和标注数量不一致！");
			}
		return news; //返回消息；		
	}
	//获取txt中的微博内容
	public static List<String> readFile(String filename){
		  List<String> filecon = new ArrayList<String>();
		  String line ;
		  BufferedReader file = null;
		  try{
		   file = new BufferedReader(new FileReader(filename));
		   while ((line = file.readLine()) != null) {
		    if (!line.equals("")) // 不需要读取空行
		    {
		     filecon.add(line);
	//	     System.out.println(line);
		    }
		   }
		   file.close();
		  }catch(IOException e){
		   e.printStackTrace();
		  }		   
		  return filecon;
		 }
	//获取txt中的标注
	public static List<String> readMark(String filename){
		  List<String> filecon = new ArrayList<String>();
		  String line ;
		  BufferedReader file = null;
		  try{
		   file = new BufferedReader(new FileReader(filename));
		   while ((line = file.readLine()) != null) {
		    if (!line.equals("")) // 不需要读取空行
		    {
		    	if(line.equals(WeiboConstants.WORD_FOR_POSITIVE)
		    			||line.equals(WeiboConstants.WORD_FOR_NEUTRAL)
		    			||line.equals(WeiboConstants.WORD_FOR_NEGATIVE))//判断标注
		    	{
		     filecon.add(line);
		   //  System.out.println(line);	
		    }
		    }
		   }
		   file.close();
		  }catch(IOException e){
		   e.printStackTrace();
		  }		   
		  return filecon;
		 }
	
	
}
