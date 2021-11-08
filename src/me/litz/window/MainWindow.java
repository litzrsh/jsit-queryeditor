package me.litz.window;

import me.litz.model.Query;
import me.litz.util.SessionUtils;
import me.litz.window.menu.FileMenu;
import me.litz.window.menu.ToolsMenu;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MainWindow extends JFrame {

	private final SideBar sideBar;

	private final QueryEditPanel queryEditPanel;

	public MainWindow() {
		super();

		sideBar = new SideBar(this);
		queryEditPanel = new QueryEditPanel(this);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideBar, queryEditPanel);

		String title = "Query editor v1.2";

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new FileMenu(this)); // File
		menuBar.add(new ToolsMenu(this)); // Tools

		setJMenuBar(menuBar);

		setTitle(title);
		add(splitPane);
		pack();
		setBounds(
				PropertyUtils.getX(),
				PropertyUtils.getY(),
				PropertyUtils.getWidth(),
				PropertyUtils.getHeight());
		if (PropertyUtils.isMaximized()) {
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}
		setVisible(true);

		if (!SessionUtils.DEFAULT_USERNAME.equalsIgnoreCase(SessionUtils.getUsername())) {
			title = title + " - " + SessionUtils.getUsername();
			setTitle(title);
		}

		saveAsProperties();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				super.componentMoved(e);
				resizeComponents();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				resizeComponents();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				sideBar.updateQueryList();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				saveAsProperties();
				dispose();
			}
		});

		try {
			ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/application.png")));
			setIconImage(icon.getImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onItemSelectionChanged(String id) {
		if (sideBar != null && queryEditPanel != null) {
			if (id == null) {
				sideBar.clearSelection();
			} else {
				queryEditPanel.setData(id);
			}
		}
	}

	public void resizeComponents() {
		try {
			sideBar.resizeComponents();
			queryEditPanel.resizeComponents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void requestUpdateQueryList() {
		try {
			sideBar.updateQueryList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void requestCloseData() {
		try {
			queryEditPanel.closeData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Query> getQueryList() {
		return this.sideBar.getQueryList();
	}

	private void saveAsProperties() {
		Properties prop = new Properties();
		int w = getWidth(), h = getHeight();
		int x = getX(), y = getY();
		boolean mx = (getExtendedState() & MAXIMIZED_BOTH) != 0;

		// 세션 사용자 이름 추가
		prop.setProperty("username", SessionUtils.getUsername());
		if (mx) {
			prop.setProperty("width", String.valueOf(PropertyUtils.getWidth()));
			prop.setProperty("height", String.valueOf(PropertyUtils.getHeight()));
			prop.setProperty("x", String.valueOf(PropertyUtils.getX()));
			prop.setProperty("y", String.valueOf(PropertyUtils.getY()));
		} else {
			prop.setProperty("width", String.valueOf(w > 100 ? w : 1024));
			prop.setProperty("height", String.valueOf(h > 100 ? h : 768));
			prop.setProperty("x", String.valueOf(Math.max(x, 0)));
			prop.setProperty("y", String.valueOf(Math.max(y, 0)));
		}
		prop.setProperty("maximized", String.valueOf(mx));

		try {
			prop.store(new FileOutputStream(PropertyUtils.FILENAME), null);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void loadingStart() {
		System.out.println("Start loading");
		this.setEnabled(false);
	}

	public void loadingEnd() {
		this.setEnabled(true);
		System.out.println("Finish loading");
	}
}
