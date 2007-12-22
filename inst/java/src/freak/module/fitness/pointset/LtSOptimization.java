/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 */

package freak.module.fitness.pointset;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import javax.print.attribute.SupportedValuesAttribute;


import freak.core.control.Schedule;
import freak.core.fitness.AbstractStaticSingleObjectiveFitnessFunction;
import freak.core.modulesupport.ClassCollector;
import freak.core.modulesupport.Configurable;
import freak.core.modulesupport.inspector.StringArrayWrapper;
import freak.core.population.Genotype;
import freak.module.fitness.pointset.math.EqSolvers;
import freak.module.fitness.pointset.math.EquationSolver;
import freak.module.fitness.pointset.math.matrix.Matrix;
import freak.module.fitness.pointset.math.matrix.Vector;
import freak.module.fitness.pointset.math.matrix.view.MatrixSimpleOperation;
import freak.module.fitness.pointset.math.matrix.view.NormalView;
import freak.module.fitness.pointset.math.matrix.view.TransposeView;
import freak.module.fitness.pointset.math.matrix.view.VectorView;
import freak.module.fitness.pointset.util.ResidualContainer;
import freak.module.searchspace.Cycle;
import freak.module.searchspace.PointSet;
import freak.module.searchspace.PointSetGenotype;



class HyperPlane {
	protected int dimension;
	protected VectorView parameter;
	
	public HyperPlane(PointSet.Point [] supportPoints){
		computeParam(supportPoints);
	}
	
	public void computeParam(PointSet.Point [] supportPoints){
		
		dimension = supportPoints[0].getDimension();
		
		if (supportPoints.length < dimension){
			throw new Error("Not enough points to determine the hyperplane.");
		}
		Matrix m = new Matrix(dimension);
		Vector b = new Vector(dimension);
		
		NormalView vm = new NormalView(m);
		VectorView vb = new VectorView(b);
		
		try{
		
			for (int i = 0; i < dimension; i++){
				vm.set(i,0,1.0);
				vb.set(i,supportPoints[i].getK(0));
					for (int j = 1; j < dimension; j++){
						vm.set(i,j,supportPoints[i].getK(j));
					}
			}
			
			/* Next Steps Computes TM*M , TM*B */
			TransposeView tm = new TransposeView(m);
			
			MatrixSimpleOperation sop = new MatrixSimpleOperation(tm);
			
			NormalView tmm = sop.multMatrix(vm); // Calcs TM*M
			
						
			NormalView tmbm = sop.multMatrix(vb); // Calcs TM * B (result Matrix)
			
			Vector tmb = new Vector(tmbm); // Converts Matrix to Vector
						
			EqSolvers eqSolver = new EqSolvers(tmm.getMatrix(),tmb);
			//EqSolvers eqSolver = new EqSolvers(m,b);
			parameter = new VectorView(eqSolver.solve());
		} catch (Exception e){
			throw new Error(e);
		}
				
	}
	
	
		
}

class ResidualHyperPlane extends HyperPlane{
	
	public ResidualHyperPlane(PointSet.Point [] supportPoints){
		super(supportPoints);
	}
	
	public void updateResiduals(Collection<ResidualContainer> col, PointSet.Point [] pAllPoints){
		
		int i = 0;
		for (Iterator<ResidualContainer> it = col.iterator(); it.hasNext();){
			ResidualContainer aktRes = it.next();
			
			
			// comute Residual value
			double resValue = 0;
			try{
				resValue = pAllPoints[i].getK(0) - parameter.get(0);
				for (int j = 1; j < dimension; j++){
					resValue -= parameter.get(j) * pAllPoints[i].getK(j); 
				}
			} catch (Exception e){throw new Error(e);}		
			aktRes.pointIndexInPointSet 	= i;
			aktRes.signedResidual 	= resValue;
			aktRes.squaredResidual 	= resValue * resValue;
			i++;
		}
		
	}
	
