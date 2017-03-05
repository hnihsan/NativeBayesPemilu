
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
 
public class TaggedWord 
{
	private String word;
	private String tag;
	
	/*
	 *Constructor
	 */
	public TaggedWord(String word, String tag) 
	{
		this.word = word;
		this.tag = tag;
	}
	
	/*
	 * Method
	 */
	public String getWord()
	{
		return this.word;
	}
	
	public String getTag()
	{
		return this.tag;
	}
}