import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class OmokUI extends JFrame {
	private final int WINDOW_WIDTH = 600;
	private final int WINDOW_HEIGHT = 600;

	private final int BOARD_HEIGHT = 500;
	private final int BOARD_WIDTH = 500;

	private final int INIT_WIDTH = 300;
	private final int INIT_HEIGHT = 60;

	private final int ROWS = 10;
	private final int COLS = 10;

	private final int BORDER_SPACE = 50;

	private final int START_X = WINDOW_WIDTH / 2 - BOARD_WIDTH / 2	- BORDER_SPACE;
	private final int START_Y = 0;

	private final int END_X = START_X + BOARD_WIDTH + BORDER_SPACE * 2;
	private final int END_Y = START_Y + BOARD_HEIGHT + BORDER_SPACE * 2;

	private final int CELL_WIDTH = BOARD_HEIGHT / COLS;
	private final int CELL_HEIGHT = BOARD_HEIGHT / ROWS;
	OmokController oc = null;

	private final int DESKTOP_WIDTH;
	private final int DESKTOP_HEIGHT;
	
	private final int POST_X = 20;
	private final int POST_Y = 20;
	String postString = "Your Turn";
	
	private int currentPosition_X = -1;
	private int currentPosition_Y = -1;
	Graphics2D g2d = null;
	Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	OmokUI ui;
	public OmokUI(OmokController oc) {
		this.oc = oc;
		ui = this;
		// ChatBox a = new ChatBox();
		JButton btnCancel1 = new JButton("Cancel");
		JButton btnCancel2 = new JButton("Cancel");
		
		
		JPanel btnPanel = new JPanel();
		JButton btnServer = new JButton("Server");
		JButton btnClient = new JButton("Client");
		btnPanel.add(btnServer);
		btnPanel.add(btnClient);

		JPanel serverPanel = new JPanel();;
		JLabel waitLabel = new JLabel();
		
		waitLabel.setText("WAITING...");
		
		serverPanel.add(waitLabel);
		serverPanel.add(btnCancel1);
		
		JPanel clientPanel = new JPanel();
		JTextField ipField = new JTextField();
		
		JButton btnOK = new JButton("OK");
		
		ipField.setPreferredSize( new Dimension( 100, 24 ) );
		clientPanel.add(ipField);
		clientPanel.add(btnOK);
		clientPanel.add(btnCancel2);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		btnCancel1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clearFrame();
				getContentPane().add(btnPanel,"Center");
				getContentPane().repaint();
				oc.stopToWaitClient();
				
			}
		});
		btnCancel2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clearFrame();
				getContentPane().add(btnPanel,"Center");
				getContentPane().repaint();
				
			}
		});
	
		btnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFrame();
				getContentPane().add(serverPanel,"Center");
				getContentPane().repaint();
				oc.toWaitClient(ui);
			}
		});
		
		
		
		btnClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFrame();
				getContentPane().add(clientPanel,"Center");
				getContentPane().repaint();
			}
		});
		
		btnOK.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String ipAddress = ipField.getText();
				
				if(oc.toConnactServer(ipAddress,ui)){
					start();
					//show game board
										
				}
			}
		});
		
		setLayout(new BorderLayout());
		add(btnPanel, "Center");
		setSize(INIT_WIDTH, INIT_HEIGHT);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		DESKTOP_WIDTH = (int)screenSize.getWidth();
		DESKTOP_HEIGHT = (int)screenSize.getHeight();
		
		setTitle("OMOK");
		setLocation(DESKTOP_WIDTH/2 - INIT_WIDTH/2,DESKTOP_HEIGHT/2 - INIT_HEIGHT/2);
		setVisible(true);
		
		//start();
	}

	public void clearFrame(){
		getContentPane().removeAll();
		getContentPane().revalidate();
	}
	
	public void start() {
		clearFrame();
		
		setLocation(DESKTOP_WIDTH/2 - WINDOW_WIDTH/2,DESKTOP_HEIGHT/2 - WINDOW_HEIGHT/2);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setLayout(new BorderLayout());
		add(new GameBoardUI(), "Center");
		
		System.out.println("GAME UI START");
		
		repaint();
	}

	class GameBoardUI extends JPanel {

		// int [][] state = new int[ROWS][COLS];

		public GameBoardUI() {
			this.setBackground(Color.WHITE);
			this.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					int x = ((e.getX() - START_X) / CELL_WIDTH) - 1;
					int y = ((e.getY() - START_Y) / CELL_HEIGHT) - 1;
					
					if (oc.isMyTuren() && x >= 0 && y >= 0 && x < 10 && y < 10) {
						oc.toClick(true, x, y);
						
						System.out.println("[" + y + "][" + x + "]");
						
						repaint();
					}
				}
					
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					currentPosition_X =	currentPosition_Y = -1;				
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				
			});
			
			this.addMouseMotionListener(new MouseMotionListener(){

				@Override
				public void mouseDragged(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					currentPosition_X = ((e.getX() - START_X) / CELL_WIDTH) - 1;
					currentPosition_Y = ((e.getY() - START_Y) / CELL_HEIGHT) - 1;
					
					
					repaint();
				}	
				
			});
		}

		public void repaint() {
			super.repaint();
		}

		
		public void drawSton(int x, int y, int i) {

		}

		
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(g2d == null){
				g2d = (Graphics2D) g.create();
				dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				g2d.setColor(Color.RED);
				g2d.setStroke(dashed);
			}
			g.setColor(new Color(204, 102, 0, 255));
			g.fill3DRect(START_X, START_Y, BOARD_WIDTH + BORDER_SPACE * 2,
					BOARD_HEIGHT + BORDER_SPACE * 2, false);

			g.setColor(Color.BLACK);
			
			g.drawString(postString, POST_X, POST_Y);
			
		
			for (int i = 0; i <= ROWS; i++) {
				g.drawLine(START_X + BORDER_SPACE, START_Y + BORDER_SPACE + i
						* (BOARD_HEIGHT / ROWS), END_X - BORDER_SPACE, START_Y
						+ BORDER_SPACE + i * (BOARD_HEIGHT / ROWS));
				g.drawLine(START_X + BORDER_SPACE + i * (BOARD_WIDTH / ROWS),
						START_Y + BORDER_SPACE, START_X + BORDER_SPACE + i
								* (BOARD_WIDTH / ROWS), END_Y - BORDER_SPACE);
			}

			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					if (oc.getXYInfo(j, i) == 1) {
						g.setColor(Color.BLACK);
						g.fillOval(START_X + j * CELL_WIDTH + BORDER_SPACE, START_Y + i * CELL_HEIGHT+BORDER_SPACE, CELL_WIDTH, CELL_HEIGHT);
					} else if (oc.getXYInfo(j, i) == 2) {
						g.setColor(Color.WHITE);
						g.fillOval(START_X + j * CELL_WIDTH+BORDER_SPACE, START_Y + i * CELL_HEIGHT+BORDER_SPACE, CELL_WIDTH, CELL_HEIGHT);
					}
				}
			}
			
			if(oc.isMyTuren() && currentPosition_X >= 0 && currentPosition_Y >= 0 && currentPosition_X < 10 && currentPosition_Y < 10 ){
				g.setColor(Color.RED);
				g2d.drawOval(START_X + currentPosition_X * CELL_WIDTH+BORDER_SPACE, START_Y + currentPosition_Y * CELL_HEIGHT+BORDER_SPACE, CELL_WIDTH, CELL_HEIGHT);
				
			}
			
			
			
		}
	}

	class ChatBox extends JPanel {
		JTextArea chatArea = new JTextArea();
		JTextField inputBox = new JTextField();

		public ChatBox() {

			this.setLayout(new BorderLayout());
			this.add(chatArea, "Center");
			this.add(inputBox, "South");

		}

	}

}
