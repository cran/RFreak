/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 * 
 * This file is a modification of the original file distributed with
 * FrEAK (http://sourceforge.net/projects/freak427/).
 * Last modification: 06/28/2007
 */

package freak.core.modulesupport;

import freak.core.control.*;
import freak.core.graph.*;
import freak.gui.graph.*;
import java.io.*;
import java.util.*;

/**
 * Collects the predefined operator graphs.
 * @author  Heiko, Michael
 */
public class OperatorGraphCollector {

	private Schedule schedule;

	/**
	 * A file filter which accepts all files with suffix .fop.
	 */
	private class GraphFileFilter implements FileFilter {
		public boolean accept(File pathname) {
			//return false;
			return pathname.getAbsolutePath().endsWith(".fop");
		}
	}

	public OperatorGraphCollector(Schedule schedule) {
		this.schedule = schedule;
	}

	/**
	 * This method returns a list of all class paths.
	 *
	 * @return a list of all class paths.
	 */
	private String[] getClassPaths() {
		return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
	}

	private boolean isCorrectOperatorGraph(File file) {
		try {
			return isCorrectOperatorGraph(new FileInputStream(file));
		} catch (Exception exc) {
			return false;
		}
	}

	private boolean isCorrectOperatorGraph(InputStream fis) {
		try {
			OperatorGraphFile ogFile = OperatorGraphFile.read(fis);
			FreakGraphModel model = ogFile.generateGraph(schedule);
			model.getOperatorGraph().testSchedule(schedule);
			model.getOperatorGraph().removeFromEventController();
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	/**
	 * This method returns a list of all predefined graphs which are compatible
	 * with the environment chosen so far. If lookFor is not null, only graphs
	 * are returned whose names equal lookFor. 
	 * @param lookFor the graph to be looked for, null if all compatible graphs should be returned. 
	 * @return the list of graphs
	 */
	public ModuleInfo[] getPredefinedGraphs(String lookFor) {
		ArrayList infos = new ArrayList();
		String[] classpaths = getClassPaths();
		String startedFrom=ClassCollector.getStartedFrom();

		// if started from the jar file
		// on Mac OS X the classpaths.length is 2 if started from a jar file, because
		// /System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Classes/.compatibility/14compatibility.jar
		// is part of the classpath
		// if (classpaths.length <= 2 && classpaths[0].length() > 4 && classpaths[0].toLowerCase().endsWith(".jar")) {
		// NEW CHECK
		if (startedFrom.toLowerCase().endsWith(".jar")) {
			//now look in the resource directory
			String resDir = startedFrom.substring(0, startedFrom.lastIndexOf("/") + 1) + "resource";
			classpaths = new String[] { resDir };
		}
		for (int cp = 0; cp < classpaths.length; cp++) {
			collectFilesInFolder(infos, lookFor, new File(classpaths[cp] + "/freak/module/graph/common/"));

			String searchspaceString = schedule.getGenotypeSearchSpace().getClass().getName();			
			searchspaceString = searchspaceString.substring(searchspaceString.lastIndexOf(".") + 1).toLowerCase();
			collectFilesInFolder(infos, lookFor, new File(classpaths[cp] + "/freak/module/graph/" + searchspaceString));
		}
		Object[] o = infos.toArray();
		java.util.Arrays.sort(o);
		ModuleInfo[] mi = new ModuleInfo[o.length];
		for (int i = 0; i < o.length; i++) {
			mi[i] = (ModuleInfo)o[i];
		}
		return mi;
	}
	
	private void collectFilesInFolder(ArrayList infos, String lookFor, File graphFolder) {
		File[] allFiles = graphFolder.listFiles(new GraphFileFilter());
		if (allFiles != null) {
			for (int i = 0; i < allFiles.length; i++) {
				ModuleInfo info = new ModuleInfo("", "", allFiles[i].getAbsolutePath());
				if ((lookFor == null) || (lookFor.equals(allFiles[i].getName()))) {
					if (isCorrectOperatorGraph(allFiles[i])) {
						String name = allFiles[i].getName().substring(0, allFiles[i].getName().indexOf('.'));
						info.setName(name);
						infos.add(info);
					}
				}
			}
		}
	}

}
