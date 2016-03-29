package root;

import help.PathManager;

import java.io.File;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import actions.ComputeSentimentScoreAction;
import actions.CopyRightOwner;
import actions.ExitProgramAction;
import actions.GameSentimentScoreAction;
import actions.GraspWeiboAction;
import actions.InstructionForUseAction;
import actions.NewProjectAction;
import actions.OpenProjectAction;
import actions.SetBiasAction;
import actions.ShowAllWeiboAction;
import actions.TransformDataAction;
import dialog.WorkspaceDialog;

public class AnalyzerWindow extends ApplicationWindow {

	Font f1, f2, f3;

	private TableViewer viewer;
	private Text weibo_text;
	private Text keyWord_text;
	private Text proj_name;
	private Text yellow_alarm_limit;
	private Text red_alarm_limit;
	private Text proj_path;
	private PathManager workspace, projDirectory; // 工作空间路径 和 项目文件夹
	private int redAlarm; // 红色预警阈值
	private int yellowAlarm; // 黄色预警阈值
	private List<Element> weibos = null;
	private WeiboFilter filter;
	private WeiboLabelProvider weiboLabelProvider;
	
	OpenProjectAction openPro_Action;
	NewProjectAction newPro_Action;
	GraspWeiboAction grasp_Action;
	ComputeSentimentScoreAction comp_Action;
	SetBiasAction setBias_Action;
	ShowAllWeiboAction showAllWeiboAction;
	ActionContributionItem paItem;
	TransformDataAction transformDataAction;
	GameSentimentScoreAction gameSentimentScoreAction;
	InstructionForUseAction instruction;
	CopyRightOwner copyRig_Action;
	ExitProgramAction exitProgra_Action;

	public AnalyzerWindow() {
		super(null);
		
		openPro_Action = new OpenProjectAction();
		newPro_Action = new NewProjectAction();
		grasp_Action = new GraspWeiboAction();
		comp_Action = new ComputeSentimentScoreAction();
		setBias_Action = new SetBiasAction();
		showAllWeiboAction = new ShowAllWeiboAction();
		paItem = new ActionContributionItem(openPro_Action);
		transformDataAction = new TransformDataAction();
		gameSentimentScoreAction = new GameSentimentScoreAction();
		instruction = new InstructionForUseAction();
		copyRig_Action = new CopyRightOwner();
		exitProgra_Action = new ExitProgramAction();

		this.addMenuBar();
		this.addToolBar(SWT.FLAT | SWT.WRAP);
		workspace = new PathManager();
		projDirectory = new PathManager();

		// openPro_Action.setProjDirectory(projDirectory);
		// openPro_Action.setWorkspace(workspace);
		// openPro_Action.setAnalyzerWindow(this);

		grasp_Action.setProjDirectory(projDirectory);
		grasp_Action.setWorkspace(workspace);
		transformDataAction.setAnalyzerWindow(this);
		comp_Action.setAnalyzerWindow(this);
		gameSentimentScoreAction.setAnalyzerWindow(this);
		setBias_Action.setAnalyzerWindow(this);

		redAlarm = 10;
		yellowAlarm = 5;
	}

	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager(null);
		MenuManager project_menu = new MenuManager("项目");
		menuBar.add(project_menu);
		project_menu.add(newPro_Action);
		project_menu.add(openPro_Action);
		project_menu.add(exitProgra_Action);
		MenuManager training_menu = new MenuManager("抓取与计算");
		menuBar.add(training_menu);
		training_menu.add(grasp_Action);
		training_menu.add(comp_Action);

		MenuManager comp_menu = new MenuManager("设置");
		menuBar.add(comp_menu);
		comp_menu.add(setBias_Action);
		comp_menu.add(showAllWeiboAction);
		
		MenuManager game_menu = new MenuManager("统计图表");
		menuBar.add(game_menu);
		game_menu.add(transformDataAction);
		game_menu.add(gameSentimentScoreAction);