	private void updateResidual(PointSet.Point [] pAllPoints, ResidualContainer residual){
		double resValue = 0;
		try{
			resValue = pAllPoints[residual.pointIndexInPointSet].getK(0) - parameter.get(0);
			for (int j = 1; j < dimension; j++){
				resValue -= parameter.get(j) * pAllPoints[residual.pointIndexInPointSet].getK(j); 
			}
		} catch (Exception e){throw new Error(e);}
		
		residual.signedResidual  = resValue;
		residual.squaredResidual = resValue*resValue;
	}
	
	public void updateResiduals(ResidualContainer[] col, PointSet.Point [] pAllPoints){
		
		for (int i = 0; i < col.length;i++){
			// comute Residual value
			double resValue = 0;
			if (col[i] != null){
				col[i].pointIndexInPointSet 	= i;
			} else {
				col[i] = new ResidualContainer(i,0,0);
			}
			updateResidual(pAllPoints, col[i]);
		}
	}
	
	public void computeParamLS(PointSet.Point[] pAllPoints, ResidualContainer [] residuals, int quantile){
		Matrix m = new Matrix(dimension);
		NormalView vm = new NormalView(m);
		for (int i = 0; i < dimension; i++){
			for (int j = 0; j < dimension; j++){
				double w = 0;
				for (int k = 0; k < quantile; k++){
					double a,b;
					if (i == 0) {
						a = 1.0;
					} else {
						a = pAllPoints[residuals[k].pointIndexInPointSet].getK(i);
					}
					if (j == 0){
						b = 1.0;
					} else {
						b = pAllPoints[residuals[k].pointIndexInPointSet].getK(j);
					}
					w+=a*b;
				}
				try{
					vm.set(i, j, w);
				} catch(Exception e){throw new Error(e);}
			}
		}
		Vector b = new Vector(dimension);
		VectorView vb = new VectorView(b);
		for (int i = 0; i < dimension; i++){
			double w = 0; 
			for (int k = 0; k < quantile; k++){
				double a = 0;
				if (i == 0){
					a = 1.0;
				} else {
					a = pAllPoints[residuals[k].pointIndexInPointSet].getK(i);
				}
				w+= a * pAllPoints[residuals[k].pointIndexInPointSet].getK(0);
			}
			try{
				vb.set(i, w);
			} catch(Exception e){throw new Error(e);}
		}
		
		try{
			EqSolvers solver = new EqSolvers(m,b);
		
			parameter = new VectorView(solver.solve());
			
			for (int i = 0; i < quantile; i++){
				updateResidual(pAllPoints, residuals[i]);
			}
		} catch (Exception e){
			throw new Error(e);
		}
		
	}
	

	public static void main (String[] args){
		PointSet.Point [] testPoints = new PointSet.Point [] {
			new PointSet.Point(2),new PointSet.Point(2)
		};
		testPoints[0].setK(0, 1);
		testPoints[0].setK(1, 1);
		
		testPoints[1].setK(0, 2);
		testPoints[1].setK(1, 2);
		
		ResidualHyperPlane testPlane = new ResidualHyperPlane(testPoints);
		
		System.out.println("Done");
		
		PointSet.Point testPoint = new PointSet.Point(2);
		testPoint.setK(0, 1);
		testPoint.setK(1, 1);
		
		double resValue = -1;
		try{
			resValue = testPoint.getK(0) - testPlane.parameter.get(0);
			for (int j = 1; j < testPlane.dimension; j++){
				resValue -= testPlane.parameter.get(j) * testPoint.getK(j); 
			}
		} catch(Exception e){
			throw new Error(e);
		}
		
		System.out.println(resValue);
		
	}
		
}



