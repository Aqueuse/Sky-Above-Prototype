package skyAbove;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameMenu implements ActionListener {
	public static JPanel Gameglobal = new JPanel();
	public static CardLayout card = new CardLayout();

    public static JPanel GamePanelsContainer = new JPanel();
	public static GridBagLayout GridBag = new GridBagLayout();

	public JButton b1 = new JButton("Carte");
	public JButton b2 = new JButton("Options");
    public JButton b3 = new JButton("Sauvegarde");
    public JButton b4 = new JButton("Quitter");
		
	public GameMenu() {
		Gameglobal.setLayout(GridBag);
		GamePanelsContainer.setLayout(card);

		Gameglobal.setBackground(new Color(173, 216, 230));
		Gameglobal.add(GamePanelsContainer);

	// les onglets (boutons) :
	   	// carte
	   	b1.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	GridBagConstraints gbc_b1 = new GridBagConstraints();
	   	gbc_b1.insets = new Insets(0, 0, 5, 5);
	   	gbc_b1.gridx = 1;
	   	gbc_b1.gridy = 1;
	   	Gameglobal.add(b1, gbc_b1);
	   	b1.addActionListener( this );

	   	// options
	   	b2.setFont(new Font("SansSerif", Font.PLAIN, 30));
        GridBagConstraints gbc_b2 = new GridBagConstraints();
        gbc_b2.insets = new Insets(0, 0, 5, 5);
        gbc_b2.gridx = 1;
        gbc_b2.gridy = 2;
        Gameglobal.add(b2, gbc_b2);
	   	b2.addActionListener( this );

	   	// sauvegarde
	   	b3.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	GridBagConstraints gbc_b3 = new GridBagConstraints();
	   	gbc_b3.insets = new Insets(0, 0, 5, 5);
	   	gbc_b3.gridx = 1;
	   	gbc_b3.gridy = 3;
	   	Gameglobal.add(b3, gbc_b3);
	   	b3.addActionListener( this );

	   	// quitter (confirmer)
	    b4.setFont(new Font("SansSerif", Font.PLAIN, 30));
	    GridBagConstraints gbc_b4 = new GridBagConstraints();
	    gbc_b4.insets = new Insets(0, 0, 0, 5);
	    gbc_b4.gridx = 1;
	    gbc_b4.gridy = 4;
	    Gameglobal.add(b4, gbc_b4);
	    b4.addActionListener( this );
	    
	    // on crée une instance des classes majeures
		@SuppressWarnings("unused")
		Map MapInstance = new Map();
		@SuppressWarnings("unused")
		optionsGame OptionsInstance = new optionsGame();
		@SuppressWarnings("unused")
		sauvegarde sauvegardeInstance = new sauvegarde();
		// le jeu lui-même
		SimplePlatformer simulation;
		try { // load the game
			simulation = new SimplePlatformer();
			simulation.run();
		} catch (IOException e1) { e1.printStackTrace();}
	}
	
	// boite de confirmation pour quitter le jeu
	static int showConfirmDialog(){
		return JOptionPane.showConfirmDialog(
				null,
				"Voulez-vous vraiment quitter ?",
				"Quitter",
				JOptionPane.YES_NO_OPTION);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Carte" :
			try {
			Map.showMap(); } catch (Exception e1) { e1.printStackTrace(); }
			card.last(GamePanelsContainer);
			break;
		case "Options" :
			break;
		case "Sauvegarde" :
			break;
		case "Quitter" :
			int halt = showConfirmDialog();
		    if(halt==0) {//si le bouton cliqué est "oui"
			     System.exit(0);
			}
			break;
		}		
	}
}