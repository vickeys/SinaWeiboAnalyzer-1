/*PathManager类用于从系统启动时的工作空间对话框向系统传送工作空间路径
 * 如果是工作空间，path时绝对路径；如果是项目；path是    \项目名\
 * 
*/

package help;

public class PathManager {
	String path;

	public PathManager(String path) {
		super();
		this.path = path;
	}
	

	public PathManager() {
		super();
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
