package skyAbove;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class options extends JComponent implements ActionListener {
    public static JPanel PanOptions = new JPanel();
    private static JButton flecheRetour = new JButton("Retour");
    private static JSlider musique = new JSlider();

    public void menuOptions() {    	
 	    GridBagLayout gbl_Options = new GridBagLayout();
 	    gbl_Options.columnWeights = new double[]{10, 10};
 	    gbl_Options.rowWeights = new double[]{10, 10};
 		PanOptions.setBackground(new Color(191, 191, 191));
	    PanOptions.setLayout(gbl_Options);

    	// bouton retour
     	GridBagConstraints gbc_retour = new GridBagConstraints();
     	gbc_retour.insets = new Insets(5, 5, 5, 5);
     	gbc_retour.gridx = 0;
     	gbc_retour.gridy = 0;
     	gbc_retour.anchor = GridBagConstraints.NORTHWEST;
    	flecheRetour.setFont(new Font("SansSerif", Font.PLAIN, 30));
 	    flecheRetour.addActionListener(this);

	    // bouton musique
    	GridBagConstraints gbc_musique = new GridBagConstraints();
     	gbc_musique.insets = new Insets(5, 5, 5, 5);
     	gbc_musique.gridx = 0;
     	gbc_musique.gridy = 0;
     	gbc_musique.anchor = GridBagConstraints.CENTER;
		musique.setFont(new Font("SansSerif", Font.PLAIN, 30));

 	    PanOptions.add(flecheRetour, gbc_retour);
    	PanOptions.add(musique, gbc_musique);
    	menu.PanelsContainer.add(PanOptions);
    }

	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case "Retour" :
			menu.PanelsContainer.remove(PanOptions);
			menu.card.first(menu.PanelsContainer);
			break;
		}
	}
}