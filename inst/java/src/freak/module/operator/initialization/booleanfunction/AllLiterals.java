/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 */

package freak.module.operator.initialization.booleanfunction;

import java.util.Iterator;

import edu.cornell.lassp.houle.RngPack.RandomElement;
import freak.core.control.Schedule;
import freak.core.fitness.AbstractMultiObjectiveFitnessFunction;
import freak.core.fitness.MultiObjectiveFitnessFunction;
import freak.core.graph.GraphException;
import freak.core.graph.Initialization;
import freak.core.graph.OperatorGraph;
import freak.core.modulesupport.Configurable;
import freak.core.modulesupport.UnsupportedEnvironmentException;
import freak.core.population.Genotype;
import freak.core.population.Individual;
import freak.core.population.IndividualList;
import freak.core.population.Population;
import freak.core.searchspace.SearchSpace;
import freak.module.searchspace.BitString;
import freak.module.searchspace.BitStringGenotype;
import freak.module.searchspace.BooleanFunction;
import freak.module.searchspace.BooleanFunctionGenotype;
import freak.module.searchspace.PointSet;
import freak.module.searchspace.logictree.AndNode;
import freak.module.searchspace.logictree.DNFTree;
import freak.module.searchspace.logictree.Data;
import freak.module.searchspace.logictree.OrNode;
import freak.module.searchspace.logictree.StaticCompareNode;

public class AllLiterals extends Initialization implements Configurable  {
	
	private boolean exclude=false;
	
	public AllLiterals(OperatorGraph graph) {
		super(graph);
	}
	
	public void testSchedule(Schedule schedule) throws UnsupportedEnvironmentException {
		super.testSchedule(schedule);
		if (!(graph.getSchedule().getGenotypeSearchSpace() instanceof BooleanFunction)){
				throw new UnsupportedEnvironmentException("Works only on the SearchSpace Boolean Function.");			
		}
	}
	
	public IndividualList[] process(IndividualList[] origin) {
		// create the resulting array of individual lists
		IndividualList[] result = new IndividualList[1];
		BooleanFunction sp = (BooleanFunction) graph.getSchedule().getGenotypeSearchSpace();		
		result[0] = new Population(graph.getSchedule());//, Data.getNumCompareNodes());
		BooleanFunctionGenotype firstGeno = (BooleanFunctionGenotype) sp.getRandomGenotype();
		result[0].addIndividual(new Individual(graph.getSchedule(), sp.getLiteral(0), null)); //only to in initialize data reading
		// now create the individuals	   
		for (int i = 1; i < Data.getNumCompareNodes(); i++) {	
			MultiObjectiveFitnessFunction ff = (MultiObjectiveFitnessFunction)getOperatorGraph().getSchedule().getFitnessFunction();
			Individual individual=new Individual(graph.getSchedule(), sp.getLiteral(i), null);
			if (i%1000==0) System.out.println(i+" "+individual.toString());
			if (exclude) {
				double[] fitness = ff.evaluate(individual,result[0]);
				Iterator it = result[0].iterator();
				int[] comp = new int[result[0].size()];
				boolean add=true;
				int j = 0;
				while(it.hasNext()) {
					Individual ind = (Individual)it.next();
					double[] fitness2 = ff.evaluate(ind,result[0]);
					comp[j] = AbstractMultiObjectiveFitnessFunction.compare(fitness2,fitness);
					if (comp[j] >= 0) {
						add=false;
						break; 	
					}
					j++;
				}
				
				if (add) {
					Population pop = new Population(getOperatorGraph().getSchedule());
					pop.addIndividual(individual); 
					it = result[0].iterator();
					j = 0;
					while(it.hasNext()) {
						Individual ind = (Individual)it.next();
						if (comp[j] == AbstractMultiObjectiveFitnessFunction.UNCOMPARABLE) {
							pop.addIndividual(ind);		
						}
						j++;		
					}	
					result[0] = pop;			
				}
			} else {	
				result[0].addIndividual(new Individual(graph.getSchedule(), sp.getLiteral(i), null));
			}	
		}
		return result;
	}

	public Boolean getPropertyExclude() {
		return new Boolean(exclude);
	}
	
	public void setPropertyExclude(Boolean value) {
		exclude=value.booleanValue();
	}
	
	public String getLongDescriptionForExclude() {
		return "Exclude dominated literals?";
	}

			
	public String getName() {
		return "All Literals";
	}
	
	public String getDescription() {
		return "Creates all possible (non-dominated) literals";
	}
}
