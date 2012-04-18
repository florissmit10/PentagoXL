package View;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Controller.*;

@SuppressWarnings("serial")
public class ChatPane extends JPanel
                       implements  KeyListener, ActionListener {
    


    private JTextField  tfPort, tfHostname, tfName, tfMymessage;
    private JComboBox tfPlayers;

    private JTextArea   taMessages;
    private JScrollPane	scrollPane;
    private Client client;
	private JButton bConnect, bHint;
	private final Integer[] pNumbers=new Integer[]{2,3,4};
	private final String[] strategies=new String[]{"MensSpeler", "DommeStrategie", "SlimmeStrategie"};
	private JComboBox tfStrat;
	private JTextField tfTime;
	private JButton bPlay;


    /** Construeert een ClientGUI object. */

    public ChatPane(Client c) {
        super();
        buildGUI();
        setVisible(true);
        client=c;    
    }

    /** Bouwt de daadwerkelijke GUI. */
    private void buildGUI() {
    	setSize(200,400);

      
    	JPanel southPanel = new JPanel(new FlowLayout());
        JPanel settingsPanel = new JPanel(new GridLayout(6,3));
        //hostnaam label en veld.
        JLabel lbHostname = new JLabel("Hostnaam: ");
        tfHostname = new JTextField("localhost",12);
        tfHostname.addKeyListener(this);
        //port label en veld.
        JLabel lbPort = new JLabel("Poort:");
        tfPort        = new JTextField("2727", 5);
        tfPort.addKeyListener(this);
        //Naam label en veld.
        JLabel lbName = new JLabel("Naam:");
        String naam="speler"+(int)(Math.random()*100);
        tfName        = new JTextField(naam, 12);
        tfName.addKeyListener(this);
        //Aantal spelers label en comboBox
        JLabel lbPlayers = new JLabel("Aantal spelers: ");
        tfPlayers        = new JComboBox(pNumbers);
        tfPlayers.setSelectedIndex(0);
        tfPlayers.setEnabled(false);
        tfPlayers.addActionListener(this);
        //strategie label en combobox
        JLabel lbStrat = new JLabel("Selecteer strategie: ");
        tfStrat        = new JComboBox(strategies);
        tfStrat.setEnabled(false);
        tfStrat.setSelectedIndex(0);
        tfStrat.addActionListener(this);
        //denktijd label en veld.
        JLabel lbTime=new JLabel("Selecteer denktijd(ms): ");
        tfTime			=new JTextField("10000");
        tfTime.addActionListener(this);
        tfTime.setEnabled(false);
       
        //connect button
        bConnect = new JButton("Connect");
        bConnect.addActionListener(this);
        bConnect.setEnabled(true);
        //hint button
        bHint=new JButton("Hint");
        bHint.addActionListener(this);
        bHint.setEnabled(false);
        //play button
        bPlay=new JButton("Play");
        bPlay.addActionListener(this);
        bPlay.setEnabled(false);
        //alle knoppen en velden worden toegevoegd aan settingsPanel.
        settingsPanel.add(lbHostname);
        settingsPanel.add(tfHostname);
        settingsPanel.add(bConnect);
        settingsPanel.add(lbPort);
        settingsPanel.add(tfPort);
        settingsPanel.add(new JLabel(""));
        settingsPanel.add(lbName);
        settingsPanel.add(tfName);
        settingsPanel.add(new JLabel(""));
        settingsPanel.add(lbPlayers);
        settingsPanel.add(tfPlayers);
        settingsPanel.add(new JLabel(""));
        settingsPanel.add(lbStrat);
        settingsPanel.add(tfStrat);
        settingsPanel.add(bPlay);
        settingsPanel.add(lbTime);
        settingsPanel.add(tfTime);
        settingsPanel.add(bHint);

        
        //SettingsPanel word aan southPanel toegevoegd
        southPanel.add(settingsPanel, BorderLayout.WEST);

        
        // Panel - Messages

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        

        
        JPanel chatTitlePanel = new JPanel();
        JPanel chatPanel = new JPanel();       
        chatTitlePanel.setLayout(new BorderLayout());
        //My message label en textveld voor invoer.
        JLabel lbMymessage = new JLabel("My message:");
        tfMymessage = new JTextField("");
        tfMymessage.setEnabled(true);
        tfMymessage.addKeyListener(this);
        //message label en textveld worden toegevoegd.
        chatTitlePanel.add(lbMymessage);
        chatTitlePanel.add(tfMymessage, BorderLayout.SOUTH);

        chatPanel.setLayout(new BorderLayout());
        //Messages label en ScrollPane met alle chatberichten.
        JLabel lbMessages = new JLabel("Messages:");
        taMessages = new JTextArea("", 10, 33);
        taMessages.setEditable(false);
        scrollPane = new JScrollPane(taMessages);
        chatPanel.add(lbMessages);
        chatPanel.add(scrollPane, BorderLayout.SOUTH);
        //messagelabel en scrollpane worden aan NorthPanel toegevoegd
        northPanel.add(chatTitlePanel, BorderLayout.NORTH); 
        northPanel.add(chatPanel, BorderLayout.SOUTH);

      //northPanel en SouthPanel worden aan deze ChatPane toegevoegd.
        this.setLayout(new BorderLayout());
        this.add(northPanel, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.SOUTH);
    }

   
    
    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    /**
     * Methode voor afhandeling van een KeyReleased event.
     */
    public void keyReleased(KeyEvent e) {
    	//Kijk of alle velden om te connecten correct zijn ingevuld
    	if (e.getSource() == tfHostname) {
    		if (isInteger(tfPort.getText())&&tfPort.getText().length() != 0 && tfName.getText().length() != 0) {
    			bConnect.setEnabled(true);
    		}
    		else { bConnect.setEnabled(false);}
    	}
     	//Kijk of alle velden om te connecten correct zijn ingevuld
    	else if (e.getSource() == tfName) {
    		if (isInteger(tfPort.getText())&&tfPort.getText().length() != 0 && tfHostname.getText().length() != 0) {
    			bConnect.setEnabled(true);
    		}
    		else { bConnect.setEnabled(false);}
    	}
     	//Kijk of alle velden om te connecten correct zijn ingevuld
    	else if (e.getSource() == tfPort) {
    		
    		if (isInteger(tfPort.getText())&&tfHostname.getText().length() != 0 && tfName.getText().length() != 0) {
    			bConnect.setEnabled(true);
    		}
    		else { bConnect.setEnabled(false);}
    		}
    	//als de bron het denktijd veld is word gekeken of het een positive integer is. 
    	//Als dat zo is word Play enabled. Anders word Play gedisabled.
    	else if (e.getSource() == tfTime) {
    		
    		if (isInteger(tfTime.getText())) {
    			bPlay.setEnabled(true);
    		}
    		else { bPlay.setEnabled(false);}
    		}
    		
    	//Als de bron tfMymessage is en de knop Enter is dan word er een chatbericht verzonden.
    	else if (e.getSource() == tfMymessage) {
    		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
    			client.chat(tfMymessage.getText()); 
    			
    			tfMymessage.setText("");
    		}
    	}
    }
    /**
     * Kijk of de string een Integer is.
     * @param text
     * @return
     */
    private boolean isInteger(String text){
    	boolean returnable=false;
    	try {
			Integer.parseInt(text);
			returnable=true;
    	}catch(NumberFormatException ex){
    			returnable=false;
    		}
    	return returnable;
    	
    }