public class LtSOptimization extends
		AbstractStaticSingleObjectiveFitnessFunction implements Configurable {

	public LtSOptimization(Schedule schedule){
		super(schedule);
		if (schedule.getPhenotypeSearchSpace() instanceof PointSet) {
			PointSet ps = (PointSet) schedule.getPhenotypeSearchSpace();
			h=computeH(ps.getDimension(),ps.getPointDimension());
		}
	}
	private int[] chosenIndices=null;
	private double[] fittedHyperplane=null;

	int offset = -5000; // is used to divide
	
	String ltsFileListCache[] = null;
	Object[] ltsFileArrayCache = null;
	
	String m_zName = "LTS (Least Trimmed Squares)";
	String m_zInstanceName = "";
//	int m_zLtSChoice = 0;
	boolean interceptAdjust = false;
	int h;	
	
	public ResidualContainer [] m_hResiduals = null;

	private int computeH(int dimension,int pointDimension){
		double alpha = 0.5;
		return (int)(2.0*(int)(((double)(dimension+pointDimension+1))/2.0)-((double)dimension)+2.0*alpha*(((double)dimension)-(int)(((double)(dimension+pointDimension+1))/2.0)));
	}

	@Override
	protected double evaluate(Genotype genotype) {
		// TODO Auto-generated method stub
		
		PointSet.Point [] supportPoints = ((PointSetGenotype)genotype).getChoosenSubSet();
		
		PointSet.Point [] allPoints = ((PointSetGenotype)genotype).getPoints();
		
/*		if (schedule.getCurrentGeneration()==2) {
			for (int i=0;i<allPoints.length;i++) {
				System.out.println(allPoints[i].toString());
			}
		}*/
		
		int pointDim = ((PointSetGenotype)genotype).getPointDimension();
		
		int quantile = h;//((PointSetGenotype)genotype).getH();
		
		if (supportPoints.length == 0){
			return -pointDim +offset;
		}
		
		if (supportPoints.length != supportPoints[0].getDimension()){
			return -(supportPoints.length - pointDim) +offset; 
		}
				
		ResidualHyperPlane hyperPlane = new ResidualHyperPlane(supportPoints);
		
		m_hResiduals = new ResidualContainer[allPoints.length];
		
		hyperPlane.updateResiduals(m_hResiduals, allPoints);
		
		java.util.Arrays.sort(m_hResiduals);
		
				
		if (interceptAdjust){
			hyperPlane.computeParamLS(allPoints, m_hResiduals, quantile);
		}
		
		double valueOfSolution = 0;
		chosenIndices=new int[quantile];
		for(int i = 0; i < quantile; i++){
			valueOfSolution += m_hResiduals[i].squaredResidual;
			chosenIndices[i]=m_hResiduals[i].pointIndexInPointSet;
		}
		int dim=hyperPlane.parameter.getDimension();
		fittedHyperplane=new double[dim];
		for (int i=0;i<dim;i++) {
			try {
				fittedHyperplane[i]=hyperPlane.parameter.get(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return -valueOfSolution;
	}
	
	public String getShortDescriptionForAdjust() {
		return "Adjust Intercept";
	}


	public String getLongDescriptionForAdjust() {
		return "Whether to perform intercept adjustment at each step.";
	}

	public void setPropertyAdjust(Boolean adjust){
		interceptAdjust = adjust.booleanValue();
	}
	
	public Boolean getPropertyAdjust(){
		return new Boolean(interceptAdjust);
	} 
	
	
	public String getDescription() {
		// TODO Auto-generated method stub
		return "The fitness value of an individual is the least squares error of the " +
				" subset of points represented by the given individual.\n We restrict the search to subsets of size d for d-dimensional points, because they uniquely describe possible solutions.";
	} 

/*	private String[] createLTSFileList(){
		
		if (ltsFileListCache != null)
			return ltsFileListCache;
		String[] classpaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		String startedFrom=ClassCollector.getStartedFrom();
		
		// if started from the jar file
		// NEW CHECK
		if (startedFrom.toLowerCase().endsWith(".jar")) {
			String resDir = startedFrom.substring(0, startedFrom.lastIndexOf(System.getProperty("file.separator")) + 1) + "resource";
			classpaths = new String[] { resDir };
		}
		Collection auswahl = new LinkedList();
		for (int cp = 0; cp < classpaths.length; cp++) {
			File ltsFolder = new File(classpaths[cp] + "/freak/module/fitness/pointset");
			if (ltsFolder.exists()) {
				File[] files = ltsFolder.listFiles(new CommonFileFilter(".lts"));
				for (int i = 0; i < files.length; i++) {
					auswahl.add(new CommonFileInfo(files[i],4));
				}
			}
		}
		Object[] o = auswahl.toArray();
		Arrays.sort(o);
		ltsFileArrayCache = o;
		String[] result = new String[o.length+1];
		result[0] = "Random Point Values";
		for (int i =0; i < o.length; i++){
			result[i+1] = o[i].toString();
		}
		ltsFileListCache = result;
		return result;
				
	}*/
	
/*	public synchronized StringArrayWrapper getPropertyPoints() {
		return new StringArrayWrapper(createLTSFileList(), m_zLtSChoice);
	}*/
	
/*	public synchronized void setPropertyPoints(StringArrayWrapper wrapper) {
		// TODO fertigstellen
		int i = wrapper.getIndex();
		// not random 2d points
		LtSFile lts; 
		if (i == 0) {
			lts = null;
			((PointSet)getSchedule().getPhenotypeSearchSpace()).setRandomPoints(true);
		} else {
			try {
				lts = new LtSFile(((CommonFileInfo)ltsFileArrayCache[i - 1]).file);
				//edge_weight_type = tsp.getEdgeWeightType();
			} catch (Exception e) {
				System.out.println(e);
				return;
			}
			((PointSet)getSchedule().getPhenotypeSearchSpace()).setRandomPoints(false);
			
//			((PointSet)getSchedule().getPhenotypeSearchSpace()).setPropertyPointDimension(lts.getPointDimension());
//			((PointSet)getSchedule().getPhenotypeSearchSpace()).setPropertyDimension(new Integer(lts.getDimension()));
			((PointSet)getSchedule().getPhenotypeSearchSpace()).setPoints(lts.pointData);
			
			// notify the schedule that the search space dimension has been
			// changed
			schedule.callInitialize();
			
		}
		m_zLtSChoice = i;		
		//schedule.callInitialize(); // !!!!!!
			
	}*/
	
/*	public String getShortDescriptionForPoints() {
		return "Data";
	}*/
	
	public double getOptimalFitnessValue()throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
	
	public double getLowerBound() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	public double getUpperBound() throws UnsupportedOperationException {
		return getOptimalFitnessValue();
	}
	
	public String getName() {
		return m_zName + ((!m_zInstanceName.equals(""))?(": "+m_zInstanceName):(""));
	}
	
	public Genotype getPhenotypeOptimum() throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}

/*	class CommonFileInfo implements Comparable, Serializable {
		File file;
		String name;
		
		public CommonFileInfo(File f, int hide) {
			String tspName = f.getName();
			name = tspName.substring(0, tspName.length() - 4).toUpperCase();
			file = f;
		}
		
		public int compareTo(Object o) {
			return name.compareTo(((CommonFileInfo)o).name);
		}
		
		public String toString(){
			return name;
		}
	}*/
	
	public String getShortDescriptionForH() {
		return "h";
	}

	public String getLongDescriptionForH() {
		return "Size of the subset of points.";
	}

	
	public Integer getPropertyH(){
		return new Integer(h);
	}
	
	public void setPropertyH(Integer H){
		if (H.intValue() > 0)
			h = H.intValue();
	}

	/**
	 * @return the chosenIndices
	 */
	public int[] getChosenIndices() {
		return chosenIndices;
	}

	/**
	 * @return the fittedHyperplane
	 */
	public double[] getFittedHyperplane() {
		return fittedHyperplane;
	}

	
/*	private static class CommonFileFilter implements FileFilter {
		String endtag;
		public CommonFileFilter(String endTag){
			this.endtag = endTag;
		}
		public boolean accept(File file) {
			return file.getName().endsWith(endtag);
		}
	}*/

	
}

