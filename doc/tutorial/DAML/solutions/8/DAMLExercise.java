/*
 * DAMLExercise.java
 *
 * (c) Copyright Hewlett-Packard Company 2002
 * All rights reserved.
 *
 * See end-of-file for license terms.
 */


import com.hp.hpl.jena.daml.*;
import com.hp.hpl.jena.daml.common.DAMLModelImpl;

import com.hp.hpl.mesa.rdf.jena.model.*;

import java.io.*;

/**
 *
 * @author  Jeremy Carroll
 */
public class DAMLExercise {


    /**
    * @param args the command line is ignored.
    */
    public static void main (String args[]) throws IOException, RDFException {
        DAMLModel model = new DAMLModelImpl();
        DAMLOntology onto = model.createDAMLOntology(null);
        String namespace = "http://www.w3.org/2001/vcard-rdf/3.0#";
        
        onto.prop_comment().addValue("This is an ontology for vCards.");
        onto.prop_comment().addValue("It's not a good one.");
        
        onto.prop_versionInfo().addValue("0.01");
        
        DAMLDatatype xsdString = model.createDAMLDatatype("http://www.w3.org/2000/10/XMLSchema#string");
        
        DAMLDatatypeProperty fn = model.createDAMLDatatypeProperty(namespace+"FN");
        
        fn.prop_range().add(xsdString);
        
        
        System.out.println("RDF/XML\n");
        model.write(new OutputStreamWriter(System.out),"RDF/XML-ABBREV");
        System.out.println("\nN-TRIPLE\n");
        model.write(new OutputStreamWriter(System.out),"N-TRIPLE");
 
    }

}


/* 
 * (c) Copyright Hewlett-Packard Company 2002
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
 *****************************************************************************/
