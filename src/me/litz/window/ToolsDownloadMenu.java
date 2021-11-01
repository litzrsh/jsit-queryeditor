package me.litz.window;

import me.litz.model.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ToolsDownloadMenu extends JMenuItem {

	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ToolsDownloadMenu(final MainWindow parent) {
		super("Download");
		final ToolsDownloadMenu _this = this;

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final java.util.List<Query> queryList = parent.getQueryList();

				if (queryList.size() < 1) {
					JOptionPane.showMessageDialog(null, "No queries to download");
					return;
				}

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int opt = fileChooser.showSaveDialog(_this);
				if (opt == 0) {
					final String basePath = fileChooser.getSelectedFile().getAbsolutePath();

					for (Query query : queryList) {
						final String filepath = basePath + "/" + query.getId() + ".sql";
						final String text = "/* Last Modified : " + df.format(query.getModified()) + " by " + query.getUpdater() + " - " + query.getTitle() + " */\r\n"
								+ query.getQuery();

						File file = new File(filepath);
						if (file.exists()) {
							file.delete();
						}

						try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
							writer.write(text);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}

					JOptionPane.showMessageDialog(null, "All " + queryList.size() + " queries download completed");
				}
			}
		});
	}
}
