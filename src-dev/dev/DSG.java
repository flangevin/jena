/*
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 * [See end of file]
 */

package dev;

import java.util.Iterator ;

import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.sparql.core.Quad ;

public interface DSG
{
    // Writing:
    // DSGWriter.write(System.out, DSG)
    
    // Events:
    //  1/ Wrapper that knows to call the event system.
    //     DSGFcatory.addEventHandler -> DSG
    //  2/ Get event handler (and one that links std event system) 
    //  3/ ??
    
    // Extends DatasetGraph (name?)
    public void add(Quad quad) ;
    public void delete(Quad quad) ;
    public Iterator<Quad> find(Quad quad) ;    // ??
    public Iterator<Quad> find(Node g, Node s, Node p , Node o) ;
    public boolean isEmpty() ;
    
    // Graph getters
    
    public boolean contains(Node g, Node s, Node p , Node o) ; // Quad?
    
    boolean isIsomorphicWith(DSG g); //??
    
    // ---
    /*
    public Graph getDefaultGraph() ;

    public Graph getGraph(Node graphNode) ;

    public boolean containsGraph(Node graphNode) ;

    public Iterator<Node> listGraphNodes() ;

    public Lock getLock() ;
    
    /** Get the context associated with this object - may be null * /
    public Context getContext() ; 
    
    /** Get the size (number of graphs) - may be -1 for unknown * / 
    public int size() ;
    
    public void close() ;     
    */
    
    // Sync
    // Close
    
    
    
}

/*
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */