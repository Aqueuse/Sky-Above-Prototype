package skyAbove;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.Font;
import java.awt.Color;

// le menu de demarrage du jeu (charger/creer un jeu)
public class menu implements ActionListener {
	public static JFrame frame = new JFrame();
	public static CardLayout card = new CardLayout();
	
	public static JPanel PanelsContainer = new JPanel();
	public static JPanel global = new JPanel();

	public static JButton b1 = new JButton("Nouveau");
	public static JButton b2 = new JButton("Charger");
	public static JButton b3 = new JButton("Options");
	public static JButton b4 = new JButton("Quitter");
	
	public static void main(String[] args) {
		menu newMenu = new menu();
		newMenu.menuGeneral();
}

	public void menuGeneral() {
		frame.setTitle("Sky Above Prototype");
	    frame.setSize(1280, 768);
	    frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		try {UIManager.setLookAndFeel( new FlatLightLaf() );} catch (UnsupportedLookAndFeelException e) {e.printStackTrace();}

	    PanelsContainer.setLayout(card);
	    frame.add(PanelsContainer);
	    
	    // LES MENUS global/nouveau/charger/options
	    // doivent être ajoutés en tant que cards
		GridBagLayout gridy = new GridBagLayout();
	    global.setBackground(new Color(173, 216, 230));
	    global.setLayout(gridy);

	   	// nouveau (creer un nouveau monde)
	   	b1.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	GridBagConstraints gbc_b1 = new GridBagConstraints();
	   	gbc_b1.insets = new Insets(0, 0, 5, 5);
	   	gbc_b1.gridx = 1;
	   	gbc_b1.gridy = 1;
	   	global.add(b1, gbc_b1);
	   	b1.addActionListener(this);

	   	// charger (charger un monde)
	   	b2.setFont(new Font("SansSerif", Font.PLAIN, 30));
        GridBagConstraints gbc_b2 = new GridBagConstraints();
        gbc_b2.insets = new Insets(0, 0, 5, 5);
        gbc_b2.gridx = 1;
        gbc_b2.gridy = 2;
        global.add(b2, gbc_b2);
	   	b2.addActionListener(this);

	   	// options (volume, musique, pour l'instant)
	   	b3.setFont(new Font("SansSerif", Font.PLAIN, 30));
	   	GridBagConstraints gbc_b3 = new GridBagConstraints();
	   	gbc_b3.insets = new Insets(0, 0, 5, 5);
	   	gbc_b3.gridx = 1;
	   	gbc_b3.gridy = 3;
	   	global.add(b3, gbc_b3);
	   	b3.addActionListener(this);

	   	// quitter (confirmer)
	    b4.setFont(new Font("SansSerif", Font.PLAIN, 30));
	    GridBagConstraints gbc_b4 = new GridBagConstraints();
	    gbc_b4.insets = new Insets(0, 0, 0, 5);
	    gbc_b4.gridx = 1;
	    gbc_b4.gridy = 4;
	    global.add(b4, gbc_b4);
		b4.addActionListener(this);

	    PanelsContainer.add(global);
		// wait for all the elements to be loaded
		// before setting the frame visible
	    frame.setVisible(true);
	    
	    frame.addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosing(WindowEvent e) {
	            // Ask for confirmation before terminating the program.
	    		int option = JOptionPane.showConfirmDialog(
	    			frame, 
	    			"Are you sure you want to close the application?",
	    			"Close Confirmation", 
	    			JOptionPane.YES_NO_OPTION, 
	    			JOptionPane.QUESTION_MESSAGE);
	    		if (option == JOptionPane.YES_OPTION) {
	    			System.exit(0);
	    		}
	    	}
	    });
	}
	
	/// interaction du menu
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Nouveau" :
			createNewLandscape newLandscapes = new createNewLandscape();
			try { newLandscapes.menuNouveau(); } catch (Exception e1) { e1.printStackTrace(); }
			card.last(PanelsContainer);
			break;
		case "Charger" :
			LoadFile newLoad = new LoadFile();
			try { newLoad.openFile() ; } catch (Exception e2) { e2.printStackTrace(); }
			card.last(PanelsContainer);
			card.last(PanelsContainer);
			break;
		case "Options" :
			options newOptions = new options();
			try { newOptions.menuOptions(); } catch (Exception e2) { e2.printStackTrace(); }
			card.last(PanelsContainer);
			card.last(PanelsContainer);
			card.last(PanelsContainer);
			break;
		case "Quitter" :
			int halt = showConfirmDialog();
		    if(halt==0) {//si le bouton cliqué est "oui"
			     System.exit(0);
			}
			break;
		}
	}

	// boite de confirmation pour quitter le jeu
	static int showConfirmDialog(){
		return JOptionPane.showConfirmDialog(
				null,
				"Voulez-vous vraiment quitter ?",
				"Quitter",
				JOptionPane.YES_NO_OPTION);
	}
}