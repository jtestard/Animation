/**
 * 
 */
package comp559.a1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author julestestard
 *
 */
public class ParticleSystemTest {
	
	ParticleSystem ps;
	
	double[] y,dydt;
	
	double time;
	
	double delta = 0.001;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ps = new ParticleSystem();
		ps.createSystem(4);
		y = new double [ps.getPhaseSpaceDim()];
		dydt = new double [ps.getPhaseSpaceDim()];
		//populating the y state.
		ps.getPhaseSpace(y);
		time=0.0;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDerivs() {
		ps.useGravity.setValue(true);
		ps.gravity.setValue(10);
		ps.viscousDamping.setValue(1);
		double [] dydt_solution = {-10,0,20,-10,10,0,-20,-10};
		
		double [] old_y = deepCopy(y);
		ps.derivs(time, y, dydt);
		
		assertArrayEquals(old_y, y,delta);		
		assertArrayEquals(dydt_solution,dydt,delta);
		
		System.out.println("Values of y :");
		for (int i = 0 ; i < y.length ; i++) {
			if (i < 4) {
				System.out.println("Particle 1 [" + i + "]:" + y[i]);
			} else {
				System.out.println("Particle 2 [" + (i-4) + "]:" + y[i]);
			}
		}
		System.out.println("Values of dydt :");
		for (int i = 0 ; i < dydt.length ; i++) {
			if (i < 4) {
				System.out.println("Particle 1 [" + i + "]:" + dydt[i]);
			} else {
				System.out.println("Particle 2 [" + (i-4) + "]:" + dydt[i]);
			}
		}
	}
	
	private static double[] deepCopy(double [] y) {
		double [] copy = new double[y.length];
		for (int i = 0 ; i < y.length ; i++) {
			copy[i] = y[i];
		}
		return copy;
	}

}
