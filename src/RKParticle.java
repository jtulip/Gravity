import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;


public class RKParticle extends Particle {
	
	public double kXacc[];
	public double kYacc[];
	public double kXpos[];
	public double kYpos[];

	public RKParticle(double x, double y, double dx, double dy, double mass, Color c) {
		super(x, y, dx,dy, mass, c);
		kXacc  = new double[4];
		kYacc  = new double[4];
		kXpos  = new double[4];
		kYpos  = new double[4];
	}

	public void calcAccK(ArrayList<RKParticle> particles, double dt, int k) {
		switch(k) {
			case 1: calcAccK1(particles, dt);
			case 2: calcAccK2(particles, dt);
			case 3: calcAccK3(particles, dt);
			case 4: calcAccK4(particles, dt);
		}
	}
	
	public void calcPosK(double dt, int k) {
		switch(k) {
			case 1: calcPosK2(dt);
			case 2: calcPosK3(dt);
			case 3: calcPosK4(dt);
			case 4: ;
		}
	}

	private void calcAccK1(ArrayList<RKParticle> particles, double dt) {
		double ax = 0.0;
		double ay = 0.0;
		for (RKParticle p : particles ) {
			if (p == this) continue;
			
			double deltaX = p.x - this.x;
			double deltaY = p.y - this.y;
			double r = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			double fij = (RKMainFrame.G * p.mass * this.mass) / (r*r);
			
			double fijx = fij*deltaX/r;
			double fijy = fij*deltaY/r;
			
			ax += fijx/this.mass;
			ay += fijy/this.mass;
		}
		this.kXacc[0] = ax;
		this.kYacc[0] = ay;
	}
	
	private void calcPosK2(double dt) {
		this.kXpos[0] = this.getX() + this.kXacc[0]*dt/2.0;
		this.kYpos[0] = this.getY() + this.kYacc[0]*dt/2.0;
	}
	
	private void calcAccK2(ArrayList<RKParticle> particles, double dt) {
		double ax = 0.0;
		double ay = 0.0;
		for (RKParticle p : particles ) {
			if (p == this) continue;
			
			double deltaX = p.kXpos[0] - this.kXpos[0];
			double deltaY = p.kYpos[0] - this.kYpos[0];
			double r = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			double fij = (RKMainFrame.G * p.getMass() * this.mass) / (r*r);
			
			double fijx = fij*deltaX/r;
			double fijy = fij*deltaY/r;
			
			ax += fijx/this.mass;
			ay += fijy/this.mass;
		}
		this.kXacc[1] = ax;
		this.kYacc[1] = ay;
	}
	
	private void calcPosK3(double dt) {
		this.kXpos[1] = this.x + this.kXacc[1]*dt/2.0;
		this.kYpos[1] = this.y + this.kYacc[1]*dt/2.0;		
	}
	
	private void calcAccK3(ArrayList<RKParticle> particles, double dt) {
		double ax = 0.0;
		double ay = 0.0;
		for (RKParticle p : particles ) {
			if (p == this) continue;
			
			double deltaX = p.kXpos[1] - this.kXpos[1];
			double deltaY = p.kYpos[1] - this.kYpos[1];
			double r = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			double fij = (RKMainFrame.G * p.getMass() * this.mass) / (r*r);
			
			double fijx = fij*deltaX/r;
			double fijy = fij*deltaY/r;
			
			ax += fijx/this.mass;
			ay += fijy/this.mass;
		}
		this.kXacc[2] = ax;
		this.kYacc[2] = ay;
	}

	private void calcPosK4(double dt) {
		this.kXpos[2] = this.x + this.kXacc[2]*dt;
		this.kYpos[2] = this.y + this.kYacc[2]*dt;		
	}
	
	private void calcAccK4(ArrayList<RKParticle> particles, double dt) {
		double ax = 0.0;
		double ay = 0.0;
		for (RKParticle p : particles ) {
			if (p == this) continue;
			
			double deltaX = p.kXpos[2] - this.kXpos[2];
			double deltaY = p.kYpos[2] - this.kYpos[2];
			double r = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			double fij = (RKMainFrame.G * p.getMass() * this.mass) / (r*r);
			
			double fijx = fij*deltaX/r;
			double fijy = fij*deltaY/r;
			
			ax += fijx/this.mass;
			ay += fijy/this.mass;
		}
		this.kXacc[3] = ax;
		this.kYacc[3] = ay;
	}

	public void move(double dt) {
		if (trail.size() > TRAIL_LENGTH) trail.remove(trail.size()-1);
		if (++count%TRAIL_INTERVAL == 0) trail.add(0,new Dimension((int) x , (int) y));
		this.dx = this.dx +((kXacc[0] + 2*kXacc[1] + 2*kXacc[2] + kXacc[3]) / 6.0) * dt;
		this.dy = this.dy +((kYacc[0] + 2*kYacc[1] + 2*kYacc[2] + kYacc[3]) / 6.0) * dt;
		x += dx*dt;
		y += dy*dt;
	}

}
