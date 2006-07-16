/*
 * (c) Copyright 2006 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sdb.engine;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.engine1.PlanElement;
import com.hp.hpl.jena.query.engine1.plan.*;
import com.hp.hpl.jena.query.expr.Expr;
import com.hp.hpl.jena.query.util.CollectionUtils;
import com.hp.hpl.jena.query.util.Context;
import com.hp.hpl.jena.sdb.core.compiler.BlockBGP;
import com.hp.hpl.jena.sdb.core.compiler.BlockOptional;
import com.hp.hpl.jena.sdb.store.ConditionCompiler;
import com.hp.hpl.jena.sdb.store.Store;


public class PlanToSDB extends TransformCopy
{
    private static Log log = LogFactory.getLog(PlanToSDB.class) ;
    
    private Query query ;
    private Store store ;
    private Context context ;
    private boolean translateOptionals ;
    private boolean translateConstraints ;
    
    PlanToSDB(Context context, Query query, Store store, boolean translateOptionals, boolean translateConstraints)
    {
        super(TransformCopy.COPY_ONLY_ON_CHANGE) ;
        this.query = query ;
        this.store = store ;
        this.context = context ;
        this.translateOptionals = translateOptionals ;
        this.translateConstraints = translateConstraints ;
    }
    
    @Override
    public PlanElement transform(PlanBlockTriples planElt)
    { 
        @SuppressWarnings("unchecked")
        List<Triple> triples = (List<Triple>)planElt.getPattern() ;
        BlockBGP b = new BlockBGP() ;
        for ( Triple t : triples )  
            b.add(t) ;
        PlanSDB x = new PlanSDB(context, query, store, b) ;
        return x ;
    }
    
   
//    @Override
//    public PlanElement transform(PlanFilter planElt)
//    {
//        PlanSDBConstraint sdb = transformFilter(planElt) ;
//        if ( sdb == null )
//            return super.transform(planElt) ;
//        return sdb ;
//    }
    
    
    @Override
    public PlanElement transform(PlanBasicGraphPattern planElt, List newElts)
    { 
        @SuppressWarnings("unchecked")
        List<PlanElement> newElements = (List<PlanElement>)newElts ;
        
        PlanSDB lastSDB = null ;
        
        for ( int i = 0 ; i < newElements.size() ; i++ )
        {
            PlanElement e = newElements.get(i) ;
            
            if ( e instanceof PlanSDB )
            {
                lastSDB = (PlanSDB)e ;
                continue ;
            }

            if ( e instanceof PlanFilter )
            {
                PlanFilter filter = (PlanFilter)e ;
                
                // If filters have not been transformed earlier.
                // Better here so can test for whether the filter is appropriate for the BGP.

                SDBConstraint c = transformFilter(filter) ;
                if ( c == null )
                    // No good.
                    continue ;
                
                // Check for complete and partial filters.
                if ( lastSDB != null && lastSDB.getBlock() instanceof BlockBGP )
                {
                    BlockBGP b = (BlockBGP)lastSDB.getBlock() ;
                    b.add(c) ;
                    if ( c.isComplete() )
                        filter = null ;
                }

                // Put back in the remained external filter (may be null). 
                newElements.set(i, filter) ;
                continue ;
            }
                
            // Or do PlanFilters in two steps.
//            if ( e instanceof PlanSDBConstraint )
//            {
//                PlanSDBConstraint c = (PlanSDBConstraint)e ;
//                PlanFilter filter = c.getOriginal() ;
//                ...            
            // Nothing done - end of SDB block (if there was one).
            lastSDB = null ;
        }

        // Nulls mean no element anymore (e.g. FILTER that has been absorbed into the SDB part)  
        CollectionUtils.removeNulls(newElements) ;
        
        if ( newElements.size() != 1 )
            // Still more than one top level step.
            // return a new PlanBasicGraphPattern with the new elements
            return planElt.copy(newElements) ; 
        
        if ( newElements.get(0) instanceof PlanSDB  )
            // Good to remove
            return (PlanSDB)newElements.get(0) ;
        
        // No good
        return super.transform(planElt, newElements) ;
    }
    
    @Override
    public PlanElement transform(PlanOptional planElt, PlanElement fixed, PlanElement optional)
    {
        if ( ! translateOptionals )
            return super.transform(planElt, fixed, optional) ;
        
        if ( fixed instanceof PlanSDB && optional instanceof PlanSDB )
        {
            PlanSDB fixedSDB = (PlanSDB)fixed ;
            PlanSDB optionalSDB = (PlanSDB)optional ;
            BlockOptional b = new BlockOptional(fixedSDB.getBlock(), optionalSDB.getBlock()) ;
            // Converted both sides - can convert this node.
            PlanSDB planSDB = new PlanSDB(context, query, store, b) ;
            return planSDB ;
        }
        // We're not interested - do whatever the default is.
        return super.transform(planElt, fixed, optional) ;
    }
    

    private SDBConstraint transformFilter(PlanFilter planElt)
    {
        if ( ! translateConstraints )
            return null ;
        
        Expr expr = planElt.getConstraint().getExpr() ; 
        // TODO Make this a feature of the store.
        ConditionCompiler cc = store.getQueryCompiler().getConditionCompiler() ;
        SDBConstraint psc = cc.recognize(planElt) ;
        // Maybe null (not recognized)
        return psc ;
    }
 
    
}

/*
 * (c) Copyright 2006 Hewlett-Packard Development Company, LP
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