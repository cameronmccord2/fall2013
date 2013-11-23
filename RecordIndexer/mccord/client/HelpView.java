package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import models.Fields;

public class HelpView extends JPanel {
	
	private ClientGUI cg;
	private JTabbedPane helpView;
	
	public HelpView(ClientGUI cg){
		super();
		
		this.cg = cg;
		this.setLayout(new BorderLayout());
		
		helpView = new JTabbedPane();
		helpView.setVisible(true);
		
		JPanel fieldHelp = new JPanel();
		helpView.addTab("Field Help", fieldHelp);
		helpView.setMnemonicAt(0, KeyEvent.VK_1);
		
		JPanel imageNavigation = new JPanel();
		helpView.addTab("Image Navigation", imageNavigation);
		helpView.setMnemonicAt(1, KeyEvent.VK_2);
		helpView.setVisible(true);
		
		this.add(helpView);
		this.setVisible(true);
	}
	
	public void populateTabs(ClientGUI cg){
		
	}

	public void changeSelectedField(int fieldIndex) {
		System.out.println("change selected field");
		Fields field = cg.batch.getFields().get(fieldIndex);
		String finalLabel = "";
		String labelPiece;
		try {
			URL helpFieldURL = new URL(field.getHelpHtml());
			BufferedReader in = new BufferedReader(new InputStreamReader(helpFieldURL.openStream()));
			
			while((labelPiece = in.readLine()) != null){
				finalLabel += labelPiece;
				continue;
			}
			finalLabel = finalLabel.substring(15);
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("final label: " + finalLabel);
			JLabel label = new JLabel(finalLabel);
			label.setVisible(true);
			JPanel fieldHelp = new JPanel();
			fieldHelp.setLayout(new BorderLayout());
			fieldHelp.add(label);
			fieldHelp.setBackground(Color.white);
			helpView.setComponentAt(0, fieldHelp);
		}
	}
}
