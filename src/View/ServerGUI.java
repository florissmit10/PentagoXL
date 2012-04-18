package View;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import javax.swing.*;

import Controller.*;

@SuppressWarnings("serial")
public class ServerGUI extends JFrame
                       implements  KeyListener, ActionListener {
    


    private JTextField  tfPort, tfHostname, tfMymessage;
	private JTextArea taMessages;

    private Server server;
	private JButton bConnect;
	private JScrollPane scrollPane;


    /** Construeert een ClientGUI object. */
    public ServerGUI() {
        super("PentagoXL server");
        buildGUI();
        setVisible(true);}
    
    public static void main(String[] args){
    	new ServerGUI();
    }

    /** Bouwt de daadwerkelijke GUI. */
    private void buildGUI() {
    	setSize(400,800);

      
    	JPanel p1 = new JPanel(new FlowLayout());
        JPanel pp = new JPanel(new GridLayout(4,3));
        //hostnaam label en veld.
        JLabel lbHostname = new JLabel("Hostnaam: ");
        tfHostname = new JTextField(getHostAddress(), 12);
        tfHostname.setEditable(false);
      //port label en veld.
        JLabel lbPort = new JLabel("Poort:");
        tfPort        = new JTextField("2727", 5);
        tfPort.addKeyListener(this);
        


        
       //Toeveogen host label en veld op JPanel pp
        pp.add(lbHostname);
        pp.add(tfHostname);       
        //Toeveogen port label en veld op JPanel pp        
        pp.add(lbPort);
        pp.add(tfPort);

        //Maken van een JButton om te hosten
        bConnect = new JButton("Host");
        bConnect.addActionListener(this);
        bConnect.setEnabled(true);

        //toevoegen van JPanel pp en JButton Host op JPanel p1
        p1.add(pp, BorderLayout.WEST);
        p1.add(bConnect, BorderLayout.EAST);
        //Bouw het chatvenster.
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        

        
        JPanel p21 = new JPanel();
        JPanel p22 = new JPanel();
        
        p21.setLayout(new BorderLayout());
        //My message label en textveld voor invoer.
        JLabel lbMymessage = new JLabel("My message:");
        tfMymessage = new JTextField("");
        tfMymessage.setEnabled(true);
        tfMymessage.addKeyListener(this);
        //message label en textveld worden toegevoegd.
        p21.add(lbMymessage);
        p21.add(tfMymessage, BorderLayout.SOUTH);

        p22.setLayout(new BorderLayout());
        //Messages label en ScrollPane met alle chatberichten.
        JLabel lbMessages = new JLabel("Messages:");
        taMessages = new JTextArea("", 35, 33);
        taMessages.setEditable(false);
        scrollPane = new JScrollPane(taMessages);
        //messagelabel en scrollpane worden toegevoegd.
        p22.add(lbMessages);
        p22.add(scrollPane, BorderLayout.SOUTH);
        
        p2.add(p21, BorderLayout.NORTH); 
        p2.add(p22, BorderLayout.SOUTH);

      
        p1.add(p2, BorderLayout.SOUTH);
       
        
        
        this.setLayout(new BorderLayout());

        this.add(p1, BorderLayout.CENTER);
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent ev) {
						System.exit(0);
					}

				}
			);
    }
/**
 * Methode die het hostadress van deze server teruggeeft
 * @return
 */
    private String getHostAddress() {
        try {
            InetAddress iaddr = InetAddress.getLocalHost();
            return iaddr.getHostAddress();
        } catch (UnknownHostException e) {
            return "?unknown?";
        }
    }
    
    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    /**
     * Methode die een KeyReleased event afhandeld
     */
    public void keyReleased(KeyEvent e) {
    	//Kijk of alle velden voor hosten correct zijn ingevuld
    	if (e.getSource() == tfPort) {
    		
    		if (isInteger(tfPort.getText())) {
    			bConnect.setEnabled(true);
    		}
    		else { bConnect.setEnabled(false);}
    		}
    	//Als de bron tfMymessage is en de knop Enter is dan word er een chatbericht verzonden.
    	else if(e.getSource()==tfMymessage){
    		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
    			server.broadcast("Server: "+tfMymessage.getText());
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
			Integer.parseInt(tfPort.getText());
			returnable=true;
    	}catch(NumberFormatException ex){
    			returnable=false;
    		}
    	return returnable;
    	
    }
/**
 * //Als de source de hostbutton is dan maakt of verbreekt hij de verbinding.
 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==bConnect){
			startListening();
			
			
		}
		
	}
	/**
	 * Methode die een nieuwe server start en laat luisteren op de opgegeven port
	 */
	public void startListening() {
		int port = 0;

        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {}
        if(portAvailable(port)){
	        tfPort.setEditable(false);
	        bConnect.setEnabled(false);
	
	        server = new Server(port, this);
	        server.start();
	        addMessage("Hosten op port "+port);
		}else{
			addMessage("De opgegeven poort is in gebruik");
		}
    }
	/**
	 * voegt een bericht toe aan de chatbox.
	 * @param s
	 */
	public void addMessage(String s){
		taMessages.append(s);
		taMessages.selectAll();
	}
/**
 * Kijkt of de gegeven port in gebruik is.
 * @param port
 * @return
 */
	private boolean portAvailable(int port){
		boolean returnable=true;
		try{
		ServerSocket ss=new ServerSocket(port);
		
		ss.close();
		ss=null;
		}
		catch(IOException e){
		returnable=false;	
		}
		
	return returnable;
	}
    

} // end of class ClientGUI
