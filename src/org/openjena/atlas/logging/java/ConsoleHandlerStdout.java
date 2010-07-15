/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package org.openjena.atlas.logging.java;

import java.util.logging.Formatter ;
import java.util.logging.Handler ;
import java.util.logging.LogManager ;
import java.util.logging.LogRecord ;


public class ConsoleHandlerStdout extends Handler 
{

    private void configure()
    {
        LogManager manager = LogManager.getLogManager();
        String cname = getClass().getName();
        
        String cls = manager.getProperty(cname+".formatter") ;
        Formatter fmt = null ;
        
        try {
            if (cls != null) {
                Class<?> clz = ClassLoader.getSystemClassLoader().loadClass(cls);
                fmt = (Formatter) clz.newInstance();
            }
        } catch (Exception ex) {
            // We got one of a variety of exceptions in creating the
            // class or creating an instance.
            // Drop through.
        }
        if ( fmt == null )
            fmt = new TextFormatter() ;
        setFormatter(fmt) ;
    }
    
    public ConsoleHandlerStdout()
    {
        configure() ;
    }
    
    @Override
    public void close() throws SecurityException
    {}

    @Override
    public void flush()
    { System.out.flush(); }

    @Override
    public void publish(LogRecord record)
    {
        if ( ! super.isLoggable(record) )
            return ;
        String s = getFormatter().format(record) ;
        System.out.print(s) ;
    }

}

/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
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