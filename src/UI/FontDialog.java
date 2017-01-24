/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Abtahi
 */
public class FontDialog extends JDialog implements ActionListener, ItemListener {

    final int SCREEN_WIDTH = 500;
    final int SCREEN_HEIGHT = 200;

    private GraphicsEnvironment graphicsEnvironment;
    private Font selectedFont;
    private TextPadUI parentFrame;

    String fontFamilyNames[];
    String[] fontSizes = {
        "9", "10", "11", "12",
        "14", "16", "18", "20",
        "22", "24", "28", "36",
        "48", "72"};
    String[] fontStyles = {
        "PLAIN",
        "BOLD",
        "ITALIC",
        "BOLD ITALIC"};
    
    JPanel fontPanel;

    JButton okButton;
    JButton cancelButton;

    JComboBox fontFamilyNameComboBox;
    JComboBox fontStyleComboBox;
    JComboBox fontSizeComboBox;

    JLabel fontPreviewLabel;
    TitledBorder titledBorder;

    public FontDialog(TextPadUI parentFrame, boolean isModal) {
        super(parentFrame, isModal);
        this.parentFrame = parentFrame;
        this.setTitle("Fonts");
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Sets System wise UI Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        fontPanel = new JPanel();

        fontFamilyNameComboBox = new JComboBox(this.getFontFamilyList());
        fontFamilyNameComboBox.setEditable(true);
        fontFamilyNameComboBox.setMaximumRowCount(5);
        //fontFamilyNameComboBox.setPopupVisible(true);

        fontStyleComboBox = new JComboBox(this.fontStyles);
        fontStyleComboBox.setEditable(true);
        fontStyleComboBox.setMaximumRowCount(5);
        //fontStyleComboBox.showPopup();

        fontSizeComboBox = new JComboBox(this.fontSizes);
        fontSizeComboBox.setEditable(true);
        fontSizeComboBox.setMaximumRowCount(5);
        //fontSizeComboBox.showPopup();

        fontFamilyNameComboBox.addItemListener(this);
        fontStyleComboBox.addItemListener(this);
        fontSizeComboBox.addItemListener(this);

        fontFamilyNameComboBox.addActionListener(this);
        fontSizeComboBox.addActionListener(this);
        fontStyleComboBox.addActionListener(this);
        
        titledBorder = new TitledBorder("Font Preview");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 12));
        titledBorder.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JPanel p = new JPanel();
        p.setBorder(titledBorder);
        fontPreviewLabel = new JLabel("The quick brown fox jumps over the lazy dog");
        fontPreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        fontPreviewLabel.setFont(selectedFont);
        p.add(fontPreviewLabel);

        okButton = new JButton("Ok");
        okButton.setActionCommand("okButton");

        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancelButton");

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        fontPanel.add(fontFamilyNameComboBox);
        fontPanel.add(fontStyleComboBox);
        fontPanel.add(fontSizeComboBox);
        fontPanel.add(okButton);
        fontPanel.add(cancelButton);

        this.getContentPane().add(fontPanel, BorderLayout.EAST);
        this.getContentPane().add(p, BorderLayout.SOUTH);
        this.setIcon();
        this.setLocationRelativeTo(parentFrame);
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    private void setSelectedFont(Font setSelectedFont) {
        this.selectedFont = setSelectedFont;
    }

    private String[] getFontFamilyList() {
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontFamilyNames = graphicsEnvironment.getAvailableFontFamilyNames();

        return fontFamilyNames;
    }

    private void setIcon() {
        try {
            String iconPath = "Resources/fontIcon.png";
            ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(iconPath));
            this.setIconImage(imageIcon.getImage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "okButton":
                String fontFamilyName = (String) this.fontFamilyNameComboBox.getSelectedItem();
                String style = (String) this.fontStyleComboBox.getSelectedItem();
                int fontStyle = - 1;

                switch (style) {
                    case "PLAIN":
                        fontStyle = Font.PLAIN;
                        break;
                    case "BOLD":
                        fontStyle = Font.BOLD;
                        break;
                    case "ITALIC":
                        fontStyle = Font.ITALIC;
                        break;
                    case "BOLD ITALIC":
                        fontStyle = Font.BOLD + Font.ITALIC;
                        break;
                    default:
                        break;
                }

                int fontSize = Integer.parseInt((String) this.fontSizeComboBox.getSelectedItem());
                this.setSelectedFont(new Font(fontFamilyName, fontStyle, fontSize));
                this.parentFrame.setSelectedFont(this.getSelectedFont());
                this.dispose();
                break;
            case "cancelButton":
                this.dispose();
                break;
            default:
                break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        int state = e.getStateChange();

        if (state == ItemEvent.SELECTED) {
            String fontFamilyName = (String) this.fontFamilyNameComboBox.getSelectedItem();
            String style = (String) this.fontStyleComboBox.getSelectedItem();
            int fontStyle = - 1;

            switch (style) {
                case "PLAIN":
                    fontStyle = Font.PLAIN;
                    break;
                case "BOLD":
                    fontStyle = Font.BOLD;
                    break;
                case "ITALIC":
                    fontStyle = Font.ITALIC;
                    break;
                case "BOLD ITALIC":
                    fontStyle = Font.BOLD + Font.ITALIC;
                    break;
                default:
                    break;
            }

            int fontSize = Integer.parseInt((String) this.fontSizeComboBox.getSelectedItem());
            this.setSelectedFont(new Font(fontFamilyName, fontStyle, fontSize));
            this.fontPreviewLabel.setFont(this.getSelectedFont());
        }
    }
}
