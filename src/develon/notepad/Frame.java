package develon.notepad;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

/**
 * 多文件编辑记事本
 * 
 * @author develon <br/>
 *         <b> 时间 2017-5
 */
public class Frame extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = 7859502382174100871L;
	// 字体
	Font font = new Font("宋体", Font.PLAIN, 20);
	// 菜单相关
	JMenuBar menuBar = new JMenuBar();
	String[][] menuItemCmd = { { "新建", "打开", "保存", "另存为", "关闭", "退出" },
			{ "查找", "查找下一个", "替换" }, { "关于" } };
	JMenu[] menu = { new JMenu("文件"), new JMenu("编辑"), new JMenu("帮助") };
	JMenuItem[][] menuItem = new JMenuItem[menu.length][];
	// 内容面板
	JPanel content = new JPanel();
	JPanel btn = new JPanel();
	JPanel edit = new JPanel();
	CardLayout card = new CardLayout();// 关联到edit
	// 文件相关
	List<File> file = new ArrayList<File>();// 记事本所有编辑/创建的文件
	List<JButton> ab = new ArrayList<JButton>();// 按钮,文件与文本编辑框具有一一对应关系
	List<TextArea> ed = new ArrayList<TextArea>();
	int index = -0x1;// 处于焦点的索引

	{
		// 初始化菜单
		for (int i = 0; i < menu.length; i++) {
			menuItem[i] = new JMenuItem[menuItemCmd[i].length];
			for (int j = 0; j < menuItemCmd[i].length; j++) {
				menuItem[i][j] = new JMenuItem(menuItemCmd[i][j]);
			}
		}

		FileKit.frame = this;

	}

	private Frame() {
		super("notepad-m");

		// 添加菜单栏 设置菜单监听
		setJMenuBar(menuBar);
		int i = 0;
		for (JMenu mMenu : menu) {
			mMenu.setFont(font);
			menuBar.add(mMenu);
			JMenuItem[] _item = menuItem[i++];
			for (JMenuItem item : _item) {
				mMenu.add(item);
				item.setFont(font);
				item.addActionListener(this);
			}
		}

		// 设置窗口关闭事件回调方法
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("exit");
				System.exit(0);
			}
		});

		// 设置并添加内容面板
		content.setLayout(new BorderLayout());
		edit.setLayout(card);
		content.add(btn, BorderLayout.NORTH);
		content.add(edit, BorderLayout.CENTER);
		add(content);

		// 设置窗体大小和位置居中
		setSize(1080, 800);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth()) / 2,
				(dim.height - getHeight()) / 2);
	}

	/** 程序入口 */
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setVisible(true);
	}

	/** 单击菜单事件 回调方法 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("退出")) {
			System.exit(0x0);
		}

		if (cmd.equals("关于")) {
			JDialog dialog = new JDialog(this, true);
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel("中南民族大学计算机科学学院", JLabel.CENTER);
			JLabel label1 = new JLabel("\n软件工程--develon", JLabel.CENTER);
			label.setFont(font);
			label1.setFont(font);
			panel.add(label, BorderLayout.NORTH);
			panel.add(label1, BorderLayout.CENTER);

			dialog.add(panel);
			dialog.pack();
			dialog.setLocation((getWidth() - dialog.getWidth()) / 2 + getX(),
					(getHeight() - dialog.getHeight()) / 2 + getY());
			dialog.setResizable(false);
			dialog.setVisible(true);
			return;
		}

		if (cmd.equals("新建")) {
			open(null);
			return;
		}

		if (cmd.equals("打开")) {
			FileDialog fd = new FileDialog(this);
			fd.setVisible(true);
			File f = new File(fd.getDirectory() + fd.getFile());
			open(f);
			return;
		}

		if (cmd.equals("关闭")) {
			// 弹出确认提示框
			close(index);// 关闭处于编辑中的文件
			return;
		}

		if (cmd.equals("保存")) {
			if (file.size() < 1)
				return;
			if (file.get(index) == null || !file.get(index).exists()) {
				// 文件另存为
				o_save();
			} else
				save();
			return;
		}

		if (cmd.equals("另存为")) {
			if (file.size() < 1)
				return;
			o_save();
			return;
		}

	}

	/** 选择文件按钮事件回调 */
	private ActionListener mActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			index = Integer.parseInt(e.getActionCommand());
			System.out.println("index-->" + index);
			_show(index);
		}
	};

	public void mouseClicked(MouseEvent e) {
		System.out.println(e);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	private void open(File f) {
		index = file.size();// 添加到序列最后,更新索引
		System.out.println("size: " + file.size() + ", index-->" + index);

		file.add(f);

		JButton b = new JButton();
		b.setActionCommand(index + "");
		ab.add(b);
		TextArea area = new TextArea();
		area.setFont(font);
		ed.add(area);

		if (f == null) {// 新建一个空白文本
			b.setText("new");
		} else {
			b.setText(f.getName());
		}

		b.addActionListener(mActionListener);
		b.setFont(font);

		btn.add(b);

		edit.add(index + "", area);
		_show(index);

		FileKit.open(f);

		setVisible(true);
		System.out.println(index);
	}

	private void save() {
		FileKit.close(file.get(index));
	}

	private void o_save() {
		FileDialog d = new FileDialog(this, "另存为...", FileDialog.SAVE);
		d.setVisible(true);
		if (d.getFile() == null)
			return;
		FileKit.close(new File(d.getDirectory() + d.getFile()));
	}

	private void close(int i) {
		if (i < 0 || i > file.size())
			return;
		// 移除文件
		file.remove(index);
		// 移除TextArea
		edit.remove(ed.get(index));
		ed.remove(index);
		// 移除按钮
		btn.remove(ab.get(index));
		ab.remove(index);
		// 修正按钮和文本域
		for (int q = index; q < ab.size(); q++) {
			ab.get(q).setActionCommand("" + q);
			edit.add("" + q, ed.get(q));
		}
		if (index == 0)
			if (file.size() > 0)
				_show(index);
			else {
				setTitle("notepad-m");
				return;
			}
		else
			_show(--index);
		setVisible(true);
	}

	void _show(int _id) {
		card.show(edit, _id + "");
		upTitle();
	}

	/** 获取处于焦点的JTextArea */
	TextArea getFT() {
		return ed.get(index);
	}

	/** 更新窗口标题 */
	void upTitle() {
		setTitle((file.get(index) == null ? "新建" : file.get(index).getPath())
				+ " -- notepad-m");
	}

}
