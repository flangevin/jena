package com.hp.hpl.jena.rdf.model.test;

/*
  (c) Copyright 2003, Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: TestReifiedStatements.java,v 1.9 2003-09-08 10:25:10 chris-dollin Exp $
*/

import com.hp.hpl.jena.rdf.model.*;
import junit.framework.*;

/**
    test the properties required of ReifiedStatement objects.
    @author kers 
*/
public class TestReifiedStatements extends ModelTestBase
    {
    public TestReifiedStatements( String name )
        { super( name ); }
    
    public static TestSuite suite()
        { TestSuite result = new TestSuite();
        result.addTest( new TestSuite( TestStandard.class ) );
        result.addTest( new TestSuite( TestConvenient.class ) );
        // result.addTest( new TestSuite( TestMinimal.class ) );
        return result; }   
        
    public Model getModel()
        { return ModelFactory.createDefaultModel(); }
        
    public static class TestStandard extends AbstractTestReifiedStatements
        {
        public TestStandard( String name ) { super( name ); }
        public Model getModel() { return ModelFactory.createDefaultModel( ModelFactory.Standard ); } 
        }
        
    public static class TestConvenient extends AbstractTestReifiedStatements
        {
        public TestConvenient( String name ) { super( name ); }
        public Model getModel() { return ModelFactory.createDefaultModel( ModelFactory.Convenient ); } 
        }
        
    public static class TestMinimal extends AbstractTestReifiedStatements
        {
        public TestMinimal( String name ) { super( name ); }
        public Model getModel() { return ModelFactory.createDefaultModel( ModelFactory.Minimal ); } 
        }
    }
    
/*
    (c) Copyright 2003 Hewlett-Packard Development Company, LP
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