/**
 * Methode die aangeroepen word als er een action event is.]
 * 
 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Als de source connect is dan maakt of verbreekt hij de verbinding.
		if(arg0.getSource()==bConnect){
			if(bConnect.getText().equals("Connect"))
			connect();
			else if(bConnect.getText().equals("Disconnect")){
				client.shutdown();
				client.exitSocket();
			}
			
		
		}
		//als de source play is dan word er een request gestuurd naar de server.
		else if(arg0.getSource()==bPlay){
			request();
			
		}
		//Als de source hint is dan word er een hint afgedrukt op het chatvenster.
		else if(arg0.getSource()==bHint){
			giveHint();
		}
		//als de source de strategiecombobox is dan word gekeken of er een AI geselecteerd is. 
		//als dat zo is dan word de denktijd textfield geenabled of gedisabled als het niet zo is. 
		else if(arg0.getSource()==tfStrat){
			if(tfStrat.getSelectedIndex()==0){
				tfTime.setEnabled(false);
			}
			else{
				tfTime.setEnabled(true);
			}
				
		}
		
	}
	/**
	 * Vraagt de client om een hint en print deze in de chatbox.
	 */
	private void giveHint() {
		int[] zet=client.getHint();
		String direction=(zet[3]>0?" met de klok mee":" tegen de klok in"+'\n');
		String hint="Zet het vakje ("+zet[0]+","+zet[1]+") en draai: sub bord: "+zet[2]+direction;
		addChatMessage("Je hebt gevraagd om een hint. De volgende hint is gegevendoor een Domme strategie:"+'\n');
		addChatMessage(hint);
	}
/**
 * Verzameld alle benodigde informatie van de GUI en roept hiermee de request methode van de client aan.
 */
	public void request(){
		int players = (Integer)tfPlayers.getSelectedItem();
		int time=Integer.parseInt(tfTime.getText());
		client.request(strategies[tfStrat.getSelectedIndex()],players, time);
		
	}
	
	/**
	 * Verzameld alle benodigde informatie van de GUI en roept hiermee de connect methode van de client aan.
	 */
	public void connect(){
		client.connect(tfHostname.getText(), Integer.parseInt(tfPort.getText()), tfName.getText());
		

		
	}
	/**
	 * voegt een chatbericht toe aan de chatbox.
	 * @param s
	 */
	public void addChatMessage(String s){
		taMessages.append(s);
		taMessages.selectAll();
		
	}
	/**
	 * stelt in of de button Play, en de velden voor aantal spelers en strategie aanpasbaar zijn.
	 * @param b
	 */
	public void setPlayEnabled(boolean b){
		bPlay.setEnabled(b);
		tfStrat.setEnabled(b);
		tfPlayers.setEnabled(b);
		if(tfStrat.getSelectedIndex()!=0)
		tfTime.setEnabled(b);
		
		
		doLayout();
	}
	/**
	 * Stelt in of de client geconnect is. Aan de hand hiervan worden andere dingen aangepast aan de GUI.
	 * @param b
	 */
	public void setConnected(boolean b){
		String state=(b?"Disconnect":"Connect");
		bConnect.setText(state);
		tfPort.setEnabled(!b);
		tfHostname.setEnabled(!b);
		tfName.setEnabled(!b);
		doLayout();
	}
	/**
	 * stelt in of de hint knop enabled is.
	 * @param b
	 */
	public void setHintEnabled(boolean b){
		bHint.setEnabled(b);
		doLayout();
	}
    

} // end of class ClientGUI
