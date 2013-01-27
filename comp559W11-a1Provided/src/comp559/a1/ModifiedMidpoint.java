package comp559.a1;

public class ModifiedMidpoint implements Integrator {

    @Override
    public String getName() {
        return "modified midpoint";
    }

	double [] dydt;
	double [] y_midpoint;
	//This int checks that if the dimension did change (which is a rare occurrence event).
	int old_n = -1;
    
    @Override
    public void step(double[] y, int n, double t, double h, double[] yout, Function derivs) {
    	
        //Avoid useless memory allocation if size did not change
    	if (old_n!=n) {
        	dydt = new double[n];
        	y_midpoint = new double[n];
        	old_n = n;
    	}
    	
        //delta_x = h*f(x(t),t)
        derivs.derivs(t, y, dydt);
        for (int i = 0 ; i < n ; i++) {
        	dydt[i] *= h;
        }
        
        //X = x(t)+delta_x/2
        for (int i = 0 ; i < n ; i++) {
        	y_midpoint[i] = y[i] + dydt[i]*(2.0/3.0);
        }
        
        //f_mid = f(X,t)
        derivs.derivs(t+h*(2.0/3.0),y_midpoint,y_midpoint);
        for (int i = 0 ; i < n ; i++) {
        	y_midpoint[i] *= h;
        }
        
        //Compute final output. x(t+delta_t) = x(t) + delta_t*f_mid
        for (int i = 0 ; i < n ; i++) {
        	yout[i] = y[i] + y_midpoint[i];
        }
        
    }

}
