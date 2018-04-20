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

package georegression;

import com.peterabeles.auto64fto32f.ConvertFile32From64;
import com.peterabeles.auto64fto32f.RecursiveConvert;

import java.io.File;

/**
 * Auto generates 32bit code from 64bit code.
 *
 * @author Peter Abeles
 */
public class Autocode64to32App extends RecursiveConvert {


	public Autocode64to32App(ConvertFile32From64 converter) {
		super(converter);
	}

	public static void main(String args[]) {
		String path = "./";
		while( true ) {
			File d = new File(path);
			if( new File(d,"main").exists() )
				break;
			path = "../"+path;
		}
		System.out.println("Path to project root: "+path);

		String directories[] = new String[]{
				"main/src", "main/test",
				"experimental/src", "experimental/test"};

		ConvertFile32From64 converter = new ConvertFile32From64(true);

		converter.replacePattern("64F", "32F");
		converter.replacePattern("_DD", "_FD");
		converter.replacePattern("lookupDDRM","lookupFDRM");
		converter.replacePattern("DMatrix", "FMatrix");
		converter.replacePattern("DCONV_TOL_", "FCONV_TOL_");
		converter.replacePattern("GrlConstants.PI", "GrlConstants.F_PI");
		converter.replacePattern("GrlConstants.EPS", "GrlConstants.F_EPS");
		converter.replacePattern("rand.nextGaussian", "(float)rand.nextGaussian");

		Autocode64to32App app = new Autocode64to32App(converter);
		for (String dir : directories) {
			app.process(new File(path,dir));
		}
	}
}