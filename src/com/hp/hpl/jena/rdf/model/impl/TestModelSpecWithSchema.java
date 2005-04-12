/*
    (c) Copyright 2005, Hewlett-Packard Development Company, LP
    All rights reserved.
    [See end of file]
*/
package com.hp.hpl.jena.rdf.model.impl;

import com.hp.hpl.jena.rdf.model.test.ModelTestBase;

import java.util.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.ModelSpecFactory;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.*;


/**
 @author hedgehog
 */
public class TestModelSpecWithSchema extends ModelTestBase
    {
    private static final Filter inJMS = new Filter() {

        public boolean accept( Object o )
            {
            Statement s = (Statement) o;
            return s.getSubject().getNameSpace().equals( JenaModelSpec.baseURI ) && s.getResource().getNameSpace().equals( JenaModelSpec.baseURI );
            }};

    public TestModelSpecWithSchema( String name )
        { super( name ); }

    public void testReificationMode()
        { testDomain( "jms:reificationMode rdfs:domain jms:MakerSpec" ); }

    public void testMaker()
        { testDomain( "jms:maker rdfs:domain jms:PlainModelSpec" ); }

    public void testImportMaker()
        { testDomain( "jms:importMaker rdfs:domain jms:OntModelSpec" ); }

    public void testOntLanguage()
        { testDomain( "jms:ontLanguage rdfs:domain jms:OntModelSpec" ); }

    public void testReasonsWith()
        { testDomain( "jms:reasonsWith rdfs:domain jms:InfModelSpec" ); }

    public void testModelName()
        { testDomain( "jms:modelName rdfs:domain jms:ModelSpec" ); }

    public void testFileBase()
        { testDomain( "jms:fileBase rdfs:domain jms:FileMakerSpec" ); }
    
    public void  testSubclasses()
        {
        Model m = ModelFactory.createRDFSModel( JenaModelSpec.getSchema() );
        Model m2 = ModelSpecFactory.withSpecSchema( JenaModelSpec.getSchema() );
        Set wanted = iteratorToSet( m.listStatements( null, RDFS.subClassOf, (RDFNode) null ).filterKeep( inJMS ) );
        Set got = iteratorToSet( m2.listStatements( null, RDFS.subClassOf, (RDFNode) null ) );
        if (!wanted.equals( got ))
            {
            Set extra = new HashSet( got ); extra.removeAll( wanted );
            Set missing = new HashSet( wanted ); missing.remove( got );
            System.err.println( ">> " + extra );
            System.err.println( ">> " + missing );
            fail( "not equal" );
            }
        }

    protected void testDomain( String triple )
        { Statement s = statement( triple );
        Property P = (Property) s.getSubject().as( Property.class );
        Resource C = s.getResource();
        Resource X = ResourceFactory.createResource(), Y = ResourceFactory.createResource();
        Model m = ModelFactory.createDefaultModel().add( X, P, Y );
        Model wanted = ModelFactory.createDefaultModel().add( m ).add( X, RDF.type, C );
        assertTrue( ModelSpecFactory.withSpecSchema( m ).contains( X, RDF.type, C ) );
        }
    
    protected void testOK( String wanted, String toTest )
        { assertIsoModels( m( wanted ), ModelSpecFactory.withSpecSchema( m( toTest ) ) ); }

    public Model m( String s )
        { return modelWithStatements( s ); }
    }

/*
 * (c) Copyright 2005 Hewlett-Packard Development Company, LP
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