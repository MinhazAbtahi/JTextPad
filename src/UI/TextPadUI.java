/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Utilities.JVMDispatcher;
import Utilities.PrintUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author abtahi
 */
public class TextPadUI extends JFrame implements ActionListener, ItemListener, UndoableEditListener, CaretListener {

    // Dimensions
    static final int SCREEN_WIDTH = 1024;
    static final int SCREEN_HEIGHT = 800;

    // MenuBar, Menus and MenuItems
    JMenuBar menuBar;

    JMenu fileMenu;
    JMenu editMenu;
    JMenu formatMenu;
    JMenu viewMenu;
    JMenu toolsMenu;
    JMenu helpMenu;

    // File Menu Items
    JMenuItem newMenuItem;
    JMenuItem openMenuItem;
    JMenuItem saveMenuItem;
    JMenuItem saveAsMenuItem;
    JMenuItem printMenuItem;
    JMenuItem exitMenuItem;

    // Edit Menu Items
    JMenuItem undoMenuItem;
    JMenuItem cutMenuItem;
    JMenuItem copyMenuItem;
    JMenuItem pasteMenuItem;
    JMenuItem deleteMenuItem;
    JMenuItem findMenuItem;
    JMenuItem replaceMenuItem;
    JMenuItem selectAllMenuItem;

    // Format Menu Items
    JCheckBoxMenuItem wordWrapCheckBoxMenuItem;
    JMenuItem fontMenuItem;

    // View Menu Items
    JCheckBoxMenuItem statusBarCheckBoxMenuItem;

    // Help Menu Items
    JMenuItem helpMenuItem;
    JMenuItem aboutMenuItem;

    // Popup Menu
    JPopupMenu popupMenu;

    JMenuItem undoPopupMenuItem;
    JMenuItem cutPopupMenuItem;
    JMenuItem copyPopupMenuItem;
    JMenuItem pastePopupMenuItem;
    JMenuItem deletePopupMenuItem;
    JMenuItem selectAllPopupMenuItem;

    // FileChooser
    JFileChooser fileChooser;

    // ToolBar and Buttons
    JToolBar statusToolBar;

    JLabel caretStatusLabel;
    JButton runButton;
    JButton exitButton;

    // TextArea
    JTextArea textArea;
    JScrollPane scrollPane;
    UndoManager undoManager;

    // Tabbed Pane
    JTabbedPane tab;

    // FontDialog
    FontDialog fontDialog;

    boolean isEdited;

    public TextPadUI() {
        super("TextPad");
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setIcon();

        // Sets System wise UI Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        this.undoManager = new UndoManager();
        this.fontDialog = new FontDialog(this, true);
        this.isEdited = false;

        // Initializes and sets MenuBar, Menus and MenuItems
        this.addMenuBar();

        // Initializes and sets File Menus and MenuItems
        this.addFileMenuItems();

        // Initializes and sets Edit Menus and MenuItems
        this.addEditMenuItems();

        // Initializes and sets Format Menus and MenuItems
        this.addFormatMenuItems();

        // Initializes and sets View Menus and MenuItems
        this.addViewMenuItems();

        // Initializes and sets Help Menus and MenuItems
        this.addHelpMenuItems();

        // Initializes and sets Popup Menu
        this.addPopupMenu();

        // Initializes and sets ToolBar and Buttons
        this.addStatusToolBar();

        // Initializes and sets TextArea, ScrollPane
        this.addTextArea();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isEdited) {
                    windowCloseDialog();
                } else {
                    System.exit(0);
                }
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void windowCloseDialog() {
        int selectedOption = JOptionPane.showConfirmDialog(this, "Do you want to save shanges?", "TextPad", JOptionPane.YES_NO_CANCEL_OPTION);

        switch (selectedOption) {
            case JOptionPane.YES_OPTION:
                this.openSaveDialog();
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                System.exit(0);
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
            default:
                break;
        }
    }

    private void addMenuBar() {
        // MenuBar
        menuBar = new JMenuBar();

        // Menus
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        formatMenu = new JMenu("Format");
        viewMenu = new JMenu("View");
        toolsMenu = new JMenu("Tools");
        helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        // Adds MenuBar to the Frame 
        this.setJMenuBar(menuBar);
    }

