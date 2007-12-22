/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 * 
 * This file is a modification of the original file distributed with
 * FrEAK (http://sourceforge.net/projects/freak427/).
 * Last modification: 06/28/2007
 */

package freak.core.view.swingsupport;

import javax.swing.*;

/**
 * A freak model for TextAreas.
 * @author  Stefan
 */
public class FreakTextAreaModel implements FreakSwingModel {
	private transient JTextArea textArea;

	private String text;

	/**
	 * Link the text of the JTextArea view to this model. The text area will be
	 * adapted once immidiately and after that on every call to flush.
	 */
	public void setView(JTextArea view) {
		textArea = view;
		flush();
	}

	/**
	 * @param text  the text to set
	 * @uml.property  name="text"
	 */
	public void setText(String text) {
		this.text = text;
	}

	public void flush() {
		if (textArea == null)
			return;

		textArea.setText(text);
	}
}
