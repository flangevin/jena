/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * [See end of file]
 */

package com.hp.hpl.jena.iri.test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hp.hpl.jena.iri.IRI;
import com.hp.hpl.jena.iri.Violation;
import com.hp.hpl.jena.iri.ViolationCodes;
import com.hp.hpl.jena.iri.impl.AbsIRIImpl;
import com.hp.hpl.jena.iri.impl.PatternCompiler;

public class TestMoreExamples extends TestCase implements
        ViolationCodes {
    static class TestReader extends DefaultHandler {
        private Stack<Test> stack = new Stack<Test>();

        TestReader(TestSuite s) {
            stack.push(s);
        }
        private void push(Test t) {
            ((TestSuite)stack.peek()).addTest(t);
            stack.push(t);
        }

        @Override
        public void startElement(String arg1, String arg2, String name,
                Attributes att) {
            if (name.equals("IRI"))
                push(new TestMEIri(att));
            else if (name.equals("Result"))
                push(new TestMEResult(att,(TestSuite)stack.peek()));
            else if (name.equals("Relativize"))
                push(new TestMERelativize(att,(TestSuite)stack.peek()));
            else if (name.equals("Resolve"))
                push(new TestSuite());
            else if (!name.equals("UriTests"))
                add(name, att);
        }

        private void add(String name, Attributes att) {
            ((TestMoreExamples) stack.peek()).add(name, att);
        }

        @Override
        public void characters(char ch[], int st, int lg) {
            String text = new String(ch,st,lg).trim();
            if (text.length()>0)
                ((TestMoreExamples) stack.peek()).add(text);
        }
        @Override
        public void endElement(String arg1, String arg2, String name) {
            if (name.equals("Resolve")) {
                TestSuite t = (TestSuite) stack.pop();
                t.
                setName(((TestCase)t.testAt(0)).getName() + "  " +
                        ((TestCase)t.testAt(1)).getName());
            } else if (name.equals("IRI") || name.equals("Result")
                    || name.equals("Relativize")) {
                stack.pop();
            }

        }

    }

    static Map<String, String> attr2map(Attributes a) {
        Map<String, String> rslt = new HashMap<String, String>();
        for (int i = a.getLength()-1;i>=0;i--)
            rslt.put(a.getQName(i),a.getValue(i));
        return rslt;
    }
    Map<String, String> att;
    TestSuite parent;
    private Map<String, Map<String, String>> methods = new HashMap<String, Map<String, String>>();
    private long violations = 0l;
    private IRI iri;

    public TestMoreExamples(String nm, Attributes att) {
        this(nm,att,null);
    }

    private String savedText = null;
    public void add(String text) {
        if (savedText!=null) {
            text = savedText + text;
            savedText = null;
//            System.err.println(text);
        }
        try {
        violations |= (1l << PatternCompiler.errorCode(text));
        }
        catch (NoSuchFieldException e){
                savedText = text;
        }
    }

    public TestMoreExamples(String nm, Attributes att, TestSuite suite) {
        super(escape(nm));
        this.att = attr2map(att);
        this.parent = suite;
    }

    private static String escape(String nm) {
        StringBuffer rslt = new StringBuffer();
        for (int i=0; i<nm.length();i++) {
            char ch = nm.charAt(i);
            if (ch>=32 && ch<=126)
                rslt.append(ch);
            else
                rslt.append("\\u"+pad4(Integer.toHexString(ch)));
                
        }
        return rslt.toString();
    }

    private static String pad4(String string) {
        switch (string.length()) {
        case 0:
            return "0000";
        case 1:
            return "000"+string;
        case 2:
            return "00"+string;
        case 3:
            return "0"+string;
            default:
                return string;
       
        }
    }

    public TestMoreExamples(String string) {
        super(escape(string));
    }
    
//    static int cnt = 0;
    
    @Override
    public void setUp() throws Exception {
//        System.err.println("setUp"+cnt);
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
//        System.err.println("tearDown"+cnt++);
        super.tearDown();
    }
    private void add(String name, Attributes attrs) {
        if (name.equals("violation"))
            return;
        if (name.equals("violations"))
            return;
        methods.put(name,attr2map(attrs));
    }

    private long getViolations() {
    	long result = 0l;
    	Iterator<Violation> it = ((AbsIRIImpl)iri).allViolations();
        while (it.hasNext()) {
           result |= (1l<<(it.next()).getViolationCode());
                  
        }
        return result;
    }
    @Override
    public void runTest() {
//        System.err.println("runTest"+cnt + " " + getName());
       iri = getIRI();
       
       
       assertEquals("violations",violations,getViolations());
       
       Iterator<Map.Entry<String, Map<String,String>>> it = methods.entrySet().iterator();
       while (it.hasNext()) {
           Map.Entry<String, Map<String,String>> ent = it.next();
           String m = ent.getKey();
           Map<String,String> attrs = ent.getValue();
           try {
               Object r = IRI.class.getDeclaredMethod(m,TestCreator.nullSign)
                .invoke(iri,new Object[]{});
               if (r==null)
                   assertEquals(attrs.get("nullValue"),"true");
               else
                   assertEquals(attrs.get("value"),r.toString());
               
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Throwable t = e;
                if (t.getCause()!=null)
                    t= t.getCause();
                String s = t.getMessage()!=null?t.getMessage():t.toString();
                
                assertEquals(attrs.get("exception"),s);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
       }
    }

    final IRI getIRI() { if (iri==null) iri = computeIRI(); return iri; }

    IRI computeIRI() {
        throw new UnsupportedOperationException();
    }

    static TestSuite suitex() throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory fact = SAXParserFactory.newInstance();
        TestSuite result = new TestSuite();
        result.setName("More IRI Tests");
        InputStream in = TestCreator.class.getClassLoader().getResourceAsStream("com/hp/hpl/jena/iri/test/test.xml");
            fact.newSAXParser().parse(in,
            new TestReader(result)
            );

       in.close();
       return result;
    }
    public static TestSuite suite() {
        try {
            return 
             suitex();
            
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
//        System.err.println("Yes chris we know");
//        return 
        TestSuite r2 = new TestSuite("exception-while-building-testsuite");
//        r2.addTest(new TestMoreExamples("testDummy"));
        return r2;
    }
}

/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP All rights
 * reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. The name of the author may not
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
