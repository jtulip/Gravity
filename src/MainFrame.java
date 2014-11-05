import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class MainFrame extends JFrame {
	
	private static final int XPOS = 400;
	private static final int YPOS = 100;
	private static final int XDIM = 1600;
	private static final int YDIM = 1200;
	
	private static final int SLEEP_INTERVAL = 20;
	private static final double ZOOM_SCALE = 0.05;
	public static final double G = 0.0005;
	
	
	private static BufferStrategy strategy;
	private static double zoomFactor;
	

	private static MainFrame frame;
	private JPanel contentPane;
	
	private static volatile boolean go_on = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		zoomFactor = 1.0;
		ArrayList<Particle> particles = new ArrayList<Particle>();
//		particles.add(new Particle(XDIM/2, YDIM/2, -0.002, 0.0, 200000, Color.RED));
//		particles.add(new Particle(XDIM/2, YDIM/4, 0.5, 0.0, 50, Color.ORANGE));
//		particles.add(new Particle(XDIM/5, YDIM/2, 0.0, -0.5, 2000, Color.YELLOW));
//		particles.add(new Particle(XDIM/5+40, YDIM/2, 0.0, -(0.50+0.15), 5, Color.GREEN));
		particles.add(new Particle(XDIM*2.0+22, YDIM/2, 0.0, 0.2-0.03805, 200, Color.BLUE));
		particles.add(new Particle(XDIM*2.0-22, YDIM/2, 0.0, 0.2+0.03805, 200, Color.MAGENTA));
		
		frame = new MainFrame();
		frame.setVisible(true);
		gameLoop(particles);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		this.setTitle("Gravity");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(XPOS, YPOS, XDIM, YDIM);
		
		contentPane = new JPanel();
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				double zoomChange = arg0.getPreciseWheelRotation();
				zoomFactor += zoomChange*ZOOM_SCALE;
				System.out.printf("Zoomfactor = %.3f\n", zoomFactor);
			}
		});
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				go_on = false;
			}
		});
		contentPane.setBorder(null);
		contentPane.setPreferredSize(new Dimension(XDIM-10,YDIM-10));
		contentPane.setLayout(null);
		contentPane.setBounds(0, 0, XDIM, YDIM);
		setContentPane(contentPane);
		contentPane.setBackground(Color.BLACK);
		
		this.setIgnoreRepaint(true);
		this.pack();
		this.setResizable(false);
		
		this.createBufferStrategy(2);
		strategy = this.getBufferStrategy();
		
	}
	
	private static void gameLoop(ArrayList<Particle> particles) {
		
		long ct = System.currentTimeMillis();
		
		while (go_on) {
			long dt = (System.currentTimeMillis() - ct);
			ct = System.currentTimeMillis();
			
			Graphics2D gr = (Graphics2D) strategy.getDrawGraphics();
						
			gr.setColor(Color.BLACK);
			gr.fillRect(3, 25, XDIM, YDIM);
			
			//calculate center of mass position
			double momentX = 0;
			double momentY = 0;
			double sumM = 0;
			for (Particle p : particles) {
				momentX += p.mass*p.x;
				momentY += p.mass*p.y;
				sumM += p.mass;
			}
			double centerMassX = momentX/sumM;
			double centerMassY = momentY/sumM;
			for (Particle p : particles) {
				p.accelerate(particles, dt);
				p.move(dt);
				p.draw(gr,centerMassX, centerMassY, zoomFactor, XDIM, YDIM);
			}
			
			gr.dispose();
			strategy.show();
			
			long st = dt > SLEEP_INTERVAL ? 0 : SLEEP_INTERVAL - dt;
			
			try { 
				Thread.sleep(st);
				} 
			catch (InterruptedException ie) {}
		}
		frame.dispose();
		
	}
}
