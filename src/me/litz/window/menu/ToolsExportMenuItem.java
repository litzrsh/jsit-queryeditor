package me.litz.window.menu;

import me.litz.model.Query;
import me.litz.window.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

public class ToolsExportMenuItem extends JMenuItem {

	private static final String NODE_NAME = "JiSungITQueryEditor";

	private static final String LAST_VISITED_DIR = "LAST_VISITED_DIR";

	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ToolsExportMenuItem(final MainWindow parent) {
		super("Export...");
		final ToolsExportMenuItem _this = this;

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final java.util.List<Query> queryList = parent.getQueryList();

				if (queryList.size() < 1) {
					JOptionPane.showMessageDialog(null, "No queries to download");
					return;
				}

				Preferences prefs;
				String lastDirectory = null;
				try {
					prefs = Preferences.userRoot().node(NODE_NAME);
					lastDirectory = prefs.get(LAST_VISITED_DIR, null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				JFileChooser fileChooser = new JFileChooser(lastDirectory);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int opt = fileChooser.showSaveDialog(_this);
				if (opt == JFileChooser.APPROVE_OPTION) {
					final String basePath = fileChooser.getSelectedFile().getAbsolutePath();
					try {
						prefs = Preferences.userRoot().node(NODE_NAME);
						prefs.put(LAST_VISITED_DIR, basePath);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

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
