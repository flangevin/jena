/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       ian.dickinson@hp.com
 * Package            @package@
 * Web site           @website@
 * Created            17-Nov-2003
 * Filename           $RCSfile: DIGProfile.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2003-12-01 22:40:07 $
 *               by   $Author: ian_dickinson $
 *
 * @copyright@
 *****************************************************************************/

// Package
///////////////
package com.hp.hpl.jena.reasoner.dig;


// Imports
///////////////

/**
 * <p>
 * Encapsulates the multiple extant versions of the DIG protocol, which have 
 * different expectations as to namespaces, XML encodings, and other variables. This
 * allows us to parameterise the DIG interface to different DIG enabled tools.
 * </p>
 *
 * @author Ian Dickinson, HP Labs (<a href="mailto:Ian.Dickinson@hp.com">email</a>)
 * @version Release @release@ ($Id: DIGProfile.java,v 1.2 2003-12-01 22:40:07 ian_dickinson Exp $)
 */
public interface DIGProfile 
{
    // Constants
    //////////////////////////////////

    /* DIG verbs and other constants */
    
    /** The DIG verb to get the identification information on the reasoner */
    public static final String GET_IDENTIFIER = "getIdentifier";

    /** The DIG attribute denoting the version string of a reasoner */
    public static final String VERSION = "version";
    
    /** The DIG attribute denoting the version message string of a reasoner */
    public static final String MESSAGE = "message";
    
    /** The DIG element denoting the capabilities of the reasoner */
    public static final String SUPPORTS = "supports";
    
    /** The DIG element denoting the language capabilities of the reasoner */
    public static final String LANGUAGE = "language";
    
    /** The DIG element denoting the ask capabilities of the reasoner */
    public static final String ASK = "ask";
    
    /** The DIG element denoting the tell capabilities of the reasoner */
    public static final String TELL = "tell";
    
    // verbs
    public static final String TELLS            = "tells";
    public static final String ASKS             = "asks";
    public static final String NEWKB            = "newKB";
    public static final String RELEASEKB        = "releaseKB";
    public static final String RESPONSE         = "response";
    
    // responses
    public static final String OK               = "ok";
    public static final String KB               = "kb";
    public static final String ERROR            = "error";
    public static final String CONCEPT_SET      = "conceptSet";
    public static final String SYNONYMS         = "synonyms";
    public static final String ROLE_SET         = "roleSet";
    public static final String INDIVIDUAL_SET   = "individualSet";
    public static final String INDIVIDUAL_PAIR_SET = "individualPairSet";
    public static final String INDIVIDUAL_PAIR  = "individualPair";
    public static final String TRUE             = "true";
    public static final String FALSE            = "false";
    public static final String WARNING          = "warning";
    
    // queries
    public static final String ALL_CONCEPT_NAMES = "allConceptNames";
    public static final String ALL_ROLE_NAMES    = "allRoleNames";
    public static final String ALL_INDIVIDUALS   = "allIndividuals";
    public static final String SATISFIABLE       = "satisfiable";
    public static final String SUBSUMES          = "subsumes";
    public static final String PARENTS           = "parents";
    public static final String CHILDREN          = "children";
    public static final String ANCESTORS         = "ancestors";
    public static final String DESCENDANTS       = "descendants";
    public static final String EQUIVALENTS       = "equivalents";
    public static final String RPARENTS          = "rparents";
    public static final String RCHILDREN         = "rchildren";
    public static final String RANCESTORS        = "rancestors";
    public static final String RDESCENDANTS      = "rdescendants";
    public static final String INSTANCES         = "instances";
    public static final String TYPES             = "types";
    public static final String INSTANCE          = "instances";
    public static final String ROLE_FILLERS      = "roleFillers";
    public static final String RELATED_INDIVIDUALS = "relatedIndividuals";
    public static final String TOLD_VALUES       = "toldValues";
    
    // tell language
    
    public static final String DEFCONCEPT       = "defconcept";
    public static final String DEFROLE          = "defrole";
    public static final String DEFFEATURE       = "deffeature";
    public static final String DEFATTRIBUTE     = "defattribute";
    public static final String DEFINDIVIDUAL    = "defindividual";
    public static final String IMPLIESC         = "impliesc";
    public static final String EQUALC           = "equalc";
    public static final String DISJOINT         = "disjoint";
    public static final String IMPLIESR         = "impliesr";
    public static final String EQUALR           = "equalr";
    public static final String DOMAIN           = "domain";
    public static final String RANGE            = "range";
    public static final String RANGEINT         = "rangeint";
    public static final String RANGESTRING      = "rangestring";
    public static final String TRANSITIVE       = "transitive";
    public static final String FUNCTIONAL       = "functional";
    public static final String INSTANCEOF       = "instanceof";
    public static final String RELATED          = "related";
    public static final String VALUE            = "value";
   
    // concept language
    
    public static final String TOP              = "top";
    public static final String BOTTOM           = "bottom";
    public static final String CATOM            = "catom";
    public static final String AND              = "and";
    public static final String OR               = "or";
    public static final String NOT              = "not";
    public static final String SOME             = "some";
    public static final String ALL              = "all";
    public static final String ATMOST           = "atmost";
    public static final String ATLEAST          = "atleast";
    public static final String ISET             = "iset";
    public static final String DEFINED          = "defined";
    public static final String STRINGMIN        = "stringmin";
    public static final String STRINGMAX        = "stringmax";
    public static final String STRINGEQUALS     = "stringequals";
    public static final String STRINGRANGE      = "stringrange";
    public static final String INTMIN           = "intmin";
    public static final String INTMAX           = "intmax";
    public static final String INTEQUALS        = "intequals";
    public static final String INTRANGE         = "intrange";
    public static final String RATOM            = "ratom";
    public static final String FEATURE          = "feature";
    public static final String INVERSE          = "inverse";
    public static final String ATTRIBUTE        = "attribute";
    public static final String CHAIN            = "chain";
    public static final String INDIVIDUAL       = "individual";
    public static final String NUM              = "num";
    public static final String IVAL             = "ival";
    public static final String SVAL             = "sval";
    
    
    // attributes
    
    public static final String NAME             = "name";
    public static final String VAL              = "val";
    public static final String MIN              = "min";
    public static final String MAX              = "max";
    public static final String URI              = "uri";
    public static final String ID               = "id";
    public static final String CODE             = "code";

    
    // External signature methods
    //////////////////////////////////

    /** Answer the root namespace for this version of the DIG protocol */
    public String getDIGNamespace();
    
    /** Answer the location of the DIG schema for this version of the DIG protocol */
    public String getSchemaLocation();
    
    /** Answer the HTTP Content-Type of a DIG request (e.g. text/xml) */
    public String getContentType();
}


/*
@footer@
*/
