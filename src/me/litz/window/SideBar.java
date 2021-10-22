package me.litz.window;

import me.litz.model.Query;
import me.litz.util.MapperUtils;
import me.litz.util.SessionFactoryUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;

public class SideBar extends JPanel {

    private final MainWindow parent;

    private final JTextField queryField;

    private JScrollPane panel;

    private JList<String> queryList = null;

    private String[] data = null;

    private final JComboBox<String> database;

    public SideBar(final MainWindow parent) {
        this.parent = parent;

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
                SessionFactoryUtils.setDatabase(key);
                queryField.setText(null);
                parent.requestCloseData();
                updateQueryList();
            }
        });

        this.updateQueryList();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    public void updateQueryList() {
        parent.onItemSelectionChanged(null);

        removeAll();
        add(queryField);
        add(database);

        List<Query> array = MapperUtils.getQueryMapper().listQueries(null);
        String sq = queryField.getText();
        if (sq == null) sq = "";
        sq = sq.trim().toUpperCase();
        final String searchStr = sq;
        final Predicate<Query> predicate = new Predicate<Query>() {
            @Override
            public boolean evaluate(Query query) {
                return query.getId().toUpperCase().startsWith(searchStr) ||
                        query.getQuery().toUpperCase().contains(searchStr);
            }
        };

        List<Query> searchResult = (List<Query>) CollectionUtils.select(array, predicate);
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
        panel = new JScrollPane(queryList);
        int w = getWidth(), h = getHeight();
        queryList.setBounds(5, 34, w - 10, h - 39);
        panel.setBounds(5, 34, w - 10, h - 39);
        add(panel);
    }

    public void resizeComponents() {
        try {
            int w = getWidth(), h = getHeight();
            database.setSize(60, 24);
            database.setBounds(5, 5, 60, 24);
            queryField.setBounds(65, 5, w - 10, 24);
            if (panel != null) {
                queryList.setBounds(5, 34, w - 10, h - 39);
                panel.setBounds(5, 34, w - 10, h - 39);
            }
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSelection() {
        queryList.clearSelection();
    }
}
