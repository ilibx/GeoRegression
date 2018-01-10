/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression.fitting.ellipse;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.EllipseRotated_F32;
import org.ddogleg.optimization.FactoryOptimization;
import org.ddogleg.optimization.UnconstrainedLeastSquares;
import org.ddogleg.optimization.functions.FunctionNtoM;
import org.ddogleg.optimization.functions.FunctionNtoMxN;
import org.ejml.data.DMatrixRMaj;

import java.util.List;

/**
 * <p>
 * Minimizes the Euclidean distance between an ellipse and a set of points which it has been fit to.  Minimization
 * is done using a user configurable unconstrained optimization algorithm.  The error for each observation 'i' is
 * computed using the following equation:<br>
 * [x,y] = [p_x,p_y] - ([x_0,y_0] - a*R*X)<br>
 * where R = [cos(phi),-sin(phi);sin(phi),cos(phi)] and X = [a*cos(theta),b*sin*(theta)], where theta is the angle
 * of the closest point on the ellipse for the point.
 * </p>
 *
 * <p>
 * NOTE: This implementation does not take advantage of the sparsity found in the Jacobian.  Could be speed up a bit.
 * </p>
 *
 * @author Peter Abeles
 */
public class RefineEllipseEuclideanLeastSquares_F32 {

	// optimization routine
	protected UnconstrainedLeastSquares optimizer;
	// convergence parameters
	float ftol= GrlConstants.FCONV_TOL_B,gtol=GrlConstants.FCONV_TOL_B;
	int maxIterations=500;

	// used to find initial theta
	ClosestPointEllipseAngle_F32 closestPoint = new ClosestPointEllipseAngle_F32(GrlConstants.FCONV_TOL_B,100);

	// passed in observations
	List<Point2D_F32> points;

	// storage for optimized parameters
	EllipseRotated_F32 found = new EllipseRotated_F32();

	// initial set of parameters
	/**/double initialParam[] = new /**/double[0];

	// error using the initial parameters
	/**/double initialError;

	public RefineEllipseEuclideanLeastSquares_F32(UnconstrainedLeastSquares optimizer ) {
		this.optimizer = optimizer;
	}

	/**
	 * Defaults to a robust solver since this problem often encounters singularities.
	 */
	public RefineEllipseEuclideanLeastSquares_F32() {
		this(FactoryOptimization.leastSquaresLM(1e-3, true));
	}

	public void setFtol(float ftol) {
		this.ftol = ftol;
	}

	public void setGtol(float gtol) {
		this.gtol = gtol;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public UnconstrainedLeastSquares getOptimizer() {
		return optimizer;
	}

	public boolean refine( EllipseRotated_F32 initial , List<Point2D_F32> points ) {
		this.points = points;

		// create initial parameters
		int numParam = 5 + points.size();
		if( numParam > initialParam.length ) {
			initialParam = new /**/double[ numParam ];
		}
		initialParam[0] = initial.center.x;
		initialParam[1] = initial.center.y;
		initialParam[2] = initial.a;
		initialParam[3] = initial.b;
		initialParam[4] = initial.phi;

		closestPoint.setEllipse(initial);
		for( int i = 0; i < points.size(); i++ ) {
			closestPoint.process(points.get(i));
			initialParam[5+i] = closestPoint.getTheta();
		}

		// start optimization
		optimizer.setFunction(new Error(),null);
		optimizer.initialize(initialParam,ftol,gtol);
		initialError = optimizer.getFunctionValue();

		for( int i = 0; i < maxIterations; i++ ) {
			if( optimizer.iterate() )
				break;
		}

		// decode found results
		/**/double[] foundParam = optimizer.getParameters();
		found.center.x = (float)foundParam[0];
		found.center.y = (float)foundParam[1];
		found.a = (float)foundParam[2];
		found.b = (float)foundParam[3];
		found.phi = (float)foundParam[4];

		return true;
	}

	public EllipseRotated_F32 getFound() {
		return found;
	}

	public float getFitError() {
		return (float)optimizer.getFunctionValue();
	}

	protected Error createError() {
		return new Error();
	}

	protected Jacobian createJacobian() {
		return new Jacobian();
	}

	/**
	 *
	 */
	public class Error implements FunctionNtoM {

		@Override
		public int getNumOfInputsN() {
			return 5 + points.size();
		}

		@Override
		public int getNumOfOutputsM() {
			return 2*points.size();
		}

		@Override
		public void process( /**/double[] input, /**/double[] output) {
			/**/double x0  = input[0];
			/**/double y0  = input[1];
			/**/double a   = input[2];
			/**/double b   = input[3];
			/**/double phi = input[4];

			/**/double c = /**/Math.cos(phi);
			/**/double s = /**/Math.sin(phi);

			int indexOut = 0;
			for( int i = 0; i < points.size(); i++ ) {
				Point2D_F32 p = points.get(i);
				/**/double theta = input[5+i];

				/**/double x = a*/**/Math.cos(theta);
				/**/double y = b*/**/Math.sin(theta);

				/**/double xx = x0 + c*x - s*y;
				/**/double yy = y0 + s*x + c*y;

				output[indexOut++] = p.x - xx;
				output[indexOut++] = p.y - yy;
			}
		}
	}

	public class Jacobian implements FunctionNtoMxN< /**/DMatrixRMaj > {

		@Override
		public int getNumOfInputsN() {
			return 5 + points.size();
		}

		@Override
		public int getNumOfOutputsM() {
			return 2*points.size();
		}

		@Override
		public void process( /**/double[] input, /**/DMatrixRMaj output) {
			/**/double a   = input[2];
			/**/double b   = input[3];
			/**/double phi = input[4];

			/**/double cp = /**/Math.cos(phi);
			/**/double sp = /**/Math.sin(phi);

			int M = getNumOfOutputsM();
			int N = getNumOfInputsN();

			int total = M*N;
			for( int i = 0; i < total; i++ )
				output.data[i] = 0;

			for( int i = 0; i < points.size(); i++ ) {
				/**/double theta = input[5+i];

				/**/double ct = /**/Math.cos(theta);
				/**/double st = /**/Math.sin(theta);

				int indexX = 2*i*N;
				int indexY = indexX + N;

				// partial x0
				output.data[indexX++] = -1;
				output.data[indexY++] = 0;
				// partial y0
				output.data[indexX++] = 0;
				output.data[indexY++] = -1;
				// partial a
				output.data[indexX++] = -cp*ct;
				output.data[indexY++] = -sp*ct;
				// partial b
				output.data[indexX++] =  sp*st;
				output.data[indexY++] = -cp*st;
				// partial phi
				output.data[indexX++] =  a*sp*ct + b*cp*st;
				output.data[indexY++] = -a*cp*ct + b*sp*st;

				// partial theta(i)
				output.data[ indexX + i] = a*cp*st + b*sp*cp;
				output.data[ indexY + i] = a*sp*st - b*cp*cp;
			}
		}

		@Override
		public /**/DMatrixRMaj declareMatrixMxN() {
			return new /**/DMatrixRMaj(getNumOfOutputsM(),getNumOfInputsN());
		}
	}
}