		MenuManager help_menu = new MenuManager("帮助");
		menuBar.add(help_menu);
		help_menu.add(instruction);
		help_menu.add(copyRig_Action);
		return menuBar;
	}

	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager tool_bar_manager = new ToolBarManager(style);
		tool_bar_manager.add(newPro_Action);
		tool_bar_manager.add(openPro_Action);
		tool_bar_manager.add(grasp_Action);
		tool_bar_manager.add(comp_Action);
		return tool_bar_manager;
	}

	protected Control createContents(Composite parent) {

		f1 = new Font(null, "Arial", 10, SWT.BOLD);
		f2 = new Font(null, "Arial", 10, SWT.NORMAL);
		f3 = new Font(null, "Arial", 11, SWT.NORMAL);// 设置内容的字体
		WorkspaceDialog dialog = new WorkspaceDialog(new Shell(
				Display.getCurrent()), workspace);
		int openValue = dialog.open();
		if (openValue == 1) // 在选择工作空间对话框中点击了取消按钮
			Display.getCurrent().dispose();
		System.out.println("工作空间是：" + workspace.getPath()); // 测试语句,返回的路径等待使用

		Composite container = null;
		try {
			container = new Composite(parent, SWT.NONE);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		   System.exit(0);
		}
		container.setLayout(new FormLayout());

		// 预警微博表
		// table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(1,20,0);
		fd_table.right = new FormAttachment(19,20,0);
     	fd_table.bottom = new FormAttachment(3,4,0);
		fd_table.left = new FormAttachment(1, 4,0);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// red_alarm_limit
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(8, true));
		layout.addColumnData(new ColumnWeightData(12, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(10, true));
		layout.addColumnData(new ColumnWeightData(10, true));
		layout.addColumnData(new ColumnWeightData(10, true));
		layout.addColumnData(new ColumnWeightData(10, true));
		layout.addColumnData(new ColumnWeightData(15, true));
		layout.addColumnData(new ColumnWeightData(10, true));
		// layout.addColumnData(new ColumnWeightData(1, 75, true)); //关键字不显示
		table.setLayout(layout);
		TableColumn idColumn = new TableColumn(table, SWT.CENTER);
		idColumn.setText("序号");
		TableColumn accountColumn = new TableColumn(table, SWT.CENTER);
		accountColumn.setText("账号");
		TableColumn textColumn = new TableColumn(table, SWT.CENTER);
		textColumn.setText("微博正文");
		TableColumn scoreColumn = new TableColumn(table, SWT.CENTER);
		scoreColumn.setText("情感分");
		TableColumn transmitNumColumn = new TableColumn(table, SWT.CENTER);
		transmitNumColumn.setText("转发数");
		TableColumn comentNumColumn = new TableColumn(table, SWT.CENTER);
		comentNumColumn.setText("评论数");
		TableColumn clickNumColumn = new TableColumn(table, SWT.CENTER);
		clickNumColumn.setText("点赞数");
		TableColumn timeColumn = new TableColumn(table, SWT.CENTER);
		timeColumn.setText("微博发表时间");
		TableColumn beizhuColumn = new TableColumn(table, SWT.CENTER);
		beizhuColumn.setText("备注");
		TableColumn keyWordColumn = new TableColumn(table, SWT.CENTER);
		keyWordColumn.setText("关键字");

		// 监听微博表，显示选中行的微博正文和关键字
		table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("You selected "+
				// table.getSelection()[0].getText(2));
				weibo_text.setText(table.getSelection()[0].getText(2));
				keyWord_text.setText(table.getSelection()[0].getText(9));
				keyWord_text.setBackground(new Color(Display.getDefault(),
						new RGB(224, 238, 224)));
			}
		});

		// File file = new
		// File("C:\\eclipse_plugIns\\workspace\\writeXml\\weiboData.xml");
		viewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object input) {
				if (input != null) {
					List<Element> weibos = (List<Element>) input;
					return weibos.toArray();
				} else
					return null;
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}
		});
		weiboLabelProvider = new WeiboLabelProvider(this.yellowAlarm,
				this.redAlarm);
		weiboLabelProvider.setRowId(0);
		viewer.setLabelProvider(weiboLabelProvider);
		filter = new WeiboFilter();
		WeiboFilter filters[] = { filter };
		viewer.setFilters(filters);
		viewer.setSorter(new WeiboSorter());
		// 选中的特定微博文本
		Label weibo_label = new Label(container, SWT.NONE);
		weibo_label.setText("微博正文:");
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(table, 0, SWT.BOTTOM);
		fd_lblNewLabel.left = new FormAttachment(1, 10, 0);
		weibo_label.setLayoutData(fd_lblNewLabel);
		weibo_label.setFont(f1);
		weibo_text = new Text(container, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY); // |SWT.READ_ONLY
		// weibo_text.setText("abc平度杜家疃拆迁又现血案，看地村民凌晨两点被泼汽油焚烧造成一死两重伤一轻伤！");
		weibo_text.setFont(f3);
		weibo_text.setBackground(new Color(Display.getDefault(), new RGB(224,
				238, 224)));
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(weibo_label, 0, SWT.BOTTOM);
		fd_text.left = new FormAttachment(1, 10, 0);
		fd_text.right = new FormAttachment(100, 0);
		fd_text.bottom = new FormAttachment(17, 20, 0);
		weibo_text.setLayoutData(fd_text);

		// 选中的特定微博关键字
		Label keywork_label = new Label(container, SWT.NONE);
		keywork_label.setText("情感关键字（中括号内为该关键字情感分*该关键字在微博中出现的次数）:");
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.top = new FormAttachment(weibo_text, 0,SWT.BOTTOM);
		fd_lblNewLabel_1.left = new FormAttachment(1, 10, 0);
		keywork_label.setLayoutData(fd_lblNewLabel_1);
		keywork_label.setFont(f1);
		keyWord_text = new Text(container, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY); // |SWT.READ_ONLY
		keyWord_text.setFont(f3);
		keyWord_text.setBackground(new Color(Display.getDefault(), new RGB(224,
				238, 224)));
		FormData kw_text = new FormData();
		kw_text.top = new FormAttachment(keywork_label, 0, SWT.BOTTOM);
		kw_text.left =  new FormAttachment(1, 10, 0);
		kw_text.right = new FormAttachment(100,0);
		kw_text.bottom = new FormAttachment(19,20,0);
		keyWord_text.setLayoutData(kw_text);

		// 项目名称
		Label proj_name_label = new Label(container, SWT.NONE);
		proj_name_label.setText("项目名称: ");
		proj_name_label.setFont(f1);
		FormData fd_proj_name_label = new FormData();
	fd_proj_name_label.top = new FormAttachment(1,10,0);
	fd_proj_name_label.left = new FormAttachment(1,70, 0);
 // 	fd_proj_name_label.bottom = new FormAttachment(table, 100, SWT.TOP);
