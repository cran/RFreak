/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 */

package freak.rinterface.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.jar.JarFile;

import freak.Freak;
import freak.core.control.Batch;
import freak.core.control.BatchList;
import freak.core.control.Schedule;
import freak.core.fitness.FitnessFunction;
import freak.core.graph.FreakGraphModel;
import freak.core.graph.Initialization;
import freak.core.mapper.Mapper;
import freak.core.modulesupport.ClassCollector;
import freak.core.modulesupport.Module;
import freak.core.modulesupport.ModuleCollector;
import freak.core.modulesupport.ModuleInfo;
import freak.core.modulesupport.OperatorGraphCollector;
import freak.core.modulesupport.UnsupportedEnvironmentException;
import freak.core.observer.Observer;
import freak.core.observer.ObserverManager;
import freak.core.observer.ObserverViewMismatchException;
import freak.core.populationmanager.PopulationManager;
import freak.core.searchspace.SearchSpace;
import freak.core.stoppingcriterion.StoppingCriterion;
import freak.core.util.GraphViz;
import freak.gui.graph.OperatorGraphFile;
import freak.gui.scheduleeditor.ScheduleDependencyChecker;
import freak.module.fitness.booleanfunction.GenericPareto;
import freak.module.fitness.pointset.LtSOptimization;
import freak.module.observer.ResultObserver;
import freak.module.operator.initialization.LTSInitialization;
import freak.module.postprocessor.booleanfunction.Count;
import freak.module.searchspace.BooleanFunction;
import freak.module.searchspace.PointSet;
import freak.module.stoppingcriterion.GenerationCount;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;


/**
 * A class to create and partly configure Schedules via .jcalls directly from R
 * @author  Dominic,Robin
 */
public class ScheduleConfigurator {
	
	static Schedule currentSchedule;
	
	static boolean editingFinished = false;
	
	public static Schedule getCurrentSchedule() {
		return currentSchedule;
	}
	
	public static void setCurrentSchedule(Schedule sched) {
		//this method is used to pass a schedule that has just been edited with the graphical ScheduleEditor
		//to the Schedule Configurator, thus it can be retrieved from R
		currentSchedule = sched;
		editingFinished=true;
		/*System.out.println("\n\nThe Schedule that has just been edited, is now available in the \nScheduleConfigurator.");
		System.out.println("To retrieve it from R you can use:");
		System.out.println("[destination object] <- getCurrentSched()\n");**/
	}

	private static void updateBatchForSchedule(Schedule schedule,int runs) {
		// Batches und Runs
		Batch batch = schedule.createBatchFromCurrentConfigurations();
		batch.setRuns(runs);
		BatchList batchList = schedule.getBatchList();
		batchList.clear();
		batchList.add(batch.copy(), true);	
		schedule.setDefaultBatch(batch);
	}
	
	// called from R
	public static void setDiscriminationR(int runs,int generations) {
		setDiscrimination("ignored",runs,generations,"ignored","");
	}
	
