package dict;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CLibrary extends Library {

	CLibrary Instance = (CLibrary) Native.loadLibrary(
			"nlpir/NLPIR", CLibrary.class);


	public int NLPIR_Init(String sDataPath, int encoding,
			String sLicenceCode);

	public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

	public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
			boolean bWeightOut);

	public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
			boolean bWeightOut);

	// public int NLPIR_ImportUserDict(byte[]sPath);
	public int NLPIR_AddUserWord(String sWord);// add by qp 2008.11.10

	public int NLPIR_DelUsrWord(String sWord);// add by qp 2008.11.10

	public String NLPIR_GetLastErrorMsg();

	public void NLPIR_Exit();
}
