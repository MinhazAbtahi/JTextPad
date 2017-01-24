/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Abtahi
 */
public class ReplaceDialog extends JDialog implements ActionListener {

    final int SCREEN_WIDTH = 320;
    final int SCREEN_HEIGHT = 90;

    TextPadUI parentFrame;

    JPanel replacePanel;

    JLabel findLabel;
    JLabel replaceLabel;

    JTextField findTextField;
    JTextField replaceTextField;

    JButton findNextButton;
    JButton replaceButton;

    public ReplaceDialog(TextPadUI parentFrame, boolean isModal) {
        super(parentFrame, isModal);
        this.parentFrame = parentFrame;
        this.setTitle("Replace");
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Sets System wise UI Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        replacePanel = new JPanel();

        findLabel = new JLabel("Find Text: ");
        replaceLabel = new JLabel("Replace: ");

        findTextField = new JTextField(20);
        replaceTextField = new JTextField(20);

        findNextButton = new JButton("Find Next");
        findNextButton.setActionCommand("find next");
        replaceButton = new JButton("Replace");
        replaceButton.setActionCommand("replace");

        findNextButton.addActionListener(this);
        replaceButton.addActionListener(this);

        replacePanel.add(findLabel);
        replacePanel.add(findTextField);
        replacePanel.add(findNextButton);
        replacePanel.add(replaceLabel);
        replacePanel.add(replaceTextField);
        replacePanel.add(replaceButton);

        this.getContentPane().add(replacePanel, BorderLayout.CENTER);
        this.setIcon();
        this.pack();
        this.setLocationRelativeTo(parentFrame);
    }

    private void setIcon() {
        try {
            String iconPath = "Resources/replaceIcon.png";
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
            case "find next":
                System.out.println("find");
                String text = parentFrame.textArea.getText();
                String inputText = this.findTextField.getText();

                int selectionStart = text.indexOf(inputText);
                int selectionEnd = inputText.length();

                if (selectionStart == -1) {
                    System.out.println("Not Found");
                } else {
                    parentFrame.textArea.select(selectionStart, selectionEnd + selectionStart);
                }
                break;
            case "replace":
                System.out.println("replace");
                String selectedText = parentFrame.textArea.getSelectedText();
                String newText = this.replaceTextField.getText();

                parentFrame.textArea.replaceSelection(newText);
                break;
            default:
                break;
        }
    }

}