	// called from "Qucik Start GPAS"
	public static void setDiscrimination(String path,int runs,int generations, String testDataPath,String saveTo) {
		createSchedule(path,runs,generations);
		//.jcall("freak/rinterface/model/ScheduleConfigurator","Lfreak/core/control/Schedule;","createSchedule", "ignored",runsJava,generationJava,"ignored");
		Module m;
		try {
			currentSchedule.setFitnessFunction(null);
			// Fitnessfunktion
			m = new GenericPareto(currentSchedule);
			((GenericPareto)m).setParetoObjective(new int[] {GenericPareto.OBJECTIVE_CASES,GenericPareto.OBJECTIVE_CONTROLS,GenericPareto.OBJECTIVE_LENGTH});
			((GenericPareto)m).setPropertySubsets(new Integer(1));
			((GenericPareto)m).setPropertySizePruning(new Integer(12));
			m.testSchedule(currentSchedule);
			m.initialize();
			m.createEvents();
			currentSchedule.setFitnessFunction((FitnessFunction)m);
		} catch (UnsupportedEnvironmentException e) {
			throw new RuntimeException("Something is wrong with the default Schedule.", e);
		}		
		// Observer, View and Postprocessor		
		ObserverManager om = currentSchedule.getObserverManager();
		m = new ResultObserver(currentSchedule);
		((ResultObserver)m).setPropertyTestData(testDataPath);
			//View
		if (saveTo.trim().equals("")) {
			freak.module.view.RReturn rReturn = new freak.module.view.RReturn(currentSchedule);
			try {
				((Observer)m).addView(rReturn);
			} catch (ObserverViewMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		} else {
			freak.module.view.FileWriter output = new freak.module.view.FileWriter(currentSchedule);
			output.setPropertyFile(new File(saveTo));
			try {
				((Observer)m).addView(output);
			} catch (ObserverViewMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
		m.initialize();
		m.createEvents();
		om.addObserver((Observer)m);
		
		updateBatchForSchedule(currentSchedule,runs);
	}
	
	// called from R
	public static void setInteractionR(int runs,int generations,String graphFile,int occurences,double ratio) {
		setInteraction("ignored",runs,generations,graphFile,occurences,ratio,"");
	}
	
	// called from "Qucik Start GPAS"	
	public static void setInteraction(String path,int runs,int generations,String graphFile,int occurences,double ratio,String saveTo) {
		createSchedule(path,runs,generations);
		Module m;
		try {
			currentSchedule.setFitnessFunction(null);
			// Fitnessfunktion
			m = new GenericPareto(currentSchedule);
			((GenericPareto)m).setParetoObjective(new int[] {GenericPareto.OBJECTIVE_CASESCONTROLS,GenericPareto.OBJECTIVE_CONTROLS,GenericPareto.OBJECTIVE_LENGTH});
			((GenericPareto)m).setPropertySubsets(new Integer(1));
			((GenericPareto)m).setPropertySizePruning(new Integer(12));
			m.testSchedule(currentSchedule);
			m.initialize();
			m.createEvents();
			currentSchedule.setFitnessFunction((FitnessFunction)m);
		} catch (UnsupportedEnvironmentException e) {
			throw new RuntimeException("Something is wrong with the default Schedule.", e);
		}		
				
		// Observer, View and Postprocessor		
		ObserverManager om = currentSchedule.getObserverManager();
		m = new ResultObserver(currentSchedule);
		((ResultObserver)m).setPropertyTestData("");
			//View
		if (saveTo.trim().equals("")) {
			freak.module.view.RReturn rReturn = new freak.module.view.RReturn(currentSchedule);
			try {
				((Observer)m).addView(rReturn);
			} catch (ObserverViewMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		} else {
			freak.module.view.FileWriter output = new freak.module.view.FileWriter(currentSchedule);
			output.setPropertyFile(new File(saveTo));
			try {
				((Observer)m).addView(output);
			} catch (ObserverViewMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
			//Postprocessor
		((ResultObserver)m).setPostprocessor(-1);
		((ResultObserver)m).setSelectPostprocessorFromGui(false);
		Count postprocessor = new Count(currentSchedule);
		Count.fileName=graphFile;
		Count.minCount=occurences;
		Count.minPercent=ratio;
		((ResultObserver)m).setSelectedProcessor(postprocessor);
		m.initialize();
		m.createEvents();
		om.addObserver((Observer)m);
		
		updateBatchForSchedule(currentSchedule,runs);
	}
			
	public static void setGenerationCount(int gencount) {
		//RJava macht Probleme bei ints, deswegen Workaround mittels String-Parsing
//		int gencount = Integer.parseInt(gc);
		try {
			GenerationCount generationCount = new GenerationCount(currentSchedule);
			generationCount.setPropertyCount(new Integer(gencount));
			generationCount.testSchedule(currentSchedule);
			generationCount.initialize();
			generationCount.createEvents();

			StoppingCriterion[] stoppingCriteria = { generationCount };
			currentSchedule.setStoppingCriteria(stoppingCriteria);
			
		} catch (UnsupportedEnvironmentException e) {
			throw new RuntimeException(
					"Something is wrong with the default Schedule.", e);
		}
		System.out.println("GenerationCount has been set from R");
	}
	
	// TODO muss noch ergŠnzt werden um BatchUpdate (da Configurable) und vielleicht auch um schedule.modulesEdited
	public static void setOperatorGraph(String ogLocation){
		File f = new File(ogLocation);
		try {
			OperatorGraphFile ogFile = OperatorGraphFile.read(new FileInputStream(f));
			FreakGraphModel model = ogFile.generateGraph(currentSchedule);
			currentSchedule.setGraphModel(model);

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		System.out.println("OperatorGraph has been set from R");
	}

	public static void printOperatorGraph(){
		GraphViz gv = new GraphViz();
		FreakGraphModel model = currentSchedule.getGraphModel();
		String dotSource = model.getOperatorGraph().toDotGraph();
	    // Save Graph in dot format
	    File graphDot = new File("operatorgraph.dot");
	    try {
	    	FileWriter fout = new FileWriter(graphDot);
	        fout.write(dotSource);
	        fout.close();
	    }
	    catch (Exception e) {
	    	System.err.println("Error: I/O error while writing the dot source");
	    }
	    // Save Graph as Gif
	    File graphGif = new File("operatorgraph.gif");
		GraphViz.DOT="C:\\Program Files\\graphviz\\Graphviz\\bin\\dot";
	    try {
		    gv.writeGraphToFile(gv.getGraph(dotSource), graphGif);
	    }
	    catch (Exception e1) {
			GraphViz.DOT="/usr/local/graphviz-2.12/bin/dot";
		    try {
			    gv.writeGraphToFile(gv.getGraph(dotSource), graphGif);
		    }
		    catch (Exception e2) {		    	
		    	System.out.println("DOT not found");
		    }
	    }
	}
	
	public static Schedule getLTSSchedule(RDoubleMatrix data,int h,boolean adjust,int runs,int generationCountStop) {
		Schedule schedule = new Schedule();
		
		ScheduleDependencyChecker scheduleDependencyChecker = new ScheduleDependencyChecker(null);
		scheduleDependencyChecker.setSchedule(schedule);
		
		Module m;
		try {
			// Suchraum
			m = new freak.module.searchspace.PointSet(schedule);
			int noOfPoints=data.getDim()[0];
			int dimension=data.getDim()[1];
			PointSet.Point[] points= new PointSet.Point[noOfPoints];
			for (int i=0;i<noOfPoints;i++) {
				points[i]= new PointSet.Point(dimension);
				for (int j=0;j<dimension;j++) {
					points[i].setK(j, data.getValues()[i+j*noOfPoints]);
				}
			}
			((PointSet)m).setPoints(points);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setPhenotypeSearchSpace((SearchSpace)m);

			// Fitnessfunktion
			m = new freak.module.fitness.pointset.LtSOptimization(schedule);
			((LtSOptimization)m).setPropertyH(new Integer(h));
			((LtSOptimization)m).setPropertyAdjust(adjust);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setFitnessFunction((FitnessFunction)m);

			// Mapper
			m = new freak.module.mapper.pointset.BitStringMapper(schedule);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setMapper((Mapper)m);

			// Stoppkriterium
			if (generationCountStop>0) {
				GenerationCount generationCount = new GenerationCount(schedule);
				generationCount.setPropertyCount(new Integer(generationCountStop));
				generationCount.testSchedule(schedule);
				generationCount.initialize();
				generationCount.createEvents();
		
				StoppingCriterion[] stoppingCriteria = {generationCount};
				schedule.setStoppingCriteria(stoppingCriteria);
			}

			// Populationmanager
			m = new freak.module.populationmanager.DefaultPopulationManager(schedule);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setPopulationManager((PopulationManager)m);
			
			// Operatorgraph
			OperatorGraphCollector collector=new OperatorGraphCollector(schedule);
				      	      
			ModuleInfo[] graphs = collector.getPredefinedGraphs("LTSGraph.fop");
			if (graphs != null) {
				try {
					OperatorGraphFile ogFile;
 					ogFile = OperatorGraphFile.read(new FileInputStream(new File(graphs[0].getClassName())));
					FreakGraphModel model = ogFile.generateGraph(schedule);
					model.getOperatorGraph().setName(graphs[0].getName());

					schedule.setGraphModel(model);

				} catch (Exception exc) {
					System.out.println("Error loading graph " + graphs[0].getClassName());
					exc.printStackTrace();
				}
			}
			
			// Initialisierung
			m = new freak.module.operator.initialization.LTSInitialization(schedule.getOperatorGraph());
			((LTSInitialization)m).setPropertyNoOfBitsSetted(dimension);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			((Initialization)m).hideNameProperty();
			schedule.setInitialization((Initialization)m);
			
			// Observer, View and Postprocessor		
			ObserverManager om = schedule.getObserverManager();
			m = new ResultObserver(schedule);

			//View
			freak.module.view.RReturn rReturn = new freak.module.view.RReturn(schedule);
			try {
				((Observer)m).addView(rReturn);
			} catch (ObserverViewMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
			m.initialize();
			m.createEvents();
			om.addObserver((Observer)m);

		} catch (UnsupportedEnvironmentException e) {
			throw new RuntimeException("Something is wrong with the default Schedule.", e);
		}		
		
		updateBatchForSchedule(schedule,runs);
		
		// Update signalisieren (hat nur Auswirkungen auf den Operatorgraph)
		schedule.modulesEdited();
		
		return schedule;
	}
		
	//ScheduleConfigurator.createSchedule(jTextField.getText(),runs,generations,"./export/resource/freak/module/graph/booleanfunction/ZuFaulZumKlicken.fop");
	
	// USED FROM THE GPAS QUICK START GUI
	//creates the same schedule as the method createSchedule in class LogRegInterface		
	public static Schedule createSchedule(String path,int runs,int generationCountStop) {//,String graphPath) {

		Schedule schedule = new Schedule();
		
		ScheduleDependencyChecker scheduleDependencyChecker = new ScheduleDependencyChecker(null);
		scheduleDependencyChecker.setSchedule(schedule);
		
		Module m;
		try {
			// Suchraum
			m = new freak.module.searchspace.BooleanFunction(schedule);
			((BooleanFunction)m).setPropertyInputPath(path);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setPhenotypeSearchSpace((SearchSpace)m);

			// Fitnessfunktion
			m = new freak.module.fitness.booleanfunction.GenericPareto(schedule);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setFitnessFunction((FitnessFunction)m);

			// Stoppkriterium
			if (generationCountStop>0) {
				GenerationCount generationCount = new GenerationCount(schedule);
				generationCount.setPropertyCount(new Integer(generationCountStop));
				generationCount.testSchedule(schedule);
				generationCount.initialize();
				generationCount.createEvents();
		
				StoppingCriterion[] stoppingCriteria = {generationCount};
				schedule.setStoppingCriteria(stoppingCriteria);
			}
			// Populationmanager
			m = new freak.module.populationmanager.DefaultPopulationManager(schedule);
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			schedule.setPopulationManager((PopulationManager)m);

			// Operatorgraph
			OperatorGraphCollector collector=new OperatorGraphCollector(schedule);
				      	      
			ModuleInfo[] graphs = collector.getPredefinedGraphs("GPASGraph.fop");
			if (graphs != null) {
				try {
					OperatorGraphFile ogFile;
 					ogFile = OperatorGraphFile.read(new FileInputStream(new File(graphs[0].getClassName())));
					FreakGraphModel model = ogFile.generateGraph(schedule);
					model.getOperatorGraph().setName(graphs[0].getName());

					schedule.setGraphModel(model);

				} catch (Exception exc) {
					System.out.println("Error loading graph " + graphs[0].getClassName());
					exc.printStackTrace();
				}
			}
			
			// Initialisierung
			m = new freak.module.operator.initialization.RandomInitialization(schedule.getOperatorGraph());
			m.testSchedule(schedule);
			m.initialize();
			m.createEvents();
			((Initialization)m).hideNameProperty();
			schedule.setInitialization((Initialization)m);			
		} catch (UnsupportedEnvironmentException e) {
			throw new RuntimeException("Something is wrong with the default Schedule.", e);
		}		
		
		updateBatchForSchedule(schedule,runs);
		
		// Update signalisieren (hat nur Auswirkungen auf den Operatorgraph)
		schedule.modulesEdited();
				
		currentSchedule = schedule;	//the static attribute is set for further editing
		return schedule;			//as well as the schedule we just created is return to the caller (ie. R)
									//for the case that no further editing is planned 
		
	}
	
	//TODO R Return View und OBSERVER durch Methode einbauen!!!!
	
	//creates a default schedule
	//### die Methode baut momentan noch denselben Schedule wie die Methode createSchedule
	//### Wie sollen die Default-Werte aussehen?
	
	// USED WHEN STARTING THE SCHEDULE EDITOR FROM R
	public static Schedule createDefaultSchedule() {
		currentSchedule = ScheduleConfigurator.createSchedule("./data/snap.csv",1,0);
		Module m;
		// Observer, View and Postprocessor		
		ObserverManager om = currentSchedule.getObserverManager();
		m = new ResultObserver(currentSchedule);

		//View
		freak.module.view.RReturn rReturn = new freak.module.view.RReturn(currentSchedule);
		try {
			((Observer)m).addView(rReturn);
		} catch (ObserverViewMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		m.initialize();
		m.createEvents();
		om.addObserver((Observer)m);
		
		updateBatchForSchedule(currentSchedule,1);
		return currentSchedule;			//as well as the schedule we just created is return to the caller (ie. R)
	}

	/**
	 * @return the editingFinished
	 */
	public static boolean isEditingFinished() {
		return editingFinished;
	}

	/**
	 * @param editingFinished the editingFinished to set
	 */
	public static void setEditingFinished(boolean editingFinished) {
		ScheduleConfigurator.editingFinished = editingFinished;
	}



}
