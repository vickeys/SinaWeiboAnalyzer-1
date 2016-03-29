/*这个类用于创建情感词典
类方法中的参数是一个保存了情感词的excel文件，每个情感词包括词语，强度和极性
类方法的返回值是Map对象，保存了所有情感词，以词语为键，以包括词语，强度和极性的SeWord对象为值
*
*/

package dict;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Dictionary {
	
	public static Map getDictionary(String filename) throws IOException {
		Map<String, SeWord> dictionary = new HashMap<String, SeWord>();
		InputStream stream = new FileInputStream(filename);
		Workbook wb = new XSSFWorkbook(stream);
		Sheet sheet1 = wb.getSheetAt(0);
		int rowNums = sheet1.getPhysicalNumberOfRows();
		SeWord sw = null;
		for(int r = 0; r<rowNums; r++){
			Row row = sheet1.getRow(r);
			String w = row.getCell(0).getStringCellValue().trim();
			int s = (int) row.getCell(1).getNumericCellValue();  
			int p = (int) row.getCell(2).getNumericCellValue();  
			sw = new SeWord(w,r,s,p);     //SeWord(String word, int id, int strength, int polar)
			dictionary.put(w, sw);
		}
		wb.close();
		return dictionary;
	}
}