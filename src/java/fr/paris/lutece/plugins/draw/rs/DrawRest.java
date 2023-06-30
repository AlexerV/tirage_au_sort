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

import fr.paris.lutece.plugins.draw.business.Draw;
import fr.paris.lutece.plugins.draw.business.DrawHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.sql.Date;
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
 * DrawRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.DRAW_PATH )
public class DrawRest
{
    private static final int VERSION_1 = 1;
    
    /**
     * Get Draw List
     * @param nVersion the API version
     * @return the Draw List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getDrawList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return getDrawListV1( );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Draw List V1
     * @return the Draw List for the version 1
     */
    private Response getDrawListV1( )
    {
        List<Draw> listDraws = DrawHome.getDrawsList( );
        
        if ( listDraws.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
        }
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( listDraws ) ) )
                .build( );
    }
    
    /**
     * Create Draw
     * @param nVersion the API version
     * @param iduser the iduser
     * @param number the number
     * @param datecreation the datecreation
     * @return the Draw if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createDraw(
    @FormParam( Constants.DRAW_ATTRIBUTE_IDUSER ) String iduser,
    @FormParam( Constants.DRAW_ATTRIBUTE_NUMBER ) String number,
    @FormParam( Constants.DRAW_ATTRIBUTE_DATECREATION ) String datecreation,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
		if ( nVersion == VERSION_1 )
		{
		    return createDrawV1( iduser, number, datecreation );
		}
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Create Draw V1
     * @param iduser the iduser
     * @param number the number
     * @param datecreation the datecreation
     * @return the Draw if created for the version 1
     */
    private Response createDrawV1( String iduser, String number, String datecreation )
    {
        if ( StringUtils.isEmpty( iduser ) || StringUtils.isEmpty( number ) || StringUtils.isEmpty( datecreation ) )
        {
            AppLogService.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Draw draw = new Draw( );
	    draw.setIduser( Integer.parseInt( iduser ) );
	    draw.setNumber( Integer.parseInt( number ) );
	    draw.setDatecreation( Date.valueOf( datecreation ) );
        DrawHome.create( draw );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( draw ) ) )
                .build( );
    }
    
    /**
     * Modify Draw
     * @param nVersion the API version
     * @param id the id
     * @param iduser the iduser
     * @param number the number
     * @param datecreation the datecreation
     * @return the Draw if modified
     */
    @PUT
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response modifyDraw(
    @PathParam( Constants.ID ) Integer id,
    @FormParam( Constants.DRAW_ATTRIBUTE_IDUSER ) String iduser,
    @FormParam( Constants.DRAW_ATTRIBUTE_NUMBER ) String number,
    @FormParam( Constants.DRAW_ATTRIBUTE_DATECREATION ) String datecreation,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return modifyDrawV1( id, iduser, number, datecreation );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Modify Draw V1
     * @param id the id
     * @param iduser the iduser
     * @param number the number
     * @param datecreation the datecreation
     * @return the Draw if modified for the version 1
     */
    private Response modifyDrawV1( Integer id, String iduser, String number, String datecreation )
    {
        if ( StringUtils.isEmpty( iduser ) || StringUtils.isEmpty( number ) || StringUtils.isEmpty( datecreation ) )
        {
            AppLogService.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Optional<Draw> optDraw = DrawHome.findByPrimaryKey( id );
        if ( !optDraw.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        else
        {
        	Draw draw = optDraw.get( );
		    draw.setIduser( Integer.parseInt( iduser ) );
		    draw.setNumber( Integer.parseInt( number ) );
		    draw.setDatecreation( Date.valueOf( datecreation ) );
	        DrawHome.update( draw );
	        
	        return Response.status( Response.Status.OK )
	                .entity( JsonUtil.buildJsonResponse( new JsonResponse( draw ) ) )
	                .build( );
        }
    }
    
    /**
     * Delete Draw
     * @param nVersion the API version
     * @param id the id
     * @return the Draw List if deleted
     */
    @DELETE
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteDraw(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return deleteDrawV1( id );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Delete Draw V1
     * @param id the id
     * @return the Draw List if deleted for the version 1
     */
    private Response deleteDrawV1( Integer id )
    {
        Optional<Draw> optDraw = DrawHome.findByPrimaryKey( id );
        if ( !optDraw.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        DrawHome.remove( id );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
    }
    
    /**
     * Get Draw
     * @param nVersion the API version
     * @param id the id
     * @return the Draw
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getDraw(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return getDrawV1( id );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Draw V1
     * @param id the id
     * @return the Draw for the version 1
     */
    private Response getDrawV1( Integer id )
    {
        Optional<Draw> optDraw = DrawHome.findByPrimaryKey( id );
        if ( !optDraw.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( optDraw.get( ) ) ) )
                .build( );
    }
}