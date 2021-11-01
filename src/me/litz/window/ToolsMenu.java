package me.litz.window;

import javax.swing.*;

public class ToolsMenu extends JMenu {

	private static final long serialVersionUID = -6797265672331381582L;

	public ToolsMenu(MainWindow parent) {
		super("Tools");

		add(new ToolsDownloadMenu(parent));
	}
}
