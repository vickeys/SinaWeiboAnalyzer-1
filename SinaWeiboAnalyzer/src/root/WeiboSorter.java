package root;

import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.viewers.Viewer;
import org.jdom.Element;

public class WeiboSorter extends ViewerSorter{
	public int compare(Viewer viewer, Object e1, Object e2)
	{
		String scoreString1 = ((Element)e1).getChildText("分值");
		int score1 = Integer.parseInt(scoreString1);
		String scoreString2 = ((Element)e2).getChildText("分值");
		int score2 = Integer.parseInt(scoreString2);
		return score1-score2;

	}

}
