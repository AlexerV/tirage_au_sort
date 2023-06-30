/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.draw.web;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.LuteceTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import java.io.IOException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import java.util.List;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import java.sql.Date;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.plugins.draw.business.Draw;
import fr.paris.lutece.plugins.draw.business.DrawHome;
/**
 * This is the business class test for the object Draw
 */
public class DrawXPageTest extends LuteceTestCase
{
    private static final int IDUSER1 = 1;
    private static final int IDUSER2 = 2;
    private static final int NUMBER1 = 1;
    private static final int NUMBER2 = 2;
	private static final Date DATECREATION1 = new Date( 1000000l );
    private static final Date DATECREATION2 = new Date( 2000000l );

public void testXPage(  ) throws AccessDeniedException, IOException
	{
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
		MockHttpServletResponse response = new MockHttpServletResponse( );
		MockServletConfig config = new MockServletConfig( );

		DrawXPage xpage = new DrawXPage( );
		assertNotNull( xpage.getCreateDraw( request ) );
		
		request = new MockHttpServletRequest();
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createDraw" ));
		
		LocalVariables.setLocal(config, request, response);
		
		request.addParameter( "action" , "createDraw" );
        request.addParameter( "iduser" , String.valueOf( IDUSER1) );
        request.addParameter( "number" , String.valueOf( NUMBER1) );
        request.addParameter( "datecreation" , DateUtil.getDateString( DATECREATION1, LocaleService.getDefault( ) ) );
		request.setMethod( "POST" );
		
		assertNotNull( xpage.doCreateDraw( request ) );
		
		
		//modify Draw	
		List<Integer> listIds = DrawHome.getIdDrawsList(); 

		assertTrue( !listIds.isEmpty( ) );

		request = new MockHttpServletRequest();
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

		assertNotNull( xpage.getModifyDraw( request ) );

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		
        request.addParameter( "iduser" , String.valueOf( IDUSER2) );
        request.addParameter( "number" , String.valueOf( NUMBER2) );
        request.addParameter( "datecreation" , DateUtil.getDateString( DATECREATION2, LocaleService.getDefault( ) ) );

		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyDraw" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		
		assertNotNull( xpage.doModifyDraw( request ) );

		//do confirm remove Draw
		request = new MockHttpServletRequest();
		request.addParameter( "action" , "confirmRemoveDraw" );
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemoveDraw" ));;
		request.setMethod("GET");

		try
		{
			xpage.getConfirmRemoveDraw( request );
		}
		catch(Exception e)
		{
			assertTrue(e instanceof SiteMessageException);
		}

		//do remove Draw
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		request.addParameter( "action" , "removeDraw" );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeDraw" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doRemoveDraw( request ) );

    }
    
}
