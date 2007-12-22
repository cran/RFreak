/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 */


package freak.module.operator.initialization;

import freak.core.control.Schedule;
import freak.core.graph.CompatibleWithDifferentSearchSpaces;
import freak.core.graph.GraphException;
import freak.core.graph.Initialization;
import freak.core.graph.OperatorGraph;
import freak.core.modulesupport.Configurable;
import freak.core.modulesupport.UnsupportedEnvironmentException;
import freak.core.population.IndividualList;
import freak.module.fitness.pointset.LtSOptimization;
import freak.module.operator.initialization.bitstring.KBitInitialization;
import freak.module.searchspace.BitString;
import freak.module.searchspace.PointSet;

public class LTSInitialization extends KBitInitialization implements Configurable,
		CompatibleWithDifferentSearchSpaces {

	public LTSInitialization(OperatorGraph graph) {
		super(graph);
		super.size 	= 1;
		if (graph.getSchedule().getPhenotypeSearchSpace() instanceof PointSet)
		super.kBits = ((PointSet)graph.getSchedule().getPhenotypeSearchSpace()).getPointDimension();
	}
	
	public void testSchedule(Schedule schedule) throws UnsupportedEnvironmentException {
		super.testSchedule(schedule);
		
		if (!(graph.getSchedule().getPhenotypeSearchSpace() instanceof PointSet)){
				throw new UnsupportedEnvironmentException("Works only on the SearchSpace PointSet.");			
		}
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return "Performs a KBit Initialization where k is the Dimension of the Points in the SearchSpace PointSet";
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "LTSInitialization";
	}

}
