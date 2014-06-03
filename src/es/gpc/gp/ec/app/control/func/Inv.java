/*
  Copyright 2012 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.app.control.func;
import es.gpc.gp.ec.gp.ADFStack;
import es.gpc.gp.ec.gp.GPData;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.gp.GPNode;
import es.gpc.gp.ec.app.control.ControlData;
import es.gpc.gp.ec.Problem;
import es.gpc.gp.ec.EvolutionState;

/* 
 * Inv.java
 * 
 * Created: Mon Apr 23 17:15:35 EDT 2012
 * By: Sean Luke


 <p>This function appears in the Keijzer function set, and is 1/x.  Note that
 the division is <b>not protected</i>, so 1/0.0 is infinity.
 <p>M. Keijzer. Improving Symbolic Regression with Interval Arithmetic and Linear Scaling. In <i>Proc. EuroGP.</i> 2003.
*/

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class Inv extends GPNode
    {
    public String toString() { return "1/"; }

    public int expectedChildren() { return 1; }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
        ControlData rd = ((ControlData)(input));

        children[0].eval(state,thread,input,stack,individual,problem);
        rd.x = 1.0 / rd.x;
        }
    }



