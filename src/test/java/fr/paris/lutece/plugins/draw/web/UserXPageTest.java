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
import fr.paris.lutece.plugins.draw.business.User;
import fr.paris.lutece.plugins.draw.business.UserHome;
/**
 * This is the business class test for the object User
 */
public class UserXPageTest extends LuteceTestCase
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

public void testXPage(  ) throws AccessDeniedException, IOException
	{
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
		MockHttpServletResponse response = new MockHttpServletResponse( );
		MockServletConfig config = new MockServletConfig( );

		UserXPage xpage = new UserXPage( );
		assertNotNull( xpage.getCreateUser( request ) );
		
		request = new MockHttpServletRequest();
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createUser" ));
		
		LocalVariables.setLocal(config, request, response);
		
		request.addParameter( "action" , "createUser" );
        request.addParameter( "name" , NAME1 );
        request.addParameter( "lastname" , LASTNAME1 );
        request.addParameter( "email" , EMAIL1 );
        request.addParameter( "phone" , String.valueOf( PHONE1) );
        request.addParameter( "adress" , ADRESS1 );
		request.setMethod( "POST" );
		
		assertNotNull( xpage.doCreateUser( request ) );
		
		
		//modify User	
		List<Integer> listIds = UserHome.getIdUsersList(); 

		assertTrue( !listIds.isEmpty( ) );

		request = new MockHttpServletRequest();
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

		assertNotNull( xpage.getModifyUser( request ) );

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		
        request.addParameter( "name" , NAME2 );
        request.addParameter( "lastname" , LASTNAME2 );
        request.addParameter( "email" , EMAIL2 );
        request.addParameter( "phone" , String.valueOf( PHONE2) );
        request.addParameter( "adress" , ADRESS2 );

		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyUser" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		
		assertNotNull( xpage.doModifyUser( request ) );

		//do confirm remove User
		request = new MockHttpServletRequest();
		request.addParameter( "action" , "confirmRemoveUser" );
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemoveUser" ));;
		request.setMethod("GET");

		try
		{
			xpage.getConfirmRemoveUser( request );
		}
		catch(Exception e)
		{
			assertTrue(e instanceof SiteMessageException);
		}

		//do remove User
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		request.addParameter( "action" , "removeUser" );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeUser" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doRemoveUser( request ) );

    }
    
}
