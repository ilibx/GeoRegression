/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.trig;

import georegression.struct.point.Point2D_F32;

/**
 * Describes a circle in 2D space using it's center and radius.
 *
 * @author Peter Abeles
 */
public class Circle2D_F32 {
	/**
	 * Radius of the circle
	 */
	public float radius;
	/**
	 * Center of the circle
	 */
	public Point2D_F32 center = new Point2D_F32();
}
