package me.litz.window.menu;

import me.litz.window.MainWindow;

import javax.swing.*;

public class ToolsMenu extends JMenu {

	private static final long serialVersionUID = -6797265672331381582L;

	public ToolsMenu(MainWindow parent) {
		super("Tools");

		add(new ToolsExportMenuItem(parent));
		// 위험한 것들...
//		add(new JPopupMenu.Separator());
//		add(new ToolsUploadByFolderMenuItem(parent));
//		add(new ToolsUploadByFileMenuItem(parent));
	}
}
