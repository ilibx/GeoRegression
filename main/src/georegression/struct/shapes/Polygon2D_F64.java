/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.struct.shapes;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.metric.Area2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import org.ddogleg.struct.FastQueue;

import java.io.Serializable;

/**
 * Describes a polygon in 2D.
 *
 * @author Peter Abeles
 */
public class Polygon2D_F64 implements Serializable {

	// vertexes in the polygon
	public FastQueue<Point2D_F64> vertexes;

	public Polygon2D_F64( int numVertexes ) {
		vertexes = new FastQueue<Point2D_F64>(Point2D_F64.class,true);

		vertexes.growArray(numVertexes);
		vertexes.size = numVertexes;
	}

	public Polygon2D_F64( double... points ) {
		if( points.length % 2 == 1 )
			throw new IllegalArgumentException("Expected an even number");
		vertexes = new FastQueue<Point2D_F64>(Point2D_F64.class,true);
		vertexes.growArray(points.length/2);
		vertexes.size = points.length/2;

		int count = 0;
		for (int i = 0; i < points.length; i += 2) {
			vertexes.data[count++].set( points[i],points[i+1]);
		}
	}

	public Polygon2D_F64() {
		vertexes = new FastQueue<Point2D_F64>(Point2D_F64.class,true);
	}

	public void set( Polygon2D_F64 orig ) {
		vertexes.resize(orig.size());
		for (int i = 0; i < orig.size(); i++) {
			vertexes.data[i].set( orig.vertexes.data[i]);
		}
	}

	public void set( int index , double x , double y ) {
		vertexes.data[index].set(x,y);
	}

	public Point2D_F64 get( int index ) {
		return vertexes.data[index];
	}

	public int size() {
		return vertexes.size();
	}

	public double area() {
		if( isConvex())
			return Area2D_F64.polygonConvex(this);
		else
			throw new RuntimeException("Doesn't support area for concave polygons yet");
	}

	public boolean isConvex() {
		return UtilPolygons2D_F64.isConvex(this);
	}

	public LineSegment2D_F64 getLine( int index , LineSegment2D_F64 storage ) {
		if( storage == null )
			storage = new LineSegment2D_F64();

		int j = (index+1)%vertexes.size;

		storage.a.set(get(index));
		storage.b.set(get(j));

		return storage;
	}
}