//		// fd_proj_name_label.right = new FormAttachment(table, -350, SWT.LEFT);
	
    	proj_name_label.setLayoutData(fd_proj_name_label);
		proj_name = new Text(container, SWT.SINGLE|SWT.READ_ONLY);
		// proj_name.setEditable(false);
		// newPro_Action.setProj_name(proj_name);
		// openPro_Action.setProj_name(proj_name);

		FormData fd_proj_name = new FormData();
		fd_proj_name.top = new FormAttachment(1, 10, 0);
		fd_proj_name.left = new FormAttachment(proj_name_label, 10, SWT.RIGHT);
		fd_proj_name.right = new FormAttachment(1, 4, 0);
		proj_name.setLayoutData(fd_proj_name);

		// 项目路径
		Label proj_path_label = new Label(container, SWT.NONE);
		proj_path_label.setText("项目路径:");
		FormData fd_proj_path_label = new FormData();
		fd_proj_path_label.top = new FormAttachment(proj_name_label, 20,
				SWT.BOTTOM);
		fd_proj_path_label.left = new FormAttachment(1, 70,
			0);

		proj_path_label.setLayoutData(fd_proj_path_label);
		proj_path_label.setFont(f1);
		proj_path = new Text(container, SWT.SINGLE | SWT.READ_ONLY);
		// proj_path.setEditable(false);
		// proj_path.setText("D:/ 工作空间/项目一");
		// newPro_Action.setProj_path(proj_path);
		// openPro_Action.setProj_path(proj_path);

		FormData fd_proj_path = new FormData();
		fd_proj_path.top = new FormAttachment(proj_path_label, 0, SWT.TOP);
		fd_proj_path.left = new FormAttachment(proj_path_label, 10, SWT.RIGHT);
		fd_proj_path.right = new FormAttachment(1, 4, 0);
		proj_path.setLayoutData(fd_proj_path);

		// 预警阈值
		Label bias_label = new Label(container, SWT.NONE);
		bias_label.setText("预警阈值(评论数+转发数+点赞数)");
		FormData fd_bias_label = new FormData();
		fd_bias_label.top = new FormAttachment(proj_name_label, 110, SWT.BOTTOM);
		fd_bias_label.left = new FormAttachment(proj_name_label, 0, SWT.LEFT);
		fd_bias_label.right=new FormAttachment(1, 4, 0);
		bias_label.setLayoutData(fd_bias_label);
		bias_label.setFont(f1);
		// 黄色预警阈值
		Label yellow_bias_label = new Label(container, SWT.NONE);
		yellow_bias_label.setText("黄色预警阈值:");
		yellow_bias_label.setFont(f1);
		FormData fd_yellow_bias_label = new FormData();
		fd_yellow_bias_label.top = new FormAttachment(bias_label, 20,
				SWT.BOTTOM);
		fd_yellow_bias_label.left = new FormAttachment(bias_label, 10, SWT.LEFT);
		yellow_bias_label.setLayoutData(fd_yellow_bias_label);

		yellow_alarm_limit = new Text(container, SWT.SINGLE | SWT.READ_ONLY);

		yellow_alarm_limit.setText(Integer.toString(this.yellowAlarm));
		// setBias_Action.setYellow_alarm_limit(yellow_alarm_limit);
		yellow_alarm_limit.setBackground(new Color(Display.getDefault(),
				new RGB(255, 165, 0)));
		FormData fd_yellow_alarm_limit = new FormData();
		fd_yellow_alarm_limit.top = new FormAttachment(yellow_bias_label, 0,
				SWT.TOP);
		fd_yellow_alarm_limit.left = new FormAttachment(yellow_bias_label, 10,
				SWT.RIGHT);
		fd_yellow_alarm_limit.right = new FormAttachment(1,
				5, 0);
		yellow_alarm_limit.setLayoutData(fd_yellow_alarm_limit);

		// 红色预警阈值
		Label red_bias_label = new Label(container, SWT.NONE);
		red_bias_label.setText("红色预警阈值:");
		red_bias_label.setFont(f1);
		FormData fd_red_bias_label = new FormData();
		fd_red_bias_label.top = new FormAttachment(yellow_bias_label, 10,
				SWT.BOTTOM);
		fd_red_bias_label.left = new FormAttachment(yellow_bias_label, 0,
				SWT.LEFT);
		red_bias_label.setLayoutData(fd_red_bias_label);

		red_alarm_limit = new Text(container, SWT.SINGLE | SWT.READ_ONLY);
		red_alarm_limit.setText(Integer.toString(this.redAlarm));
		// setBias_Action.setRed_alarm_limit(red_alarm_limit);
		red_alarm_limit.setBackground(new Color(Display.getDefault(), new RGB(
				255, 99, 71)));
		FormData fd_red_alarm_limit = new FormData();
		fd_red_alarm_limit.top = new FormAttachment(red_bias_label, 0, SWT.TOP);
		fd_red_alarm_limit.left = new FormAttachment(red_bias_label, 10,
				SWT.RIGHT);
		fd_red_alarm_limit.right = new FormAttachment(1, 5,
				0);
		red_alarm_limit.setLayoutData(fd_red_alarm_limit);

     	getShell().setMaximized(true);	
		getShell().open(); // 窗口最大化
		getShell().setImage(new Image(null,new File("./logo.png").getPath()));
		getShell().setText("网络评论情感分析器");

		newPro_Action.setAnalyzerWindow(this);
		openPro_Action.setAnalyzerWindow(this);
		showAllWeiboAction.setAnalyzerWindow(this);
		return container;
	}

	// 为主界面中的预警微博表设置显示数据
	public void callInput(List<Element> weibos) {
		weiboLabelProvider.setRowId(0);
		viewer.setLabelProvider(weiboLabelProvider);
		this.weibos = weibos;
		viewer.setInput(weibos);
	}

	// 菜单中重新设置预警值后刷新主界面
	public void resetAlarmBias(int yellowBias, int redBias) {
		yellow_alarm_limit.setText(Integer.toString(yellowBias));
		red_alarm_limit.setText(Integer.toString(redBias));
		weiboLabelProvider.setRowId(0);
		weiboLabelProvider.setYellowAlarm(yellowBias);
		weiboLabelProvider.setRedAlarm(redBias);
		//viewer.setLabelProvider(new WeiboLabelProvider(yellowBias, redBias));
		viewer.setInput(weibos);
	}

	public PathManager getProjDirectory() {
		return projDirectory;
	}

	public PathManager getWorkspace() {
		return workspace;
	}

	public void setWorkspace(PathManager workspace) {
		this.workspace = workspace;
	}

	public void setProjDirectory(PathManager projDirectory) {
		this.projDirectory = projDirectory;
	}

	public void setProj_name(Text proj_name) {
		this.proj_name = proj_name;
	}

	public void setProj_path(Text proj_path) {
		this.proj_path = proj_path;
	}

	public Text getProj_name() {
		return proj_name;
	}

	public Text getProj_path() {
		return proj_path;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public Text getWeibo_text() {
		return weibo_text;
	}

	public void setWeibo_text(Text weibo_text) {
		this.weibo_text = weibo_text;
	}

	public Text getKeyWord_text() {
		return keyWord_text;
	}

	public void setKeyWord_text(Text keyWord_text) {
		this.keyWord_text = keyWord_text;
	}

	public List<Element> getWeibos() {
		return weibos;
	}

	public WeiboFilter getFilter() {
		return filter;
	}
	
}
