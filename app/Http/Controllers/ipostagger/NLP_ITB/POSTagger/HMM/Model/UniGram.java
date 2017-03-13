
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

public class UniGram 
{
	private int tag1;
	
	public UniGram(int t1) 
	{
		this.tag1 = t1;
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
		
		UniGram other = (UniGram) otherObject;
		
		return this.tag1 == other.tag1;
	}
	
	@Override
	public int hashCode() 
	{
		return this.tag1;
	}
	
	public int t1() 
	{
		return this.tag1;
	}
}
