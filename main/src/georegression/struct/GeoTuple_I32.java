/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct;

/**
 * Integer tuple class
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple_I32 <T extends GeoTuple_I32<T>> extends GeoTuple<T> {
	/**
	 * Checks to see if the two GeoTuple have values which are nearly the same.  False is always
	 * returned if the dimension is different.
	 *
	 * @param t   The GeoTuple it is being compared against.
	 * @param tol How similar each element must be for them to be considered identical.
	 * @return if they are identical or not.
	 */
	public boolean isIdentical( T t, double tol ) {
		if( t.getDimension() != getDimension() )
			return false;

		int N = getDimension();
		for( int i = 0; i < N; i++ ) {
			double diff = Math.abs( getIdx( i ) - t.getIdx( i ) );

			if( diff > tol )
				return false;
		}

		return true;
	}

	/**
	 * Generic copy routine.  It is recommended that this be overridden with a faster implementation.
	 *
	 * @return An exact copy of this GeoTuple.
	 */
	@Override
	public T copy() {
		T ret = createNewInstance();

		int N = getDimension();
		for( int i = 0; i < N; i++ ) {
			ret.setIdx( i, getIdx( i ) );
		}

		return ret;
	}

	/**
	 * Returns the value of the tuple along the specified coordinate system axis.
	 *
	 * @param index Which axis in the coordinate system.
	 * @return Its value.
	 */
	public abstract int getIdx(int index );

	public abstract void setIdx(int index, int value );
}
