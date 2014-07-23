/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package es.gpc.gp.ec.app.control;

import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.util.ThreadPool;
import es.gpc.gp.ec.simple.SimpleProblemForm;
import es.gpc.gp.ec.Population;
import es.gpc.gp.ec.Evaluator;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.Subpopulation;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.Fitness;

/* 
 * SimpleEvaluator.java
 * 
 * Created: Wed Aug 18 21:31:18 1999
 * By: Sean Luke
 */
/**
 * The SimpleEvaluator is a simple, non-coevolved generational evaluator which
 * evaluates every single member of every subpopulation individually in its own
 * problem space. One Problem instance is cloned from p_problem for each
 * evaluating thread. The Problem must implement SimpleProblemForm.
 *
 * @author Sean Luke
 * @version 2.0
 *
 * Thanks to Ralf Buschermohle <lobequadrat@googlemail.com> for early versions
 * of code which led to this version.
 *
 */
public class ControlEvaluator extends Evaluator {

    private static final long serialVersionUID = 1;
    public static final String P_CLONE_PROBLEM = "clone-problem";
    public static final String P_NUM_TESTS = "num-tests";
    public static final String P_MERGE = "merge";

    public static final String V_MEAN = "mean";
    public static final String V_MEDIAN = "median";
    public static final String V_BEST = "best";

    public static final String P_CHUNK_SIZE = "chunk-size";
    public static final String V_AUTO = "auto";

    public static final int MERGE_MEAN = 0;
    public static final int MERGE_MEDIAN = 1;
    public static final int MERGE_BEST = 2;

    public int numTests = 1;
    public int mergeForm = MERGE_MEAN;
    public boolean cloneProblem;

    Object[] lock = new Object[0];          // Arrays are serializable
    int individualCounter = 0;
    int subPopCounter = 0;
    int chunkSize;  // a value >= 1, or C_AUTO
    public static final int C_AUTO = 0;

    public ThreadPool pool = new ThreadPool();

    // checks to make sure that the Problem implements SimpleProblemForm
    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        if (!(p_problem instanceof SimpleProblemForm)) {
            state.output.fatal("" + this.getClass() + " used, but the Problem is not of SimpleProblemForm",
                    base.push(P_PROBLEM));
        }

        cloneProblem = state.parameters.getBoolean(base.push(P_CLONE_PROBLEM), null, true);
        if (!cloneProblem && (state.breedthreads > 1)) // uh oh, this can't be right
        {
            state.output.fatal("The Evaluator is not cloning its Problem, but you have more than one thread.", base.push(P_CLONE_PROBLEM));
        }

        numTests = state.parameters.getInt(base.push(P_NUM_TESTS), null, 1);
        if (numTests < 1) {
            numTests = 1;
        } else if (numTests > 1) {
            String m = state.parameters.getString(base.push(P_MERGE), null);
            if (m == null) {
                state.output.warning("Merge method not provided to SimpleEvaluator.  Assuming 'mean'");
            } else if (m.equals(V_MEAN)) {
                mergeForm = MERGE_MEAN;
            } else if (m.equals(V_MEDIAN)) {
                mergeForm = MERGE_MEDIAN;
            } else if (m.equals(V_BEST)) {
                mergeForm = MERGE_BEST;
            } else {
                state.output.fatal("Bad merge method: " + m, base.push(P_NUM_TESTS), null);
            }
        }

