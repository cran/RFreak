/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@tu-dortmund.deu>.
 */

package freak.module.fitness.pointset;

import freak.core.control.Schedule;
import freak.core.fitness.AbstractFitnessFunction;
import freak.core.modulesupport.Configurable;
import freak.core.population.Genotype;
import freak.module.fitness.pointset.util.ResidualContainer;
import freak.module.fitness.pointset.util.ResidualHyperPlane;
import freak.module.searchspace.PointSet;
import freak.module.searchspace.PointSetGenotype;

public abstract class AbstractRobustRegressionFitness extends AbstractFitnessFunction implements Configurable {

	protected int[] chosenIndices=null;
	private double[] fittedHyperplane=null;

	protected int offset = -5000; // is used to divide
	
	protected String m_zName = "LXX";
	protected String m_zInstanceName = "";

	protected int h;	
	
	protected ResidualHyperPlane hyperPlane = new ResidualHyperPlane();

	protected boolean interceptAdjust = false;
	
	public ResidualContainer [] m_hResiduals = null;

	public AbstractRobustRegressionFitness(Schedule schedule) {
		super(schedule);
		if (schedule.getPhenotypeSearchSpace() instanceof PointSet) {
			PointSet ps = (PointSet) schedule.getPhenotypeSearchSpace();
			h=computeH(ps.getDimension(),ps.getPointDimension());
		}
	}
	
	public ResidualHyperPlane getResidualHyperPlane(){
		return hyperPlane;
	}
	
	public ResidualContainer [] getResiduals(){
		return m_hResiduals;
	}
	
	protected void rememberChoosenParameters(){
		int dim=hyperPlane.getParameter().getDimension();
		fittedHyperplane=new double[dim];
		for (int i=0;i<dim;i++) {
			try {
				fittedHyperplane[i]=hyperPlane.getParameter().get(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		chosenIndices= new int[h];
		for(int i = 0; i < h; i++){
			chosenIndices[i]=m_hResiduals[i].pointIndexInPointSet;
		}
	}

	
	
	protected abstract double evaluateResiduals();


	protected int computeH(int dimension,int pointDimension){
		double alpha = 0.5;
		return (int)(2.0*(int)(((double)(dimension+pointDimension+1))/2.0)-((double)dimension)+2.0*alpha*(((double)dimension)-(int)(((double)(dimension+pointDimension+1))/2.0)));
	}

	protected void adjustHyperplane(Genotype genotype) {
		// Adjust hyperplane by OLS
		java.util.Arrays.sort(m_hResiduals);
		hyperPlane.computeParamLS(((PointSetGenotype)genotype).getPoints(), m_hResiduals, h);
	}

	protected void calculateResiduals(Genotype genotype) throws WrongNumberOfPointsException {
		/*calculate Residuals in the same way as the c++ impelmentation
		 *the calculated Residuals will be stored in the array m_hResiduals 
		 */

		PointSet.Point [] supportPoints = ((PointSetGenotype)genotype).getChoosenSubSet();
		
		PointSet.Point [] allPoints = ((PointSetGenotype)genotype).getPoints();
		
		/*if (schedule.getCurrentGeneration()==2) {
			for (int i=0;i<allPoints.length;i++) {
				System.out.println(allPoints[i].toString());
			}
		}*/
		
		int pointDim = ((PointSetGenotype)genotype).getPointDimension();
		
		int quantile = h;//((PointSetGenotype)genotype).getH();
		
		if (supportPoints.length == 0){
			throw new WrongNumberOfPointsException(-pointDim);
		}
		
		if (supportPoints.length != supportPoints[0].getDimension()){
			throw new WrongNumberOfPointsException( -(supportPoints.length - pointDim)); 
		}
		
		hyperPlane.computeParam(supportPoints);
		
		m_hResiduals = new ResidualContainer[allPoints.length];
		
		hyperPlane.updateResiduals(m_hResiduals, allPoints);
			
	}
	
	/**
	 * @return the fittedHyperplane
	 */
	public double[] getFittedHyperplane() {
		return fittedHyperplane;
	}	

	public String getName() {
		return m_zName + ((!m_zInstanceName.equals(""))?(": "+m_zInstanceName):(""));
	}
	
	public Genotype getPhenotypeOptimum() throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
	
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


}
