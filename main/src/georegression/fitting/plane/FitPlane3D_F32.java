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

package georegression.fitting.plane;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.linsol.qr.SolveNullSpaceQR_FDRM;

import java.util.List;

/**
 * Various functions for fitting planes in 3D to point clouds.
 *
 * @author Peter Abeles
 */
public class FitPlane3D_F32 {

	SolveNullSpaceQR_FDRM solverNull = new SolveNullSpaceQR_FDRM();

	FMatrixRMaj A = new FMatrixRMaj(3,3);
	FMatrixRMaj nullspace = new FMatrixRMaj(3,1);

	/**
	 * SVD based method for fitting a plane to a set of points.  The plane's equation is returned
	 * as a point on the plane and the normal vector.
	 *
	 * @param points (Input) Set of points on a plane.
	 * @param outputCenter (Output) Centroid of the passed in points. Modified.
	 * @param outputNormal (Output) Vector tangent to the plane.  Normalized.  Modified.
	 * @return true if successful or false if it failed.
	 */
	public boolean svd( List<Point3D_F32> points , Point3D_F32 outputCenter , Vector3D_F32 outputNormal ) {

		final int N = points.size();

		// find the centroid
		outputCenter.set(0,0,0);
		for( int i = 0; i < N; i++ ) {
			Point3D_F32 p = points.get(i);
			outputCenter.x += p.x;
			outputCenter.y += p.y;
			outputCenter.z += p.z;
		}

		outputCenter.x /= N;
		outputCenter.y /= N;
		outputCenter.z /= N;

		return solvePoint(points,outputCenter,outputNormal);
	}

	/**
	 * SVD based method for fitting a plane to a set of points and a known point on the plane.  The plane's
	 * equation is returned as a point on the plane and the normal vector.
	 *
	 * @param points (Input)Set of points on a plane.
	 * @param pointOnPlane (Input) A known point on the plane
	 * @param outputNormal (Output) Vector tangent to the plane.  Normalized. Modified.
	 * @return true if successful or false if it failed.
	 */
	public boolean solvePoint(List<Point3D_F32> points , Point3D_F32 pointOnPlane , Vector3D_F32 outputNormal ) {

		final int N = points.size();

		// construct the matrix
		A.reshape(N,3);
		int index = 0;
		for( int i = 0; i < N; i++ ) {
			Point3D_F32 p = points.get(i);
			A.data[index++] = p.x - pointOnPlane.x;
			A.data[index++] = p.y - pointOnPlane.y;
			A.data[index++] = p.z - pointOnPlane.z;
		}

		// decompose and find the singular value
		if( !solverNull.process(A,1,nullspace) )
			return false;

		// the normal is the singular vector
		outputNormal.x = (float) nullspace.unsafe_get(0,0);
		outputNormal.y = (float) nullspace.unsafe_get(1,0);
		outputNormal.z = (float) nullspace.unsafe_get(2,0);

		return true;
	}
}
