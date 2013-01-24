package comp559.a1;

/**
 * TODO: finish this class!
 * @author kry
 */
public class ForwardEuler implements Integrator {
    
	double [] dydt;
	
	/**
	 * This int checks that if the dimension did change (which is a rare occurrence event).
	 * -1 is chosen as default because it is an impossible value for the dimension.
	 */
	int old_n = -1;
	
	
    @Override
    public String getName() {
        return "Forward Euler";
    }
    
    /** 
     * Advances the system at t by h 
     * @param y The state at time h
     * @param n The dimension of the state (i.e., y.length)
     * @param t The current time (in case the derivs function is time dependent)
     * @param h The step size
     * @param yout  The state of the system at time t+h
     * @param derivs The object which computes the derivative of the system state
     */
    @Override
    public void step(double[] y, int n, double t, double h, double[] yout, Function derivs) {
        // TODO: implement this method
        //Avoid useless memory allocation if size did not change
    	if (old_n!=n) {
        	dydt = new double[n];
        	old_n = n;
    	}
    	
    	dydt = new double[n];
        
        //Get the derivatives
        derivs.derivs(t, y, dydt);
        
        //Compute with forward euler.
        for (int i = 0 ; i < n ; i++) {
        	yout[i] = y[i] + h*dydt[i];
        }
   }

}