    private void addFileMenuItems() {
        // File MenuItems
        newMenuItem = new JMenuItem("New");
        newMenuItem.setActionCommand("new");
        openMenuItem = new JMenuItem("Open");
        openMenuItem.setActionCommand("open");
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setActionCommand("save");
        saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.setActionCommand("saveAs");
        printMenuItem = new JMenuItem("Print...");
        printMenuItem.setActionCommand("print");
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("exit");

        fileChooser = new JFileChooser();

        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        saveAsMenuItem.addActionListener(this);
        printMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
    }

    private void addEditMenuItems() {
        // Edit MenuItems
        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.setActionCommand("undo");
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setActionCommand("cut");
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setActionCommand("copy");
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setActionCommand("paste");
        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setActionCommand("delete");
        findMenuItem = new JMenuItem("Find...");
        findMenuItem.setActionCommand("find");
        replaceMenuItem = new JMenuItem("Replace...");
        replaceMenuItem.setActionCommand("replace");
        selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.setActionCommand("select all");

        copyMenuItem.addActionListener(this);
        cutMenuItem.addActionListener(this);
        pasteMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);
        findMenuItem.addActionListener(this);
        replaceMenuItem.addActionListener(this);
        selectAllMenuItem.addActionListener(this);

        editMenu.add(undoMenuItem);
        editMenu.addSeparator();
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.add(deleteMenuItem);
        editMenu.addSeparator();
        editMenu.add(findMenuItem);
        editMenu.add(replaceMenuItem);
        editMenu.addSeparator();
        editMenu.add(selectAllMenuItem);
    }

    private void addFormatMenuItems() {
        wordWrapCheckBoxMenuItem = new JCheckBoxMenuItem("Word Wrap");
        fontMenuItem = new JMenuItem("Fonts...");
        fontMenuItem.setActionCommand("fonts");

        wordWrapCheckBoxMenuItem.addItemListener(this);
        fontMenuItem.addActionListener(this);

        formatMenu.add(wordWrapCheckBoxMenuItem);
        formatMenu.addSeparator();
        formatMenu.add(fontMenuItem);
    }

    private void addViewMenuItems() {
        statusBarCheckBoxMenuItem = new JCheckBoxMenuItem("Status Bar");

        statusBarCheckBoxMenuItem.addItemListener(this);

        viewMenu.add(statusBarCheckBoxMenuItem);
    }

    private void addHelpMenuItems() {
        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.setActionCommand("help");
        aboutMenuItem = new JMenuItem("About TextPad...");
        aboutMenuItem.setActionCommand("about");

        helpMenuItem.addActionListener(this);
        aboutMenuItem.addActionListener(this);

        helpMenu.add(helpMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutMenuItem);
    }

    private void addPopupMenu() {
        popupMenu = new JPopupMenu();

        undoPopupMenuItem = new JMenuItem("Undo");
        undoPopupMenuItem.setActionCommand("undo");
        cutPopupMenuItem = new JMenuItem("Cut");
        cutPopupMenuItem.setActionCommand("cut");
        copyPopupMenuItem = new JMenuItem("Copy");
        copyPopupMenuItem.setActionCommand("copy");
        pastePopupMenuItem = new JMenuItem("Paste");
        pastePopupMenuItem.setActionCommand("paste");
        deletePopupMenuItem = new JMenuItem("Delete");
        deletePopupMenuItem.setActionCommand("delete");
        selectAllPopupMenuItem = new JMenuItem("Select All");
        selectAllPopupMenuItem.setActionCommand("select all");

        popupMenu.add(undoPopupMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(cutPopupMenuItem);
        popupMenu.add(copyPopupMenuItem);
        popupMenu.add(pastePopupMenuItem);
        popupMenu.add(deletePopupMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(selectAllPopupMenuItem);

        undoPopupMenuItem.addActionListener(this);
        cutPopupMenuItem.addActionListener(this);
        copyPopupMenuItem.addActionListener(this);
        pastePopupMenuItem.addActionListener(this);
        deletePopupMenuItem.addActionListener(this);
        selectAllPopupMenuItem.addActionListener(this);
    }

    private void addStatusToolBar() {
        statusToolBar = new JToolBar();
        statusToolBar.setRollover(true);
        statusToolBar.setFloatable(false);

        caretStatusLabel = new JLabel("Line: " + 1 + " Column: " + 0);

        runButton = new JButton("Run");
        runButton.setToolTipText("Builds and Runs the Program");
        runButton.setActionCommand("run");

        exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exits the the Application");
        exitButton.setActionCommand("exit");

        runButton.addActionListener(this);
        exitButton.addActionListener(this);

        statusToolBar.add(Box.createHorizontalGlue());
        statusToolBar.addSeparator();
        statusToolBar.add(caretStatusLabel);
        statusToolBar.add(new JLabel("        "));
        //toolBar.add(exitButton);

        this.add(statusToolBar, BorderLayout.SOUTH);
    }

    private void addTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 15));
        textArea.setTabSize(4);

        textArea.addCaretListener(this);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        textArea.addMouseListener(new MouseAdapter() {
            // Shows PopupMenu
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        Document doc = textArea.getDocument();
        doc.addUndoableEditListener(this);

        this.add(scrollPane);
    }

    private void setIcon() {
        try {
            String iconPath = "Resources/notepadIcon.png";
            ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            this.setIconImage(imageIcon.getImage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSelectedFont(Font selectedFont) {
        textArea.setFont(selectedFont);
    }

    private void updateMenuItems() {
        this.getContentPane().validate();
        undoMenuItem.setEnabled(undoManager.canUndo());
        undoPopupMenuItem.setEnabled(undoManager.canUndo());
    }

    private void openFileChooserDialog() {
        int openState = fileChooser.showOpenDialog(this);

        switch (openState) {
            case JFileChooser.APPROVE_OPTION:
                File file = fileChooser.getSelectedFile();
                FileReader fileReader = null;

                try {
                    fileReader = new FileReader(file);
                    textArea.read(fileReader, "Opening File");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                System.out.println(file.getName() + " Selected");
                break;
            case JFileChooser.CANCEL_OPTION:
                System.out.println("Canceled");
                break;
            case JFileChooser.ERROR_OPTION:
                System.out.println("Error");
                break;
            default:
                break;
        }
    }
    
    private void openSaveDialog() {
        int saveAsState = fileChooser.showSaveDialog(this);

        switch (saveAsState) {
            case JFileChooser.APPROVE_OPTION:
                File file = fileChooser.getSelectedFile();
                FileWriter fileWriter = null;

                try {
                    fileWriter = new FileWriter(file);
                    fileWriter.write(textArea.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                System.out.println(file.getName() + " Selected");
                break;
            case JFileChooser.CANCEL_OPTION:
                System.out.println("Canceled");
                break;
            case JFileChooser.ERROR_OPTION:
                System.out.println("Error");
                break;
            default:
                break;
        }
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (statusBarCheckBoxMenuItem.isSelected()) {
            //this.add(statusToolBar);
        } else if (wordWrapCheckBoxMenuItem.isSelected()) {
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
        } else {
            textArea.setLineWrap(false);
            textArea.setWrapStyleWord(false);
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        try {
            int caretPosition = textArea.getCaretPosition();
            int lineNumber = textArea.getLineCount();

            System.out.println(lineNumber + ":" + caretPosition);
            caretStatusLabel.setText("Line: " + lineNumber + " Column: " + caretPosition);

            this.isEdited = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand() + "MenuItem Selected");

        switch (e.getActionCommand()) {
            case "new":
                textArea.setText("");
                break;
            case "open":
                this.openFileChooserDialog();
                break;
            case "saveAs":
                this.openSaveDialog();
                break;
            case "print":
                PrintUtilities.printComponent(textArea);
                break;
            case "run":
                JVMDispatcher jvm = new JVMDispatcher();
                jvm.buildAndRun();
                break;
            case "exit":
                System.exit(0);
                break;
            case "undo":
                try {
                    if (undoManager.canUndo()) {
                        System.out.println(undoManager.canUndo());
                        undoManager.undo();
                    }
                } catch (CannotUndoException ex) {
                    ex.printStackTrace();
                }
            case "redo":
                try {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                } catch (CannotRedoException ex) {
                    ex.printStackTrace();
                }
                break;
            case "cut":
                textArea.cut();
                break;
            case "copy":
                textArea.copy();
                break;
            case "paste":
                textArea.paste();
                break;
            case "delete":
                textArea.replaceSelection("");
                break;
            case "select all":
                textArea.selectAll();
                break;
            case "find":
                FindDialog findDialog = new FindDialog(this, false);
                findDialog.setVisible(true);
                break;
            case "replace":
                ReplaceDialog replaceDialog = new ReplaceDialog(this, false);
                replaceDialog.setAlwaysOnTop(true);
                break;
            case "fonts":
                // FoontChooser Dialog
                fontDialog.setVisible(true);
                break;
            case "help":
                // Blank
                break;
            case "about":
                // About Dialog
                AboutDialog aboutDialog = new AboutDialog(this, true);
                aboutDialog.setVisible(true);
                break;
            default:
                break;
        }
    }
}
