import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class testParticle {
	
	Particle p1, p2;

	@Before
	public void setUp() throws Exception {
		p1 = new Particle(1, 2, 3, 2, 1, Color.BLUE);
		p2 = new Particle(4, 2, -2, 3, 1, Color.BLUE);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		p1.collide(p2);
	}

}
