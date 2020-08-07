package skyAbove;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class pause extends JComponent implements ActionListener {
    public final static JPanel PanPause = new JPanel();
    private final static JButton save = new JButton("sauvegarder");
//    private final static JButton optionsGame = new JButton("options");
//    private final static JButton quitGame = new JButton("quitter");

	public void paintLandscape () throws IOException {
 	    GridBagLayout gbl_pause = new GridBagLayout();
 	    gbl_pause.columnWeights = new double[]{10, 10};
 	    gbl_pause.rowWeights = new double[]{10, 10};
 	    PanPause.setLayout(gbl_pause);

 	    // bouton sauvegarder
     	GridBagConstraints gbc_save = new GridBagConstraints();
     	gbc_save.insets = new Insets(5, 5, 5, 5);
     	gbc_save.gridx = 0;
     	gbc_save.gridy = 0;
     	gbc_save.anchor = GridBagConstraints.CENTER;
    	save.setFont(new Font("SansSerif", Font.PLAIN, 30));
 	    save.addActionListener(this);

		// lecture de l'image
		JLabel imgLabel = new JLabel(new ImageIcon("images/calico_kittyjpg.jpg"));

		PanPause.add(save, gbc_save);
		PanPause.add(imgLabel);
	}

	public void showPause() {
		menu.PanelsContainer.add(PanPause);
	}

	public void hidePause() {
		menu.PanelsContainer.remove(PanPause);
	}
	
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "sauvegarder" :
			System.out.println("sauvegarder");
			hidePause();
			// affiche le Pan pause par dessus le panGame
			break;
		}
	}
}