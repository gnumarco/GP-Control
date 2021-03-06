/*
  Copyright 2012 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.app.control.func;
import es.gpc.gp.ec.gp.ADFStack;
import es.gpc.gp.ec.gp.GPData;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.app.control.ControlData;
import es.gpc.gp.ec.Problem;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;


/* 
 * VladERCC.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke

 <p>This ERC appears all three the Vladislavleva function sets.  It is not a constant but rather a function of one parameter (n) with an internal constant (c) and returns n * c.  Note that the value of c is drawn from the fully-closed range [-5.0, 5.0]. 
*/

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class VladERCC extends VladERCA
    {
    public String name() { return "VladERCC"; }

    public String toStringForHumans()
        { return "n*" + (float)value; }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
        ControlData rd = ((ControlData)(input));

        children[0].eval(state,thread,input,stack,individual,problem);
        rd.x = rd.x * value;
        }

    }



