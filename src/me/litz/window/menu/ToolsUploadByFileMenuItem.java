package me.litz.window.menu;

import me.litz.exception.QueryUploadException;
import me.litz.util.UploadUtils;
import me.litz.window.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class ToolsUploadByFileMenuItem extends JMenuItem {

	private final MainWindow parent;

	public ToolsUploadByFileMenuItem(final MainWindow parent) {
		super("Upload by file");

		this.parent = parent;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uploadByFile();
			}
		});
	}

	private void uploadByFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				String extension = f.getName().replaceAll("^(.*)\\.(\\w+)$", "$2");
				return "SQL".equalsIgnoreCase(extension);
			}

			@Override
			public String getDescription() {
				return "SQL Script";
			}
		});

		int opt = fileChooser.showOpenDialog(parent);
		if (opt == 0) {
			final java.util.List<QueryUploadException> exceptions = new ArrayList<>();
			File[] files = fileChooser.getSelectedFiles();
			for (File file : files) {
				QueryUploadException e = UploadUtils.uploadByFile(file);
				if (e != null) {
					exceptions.add(e);
				}
			}

			if (!exceptions.isEmpty()) {
				StringBuilder message = new StringBuilder("Total " + files.length + " files to upload, but perform " + exceptions.size() + " files upload failed\r\n");
				for (QueryUploadException e : exceptions) {
					message.append("[").append(e.getQuery().getId()).append("] : ").append(e.getMessage()).append("\r\n");
				}
				JOptionPane.showMessageDialog(null, message.toString());
			}
		}
	}
}
