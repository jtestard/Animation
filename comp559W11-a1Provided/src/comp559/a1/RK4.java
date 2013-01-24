package comp559.a1;

public class RK4 implements Integrator {
    
    @Override
    public String getName() {
        return "RK4";
    }

	double [] dydt;
	double [] k1;
	double [] k2;
	double [] k3;
	double [] k4;
	
	//This int checks that if the dimension did change (which is a rare occurrence event).
	int old_n;
	
	public RK4 () {
		old_n = -1;
	}
    
    @Override
    public void step(double[] y, int n, double t, double h, double[] yout, Function derivs) {
        //Avoid useless memory allocation if size did not change
    	if (old_n!=n) {
        	dydt = new double[n];
        	k1 = new double[n];
        	k2 = new double[n];
        	k3 = new double[n];
        	k4 = new double[n];
        	old_n = n;
    	}
    	
        //Get the derivatives
        derivs.derivs(t, y, dydt);
        //Compute k1
        for (int i = 0 ; i < n ; i++) {
        	k1[i] = y[i] + h*dydt[i];
        }

        //derive at k1.
        derivs.derivs(t,k1,dydt);
        //Compute k2
        for (int i = 0 ; i < n ; i++) {
        	k2[i] = (y[i] + h*dydt[i])/2;
        }
        
        //derive at k2.
        derivs.derivs(t,k2,dydt);
        //Compute k3
        for (int i = 0 ; i < n ; i++) {
        	k3[i] = (y[i] + h*dydt[i])/2;
        }

        //derive at k3.
        derivs.derivs(t,k3,dydt);
        //Compute k4
        for (int i = 0 ; i < n ; i++) {
        	k4[i] = y[i] + h*dydt[i];
        	
        }
        
        //Compute final output.
        for (int i = 0 ; i < n ; i++) {
        	yout[i] = y[i] + (1/6)*k1[i] + (1/3)*k2[i] + (1/3)*k3[i] + (1/6)*k4[i];
        }
    }
    
    private void printArray(double[] array, String name) {
    	System.out.println("Printing contents of " + name + ":");
    	for (double d : array) {
    		
    	}
    }
}