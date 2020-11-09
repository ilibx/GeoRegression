/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.polygon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestThreeIndexes {
	@Test void set() {
		var a = new ThreeIndexes();
		a.set(2,3,4);
		assertEquals(2, a.idx0);
		assertEquals(3, a.idx1);
		assertEquals(4, a.idx2);
	}

	@Test void reset() {
		var a = new ThreeIndexes();
		a.set(2,3,4);
		a.reset();
		assertEquals(-1, a.idx0);
		assertEquals(-1, a.idx1);
		assertEquals(-1, a.idx2);
	}
}