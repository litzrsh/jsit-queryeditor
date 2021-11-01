package me.litz.window.menu;

import me.litz.window.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class FileExitMenuItem extends JMenuItem {

	public FileExitMenuItem(final MainWindow parent) {
		super("Exit");

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
}