        if (!state.parameters.exists(base.push(P_CHUNK_SIZE), null)) {
            chunkSize = C_AUTO;
        } else if (state.parameters.getString(base.push(P_CHUNK_SIZE), null).equalsIgnoreCase(V_AUTO)) {
            chunkSize = C_AUTO;
        } else {
            chunkSize = (state.parameters.getInt(base.push(P_CHUNK_SIZE), null, 1));
            if (chunkSize == 0) // uh oh
            {
                state.output.fatal("Chunk Size must be either an integer >= 1 or 'auto'", base.push(P_CHUNK_SIZE), null);
            }
        }
    }

    /**
     * A simple evaluator that doesn't do any coevolutionary evaluation.
     * Basically it applies evaluation pipelines, one per thread, to various
     * subchunks of a new population.
     *
     * @param state
     */
    @Override
    public void evaluatePopulation(final EvolutionState state) {

        // reset counters.  Only used in multithreading
        individualCounter = 0;
        subPopCounter = 0;

        // start up if single-threaded?
        if (state.evalthreads == 1) {
            int[] numinds = new int[state.population.subpops.length];
            int[] from = new int[numinds.length];

            for (int i = 0; i < numinds.length; i++) {
                numinds[i] = state.population.subpops[i].individuals.length;
                from[i] = 0;
            }

            SimpleProblemForm prob = null;
            if (cloneProblem) {
                prob = (SimpleProblemForm) (p_problem.clone());
            } else {
                prob = (SimpleProblemForm) (p_problem);  // just use the prototype
            }
            evalPopChunk(state, numinds, from, 0, prob);
        } else {
            Thread[] threads = new Thread[state.evalthreads];
            for (int i = 0; i < threads.length; i++) {
                SimpleEvaluatorThreadCG run = new SimpleEvaluatorThreadCG();
                run.threadnum = i;
                run.state = state;
                run.prob = (SimpleProblemForm) (p_problem.clone());
                threads[i] = pool.startThread("ECJ Evaluation Thread " + i, run);
            }
            // join
            for (int i = 0; i < threads.length; i++) {
                pool.joinAndReturn(threads[i]);
            }
        }
    }

    /**
     * The SimpleEvaluator determines that a run is complete by asking each
     * individual in each population if he's optimal; if he finds an individual
     * somewhere that's optimal, he signals that the run is complete.
     */
    public boolean runComplete(final EvolutionState state) {
        for (int x = 0; x < state.population.subpops.length; x++) {
            for (int y = 0; y < state.population.subpops[x].individuals.length; y++) {
                if (state.population.subpops[x].individuals[y].fitness.isIdealFitness()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A private helper function for evaluatePopulation which evaluates a chunk
     * of individuals in a subpopulation for a given thread. Although this
     * method is declared public (for the benefit of a private helper class in
     * this file), you should not call it.
     */
    protected void evalPopChunk(EvolutionState state, int[] numinds, int[] from,
            int threadnum, SimpleProblemForm p) {
        ((es.gpc.gp.ec.Problem) p).prepareToEvaluate(state, threadnum);

        Subpopulation[] subpops = state.population.subpops;
        int len = subpops.length;

        for (int pop = 0; pop < len; pop++) {
            // start evaluatin'!
            int fp = from[pop];
            int upperbound = fp + numinds[pop];
            Individual[] inds = subpops[pop].individuals;
            for (int x = fp; x < upperbound; x++) {
                p.evaluate(state, inds[x], pop, threadnum);
            }
        }

        ((es.gpc.gp.ec.Problem) p).finishEvaluating(state, threadnum);
    }

    // computes the chunk size if 'auto' is set.  This may be different depending on the subpopulation,
    // which is backward-compatible with previous ECJ approaches.
    int computeChunkSizeForSubpopulation(EvolutionState state, int subpop, int threadnum) {
        int numThreads = state.evalthreads;

        // we will have some extra individuals.  We distribute these among the early subpopulations
        int individualsPerThread = state.population.subpops[subpop].individuals.length / numThreads;  // integer division
        int slop = state.population.subpops[subpop].individuals.length - numThreads * individualsPerThread;

        if (threadnum >= slop) // beyond the slop
        {
            return individualsPerThread;
        } else {
            return individualsPerThread + 1;
        }
    }

    /**
     * A helper class for implementing multithreaded evaluation
     */
    class SimpleEvaluatorThreadCG implements Runnable {

        public int threadnum;
        public EvolutionState state;
        public SimpleProblemForm prob = null;

        public void run() {
            Subpopulation[] subpops = state.population.subpops;

            int[] numinds = new int[subpops.length];
            int[] from = new int[subpops.length];

            int count = 1;
            int start = 0;
            int subpop = 0;

            while (true) {
                // We need to grab the information about the next chunk we're responsible for.  This stays longer
                // in the lock than I'd like :-(
                synchronized (lock) {
                    // has everyone done all the jobs?
                    if (subPopCounter >= subpops.length) // all done
                    {
                        return;
                    }

                    // has everyone finished the jobs for this subpopulation?
                    if (individualCounter >= subpops[subPopCounter].individuals.length) // try again, jump to next subpop
                    {
                        individualCounter = 0;
                        subPopCounter++;

                        // has everyone done all the jobs?  Check again.
                        if (subPopCounter >= subpops.length) // all done
                        {
                            return;
                        }
                    }

                    start = individualCounter;
                    subpop = subPopCounter;
                    count = chunkSize;
                    if (count == C_AUTO) // compute automatically for subpopulations
                    {
                        count = computeChunkSizeForSubpopulation(state, subpop, threadnum);
                    }

                    individualCounter += count;  // it can be way more than we'll actually do, that's fine                    
                }

                // Modify the true count
                if (count >= subpops[subpop].individuals.length - start) {
                    count = subpops[subpop].individuals.length - start;
                }

                // Load into arrays to reuse evalPopChunk
                for (int i = 0; i < from.length; i++) {
                    numinds[i] = 0;
                }

                numinds[subpop] = count;
                from[subpop] = start;
                evalPopChunk(state, numinds, from, threadnum, prob);
            }
        }
    }

}
