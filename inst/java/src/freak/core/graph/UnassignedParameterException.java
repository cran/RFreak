/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 * 
 * This file is a copy of the original file distributed with
 * FrEAK (http://sourceforge.net/projects/freak427/).
 */

package freak.core.graph;

/**
 * @author Kai
 */
public class UnassignedParameterException extends GraphSyntaxException {

	/**
	 * Standard constructor.
	 */
	public UnassignedParameterException() {
	}

	/**
	 * Constructor which takes a String message as
	 * argument.
	 * 
	 * @param msg the message.
	 */
	public UnassignedParameterException(String msg) {
		super(msg);
	}

}
