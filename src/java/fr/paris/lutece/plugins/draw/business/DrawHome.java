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
 *
 * License 1.0
 */


 package fr.paris.lutece.plugins.draw.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;


import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for Draw objects
 */
public final class DrawHome
{
    // Static variable pointed at the DAO instance
    private static IDrawDAO _dao = SpringContextService.getBean( "draw.drawDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "draw" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DrawHome(  )
    {
    }

    /**
     * Create an instance of the draw class
     * @param draw The instance of the Draw which contains the informations to store
     * @return The  instance of draw which has been created with its primary key.
     */
    public static Draw create( Draw draw )
    {
        _dao.insert( draw, _plugin );

        return draw;
    }

    /**
     * Update of the draw which is specified in parameter
     * @param draw The instance of the Draw which contains the data to store
     * @return The instance of the  draw which has been updated
     */
    public static Draw update( Draw draw )
    {
        _dao.store( draw, _plugin );

        return draw;
    }

    /**
     * Remove the draw whose identifier is specified in parameter
     * @param nKey The draw Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a draw whose identifier is specified in parameter
     * @param nKey The draw primary key
     * @return an instance of Draw
     */
    public static Optional<Draw> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the draw objects and returns them as a list
     * @return the list which contains the data of all the draw objects
     */
    public static List<Draw> getDrawsList( )
    {
        return _dao.selectDrawsList( _plugin );
    }
    
    /**
     * Load the id of all the draw objects and returns them as a list
     * @return the list which contains the id of all the draw objects
     */
    public static List<Integer> getIdDrawsList( )
    {
        return _dao.selectIdDrawsList( _plugin );
    }
    
    /**
     * Load the data of all the draw objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the draw objects
     */
    public static ReferenceList getDrawsReferenceList( )
    {
        return _dao.selectDrawsReferenceList( _plugin );
    }
    
	
    /**
     * Load the data of all the avant objects and returns them as a list
     * @param listIds liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<Draw> getDrawsListByIds( List<Integer> listIds )
    {
    	List<Draw> listResult = _dao.selectDrawsListByIds( _plugin, listIds);
    	for(Draw draw:listResult) {
    		Optional <User> user = UserHome.findByPrimaryKey(draw.getIduser());
    		if(user != null && user.isPresent()) {
    		System.out.println("test");
    		draw.setUser(user.get());
    		}
    	}
    	for(Draw draw:listResult) {
    		if(draw!=null) {
    		System.out.println(Integer.toString(draw.getIduser()));
    		
    		if(draw.getUser()!=null&&draw.getUser().getName()!=null) {
    		System.out.println(draw.getUser().getName());
    		}
    		}
    	}
    	return listResult;
    }

}

