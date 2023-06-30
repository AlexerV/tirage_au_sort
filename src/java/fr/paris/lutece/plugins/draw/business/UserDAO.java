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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides Data Access methods for User objects
 */
public final class UserDAO implements IUserDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_user, name, lastname, email, phone, adress, status FROM draw_user WHERE id_user = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO draw_user ( name, lastname, email, phone, adress ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM draw_user WHERE id_user = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE draw_user SET name = ?, lastname = ?, email = ?, phone = ?, adress = ?, status = ? WHERE id_user = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user, name, lastname, email, phone, adress, status FROM draw_user";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_user FROM draw_user";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id_user, name, lastname, email, phone, adress, status FROM draw_user WHERE id_user IN (  ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( User user, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , user.getName( ) );
            daoUtil.setString( nIndex++ , user.getLastname( ) );
            daoUtil.setString( nIndex++ , user.getEmail( ) );
            daoUtil.setInt( nIndex++ , user.getPhone( ) );
            daoUtil.setString( nIndex++ , user.getAdress( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                user.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<User> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        User user = null;
	
	        if ( daoUtil.next( ) )
	        {
	            user = new User();
	            int nIndex = 1;
	            
	            user.setId( daoUtil.getInt( nIndex++ ) );
			    user.setName( daoUtil.getString( nIndex++ ) );
			    user.setLastname( daoUtil.getString( nIndex++ ) );
			    user.setEmail( daoUtil.getString( nIndex++ ) );
			    user.setPhone( daoUtil.getInt( nIndex++ ) );
			    user.setAdress( daoUtil.getString( nIndex++ ) );
			    user.setStatus(daoUtil.getInt( nIndex ) );
			    
	        }
	
	        return Optional.ofNullable( user );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( User user, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
            	daoUtil.setString( nIndex++ , user.getName( ) );
            	daoUtil.setString( nIndex++ , user.getLastname( ) );
            	daoUtil.setString( nIndex++ , user.getEmail( ) );
            	daoUtil.setInt( nIndex++ , user.getPhone( ) );
            	daoUtil.setString( nIndex++ , user.getAdress( ) );
            	daoUtil.setInt( nIndex++ , user.getStatus() );
	           
            	daoUtil.setInt( nIndex , user.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<User> selectUsersList( Plugin plugin )
    {
        List<User> userList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            User user = new User(  );
	            int nIndex = 1;
	            
	            user.setId( daoUtil.getInt( nIndex++ ) );
			    user.setName( daoUtil.getString( nIndex++ ) );
			    user.setLastname( daoUtil.getString( nIndex++ ) );
			    user.setEmail( daoUtil.getString( nIndex++ ) );
			    user.setPhone( daoUtil.getInt( nIndex++ ) );
			    user.setAdress( daoUtil.getString( nIndex++ ) );
			    user.setStatus(daoUtil.getInt( nIndex ) );
	            userList.add( user );
	        }
	
	        return userList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdUsersList( Plugin plugin )
    {
        List<Integer> userList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            userList.add( daoUtil.getInt( 1 ) );
	        }
	
	        return userList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectUsersReferenceList( Plugin plugin )
    {
        ReferenceList userList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            userList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        return userList;
    	}
    }
    
    /**
     * {@inheritDoc }
     */
	@Override
	public List<User> selectUsersListByIds( Plugin plugin, List<Integer> listIds ) {
		List<User> userList = new ArrayList<>(  );
		
		StringBuilder builder = new StringBuilder( );

		if ( !listIds.isEmpty( ) )
		{
			for( int i = 0 ; i < listIds.size(); i++ ) {
			    builder.append( "?," );
			}
	
			String placeHolders =  builder.deleteCharAt( builder.length( ) -1 ).toString( );
			String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";
			
			
	        try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
	        {
	        	int index = 1;
				for( Integer n : listIds ) {
					daoUtil.setInt(  index++, n ); 
				}
	        	
	        	daoUtil.executeQuery(  );
	        	while ( daoUtil.next(  ) )
		        {
		        	User user = new User(  );
		            int nIndex = 1;
		            
		            user.setId( daoUtil.getInt( nIndex++ ) );
				    user.setName( daoUtil.getString( nIndex++ ) );
				    user.setLastname( daoUtil.getString( nIndex++ ) );
				    user.setEmail( daoUtil.getString( nIndex++ ) );
				    user.setPhone( daoUtil.getInt( nIndex++ ) );
				    user.setAdress( daoUtil.getString( nIndex++ ) );
				    user.setStatus(daoUtil.getInt( nIndex ) );
				    
		            userList.add( user );
		        }
		
		        daoUtil.free( );
		        
	        }
	    }
		return userList;
		
	}
}
