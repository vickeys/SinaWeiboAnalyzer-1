package root;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.jdom.Element;

public class WeiboFilter extends ViewerFilter{
	int bias = 0;

	public int getBias() {
		return bias;
	}


	public void setBias(int bias) {
		this.bias = bias;
	}


	public boolean select(Viewer arg0, Object arg1,  Object arg2) {
		// TODO Auto-generated method stub
		
		String scoreString = ((Element)arg2).getChildText("·ÖÖµ");
		int score = Integer.parseInt(scoreString);
		return score<bias;
	}

}
