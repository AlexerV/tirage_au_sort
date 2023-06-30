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

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import java.util.List;
import java.io.IOException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.plugins.draw.business.Draw;
import fr.paris.lutece.plugins.draw.business.DrawHome;
import java.sql.Date;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.portal.web.l10n.LocaleService;
/**
 * This is the business class test for the object Draw
 */
public class DrawJspBeanTest extends LuteceTestCase
{
    private static final int IDUSER1 = 1;
    private static final int IDUSER2 = 2;
    private static final int NUMBER1 = 1;
    private static final int NUMBER2 = 2;
	private static final Date DATECREATION1 = new Date( 1000000l );
    private static final Date DATECREATION2 = new Date( 2000000l );

public void testJspBeans(  ) throws AccessDeniedException, IOException
	{	
     	MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockServletConfig config = new MockServletConfig();

		//display admin Draw management JSP
		DrawJspBean jspbean = new DrawJspBean();
		String html = jspbean.getManageDraws( request );
		assertNotNull(html);

		//display admin Draw creation JSP
		html = jspbean.getCreateDraw( request );
		assertNotNull(html);

		//action create Draw
		request = new MockHttpServletRequest();

		response = new MockHttpServletResponse( );
		AdminUser adminUser = new AdminUser( );
		adminUser.setAccessCode( "admin" );
		
        
        request.addParameter( "iduser" , String.valueOf( IDUSER1) );
        request.addParameter( "number" , String.valueOf( NUMBER1) );
        request.addParameter( "datecreation" , DateUtil.getDateString( DATECREATION1, LocaleService.getDefault( ) ) );
		request.addParameter("action","createDraw");
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createDraw" ));
		request.setMethod( "POST" );
        
		
		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 
			
			
			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}

		//display modify Draw JSP
		request = new MockHttpServletRequest();
        request.addParameter( "iduser" , String.valueOf( IDUSER1) );
        request.addParameter( "number" , String.valueOf( NUMBER1) );
        request.addParameter( "datecreation" , DateUtil.getDateString( DATECREATION1, LocaleService.getDefault( ) ) );
		List<Integer> listIds = DrawHome.getIdDrawsList();
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new DrawJspBean();
		
		assertNotNull( jspbean.getModifyDraw( request ) );	

		//action modify Draw
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");
		
        request.addParameter( "iduser" , String.valueOf( IDUSER2) );
        request.addParameter( "number" , String.valueOf( NUMBER2) );
        request.addParameter( "datecreation" , DateUtil.getDateString( DATECREATION2, LocaleService.getDefault( ) ) );
		request.setRequestURI("jsp/admin/plugins/example/ManageDraws.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createDraw, qui est l'action par défaut
		request.addParameter("action","modifyDraw");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyDraw" ));

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response );

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}
		
		//get remove Draw
		request = new MockHttpServletRequest();
        //request.setRequestURI("jsp/admin/plugins/example/ManageDraws.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new DrawJspBean();
		request.addParameter("action","confirmRemoveDraw");
		assertNotNull( jspbean.getModifyDraw( request ) );
				
		//do remove Draw
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("jsp/admin/plugins/example/ManageDrawts.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createDraw, qui est l'action par défaut
		request.addParameter("action","removeDraw");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeDraw" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.setMethod("POST");
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}	
     
     }
}
