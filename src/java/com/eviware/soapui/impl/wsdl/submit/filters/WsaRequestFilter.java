/*
 *  soapUI, copyright (C) 2004-2008 eviware.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl.wsdl.submit.filters;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.MustUnderstandTypeConfig;
import com.eviware.soapui.config.WsaVersionTypeConfig;
import com.eviware.soapui.impl.support.AbstractHttpRequest;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.submit.transports.http.BaseHttpRequestTransport;
import com.eviware.soapui.impl.wsdl.submit.transports.http.ExtendedHttpMethod;
import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.SubmitContext;
import com.eviware.soapui.support.editor.inspectors.wsa.WsaUtils;

/**
 * RequestFilter that expands properties in request content
 * 
 * @author Ole.Matzura
 */

public class WsaRequestFilter extends AbstractRequestFilter
{
	public final static Logger log = Logger.getLogger(WsaRequestFilter.class);
	
	public void filterAbstractHttpRequest(SubmitContext context, AbstractHttpRequest<?> wsdlRequest)
	{
		if( !(wsdlRequest instanceof WsdlRequest) || !((WsdlRequest)wsdlRequest).isWsAddressing())
			return;
		
		String content = (String) context.getProperty( BaseHttpRequestTransport.REQUEST_CONTENT );
		if( content == null )
		{
			log.warn( "Missing request content in context, skipping ws-addressing" );
		}
		else
		{
			ExtendedHttpMethod httpMethod = (ExtendedHttpMethod) context.getProperty( BaseHttpRequestTransport.HTTP_METHOD );
			Operation operation = ((WsdlRequest)wsdlRequest).getOperation();
			SoapVersion soapVersion = ((WsdlRequest)wsdlRequest).getOperation().getInterface().getSoapVersion();
			content = new WsaUtils(soapVersion, operation).addWSAddressing(content, (WsdlRequest) wsdlRequest, httpMethod);
			if( content != null )
				context.setProperty( BaseHttpRequestTransport.REQUEST_CONTENT, content );
		}
	}
	
}


