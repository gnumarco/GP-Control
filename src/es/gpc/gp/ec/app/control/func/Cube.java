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
 * Cube.java
 * 
 * Created: Mon Apr 23 17:15:35 EDT 2012
 * By: Sean Luke
 
 <p>This function appears in the Korns function set, and is just x * x * x
 <p>M. F. Korns. Accuracy in Symbolic Regression. In <i>Proc. GPTP.</i> 2011.
 
*/

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class Cube extends GPNode
    {
    public String toString() { return "cube"; }

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
        rd.x = rd.x * rd.x * rd.x;
        }
    }



