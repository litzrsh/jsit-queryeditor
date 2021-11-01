package me.litz.window.menu;

import me.litz.exception.QueryUploadException;
import me.litz.model.UploadFileRecursiveResult;
import me.litz.util.UploadUtils;
import me.litz.window.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class ToolsUploadByFolderMenuItem extends JMenuItem {

	private final MainWindow parent;

	public ToolsUploadByFolderMenuItem(final MainWindow parent) {
		super("Upload by folder");

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
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int opt = fileChooser.showOpenDialog(parent);
		if (opt == 0) {
			File dir = fileChooser.getSelectedFile();

			UploadFileRecursiveResult result = uploadFiles(dir, new UploadFileRecursiveResult());

			if (!result.getExceptions().isEmpty()) {
				StringBuilder message = new StringBuilder("Total " + result.getTotalFiles() + " files to upload, but perform " + result.getExceptions().size() + " files upload failed\r\n");
				for (QueryUploadException e : result.getExceptions()) {
					message.append("[").append(e.getQuery().getId()).append("] : ").append(e.getMessage()).append("\r\n");
				}
				JOptionPane.showMessageDialog(null, message.toString());
			}
		}
	}

	private UploadFileRecursiveResult uploadFiles(File folder, UploadFileRecursiveResult result) {
		for (final File file : Objects.requireNonNull(folder.listFiles())) {
			if (file.isDirectory()) {
				return uploadFiles(file, result);
			} else {
				result.addCount();
				String extension = file.getName().replaceAll("^(.*)\\.(\\w+)$", "$2");
				if ("SQL".equalsIgnoreCase(extension)) {
					result.addException(UploadUtils.uploadByFile(file));
				}
			}
		}
		return result;
	}
}
