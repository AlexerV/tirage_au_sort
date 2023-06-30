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
import fr.paris.lutece.plugins.draw.business.User;
import fr.paris.lutece.plugins.draw.business.UserHome;
/**
 * This is the business class test for the object User
 */
public class UserJspBeanTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String LASTNAME1 = "Lastname1";
    private static final String LASTNAME2 = "Lastname2";
	private static final String EMAIL1 = "email1@test.com";
    private static final String EMAIL2 = "email2@test.com";
    private static final int PHONE1 = 1;
    private static final int PHONE2 = 2;
    private static final String ADRESS1 = "Adress1";
    private static final String ADRESS2 = "Adress2";

public void testJspBeans(  ) throws AccessDeniedException, IOException
	{	
     	MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockServletConfig config = new MockServletConfig();

		//display admin User management JSP
		UserJspBean jspbean = new UserJspBean();
		String html = jspbean.getManageUsers( request );
		assertNotNull(html);

		//display admin User creation JSP
		html = jspbean.getCreateUser( request );
		assertNotNull(html);

		//action create User
		request = new MockHttpServletRequest();

		response = new MockHttpServletResponse( );
		AdminUser adminUser = new AdminUser( );
		adminUser.setAccessCode( "admin" );
		
        
        request.addParameter( "name" , NAME1 );
        request.addParameter( "lastname" , LASTNAME1 );
        request.addParameter( "email" , EMAIL1 );
        request.addParameter( "phone" , String.valueOf( PHONE1) );
        request.addParameter( "adress" , ADRESS1 );
		request.addParameter("action","createUser");
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createUser" ));
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

		//display modify User JSP
		request = new MockHttpServletRequest();
        request.addParameter( "name" , NAME1 );
        request.addParameter( "lastname" , LASTNAME1 );
        request.addParameter( "email" , EMAIL1 );
        request.addParameter( "phone" , String.valueOf( PHONE1) );
        request.addParameter( "adress" , ADRESS1 );
		List<Integer> listIds = UserHome.getIdUsersList();
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new UserJspBean();
		
		assertNotNull( jspbean.getModifyUser( request ) );	

		//action modify User
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");
		
        request.addParameter( "name" , NAME2 );
        request.addParameter( "lastname" , LASTNAME2 );
        request.addParameter( "email" , EMAIL2 );
        request.addParameter( "phone" , String.valueOf( PHONE2) );
        request.addParameter( "adress" , ADRESS2 );
		request.setRequestURI("jsp/admin/plugins/example/ManageUsers.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createUser, qui est l'action par défaut
		request.addParameter("action","modifyUser");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyUser" ));

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
		
		//get remove User
		request = new MockHttpServletRequest();
        //request.setRequestURI("jsp/admin/plugins/example/ManageUsers.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new UserJspBean();
		request.addParameter("action","confirmRemoveUser");
		assertNotNull( jspbean.getModifyUser( request ) );
				
		//do remove User
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("jsp/admin/plugins/example/ManageUserts.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createUser, qui est l'action par défaut
		request.addParameter("action","removeUser");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeUser" ));
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
