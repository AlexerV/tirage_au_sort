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
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.draw.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Optional;


/**
 * This is the business class test for the object User
 */
public class UserBusinessTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String LASTNAME1 = "Lastname1";
    private static final String LASTNAME2 = "Lastname2";
    private static final String EMAIL1 = "Email1";
    private static final String EMAIL2 = "Email2";
    private static final int PHONE1 = 1;
    private static final int PHONE2 = 2;
    private static final String ADRESS1 = "Adress1";
    private static final String ADRESS2 = "Adress2";

	/**
	* test User
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        User user = new User();
        user.setName( NAME1 );
        user.setLastname( LASTNAME1 );
        user.setEmail( EMAIL1 );
        user.setPhone( PHONE1 );
        user.setAdress( ADRESS1 );

        // Create test
        UserHome.create( user );
        Optional<User> optUserStored = UserHome.findByPrimaryKey( user.getId( ) );
        User userStored = optUserStored.orElse( new User ( ) );
        assertEquals( userStored.getName( ) , user.getName( ) );
        assertEquals( userStored.getLastname( ) , user.getLastname( ) );
        assertEquals( userStored.getEmail( ) , user.getEmail( ) );
        assertEquals( userStored.getPhone( ) , user.getPhone( ) );
        assertEquals( userStored.getAdress( ) , user.getAdress( ) );

        // Update test
        user.setName( NAME2 );
        user.setLastname( LASTNAME2 );
        user.setEmail( EMAIL2 );
        user.setPhone( PHONE2 );
        user.setAdress( ADRESS2 );
        UserHome.update( user );
        optUserStored = UserHome.findByPrimaryKey( user.getId( ) );
        userStored = optUserStored.orElse( new User ( ) );
        
        assertEquals( userStored.getName( ) , user.getName( ) );
        assertEquals( userStored.getLastname( ) , user.getLastname( ) );
        assertEquals( userStored.getEmail( ) , user.getEmail( ) );
        assertEquals( userStored.getPhone( ) , user.getPhone( ) );
        assertEquals( userStored.getAdress( ) , user.getAdress( ) );

        // List test
        UserHome.getUsersList( );

        // Delete test
        UserHome.remove( user.getId( ) );
        optUserStored = UserHome.findByPrimaryKey( user.getId( ) );
        userStored = optUserStored.orElse( null );
        assertNull( userStored );
        
    }
    
    
     

}