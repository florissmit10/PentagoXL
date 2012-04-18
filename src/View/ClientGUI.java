package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import Model.*;
import Controller.*;



@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements Observer, MouseListener, ActionListener {
	//kleuren van de spelers
	public static final Color LEEG=UIManager.getColor( "Button.background" );
	public static final Color S1=Color.green;
	public static final Color S2=Color.blue;
	public static final Color S3=Color.red;
	public static final Color S4=Color.yellow;
	
	private JButton[][] vakjes=new JButton[Bord.DIM][Bord.DIM];
	private JButton[] draaiKnopjes=new JButton[4];
	private char[] draaiKnopRichtingen=new char[4];
	private JPanel bord, msgPanel;
	private JLabel msg;
	private JPanel[] subBord=new JPanel[Bord.DIM];
	private int huidigSubBordMuisIndex;
	private boolean moetDraaien=false;
	private Client client;
	private ChatPane chatpane;
	private int[] currentMove=new int[3];
	private char currentMoveDir;
	

	
	public ClientGUI(Client c){
		super("PentagoXL");
		this.client=c;
		
		init();
	}
	
	private void init(){
		Container c =this.getContentPane();
		this.setLayout(new BorderLayout());
		bord=new JPanel(new GridLayout(Bord.SUBDIM,Bord.SUBDIM, 10, 10));
		bord.setSize(480,480);
		
		msgPanel=new JPanel();
		msgPanel.setSize(480, 50);
		
		msg=new JLabel("Verbind met een server om een spel te starten.", SwingConstants.LEFT);
		msgPanel.add(msg);

		//elementen toevoegen aan de Frame
		c.add(bord, BorderLayout.CENTER);
		c.add(msgPanel, BorderLayout.SOUTH);
		chatpane=new ChatPane(client);
		c.add(chatpane, BorderLayout.EAST);
		maakSpeelveld();
		
		client.getSpel().addObserver(this);
		
		
		
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent ev) {
						
						try{
						client.shutdown();
						}catch(Exception e){}
						System.exit(0);
					}

				}
			);
		this.addComponentListener(new java.awt.event.ComponentAdapter() 
		{
			public void componentResized(ComponentEvent e)
			{

			}
		});
		this.setSize(1000,600);
		this.setVisible(true);
		setBordEnabled(false);
	}
	
		
		private void maakSpeelveld(){
			//Het maken van 9 subBorden.
			for(int i=0;i<Bord.DIM;i++){
			subBord[i]=new JPanel(new GridLayout(3,3));
			subBord[i].setSize(170, 170);
			bord.add(subBord[i]);
			}
			//Het maken van 81 knopjes en het plaatsen van deze knopjes op de subBorden.
			for(int y=0;y<Bord.DIM;y++){
				for(int x=0;x<Bord.DIM;x++){
					JButton jb=new JButton(" ");
					jb.setSize(50, 50);
					jb.addActionListener(this);
					jb.addMouseListener(this);
					jb.setEnabled(true);
					vakjes[x][y]=jb;
					subBord[client.getSpel().getBord().getSubBord(x,y)].add(jb);
				}
		}
	}
		/**
		 * Bethode die de coordinaten van een bepaalde JButton terugGeeft
		 * @param jb
		 * @return -1,-1 if jb==null||x,y
		 */
		private int[] getButtonIndex(JButton jb){
			int[] returnable=new int[2];
			returnable[0]=-1;
			returnable[1]=-1;
			for(int y=0;y<Bord.DIM;y++){
				for(int x=0;x<Bord.DIM;x++){
				if(jb.equals(vakjes[x][y])){
					returnable[0]=x;
					returnable[1]=y;
				}
				}				
				}
			
			
			return returnable;
		}
	
		

	/**
	 * Afhandeling van een action event.
	 * @param arg0 de actionEvent
	 */


	@Override
	public void actionPerformed(ActionEvent arg0) {
		//alleen als de Client aan de beurt is
		if (client.ownerOnTurn()) {
		if(arg0.getSource() instanceof JButton){
			JButton j =(JButton)arg0.getSource();
			//Draai het bord.
			if(moetDraaien){
				int i=0;
				for(JButton jb:draaiKnopjes){
					if(jb.equals(j)){
						
						currentMoveDir=draaiKnopRichtingen[i];
						currentMove[2]=huidigSubBordMuisIndex;
						client.doMove(currentMove[0], currentMove[1], currentMove[2], currentMoveDir);
						moetDraaien=false;
					}

					i++;
				}
				if(!moetDraaien){
					//zorg dat het bord niet meer draaibaar is, en dat de layout gedaan word.
				setSubBordDraaibaar(subBord[huidigSubBordMuisIndex], false);
				doLayout();
				}
			}else{
				//Plaats een marble op het bord.
					currentMove[0]=getButtonIndex(j)[0];
					currentMove[1]=getButtonIndex(j)[1];
					Bord copy= client.getSpel().getBord().deepCopy();
					copy.setVakje(currentMove[0], currentMove[1], client.getSpel().getHuidigSpeler().getMark());
					if(copy.heeftWinnaar()){
						
						//als de zet zorgt dat de speler wint dan word het draaien overgeslagen en word er een 
						//dummy draaizet meegegeven aan de client.
						currentMoveDir='+';
						currentMove[2]=0;
						client.doMove(currentMove[0], currentMove[1], currentMove[2], currentMoveDir);
						moetDraaien=false;
					}
					//Als het vakje leeg is dan word de kleur ingesteld op de kleur van de huidige speler.
					if(j.getBackground().equals(LEEG))
					j.setBackground(getColor(client.getSpel().getHuidigSpeler().getMark()));
					//zorg dat het subBord te draaien is.
					huidigSubBordMuisIndex=client.getSpel().getBord().getSubBord(getButtonIndex(j)[0], getButtonIndex(j)[1]);
					moetDraaien=true;
					setSubBordDraaibaar(subBord[huidigSubBordMuisIndex], true);
			}
		}
		}
		else {
			setMessage("Een andere speler is nu aan de beurt");
		}

		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	
	/**
	 * Methode die de mouseEntered event afhandeld. Dit word vooral gebruikt voor het tekenen van 
	 * de draaipijltjes op het bord wanneer dat nodig is.
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		if(arg0.getSource()instanceof JButton){
			//alleen als er een subBord gedraait moet worden.
			if(moetDraaien){
			JButton j=(JButton)arg0.getSource();
			//kijk op welk subBord de muis zich bevind.
		int index=client.getSpel().getBord().getSubBord(getButtonIndex(j)[0],getButtonIndex(j)[1]);
		if(index!=huidigSubBordMuisIndex){
		//als de muis naar een nieuw subBord gaat moet het nieuwe subBord draaibaar gemaakt worden, 
		// en het oude weer normaal.
			setSubBordDraaibaar(subBord[huidigSubBordMuisIndex],false);
			setSubBordDraaibaar(subBord[index],true);
			huidigSubBordMuisIndex=index;
		}
		}
		}
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	

	/**
	 * Stelt in dat een bepaald subBord draaibaar is of niet.
	 * @param jp het subBord.
	 * @param b boolean voor draaibaar of niet draaibaar.
	 */
	
	private void setSubBordDraaibaar(JPanel jp, boolean b){
			//de hulp arrays worden leeg gemaakt.
		for(int i=0;i<draaiKnopjes.length;i++){
				draaiKnopjes[i]=null;
				draaiKnopRichtingen[i]=' ';

			}		
		int i=0;
		int j=0;
		int[] subBord=client.getSpel().getBord().getSubBordVakjes(getSubBordIndex(jp));
		//Vakjes van het subBord worden doorgelopen. B
			for(int y=subBord[2];y<=subBord[3];y++){
				for(int x=subBord[0];x<=subBord[1];x++){
					//als b==true dan worden er pijltjes getekend op een aantal JButtons
					if(b){
						if(i==0){
							vakjes[x][y].setText("-->");
							draaiKnopjes[j]=vakjes[x][y];
							draaiKnopRichtingen[j]='+';
							j++;
						}else if(i==2){
							vakjes[x][y].setText("<--");
							draaiKnopjes[j]=vakjes[x][y];
							draaiKnopRichtingen[j]='-';
							j++;
						}else if(i==6){
							vakjes[x][y].setText("-->");
							draaiKnopjes[j]=vakjes[x][y];
							draaiKnopRichtingen[j]='-';
							j++;
						}else if(i==8){
							vakjes[x][y].setText("<--");
							draaiKnopjes[j]=vakjes[x][y];
							draaiKnopRichtingen[j]='+';
							j++;
						}
					i++	;
					//als b!=true worden alle JButtons leeg gemaakt.
					}else{
						
						vakjes[x][y].setText(" ");
					}
				}
			}
			doLayout();
	
	}
	/**
	 * methode die de kleur van een bepaalde mark teruggeeft. Deze worden bovenaan de methode gedeclareerd.
	 * @param m Mark
	 * @return LEEG||S1||S2||S3||S4
	 */
	private Color getColor(Model.Mark m){
		Color returnable=null;
		if(m.equals(Mark.LEEG)){
			returnable=LEEG;
		}else if(m.equals(Mark.S1)){
			returnable=S1;
		}else if(m.equals(Mark.S2)){
			returnable=S2;
		}else if(m.equals(Mark.S3)){
			returnable=S3;
		}else if(m.equals(Mark.S4)){
			returnable=S4;
		}
		return returnable;
	}

	
	/**
	 * Geeft de index van een bepaalde JPanel terug.
	 * @require j is a SubBord
	 * @param j
	 * @return returnable <=0||returnable <Bord.DIM
	 */
	private int getSubBordIndex(JPanel j){
		int returnable=-1;
		for(int i=0;i<9;i++){
			if(j.equals(subBord[i])){
				returnable=i;
			}
		}
		return returnable;
	}
/**
 * stuurt een request.
 * TestMethode.
 */
	public void request(){
		chatpane.request();
	}
	
	/**
	 * Update het bord.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof Spel){
			for(int y=0;y<Bord.DIM;y++){
				for(int x=0;x<Bord.DIM;x++){
					Mark m=client.getSpel().getBord().getVakje(x, y);
					vakjes[x][y].setBackground(getColor(m));
					
				}
			}
			
		}
		
	}
	/**
	 * Veranderd de melding in String s
	 * @param s
	 */
	public void setMessage(String s){msg.setText(s);
		doLayout();
	}
	/**
	 * voegt een String s toe aan de chatbox van chatpane
	 * @param s
	 */
	public void addChatMessage(String s){
		chatpane.addChatMessage(s);
	}
	/**
	 * probeert te connecten.
	 * TestMethode.
	 */
	public void connect(){
		chatpane.connect();
	}
/**
 * stelt in of de button Play, en de velden voor aantal spelers en strategie aanpasbaar zijn.
 * @param b
 */
	public void setPlayEnabled(boolean b){
		chatpane.setPlayEnabled(b);
	}
	
	/**
	 * Stelt in of de client geconnect is. Aan de hand hiervan worden andere dingen aangepast aan de GUI.
	 * @param b
	 */
	public void setConnected(boolean b){
		chatpane.setConnected(b);
	}
	/**
	 * stelt in of de hint knop enabled is.
	 * @param b
	 */
	public void setHintEnabled(boolean b){
		chatpane.setHintEnabled(b);
	}
	/**
	 * stelt in of het bord knoppen enabled zijn.
	 * @param b
	 */
	public void setBordEnabled(boolean b){
		for (JButton[] j:vakjes){
			for (JButton jb:j){
				jb.setEnabled(b);
			}
		}
	}
	/**
	 * Maakt alle knopjes van het bord de LEEG kleur.
	 */
	public void resetBord(){
		for(JButton[] j:vakjes){
			for(JButton jb:j){
				jb.setBackground(LEEG);
			}
		}
	}
	
	
}


