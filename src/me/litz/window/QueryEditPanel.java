package me.litz.window;

import me.litz.mapper.QueryMapper;
import me.litz.model.Query;
import me.litz.util.MapperUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QueryEditPanel extends JPanel {

    public static final int MAX_HISTORY_SIZE = 256;

    private final MainWindow parent;

    private Query entity = null;

    private final JTextField id;

    private final JTextField title;

    private final JTextArea query;

    private final JLabel labelId;

    private final JLabel labelTitle;

    private final JLabel labelQuery;

    private final JButton newButton;

    private final JButton saveButton;

    private final JButton deleteButton;

    private final JButton closeButton;

    private final JScrollPane jsp;

    private final List<String> queryHistory = new ArrayList<>();

    private boolean active = false;

    private Timer timer;

    public QueryEditPanel(final MainWindow parent) {
        this.parent = parent;

        newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newData();
            }
        });
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuery();
            }
        });
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryMapper queryMapper = MapperUtils.getQueryMapper();
                int r = JOptionPane.showConfirmDialog(null, "Really want to delete this query data?");
                if (r == 0) {
                    r = JOptionPane.showConfirmDialog(null, "Really Really want to delete this query data?");

                    if (r == 0) {
                        r = JOptionPane.showConfirmDialog(null, "Really Really Really want to delete this query data?");
                        if (r == 0) {
                            try {
                                queryMapper.deleteQuery(entity.getId());
                                JOptionPane.showMessageDialog(null, "Query data deleted successfully");
                                closeData();
                                parent.requestUpdateQueryList();
                            } catch (Throwable t) {
                                t.printStackTrace();
                                JOptionPane.showMessageDialog(null, t.getMessage());
                            }
                        }
                    }
                }
            }
        });
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeData();
            }
        });

        labelId = new JLabel("ID");
        labelTitle = new JLabel("Title");
        labelQuery = new JLabel("Query");

        id = new JTextField();
        title = new JTextField();
        query = new JTextArea();
        try {
            InputStream is = getClass().getResourceAsStream("/D2Coding.ttc");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            Font fontBase = font.deriveFont(15f);
            query.setFont(fontBase);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        query.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!active) return;
                // Control event
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_S:
                            saveQuery();
                            return;
                        case KeyEvent.VK_Z:
                            restoreQueryFromHistory();
                            return;
                        default:
                            break;
                    }
                }
                addQueryHistory();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        jsp = new JScrollPane(query);

        add(newButton);
        add(saveButton);
        add(deleteButton);
        add(closeButton);
        add(labelId);
        add(labelTitle);
        add(labelQuery);
        add(id);
        add(title);
        add(jsp);

        deactivatePanel();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    public void newData() {
        parent.onItemSelectionChanged(null);
        entity = new Query();
        activatePanel(false);
    }

    public void setData(String id) {
        entity = MapperUtils.getQueryMapper().getQuery(id);
        if (entity != null) {
            entity.setOld(true);
            activatePanel(true);
        } else {
            deactivatePanel();
        }
    }

    public void closeData() {
        parent.onItemSelectionChanged(null);
        deactivatePanel();
    }

    public void activatePanel(boolean isEdit) {

        newButton.setEnabled(false);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(isEdit);
        closeButton.setEnabled(true);

        id.setText(entity.getId());
        id.setEnabled(true);
        id.setEditable(!isEdit);
        title.setText(entity.getTitle());
        title.setEnabled(true);
        query.setText(entity.getQuery());
        jsp.setEnabled(true);
        query.setEnabled(true);

        repaint();

        active = true;
        // Ctrl+Z가 하고 싶다
        clearQueryHistory();
        addQueryHistory();
    }

    public void deactivatePanel() {
        active = false;

        // Ctrl+Z가 하고 싶다
        clearQueryHistory();

        newButton.setEnabled(true);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
        closeButton.setEnabled(false);

        id.setText(null);
        id.setEnabled(false);
        title.setText(null);
        title.setEnabled(false);
        query.setText(null);
        jsp.setEnabled(false);
        query.setEnabled(false);

        repaint();
    }

    public void resizeComponents() {
        try {
            int w = getWidth(), h = getHeight();
            newButton.setBounds(5, 5, 100, 24);
            saveButton.setBounds(110, 5, 100, 24);
            deleteButton.setBounds(215, 5, 100, 24);
            closeButton.setBounds(320, 5, 100, 24);
            labelId.setBounds(5, 39, 100, 24);
            id.setBounds(105, 39, w - 110, 24);
            labelTitle.setBounds(5, 73, 100, 24);
            title.setBounds(105, 73, w - 110, 24);
            labelQuery.setBounds(5, 107, 100, 24);
            jsp.setBounds(105, 107, w - 110, h - 112);
            query.setBounds(105, 107, w - 110, h - 112);

            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addQueryHistory() {
        String text = query.getText();
        int size = queryHistory.size();
        if (size > 0) {
            String last = queryHistory.get(size - 1);
            if (!last.equals(text)) {
                queryHistory.add(text);
                if (size > MAX_HISTORY_SIZE) {
                    queryHistory.remove(0);
                }
            }
        } else {
            queryHistory.add(text);
        }
    }

    protected void clearQueryHistory() {
        queryHistory.clear();
    }

    protected void restoreQueryFromHistory() {
        int size = queryHistory.size();
        if (size > 0) {
            String text = queryHistory.get(size - 1);
            queryHistory.remove(size - 1);
            query.setText(text);
        }
    }

    protected void saveQuery() {
        int r = JOptionPane.showConfirmDialog(null, "Save the query?");
        QueryMapper queryMapper = MapperUtils.getQueryMapper();
        if (r == 0) {
            String _id = id.getText();
            String _title = title.getText();
            String _query = query.getText();

            if (isEmpty(_id)) {
                JOptionPane.showMessageDialog(null, "ID cannot be empty string");
                return;
            }

            if (isEmpty(_title)) {
                JOptionPane.showMessageDialog(null, "Title cannot be empty string");
                return;
            }

            if (_title.length() < 10 || _title.length() > 128) {
                JOptionPane.showMessageDialog(null, "Title text cannot shorter than 10 or longer than 128");
                return;
            }

            if (isEmpty(_query)) {
                JOptionPane.showMessageDialog(null, "Query cannot be empty string");
                return;
            }

            try {
                entity.setTitle(_title);
                entity.setQuery(_query);
                if (entity.isOld()) {
                    queryMapper.editQuery(entity);
                } else {
                    entity.setId(_id);
                    queryMapper.addQuery(entity);
                }
                JOptionPane.showMessageDialog(null, "Query save successful");

                parent.requestUpdateQueryList();
                closeData();
            } catch (Throwable exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, exception.getMessage());
            }
        }
    }

    protected boolean isEmpty(String value) {
        if (value == null) return true;
        value = value.trim();
        return "".equals(value);
    }
}
