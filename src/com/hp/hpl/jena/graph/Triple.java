/*
  (c) Copyright 2002, 2003, Hewlett-Packard Company, all rights reserved.
  [See end of file]
  $Id: Triple.java,v 1.4 2003-06-10 08:08:00 chris-dollin Exp $
*/

package com.hp.hpl.jena.graph;

import java.util.*;

/**
    Triples are the basis for RDF statements; they have a subject, predicate, and
    object field (all nodes) and express the notion that the relationship named
    by the predicate holds between the subject and the object.
    
    @author Jeremy Carroll
 */
final public class Triple {
	private final Node subj, pred, obj;
	public Triple(Node s, Node p, Node o) {
//        if (s == null) throw new UnsupportedOperationException( "subject cannot be null" );
//        if (p == null) throw new UnsupportedOperationException( "predicate cannot be null" );
//        if (o == null) throw new UnsupportedOperationException( "object cannot be null" );
		subj = s;
		pred = p;
		obj = o;
	}
	
    /**
        return a human-readable string "subject @predicate object" describing the triple
    */
	public String toString() {
		return subj + " @" + pred + " " + obj;
	}
    
    /**
        @return the subject of the triple
    */
	public Node getSubject() {
		return subj;
	}
    
    /**
        @return the predicate of the triple
    */
	public Node getPredicate() {
		return pred;
	}
    
    /**
        @return the object of the triple
    */
	public Node getObject() {
		return obj;
	}

    /** 
        triples only equal other triples with the same components. 
    <p>
        internals: avoids grubbing around in the insides of the other triple.  
    */
    
	public boolean equals(Object o) 
        { return o instanceof Triple && ((Triple) o).sameAs( subj, pred, obj ); }
    
    /** 
        component-wise equality, might choose to make public.
    */    
    private boolean sameAs( Node s, Node p, Node o )
        { return subj.equals( s ) && pred.equals( p ) && obj.equals( o ); }
        
    public boolean matches( Triple other )
        { return other.matchedBy( subj, pred, obj  ); }
        
    private boolean matchedBy( Node s, Node p, Node o )
        { return s.matches( subj ) && p.matches( pred ) && o.matches( obj ); }
        
    /**
        The hash-code of a triple is the hash-codes of its components munged
        together. The current hash-code is an exclusive-or of the slightly-shifted
        component hashcodes; this means (almost) all of the bits count, and
        the order matters, so (S @P O) has a different hash from (O @P S), etc.
    */
    public int hashCode() {
    	return (subj.hashCode() >> 1) ^ pred.hashCode() ^ (obj.hashCode() << 1);
    }
        
    /**
        Factory method for creating triples, allows caching opportunities.
        
        @return a triple with subject=s, predicate=p, object=o
    */
    public static Triple create( Node s, Node p, Node o )
        { return new Triple( s, p, o ); }
        
    /**
        Utility factory method for creating a triple based on the content of an
        "S P O" string. The S, P, O are processed by Node.create, see which for
        details of the supported syntax. This method exists to support test code.
    */
    public static Triple create( String fact )
        {
        StringTokenizer st = new StringTokenizer( fact );
        Node sub = Node.create( st.nextToken() );
        Node pred = Node.create( st.nextToken() );
        Node obj = Node.create( st.nextToken() );
        return new Triple( sub, pred, obj );
        }
            
}

/*
    (c) Copyright Hewlett-Packard Company 2002, 2003
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
