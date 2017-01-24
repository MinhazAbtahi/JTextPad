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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Abtahi
 */
public class FindDialog extends JDialog implements ActionListener {

    final int SCREEN_WIDTH = 300;
    final int SCREEN_HEIGHT = 100;

    TextPadUI parentFrame;

    JPanel findPanel;

    JLabel findLabel;
    JTextField findTextField;

    JButton findNextButton;
    JButton cancelButton;

    public FindDialog(TextPadUI parentFrame, boolean isModal) {
        super(parentFrame, isModal);
        this.parentFrame = parentFrame;
        this.setTitle("Find");
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

        findPanel = new JPanel();

        findLabel = new JLabel("Find Text: ");
        findTextField = new JTextField(20);

        findNextButton = new JButton("Find Next");
        findNextButton.setActionCommand("find next");
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");

        findNextButton.addActionListener(this);
        cancelButton.addActionListener(this);

        findPanel.add(findLabel);
        findPanel.add(findTextField);
        findPanel.add(findNextButton);
        //findPanel.add(cancelButton);

        this.getContentPane().add(findPanel, BorderLayout.CENTER);
        this.setIcon();
        this.pack();
        this.setLocationRelativeTo(parentFrame);
    }

    private void setIcon() {
        try {
            String iconPath = "Resources/findIcon.png";
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
                String text = parentFrame.textArea.getText();
                String inputText = this.findTextField.getText();

                int selectionStart = text.indexOf(inputText);
                int selectionEnd = inputText.length();

                if (selectionStart == -1) {
                    System.out.println("Not Found");
                } else {
                    parentFrame.textArea.select(selectionStart, selectionEnd + selectionStart);
                }

                //System.out.println(text);
                break;
            default:
                break;
        }
    }
}
