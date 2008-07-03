package freak.module.fitness.pointset.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import freak.module.fitness.pointset.math.EqSolvers;
import freak.module.fitness.pointset.math.matrix.Matrix;
import freak.module.fitness.pointset.math.matrix.Vector;
import freak.module.fitness.pointset.math.matrix.view.NormalView;
import freak.module.fitness.pointset.math.matrix.view.VectorView;
import freak.module.searchspace.PointSet;

public class ResidualHyperPlane extends HyperPlane implements Serializable{
	
	public ResidualHyperPlane(){
		
	}
	
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
