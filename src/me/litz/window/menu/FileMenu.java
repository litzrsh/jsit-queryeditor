package me.litz.window.menu;

import me.litz.window.MainWindow;

import javax.swing.*;

public class FileMenu extends JMenu {

	public FileMenu(final MainWindow parent) {
		super("File");

		add(new FileExitMenuItem(parent));
	}
}
