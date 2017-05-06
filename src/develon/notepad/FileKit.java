package develon.notepad;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileKit {
	static Frame frame;

	FileKit() {

	}

	/** 将文本文件内容写到JTextArea上 */
	static void open(File f) {
		if (f == null || !f.exists()) {
			System.out.println("文件" + f + "不存在!");
			return;
		}
		TextArea _area = frame.getFT();
		String _s = null;
		try {
			FileReader fr = new FileReader(f);
			char[] _c = new char[(int) f.length()];
			fr.read(_c);
			fr.close();
			_s = new String(_c);
			_area.setText(_s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void close(File f) {
		if (f == null) {
			System.out.println(f + "不存在!");
			return;
		}
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(frame.getFT().getText());
			fw.close();

			final JDialog dialog = new JDialog(frame, true);
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel("保存成功 !", JLabel.CENTER);
			JButton label1 = new JButton("OK");
			label1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialog.setVisible(false);
				}
			});
			label.setFont(frame.font);
			label1.setFont(frame.font);
			panel.add(label, BorderLayout.NORTH);
			panel.add(label1, BorderLayout.SOUTH);
			dialog.add(panel);
			dialog.pack();
			dialog.setLocation((frame.getWidth() - dialog.getWidth()) / 2
					+ frame.getX(), (frame.getHeight() - dialog.getHeight())
					/ 2 + frame.getY());
			dialog.setResizable(false);
			frame.file.set(frame.index, f);
			frame.ab.get(frame.index).setText(f.getName());
			frame.upTitle();
			dialog.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
