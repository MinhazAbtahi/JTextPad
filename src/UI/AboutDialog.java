/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Abtahi
 */
public class AboutDialog extends JDialog {

    final int SCREEN_WIDTH = 320;
    final int SCREEN_HEIGHT = 400;

    JPanel aboutPanel;
    JButton okButton;

    public AboutDialog(JFrame parentFrame, boolean isModal) {
        super(parentFrame, isModal);
        this.setTitle("About TextPad");
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        aboutPanel = new JPanel();

        okButton = new JButton("Ok");

        aboutPanel.setLayout(null);
        aboutPanel.add(okButton);

        this.getContentPane().add(aboutPanel, BorderLayout.CENTER);
        this.setIcon();
        this.setLocationRelativeTo(parentFrame);
    }

    private void setIcon() {
        try {
            String iconPath = "Resources/aboutIcon.png";
            ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(iconPath));
            this.setIconImage(imageIcon.getImage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
