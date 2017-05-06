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
 * ���ļ��༭���±�
 * 
 * @author develon <br/>
 *         <b> ʱ�� 2017-5
 */
public class Frame extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = 7859502382174100871L;
	// ����
	Font font = new Font("����", Font.PLAIN, 20);
	// �˵����
	JMenuBar menuBar = new JMenuBar();
	String[][] menuItemCmd = { { "�½�", "��", "����", "���Ϊ", "�ر�", "�˳�" },
			{ "����", "������һ��", "�滻" }, { "����" } };
	JMenu[] menu = { new JMenu("�ļ�"), new JMenu("�༭"), new JMenu("����") };
	JMenuItem[][] menuItem = new JMenuItem[menu.length][];
	// �������
	JPanel content = new JPanel();
	JPanel btn = new JPanel();
	JPanel edit = new JPanel();
	CardLayout card = new CardLayout();// ������edit
	// �ļ����
	List<File> file = new ArrayList<File>();// ���±����б༭/�������ļ�
	List<JButton> ab = new ArrayList<JButton>();// ��ť,�ļ����ı��༭�����һһ��Ӧ��ϵ
	List<TextArea> ed = new ArrayList<TextArea>();
	int index = -0x1;// ���ڽ��������

	{
		// ��ʼ���˵�
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

		// ��Ӳ˵��� ���ò˵�����
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

		// ���ô��ڹر��¼��ص�����
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("exit");
				System.exit(0);
			}
		});

		// ���ò�����������
		content.setLayout(new BorderLayout());
		edit.setLayout(card);
		content.add(btn, BorderLayout.NORTH);
		content.add(edit, BorderLayout.CENTER);
		add(content);

		// ���ô����С��λ�þ���
		setSize(1080, 800);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth()) / 2,
				(dim.height - getHeight()) / 2);
	}

	/** ������� */
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setVisible(true);
	}

	/** �����˵��¼� �ص����� */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("�˳�")) {
			System.exit(0x0);
		}

		if (cmd.equals("����")) {
			JDialog dialog = new JDialog(this, true);
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel("���������ѧ�������ѧѧԺ", JLabel.CENTER);
			JLabel label1 = new JLabel("\n�������--develon", JLabel.CENTER);
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

		if (cmd.equals("�½�")) {
			open(null);
			return;
		}

		if (cmd.equals("��")) {
			FileDialog fd = new FileDialog(this);
			fd.setVisible(true);
			File f = new File(fd.getDirectory() + fd.getFile());
			open(f);
			return;
		}

		if (cmd.equals("�ر�")) {
			// ����ȷ����ʾ��
			close(index);// �رմ��ڱ༭�е��ļ�
			return;
		}

		if (cmd.equals("����")) {
			if (file.size() < 1)
				return;
			if (file.get(index) == null || !file.get(index).exists()) {
				// �ļ����Ϊ
				o_save();
			} else
				save();
			return;
		}

		if (cmd.equals("���Ϊ")) {
			if (file.size() < 1)
				return;
			o_save();
			return;
		}

	}

	/** ѡ���ļ���ť�¼��ص� */
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
		index = file.size();// ��ӵ��������,��������
		System.out.println("size: " + file.size() + ", index-->" + index);

		file.add(f);

		JButton b = new JButton();
		b.setActionCommand(index + "");
		ab.add(b);
		TextArea area = new TextArea();
		area.setFont(font);
		ed.add(area);

		if (f == null) {// �½�һ���հ��ı�
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
		FileDialog d = new FileDialog(this, "���Ϊ...", FileDialog.SAVE);
		d.setVisible(true);
		if (d.getFile() == null)
			return;
		FileKit.close(new File(d.getDirectory() + d.getFile()));
	}

	private void close(int i) {
		if (i < 0 || i > file.size())
			return;
		// �Ƴ��ļ�
		file.remove(index);
		// �Ƴ�TextArea
		edit.remove(ed.get(index));
		ed.remove(index);
		// �Ƴ���ť
		btn.remove(ab.get(index));
		ab.remove(index);
		// ������ť���ı���
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

	/** ��ȡ���ڽ����JTextArea */
	TextArea getFT() {
		return ed.get(index);
	}

	/** ���´��ڱ��� */
	void upTitle() {
		setTitle((file.get(index) == null ? "�½�" : file.get(index).getPath())
				+ " -- notepad-m");
	}

}
