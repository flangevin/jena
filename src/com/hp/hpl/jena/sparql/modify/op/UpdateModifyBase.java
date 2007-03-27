/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.modify.op;

import java.util.Collection;
import java.util.List;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import com.hp.hpl.jena.sparql.syntax.Template;

public abstract class UpdateModifyBase extends UpdatePattern
{
    protected Template deletes = null ; 
    protected Template inserts = null ;
    
    protected UpdateModifyBase() {}
    
    protected UpdateModifyBase(Template inserts, Template deletes)
    {
        this.inserts = inserts ;
        this.deletes = deletes ;
    }

    protected void setDeleteTemplateBase(Template template)
    { this.deletes = template ; }
    
    protected void setInsertTemplateBase(Template template)
    { this.inserts = template ; }

    /** Parse the string into a template - string must include the surrounding {} */
    protected void setDeleteTemplateBase(String template)
    { this.deletes = QueryFactory.createTemplate(template) ; }
    
    /** Parse the string into a template - string must include the surrounding {} */
    protected void setInsertTemplateBase(String template)
    { this.inserts = QueryFactory.createTemplate(template) ; }

    protected Template getDeleteTemplateBase()
    { return deletes ; }
    
    protected Template getInsertTemplateBase()
    { return inserts ; }

    //Override
    protected void exec(Graph graph, List bindings)
    {
        execDeletes(graph, bindings) ;
        execInserts(graph, bindings) ;
    }

    private void execDeletes(Graph graph, List bindings)
    {
        if ( deletes != null )
        {
            QueryIterator qIter = new QueryIterPlainWrapper(bindings.iterator()) ;
            Collection acc = subst(deletes, qIter) ;
            graph.getBulkUpdateHandler().delete(acc.iterator()) ;
        }
    }

    private void execInserts(Graph graph, List bindings)
    {
        if ( inserts != null )
        {
            QueryIterator qIter = new QueryIterPlainWrapper(bindings.iterator()) ;
            Collection acc = subst(inserts, qIter) ;
            graph.getBulkUpdateHandler().add(acc.iterator()) ;
        }
    }
}

/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
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