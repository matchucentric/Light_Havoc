import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import javax.imageio.*;

import java.awt.image.*;


public class RoomBuilder extends JFrame implements ActionListener {
	JPanel cards;
	CardLayout cLayout = new CardLayout();

	javax.swing.Timer myTimer;
	GamePanel game; 


	private int myTick; 

    public RoomBuilder() {

		super("Room builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1064, 700);


		game = new GamePanel(); 

		myTimer = new javax.swing.Timer(10, this);	 // trigger every 10 ms
		myTimer.start();

		cards = new JPanel(cLayout);
		cards.add(game, "game");
				
		add(cards);
		setResizable(false);
		setVisible(true);
    }

	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();

		if(game != null){
			game.requestFocus();
			game.refresh(myTick);
			game.repaint();
			if(source == myTimer){
				++myTick;
			}
		}	
	}

    public static void main(String[] args) {
		RoomBuilder frame = new RoomBuilder();
    }

}

class GamePanel extends JPanel implements MouseListener, KeyListener{

	private boolean[] keys; 
	private int importTick, holding, roomNum;
	private int[][] room;
	private boolean canInc, canDec, canFilePlus, canFileMinus;

	private ArrayList<String> imageFiles;
	private BufferedImage[] tiles;

	private final int rW = 16;
	private final int rH = 10;

	private final double[][] roomStarts = new double[][] {{0, 0.5}, {0, 3.5}, {1, 0}, {1, 1}, {1, 3}, 
														{1, 4}, {1.5, 2}, {2, 0.5}, {2, 3.5}, {2.5, 1.5}, 
														{2.5, 2.5}, {3, 0.5}, {3, 3.5}, {3.5, 2}, {4, 0}, 
														{4, 1}, {4, 3}, {4, 4}, {5, 0.5}, {5, 3.5}};

	public GamePanel(){
		addKeyListener(this);
		addMouseListener(this);
		keys = new boolean[KeyEvent.KEY_LAST+1];

		room = new int[rW][rH];
		canInc = true;
		canDec = true;
		canFilePlus = true;
		canFileMinus = true;
		roomNum = 2;
		holding = 1;


		Filefetcher imageGetter = new Filefetcher();
		imageFiles = imageGetter.showFiles(System.getProperty("user.dir") + "/environment");
		tiles = new BufferedImage[imageFiles.size()];

		for (int i = 0; i < imageFiles.size(); i++) {
			try {
			    tiles[i] = ImageIO.read(new File("environment/" + imageFiles.get(i)));

			} catch (IOException e) {System.out.println("Image not found");}
		}
	}

    public void refresh(int myTick){ 
    	importTick = myTick;

		if (keys[KeyEvent.VK_RIGHT]){
			if (canInc) {
				holding = Math.min(tiles.length - 1, holding + 1);
				canInc = false;
			}
		} else {
			canInc = true;
		}
		if (keys[KeyEvent.VK_LEFT]){
			if (canDec) {
				holding = Math.max(1, holding - 1);
				canDec = false;
			}
		} else {
			canDec = true;
		}

		if (keys[KeyEvent.VK_UP]){
			if (canFilePlus) {
				roomNum = Math.min(19, roomNum + 1);
				canFilePlus = false;
			}
		} else {
			canFilePlus = true;
		}
		if (keys[KeyEvent.VK_DOWN]){
			if (canFileMinus) {
				roomNum = Math.max(0, roomNum - 1);
				canFileMinus = false;
			}
		} else {
			canFileMinus = true;
		}	
		if (keys[KeyEvent.VK_S]){
			writeMap();
		}

		if (keys[KeyEvent.VK_Q] && keys[KeyEvent.VK_P]) {
			wipeArena();
		}
		//System.out.println(holding);	
	}

	@Override
    public void paintComponent(Graphics g){
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, 960, 640);
    	g.setColor(Color.BLACK);
    	for (int i = 0; i < rW + 1; i++) {
    		g.fillRect(i * 64 - 2, 0, 4, rH * 64);
    	}
    	for (int i = 0; i < rH + 1; i++) {
    		g.fillRect(0, i * 64 - 2, rW * 64, 4);
    	}

    	for (int i = 0; i < rW; i++) {
	    	for (int j = 0; j < rH; j++) {
	    		g.drawImage(tiles[room[i][j]], i * 64, j * 64, null);
	    	}
	    }

    	Point m = MouseInfo.getPointerInfo().getLocation();
    	g.drawImage(tiles[holding], m.x - 32, m.y - 64, null);

    	g.setColor(Color.BLACK);
    	g.drawString("RoomNum: " + roomNum, 50, 50);

    } 

    public void wipeArena() {
    	String line = "";
    	PrintWriter filler = null;
    	for(int i = 0; i < 6 * rW; i++) {
    		line += " ";
    	}
    	try {
    	filler = new PrintWriter(new FileWriter(new File("rooms/arena.txt")));
	    } catch (IOException e) {
	    	System.out.println("File not found");
	    }
		for (int i = 0; i < 5 * rH; i++) {
			filler.println(line);
		}

		filler.close();
	}

	public void open() {
		BufferedReader get = new BufferedReader(new FileReader("rooms/arena.txt"));

		for(int i = 0; i < )
	}

    public void writeMap() {
    	try {
    		BufferedReader firstGet = new BufferedReader(new FileReader("rooms/arena.txt"));

    		String[] lines = new String[5 * rH];
    		String line;
    		int counter = 0;

    		while ((line = firstGet.readLine()) != null ) {
    			System.out.println(counter + "." + ((int)(roomStarts[roomNum][1] * rH)) + "." + ((int)(roomStarts[roomNum][1] * rH) + rH - 1) );
    			if ((int)(roomStarts[roomNum][1] * rH) <= counter && (int)(roomStarts[roomNum][1] * rH) + rH - 1 >= counter) {
    				//System.out.println(line.length());
    				String bef = line.substring(0, (int)(roomStarts[roomNum][0] * rW));
    				String aft = line.substring((int)(roomStarts[roomNum][0] * rW) + rW - 1, line.length() - 1);
    				String mid = "";

    				for (int i = 0; i < rW; i++) {
    					System.out.println(counter + " " + (i) + " " + ((int)(roomStarts[roomNum][1] * rH) + counter));
    					mid += (char)(room[i][counter - (int)(roomStarts[roomNum][1] * rH)] + 32);
    					//mid += "" + roomNum;

    				}
    				System.out.println(bef + "." + mid + "." + aft);

    				line = bef + mid + aft;
    			}
    			lines[counter] = line;
    			counter += 1;
    		}

    		firstGet.close();
    		PrintWriter filler = new PrintWriter(new FileWriter(new File("rooms/arena.txt")));
    		for (int i = 0; i < lines.length; i++) {
    			filler.println(lines[i]);
    		}

    		filler.close();
    	} catch (IOException e) {
    		System.out.println("File Error");
    	}
    	

    	for (int i = 0; i < rW; i++) {
    		for (int j = 0; j < rH; j++) {
    			
    		}
    		System.out.println();
    	}
    }	

	public void keyTyped(KeyEvent e) {

	} 
    public void keyPressed(KeyEvent e) {
    	setKey(e.getKeyCode(),true);

    }
    public void keyReleased(KeyEvent e) {
    	setKey(e.getKeyCode(),false);
    }
    public void setKey(int k, boolean v) {
    	keys[k] = v;
    }  

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e){
    	if (e.getX() / 64 < rW && e.getY() / 64 < rH) {
    		room[(int)e.getX() / 64][(int)e.getY() / 64] = holding;

    	}
	}
}

