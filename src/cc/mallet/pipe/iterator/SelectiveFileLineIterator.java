/* Copyright (C) 2010 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */

package cc.mallet.pipe.iterator;

import java.io.*;
import java.util.Iterator;
import java.net.URI;

import cc.mallet.types.*;

/**
 * Very similar to the SimpleFileLineIterator, 
 * but skips lines that match a regular expression.
 * 
 * @author Gregory Druck
 */
public class SelectiveFileLineIterator implements Iterator<Instance> {

	BufferedReader reader = null;
	int index = -1;
	String currentLine = null;
	boolean hasNextUsed = false;
	String skipRegex;
	boolean isName = false;
	
	public SelectiveFileLineIterator (Reader reader, String skipRegex) {
		this.reader = new BufferedReader (reader);
		this.index = 0;
		this.skipRegex = skipRegex;
	}

	
		// a special change in order to present the terms in the result of the svm2classify. 
	// Without the change, the terms are ignored and replaced by "Array i". 
	// We added a new constructor "SelectiveFileLineIterator" that gets a bool value. 
	// When this value is checked to be true, the terms are stored and printed instead of the uri ("Array i").
	public SelectiveFileLineIterator (Reader reader, String skipRegex, boolean includeName) {
		this.reader = new BufferedReader (reader);
		this.index = 0;
		this.skipRegex = skipRegex;
		this.isName = true;
	}
	
	public Instance next () {
		if (!hasNextUsed) {
			try {
				currentLine = reader.readLine();
				while (currentLine != null && currentLine.matches(skipRegex)) {
					currentLine = reader.readLine();
				}
			}
			catch (IOException e) {
				throw new RuntimeException (e);
			}
		}
		else {
			hasNextUsed = false;
		}
		
		String [] tokens = null;
		String name = null;
		
		// in this case assign the term to the first element instead of uri
		if(isName)
		{
			tokens = currentLine.split("\\s+");
			name = tokens[0];
		}

		URI uri = null;
		try { uri = new URI ("array:" + index++); }
		catch (Exception e) { throw new RuntimeException (e); }
		
		// if need to print terms, replace uri with the name
		if(isName == false)
		{
			return new Instance (currentLine, null, uri, null);
		}
		else
		{
			return new Instance (currentLine, null, name, null);
		}
	}

	public boolean hasNext ()	{	
		hasNextUsed = true; 
		try {
			currentLine = reader.readLine();
			while (currentLine != null && currentLine.matches(skipRegex)) {
				currentLine = reader.readLine();
			} 
		}
		catch (IOException e) {
			throw new RuntimeException (e);
		}
		return (currentLine != null);	
	}
	
	public void remove () {
		throw new IllegalStateException ("This Iterator<Instance> does not support remove().");
	}
}
