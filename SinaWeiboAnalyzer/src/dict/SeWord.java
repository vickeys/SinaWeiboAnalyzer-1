/*
这个类用于创建情感词典中的情感关键字对象，成员包括情感词，强度和极性

*/
package dict;

public class SeWord {
	private String word;   //情感词
	private int id;        //在Excel中策序号
	private int strength;  //强度，情感强度分为1,3,5,7,9五档，9表示强度最大，1为强度最小
	private int polar;      //极性，1代表褒义，2代表贬义，3代表兼有褒贬两性
	public SeWord(String word, int id, int strength, int polar) {
		super();
		this.word = word;
		this.id = id;
		this.strength = strength;
		this.polar = polar;
	}
	public String getWord() {
		return word;
	}
	public int getId() {
		return id;
	}
	public int getStrength() {
		return strength;
	}
	public int getPolar() {
		return polar;
	}
	public String toString(){
		return word+" "+strength+" "+polar;
	}
}