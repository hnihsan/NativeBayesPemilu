
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
 
 package NLP_ITB.POSTagger.HMM.Train;
 
 import java.util.Collection;
 import java.util.Iterator;
 
 public class Utility 
 {
	public static String replaceCharAt(String str, int pos, char c) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(str.substring(0, pos));
		sb.append(c);
		sb.append(str.substring(pos + 1));
		return sb.toString();
	}
	
	public static String join(Collection<String> strings, String delimiter) 
	{
		StringBuilder sb = new StringBuilder();
		
		Iterator<String> iter = strings.iterator();
		while (iter.hasNext()) 
		{
			sb.append(iter.next());
			
			if (iter.hasNext())
			{
				sb.append(delimiter);
			}
		}
		
		return sb.toString();
	}
 }