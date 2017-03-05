
/*=======================================================================*
 * Copyright 2009-2010		                                             *
 * Alfan Farizki Wicaksono			                                     *
 * Institute of Technology Bandung, INDONESIA                            *
 *																	     *
 * This program is free software; you can redistribute it and/or modify  *
 * it under the terms of the GNU General Public License as published by  *
 * the Free Software Foundation; either version 2 of the License, or     *
 * (at your option) any later version.                                   *
 * 																		 *
 * This program is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 * GNU General Public License for more details.                          *
 *                                                                       *
 *=======================================================================*/
 
package NLP_ITB.POSTagger.HMM.Decoder;
 
public class MatrixEntryBigram
{
	public int tag;
	public MatrixEntryBigram bps;
	public double probs;
	
	public MatrixEntryBigram(int tag) 
	{
		this.tag = tag;
	}
}
