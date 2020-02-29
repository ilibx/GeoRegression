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

package georegression.fitting.sphere;

import georegression.misc.GrlConstants;
import georegression.struct.shapes.Sphere3D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestModelManagerSphere3D_F64 {

	@Test
	void createModelInstance() {
		ModelManagerSphere3D_F64 alg = new ModelManagerSphere3D_F64();

		assertTrue( alg.createModelInstance() != null);
	}

	@Test
	void copyModel() {
		ModelManagerSphere3D_F64 alg = new ModelManagerSphere3D_F64();

		Sphere3D_F64 model = new Sphere3D_F64(1,2,3,4);
		Sphere3D_F64 found = new Sphere3D_F64();

		alg.copyModel(model,found);

		assertEquals(model.center.x,found.center.x, GrlConstants.TEST_F64);
		assertEquals(model.center.y,found.center.y, GrlConstants.TEST_F64);
		assertEquals(model.center.z,found.center.z, GrlConstants.TEST_F64);

		assertEquals(model.radius, found.radius, GrlConstants.TEST_F64);
	}

}
