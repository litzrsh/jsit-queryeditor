package me.litz.window;

import me.litz.constants.DbInfo;
import me.litz.model.Query;
import me.litz.util.MapperUtils;
import me.litz.util.SessionFactoryUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SideBar extends JPanel {

	private final MainWindow parent;

	private final JTextField queryField;

	private JList<String> queryList = null;

	private String[] data = null;

	private final JComboBox<String> database;

	private List<Query> dataList = new ArrayList<>();

	public SideBar(final MainWindow parent) {
		this.parent = parent;

		// Create and set layout manager
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(5);
		this.setLayout(borderLayout);

		queryField = new JTextField();

		this.queryField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					updateQueryList();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		database = new JComboBox<>(new String[]{"EMS", "ESS"});
		database.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = String.valueOf(database.getSelectedItem());
				QueryEditPanel.updateDbProperty(DbInfo.valueOf(key));
				SessionFactoryUtils.setDatabase(key);
				queryField.setText(null);
				parent.requestCloseData();
				updateQueryList();
			}
		});

		createTopItems();
		createScrollPanel(new JList<String>());

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeComponents();
			}
		});
	}

	public void updateQueryList() {
		parent.loadingStart();

		parent.onItemSelectionChanged(null);

		removeAll();
		createTopItems();

//        List<Query> array = MapperUtils.getQueryMapper().listQueries(null);
//        String sq = queryField.getText();
//        if (sq == null) sq = "";
//        sq = sq.trim().toUpperCase();
//        final Pattern pattern = Pattern.compile(sq, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//        final String searchStr = sq;
//        final Predicate<Query> predicate = new Predicate<Query>() {
//            @Override
//            public boolean evaluate(Query query) {
//                if (query.getId().toUpperCase().startsWith(searchStr)) return true;
//                return pattern.matcher(query.getQuery()).find();
//            }
//        };
//
//        List<Query> searchResult;
//        if ("".equals(sq)) {
//            searchResult = array;
//        } else {
//            searchResult = (List<Query>) CollectionUtils.select(array, predicate);
//        }

		String sq = queryField.getText();
		if (sq != null) {
			sq = sq.trim();
			if ("".equals(sq)) sq = null;
		}

		List<Query> searchResult = new ArrayList<>();
		if (sq != null) {
			searchResult = MapperUtils.getQueryMapper().listQueries(sq);
		}
		dataList = searchResult;
		data = new String[searchResult.size()];
		for (int i = 0; i < searchResult.size(); i++) {
			data[i] = searchResult.get(i).getId();
		}

		queryList = new JList<>(data);
		queryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		queryList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (e.getValueIsAdjusting()) {
					if (lsm.isSelectionEmpty()) {
						parent.onItemSelectionChanged(null);
					} else {
						parent.onItemSelectionChanged(data[lsm.getMinSelectionIndex()]);
					}
				}
			}
		});
		queryList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 목록 복사
				if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_C) {
					StringBuilder text = new StringBuilder();
					for (Query d : dataList) {
						text.append(d.getId())
								.append("\t")
								.append(d.getTitle())
								.append("\t")
								.append(sdf.format(d.getModified()))
								.append("\t")
								.append(d.getCreator().equals(d.getUpdater()))
								.append("\t")
								.append("\"" + d.getQuery() + "\"")
								.append("\r\n");
					}
					StringSelection t = new StringSelection(text.toString());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(t, null);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		createScrollPanel(queryList);
		updateUI();

		parent.loadingEnd();
	}

	private void createTopItems() {
		JPanel pane = new JPanel();
		BorderLayout paneLayout = new BorderLayout();
		paneLayout.setVgap(5);
		paneLayout.setHgap(5);
		pane.setLayout(paneLayout);
		pane.add(database, BorderLayout.WEST);
		pane.add(queryField, BorderLayout.CENTER);
		add(pane, BorderLayout.NORTH);
	}

	private void createScrollPanel(JList<String> queryList) {
		JScrollPane panel = new JScrollPane(queryList);
		ScrollPaneLayout layout = new ScrollPaneLayout();
		panel.setLayout(layout);
//        int w = getWidth(), h = getHeight();
//        queryList.setBounds(5, 34, w - 10, h - 39);
//        panel.setBounds(5, 34, w - 10, h - 39);
		add(panel, BorderLayout.CENTER);
		this.queryList = queryList;
	}

	public void resizeComponents() {
//        try {
//            int w = getWidth(), h = getHeight();
//            database.setSize(60, 24);
//            database.setBounds(5, 5, 60, 24);
//            queryField.setBounds(65, 5, w - 10, 24);
//            if (panel != null) {
//                queryList.setBounds(5, 34, w - 10, h - 39);
//                panel.setBounds(5, 34, w - 10, h - 39);
//            }
//            repaint();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
	}

	public void clearSelection() {
		queryList.clearSelection();
	}

	public List<Query> getQueryList() {
		return dataList;
	}
}
