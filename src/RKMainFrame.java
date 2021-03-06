import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class RKMainFrame extends JFrame {
	
	private static final int XPOS = 400;
	private static final int YPOS = 100;
	private static final int XDIM = 1600;
	private static final int YDIM = 1200;
	
	private static final int SLEEP_INTERVAL = 0;
	private static final double ZOOM_SCALE = 0.01;
	public static final double G = 0.0005;
	
	
	private static BufferStrategy strategy;
	private static double zoomFactor;
	

	private static RKMainFrame frame;
	private JPanel contentPane;
	
	private static volatile boolean go_on = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		zoomFactor = 1.0;
		ArrayList<RKParticle> particles = new ArrayList<RKParticle>();
		particles.add(new RKParticle(XDIM/2, YDIM/2, -0.002, 0.0, 200000, Color.RED));
		particles.add(new RKParticle(XDIM/2, YDIM/4, 0.5, 0.0, 50, Color.ORANGE));
		particles.add(new RKParticle(XDIM/5, YDIM/2, 0.0, -0.5, 2000, Color.YELLOW));
		particles.add(new RKParticle(XDIM/5+40, YDIM/2, 0.0, -(0.50+0.155), 5, Color.GREEN));
		particles.add(new RKParticle(XDIM*2.0+22, YDIM/2, 0.0, 0.2-0.03605, 200, Color.BLUE));
		particles.add(new RKParticle(XDIM*2.0-22, YDIM/2, 0.0, 0.2+0.03605, 200, Color.MAGENTA));
		
		frame = new RKMainFrame();
		frame.setVisible(true);
		gameLoop(particles);
	}

	/**
	 * Create the frame.
	 */
	public RKMainFrame() {
		
		this.setTitle("RKGravity");
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
	
	private static void gameLoop(ArrayList<RKParticle> particles) {
		
		long ct = System.nanoTime();
		
		while (go_on) {
			long nt = System.nanoTime();
			long dtL = (nt - ct);
			ct = nt;
			
			double dt = (double) dtL/1000000.0;
			
			Graphics2D gr = (Graphics2D) strategy.getDrawGraphics();
						
			gr.setColor(Color.BLACK);
			gr.fillRect(3, 25, XDIM, YDIM);
			
			//calculate center of mass position
			double momentX = 0;
			double momentY = 0;
			double sumM = 0;
			for (Particle p : particles) {
				momentX += p.getMass()*p.getX();
				momentY += p.getMass()*p.getY();
				sumM += p.getMass();
			}
			double centerMassX = momentX/sumM;
			double centerMassY = momentY/sumM;
			
			//do the Runge-Kutta thing
			for (int k = 1; k <= 4; k++) {
				for (RKParticle p : particles) {
					p.calcAccK(particles, dt, k);
					p.calcPosK(dt, k);
				}				
			}
			
			for (RKParticle p : particles) {
				p.move(dt);
				p.draw(gr,centerMassX, centerMassY, zoomFactor, XDIM, YDIM);
			}
			
			gr.dispose();
			strategy.show();
			
			//long st = dt > SLEEP_INTERVAL ? 0 : SLEEP_INTERVAL - dt;
			
			try { 
				Thread.sleep(SLEEP_INTERVAL);
				} 
			catch (InterruptedException ie) {}
		}
		frame.dispose();
		
	}
}
