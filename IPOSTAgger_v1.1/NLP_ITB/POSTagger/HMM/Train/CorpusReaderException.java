
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
 
public class CorpusReaderException extends Exception 
{
	private CorpusReadError d_error;
	
	public static enum CorpusReadError { MISSING_TAG, ZERO_LENGTH_WORD }
	
	public CorpusReaderException(String msg, CorpusReadError error) 
	{
		super(msg);
		d_error = error;
	}
	
	public CorpusReadError error() 
	{
		return d_error;
	}
}
