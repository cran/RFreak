/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 * 
 * This file is a copy of the original file distributed with
 * FrEAK (http://sourceforge.net/projects/freak427/).
 */

package freak.gui.scheduleeditor;

import java.awt.Dialog;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.text.JTextComponent;

import freak.core.control.GenerationIndex;
import freak.core.control.Schedule;
import freak.core.fitness.FitnessFunction;
import freak.core.fitness.FitnessTransformer;
import freak.core.graph.FreakGraphModel;
import freak.core.modulesupport.Configurable;
import freak.core.modulesupport.Configuration;
import freak.core.modulesupport.Module;
import freak.gui.PropertyDialog;
import freak.gui.graph.OperatorGraphFile;

/**
 * @author Oliver, Matthias
 */
public final class Util {

	/**
	 * Creates a <code>DefaultListModel</code> for use in <code>JLists</code>
	 * and initializes it with values from a <code>List</code>.
	 * @param l the <code>List</code>
	 * @return the newly created <code>DefaultListModel</code>
	 */
	public static DefaultListModel createListModelFrom(List l) {
		DefaultListModel listModel = new DefaultListModel();
		for (Iterator i = l.iterator(); i.hasNext();) {
			Object o = i.next();
			if (Module.class.isAssignableFrom(o.getClass())) {
				Module m = (Module)o;
				listModel.addElement(m.getName());
			} else {
				listModel.addElement(o);
			}
		}
		return listModel;
	}

	/**
	 * Opens a PropertyDialog and lets the user configure a Module object.
	 * Finally it returns a String with the new options in human readable format.
	 * @param parent the parent dialog
	 * @param mod the Module to be configured
	 * @param schedule the currently active <code>Schedule</code>
	 * @return the configuration string
	 */
	public static String configureModule(Dialog parent, Module mod, Schedule schedule) {
		if ((mod != null) && (mod instanceof Configurable)) {
			PropertyDialog pd = new PropertyDialog(parent, schedule.getEventController(), mod, schedule.getPossibleEventSources(), schedule.getBatchList());
			pd.setVisible(true);
			Configuration conf = ((Configurable)mod).getConfiguration();
			return conf.getDescription();
		} else
			return "<not configurable>";
	}

	/**
	 * Sets the Text of a JTextComponent <data>ta</data> to the specified value
	 * <data>s</data> and sets the caret position to 0.
	 * @param ta the JTextArea component
	 * @param s the new text
	 */
	public static void displayText(JTextComponent tc, String s) {
		tc.setText(s);
		tc.setCaretPosition(0);
	}

	public static List getFitnessTransformersFrom(Schedule schedule) {
		List l = new ArrayList();
		FitnessFunction f = schedule.getFitnessFunction();
		if (f instanceof FitnessTransformer) {
			FitnessTransformer ft = (FitnessTransformer)f;
			while (ft != null) {
				l.add(ft);
				if (ft.getFitnessFunction() instanceof FitnessTransformer) {
					ft = (FitnessTransformer)ft.getFitnessFunction();
				} else {
					ft = null;
				}
			}
		}
		return l;
	}

	public static boolean scheduleStarted(Schedule schedule) {
		return !schedule.getCurrentTimeIndex().equals(GenerationIndex.START);
	}

	/**
	 * Loads an <code>OperatorGraph</code> from the given <code>fileName</code>.
	 * @param fileName the filename of the graph
	 * @param schedule the current schedule
	 * @return the loaded <code>OperatorGraph</code>
	 */
	public static FreakGraphModel loadOperatorGraph(String fileName, Schedule schedule) {
		try {
			OperatorGraphFile ogFile = OperatorGraphFile.read(new FileInputStream(new File(fileName)));
			return ogFile.generateGraph(schedule);
		} catch (Exception exc) {
			System.out.println("Error loading graph " + fileName);
		}
		return null;
	}
}
