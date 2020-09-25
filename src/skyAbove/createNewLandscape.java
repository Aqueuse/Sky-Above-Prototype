package skyAbove;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class createNewLandscape implements ActionListener {
    public static JPanel PanNouveau = new JPanel();
    public static JTextField nameWorld = new JTextField(" mon monde ");
    private final static JButton flecheRetour = new JButton("Retour");
    private final static JButton creer = new JButton("Créer");
	public static boolean ADNgen = false;

    public void menuNouveau() throws Exception {
    	GridBagLayout gbl_PanNouveau = new GridBagLayout();
 	    PanNouveau.setBackground(new Color(173, 216, 230));
 	    gbl_PanNouveau.columnWeights = new double[]{10, 10};
 	    gbl_PanNouveau.rowWeights = new double[]{10, 10};
 	    PanNouveau.setLayout(gbl_PanNouveau);

     	GridBagConstraints gbc_retour = new GridBagConstraints();
     	gbc_retour.insets = new Insets(5, 5, 5, 5);
     	gbc_retour.gridx = 0;
     	gbc_retour.gridy = 0;
     	gbc_retour.anchor = GridBagConstraints.NORTHWEST;
    	flecheRetour.setFont(new Font("SansSerif", Font.PLAIN, 30));
 	    flecheRetour.addActionListener(this);
    	PanNouveau.add(flecheRetour, gbc_retour);

     	GridBagConstraints gbc_texteField = new GridBagConstraints();
     	gbc_texteField.insets = new Insets(5, 5, 5, 5);
     	gbc_texteField.gridx = 1;
     	gbc_texteField.gridy = 0;
     	gbc_texteField.anchor = GridBagConstraints.CENTER;
    	nameWorld.setFont(new Font("SansSerif", Font.PLAIN, 30));
		nameWorld.addActionListener(this);
    	PanNouveau.add(nameWorld, gbc_texteField);

     	GridBagConstraints gbc_creer = new GridBagConstraints();
     	gbc_creer.insets = new Insets(5, 5, 5, 5);
     	gbc_creer.gridx = 1;
     	gbc_creer.gridy = 1;
     	gbc_creer.anchor = GridBagConstraints.SOUTHEAST;
 	    creer.setFont(new Font("SansSerif", Font.PLAIN, 30));
 	    creer.addActionListener(this);
 	    PanNouveau.add(creer, gbc_creer);

    	menu.PanelsContainer.add(PanNouveau, BorderLayout.CENTER);
 	    }
    
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Retour" :
			menu.PanelsContainer.remove(PanNouveau);
			menu.card.first(menu.PanelsContainer);
			break;
		case "Créer" :
			try {
				ADNgen = true;
				generateWorld.generate();
				while (ADNgen == true) {
					wait();
				}
				
				LoadFile.loadWorld(generateWorld.cheminNewWorld, generateWorld.cheminNewPlayer);
				SimplePlatformer simulation = new SimplePlatformer(null, 0);
				simulation.run();
				menu.PanelsContainer.remove(createNewLandscape.PanNouveau);
				menu.card.last(menu.PanelsContainer);
			}
			catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
				}
			break;
		}
	}
}