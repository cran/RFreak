/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 * 
 * This file is a copy of the original file distributed with
 * FrEAK (http://sourceforge.net/projects/freak427/).
 */

package freak.core.searchspace;

import freak.core.control.*;
import freak.core.modulesupport.*;

/**
 * An abstract superclass for all search spaces.
 * 
 * @author Stefan
 */
public abstract class AbstractSearchSpace extends AbstractModule implements SearchSpace {
	public AbstractSearchSpace(Schedule schedule) {
		super(schedule);
	}
}
