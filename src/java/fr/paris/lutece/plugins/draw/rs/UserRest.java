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

package fr.paris.lutece.plugins.draw.rs;

import fr.paris.lutece.plugins.draw.business.User;
import fr.paris.lutece.plugins.draw.business.UserHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * UserRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.USER_PATH )
public class UserRest
{
    private static final int VERSION_1 = 1;
    
    /**
     * Get User List
     * @param nVersion the API version
     * @return the User List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getUserList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return getUserListV1( );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get User List V1
     * @return the User List for the version 1
     */
    private Response getUserListV1( )
    {
        List<User> listUsers = UserHome.getUsersList( );
        
        if ( listUsers.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
        }
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( listUsers ) ) )
                .build( );
    }
    
    /**
     * Create User
     * @param nVersion the API version
     * @param name the name
     * @param lastname the lastname
     * @param email the email
     * @param phone the phone
     * @param adress the adress
     * @return the User if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createUser(
    @FormParam( Constants.USER_ATTRIBUTE_NAME ) String name,
    @FormParam( Constants.USER_ATTRIBUTE_LASTNAME ) String lastname,
    @FormParam( Constants.USER_ATTRIBUTE_EMAIL ) String email,
    @FormParam( Constants.USER_ATTRIBUTE_PHONE ) String phone,
    @FormParam( Constants.USER_ATTRIBUTE_ADRESS ) String adress,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
		if ( nVersion == VERSION_1 )
		{
		    return createUserV1( name, lastname, email, phone, adress );
		}
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Create User V1
     * @param name the name
     * @param lastname the lastname
     * @param email the email
     * @param phone the phone
     * @param adress the adress
     * @return the User if created for the version 1
     */
    private Response createUserV1( String name, String lastname, String email, String phone, String adress )
    {
        if ( StringUtils.isEmpty( name ) || StringUtils.isEmpty( lastname ) || StringUtils.isEmpty( email ) || StringUtils.isEmpty( phone ) || StringUtils.isEmpty( adress ) )
        {
            AppLogService.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        User user = new User( );
    	user.setName( name );
    	user.setLastname( lastname );
    	user.setEmail( email );
	    user.setPhone( Integer.parseInt( phone ) );
    	user.setAdress( adress );
        UserHome.create( user );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( user ) ) )
                .build( );
    }
    
    /**
     * Modify User
     * @param nVersion the API version
     * @param id the id
     * @param name the name
     * @param lastname the lastname
     * @param email the email
     * @param phone the phone
     * @param adress the adress
     * @return the User if modified
     */
    @PUT
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response modifyUser(
    @PathParam( Constants.ID ) Integer id,
    @FormParam( Constants.USER_ATTRIBUTE_NAME ) String name,
    @FormParam( Constants.USER_ATTRIBUTE_LASTNAME ) String lastname,
    @FormParam( Constants.USER_ATTRIBUTE_EMAIL ) String email,
    @FormParam( Constants.USER_ATTRIBUTE_PHONE ) String phone,
    @FormParam( Constants.USER_ATTRIBUTE_ADRESS ) String adress,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return modifyUserV1( id, name, lastname, email, phone, adress );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Modify User V1
     * @param id the id
     * @param name the name
     * @param lastname the lastname
     * @param email the email
     * @param phone the phone
     * @param adress the adress
     * @return the User if modified for the version 1
     */
    private Response modifyUserV1( Integer id, String name, String lastname, String email, String phone, String adress )
    {
        if ( StringUtils.isEmpty( name ) || StringUtils.isEmpty( lastname ) || StringUtils.isEmpty( email ) || StringUtils.isEmpty( phone ) || StringUtils.isEmpty( adress ) )
        {
            AppLogService.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Optional<User> optUser = UserHome.findByPrimaryKey( id );
        if ( !optUser.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        else
        {
        	User user = optUser.get( );
	    	user.setName( name );
	    	user.setLastname( lastname );
	    	user.setEmail( email );
		    user.setPhone( Integer.parseInt( phone ) );
	    	user.setAdress( adress );
	        UserHome.update( user );
	        
	        return Response.status( Response.Status.OK )
	                .entity( JsonUtil.buildJsonResponse( new JsonResponse( user ) ) )
	                .build( );
        }
    }
    
    /**
     * Delete User
     * @param nVersion the API version
     * @param id the id
     * @return the User List if deleted
     */
    @DELETE
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteUser(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return deleteUserV1( id );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Delete User V1
     * @param id the id
     * @return the User List if deleted for the version 1
     */
    private Response deleteUserV1( Integer id )
    {
        Optional<User> optUser = UserHome.findByPrimaryKey( id );
        if ( !optUser.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        UserHome.remove( id );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
    }
    
    /**
     * Get User
     * @param nVersion the API version
     * @param id the id
     * @return the User
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getUser(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return getUserV1( id );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get User V1
     * @param id the id
     * @return the User for the version 1
     */
    private Response getUserV1( Integer id )
    {
        Optional<User> optUser = UserHome.findByPrimaryKey( id );
        if ( !optUser.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( optUser.get( ) ) ) )
                .build( );
    }
}