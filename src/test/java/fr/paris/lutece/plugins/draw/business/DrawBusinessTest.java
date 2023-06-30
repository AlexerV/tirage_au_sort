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

import java.sql.Date;

/**
 * This is the business class test for the object Draw
 */
public class DrawBusinessTest extends LuteceTestCase
{
    private static final int IDUSER1 = 1;
    private static final int IDUSER2 = 2;
    private static final int NUMBER1 = 1;
    private static final int NUMBER2 = 2;
	private static final Date DATECREATION1 = new Date( 1000000l );
    private static final Date DATECREATION2 = new Date( 2000000l );

	/**
	* test Draw
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        Draw draw = new Draw();
        draw.setIduser( IDUSER1 );
        draw.setNumber( NUMBER1 );
        draw.setDatecreation( DATECREATION1 );

        // Create test
        DrawHome.create( draw );
        Optional<Draw> optDrawStored = DrawHome.findByPrimaryKey( draw.getId( ) );
        Draw drawStored = optDrawStored.orElse( new Draw ( ) );
        assertEquals( drawStored.getIduser( ) , draw.getIduser( ) );
        assertEquals( drawStored.getNumber( ) , draw.getNumber( ) );
        assertEquals( drawStored.getDatecreation( ).toString() , draw.getDatecreation( ).toString( ) );

        // Update test
        draw.setIduser( IDUSER2 );
        draw.setNumber( NUMBER2 );
        draw.setDatecreation( DATECREATION2 );
        DrawHome.update( draw );
        optDrawStored = DrawHome.findByPrimaryKey( draw.getId( ) );
        drawStored = optDrawStored.orElse( new Draw ( ) );
        
        assertEquals( drawStored.getIduser( ) , draw.getIduser( ) );
        assertEquals( drawStored.getNumber( ) , draw.getNumber( ) );
        assertEquals( drawStored.getDatecreation( ).toString( ) , draw.getDatecreation( ).toString( ) );

        // List test
        DrawHome.getDrawsList( );

        // Delete test
        DrawHome.remove( draw.getId( ) );
        optDrawStored = DrawHome.findByPrimaryKey( draw.getId( ) );
        drawStored = optDrawStored.orElse( null );
        assertNull( drawStored );
        
    }
    
    
     

}