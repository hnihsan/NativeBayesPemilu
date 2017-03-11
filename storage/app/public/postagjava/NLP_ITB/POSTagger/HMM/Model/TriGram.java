
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

package NLP_ITB.POSTagger.HMM.Model;

public class TriGram 
{
	private int tag1;
	private int tag2;
	private int tag3;
	
	public TriGram(int t1, int t2, int t3) 
	{
		this.tag1 = t1;
		this.tag2 = t2;
		this.tag3 = t3;
	}
	
	@Override
	public boolean equals(Object otherObject) 
	{
		if (this == otherObject)
		{
			return true;
		}
		
		if (otherObject == null)
		{
			return false;
		}
		
		if (getClass() != otherObject.getClass())
		{
			return false;
		}
		
		TriGram other = (TriGram) otherObject;
		
		return (this.tag1 == other.tag1 && this.tag2 == other.tag2 && this.tag3 == other.tag3);
	}
	
	@Override
	public int hashCode() 
	{
		int seed = 0;
		seed = this.tag1;
		seed ^= this.tag2 + 0x9e3779b9 + (seed << 6) + (seed >> 2);
		seed ^= this.tag3 + 0x9e3779b9 + (seed << 6) + (seed >> 2);
		return seed;
	}
	
	public int t1() 
	{
		return this.tag1;
	}
	
	public int t2() 
	{
		return this.tag2;
	}

	public int t3() 
	{
		return this.tag3;
	}
}
