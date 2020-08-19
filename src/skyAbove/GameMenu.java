package skyAbove;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameMenu implements ActionListener {	
	public static JPanel MenuGameGlobal = new JPanel();
	public static GridBagLayout GridBagMenu = new GridBagLayout();

	public JButton b1 = new JButton("Carte");
	public JButton b2 = new JButton("Options");
    public JButton b3 = new JButton("Sauver");
    public JButton b4 = new JButton("Quitter");
    public JButton b5 = new JButton("retour");

	public void ShowGameMenu() {
		MenuGameGlobal.setLayout(GridBagMenu);
		MenuGameGlobal.setBackground(SimulationFrame.BleuCiel);
		
		SimulationFrame.menuFrame.add(MenuGameGlobal, BorderLayout.WEST);

	// les onglets (boutons) :
	   	// carte
		GridBagConstraints gbc_1 = new GridBagConstraints();
	   	b1.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	gbc_1.gridx = 0;
	   	gbc_1.gridy = 0;
	   	gbc_1.ipadx = gbc_1.anchor = GridBagConstraints.WEST;
	   	gbc_1.insets = new Insets(5, 5, 5, 5);
        MenuGameGlobal.add(b1, gbc_1);
	   	b1.addActionListener( this );
	   	
	   	// options
		GridBagConstraints gbc_2 = new GridBagConstraints();
	   	b2.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	gbc_2.gridx = 0;
	   	gbc_2.gridy = 1;
	   	gbc_2.ipadx = gbc_2.anchor = GridBagConstraints.WEST;
	   	gbc_2.insets = new Insets(5, 5, 5, 5);
        MenuGameGlobal.add(b2, gbc_2);
	   	b2.addActionListener( this );

	   	// sauvegarde
		GridBagConstraints gbc_3 = new GridBagConstraints();
	   	b3.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	gbc_3.gridx = 0;
	   	gbc_3.gridy = 2;
	   	gbc_3.ipadx = gbc_3.anchor = GridBagConstraints.WEST;
	   	gbc_3.insets = new Insets(5, 5, 5, 5);
	   	MenuGameGlobal.add(b3, gbc_3);
	   	b3.addActionListener( this );

	   	// quitter
		GridBagConstraints gbc_4 = new GridBagConstraints();
	    b4.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	gbc_4.gridx = 0;
	   	gbc_4.gridy = 3;
	   	gbc_4.ipadx = gbc_4.anchor = GridBagConstraints.WEST;
	   	gbc_4.insets = new Insets(5, 5, 5, 5);
	    MenuGameGlobal.add(b4, gbc_4);
	    b4.addActionListener( this );

		SimplePlatformer.GameMenuLoaded = true;
		Map GameMap = new Map();
		try {GameMap.main(); } catch (Exception e1) { e1.printStackTrace(); }
		SimulationFrame.CardGame.last(SimulationFrame.PanelsContainerGame);
}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Carte" :
			Map GameMap = new Map();
			try {GameMap.main(); } catch (Exception e1) { e1.printStackTrace(); }
			SimulationFrame.CardGame.last(SimulationFrame.PanelsContainerGame);
			break;
		case "Options" :
			optionsGame GameOptions = new optionsGame();
			try {GameOptions.showOptions(); } catch (Exception e1) { e1.printStackTrace(); }
			SimulationFrame.CardGame.last(SimulationFrame.PanelsContainerGame);
			break;
		case "Sauver" :
			sauvegarde GameSave = new sauvegarde();
			try {GameSave.Save(); } catch (Exception e1) { e1.printStackTrace(); }
			break;
		case "Quitter" :
			int halt = showConfirmDialog();
		    if(halt==0) {//si le bouton cliqué est "oui"
			     System.exit(0);
			}
			break;
		case "Retour" :
			SimulationFrame.CardGame.first(SimulationFrame.PanelsContainerGame);
			break;
		}
	}
	
	// boite de confirmation pour quitter le jeu
	// proposer de sauvegarder
	static int showConfirmDialog(){
		return JOptionPane.showConfirmDialog(
				null,
				"Voulez-vous vraiment quitter ?",
				"Quitter",
				JOptionPane.YES_NO_OPTION);
	}
}