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
 	
 
package fr.paris.lutece.plugins.draw.web;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;



import fr.paris.lutece.plugins.draw.business.Draw;
import fr.paris.lutece.plugins.draw.business.DrawHome;
import fr.paris.lutece.plugins.draw.business.User;
import fr.paris.lutece.plugins.draw.business.UserHome;

/**
 * This class provides the user interface to manage Draw features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDraws.jsp", controllerPath = "jsp/admin/plugins/draw/", right = "DRAW_MANAGEMENT" )
public class DrawJspBean extends AbstractManageDrawJspBean <Integer, Draw>
{
    // Templates
    private static final String TEMPLATE_MANAGE_DRAWS = "/admin/plugins/draw/manage_draws.html";
    private static final String TEMPLATE_CREATE_DRAW = "/admin/plugins/draw/create_draw.html";
    private static final String TEMPLATE_MODIFY_DRAW = "/admin/plugins/draw/modify_draw.html";

    // Parameters
    private static final String PARAMETER_ID_DRAW = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DRAWS = "draw.manage_draws.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DRAW = "draw.modify_draw.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DRAW = "draw.create_draw.pageTitle";

    // Markers
    private static final String MARK_DRAW_LIST = "draw_list";
    private static final String MARK_DRAW = "draw";

    private static final String JSP_MANAGE_DRAWS = "jsp/admin/plugins/draw/ManageDraws.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DRAW = "draw.message.confirmRemoveDraw";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "draw.model.entity.draw.attribute.";

    // Views
    private static final String VIEW_MANAGE_DRAWS = "manageDraws";
    private static final String VIEW_CREATE_DRAW = "createDraw";
    private static final String VIEW_MODIFY_DRAW = "modifyDraw";

    // Actions
    private static final String ACTION_CREATE_DRAW = "createDraw";
    private static final String ACTION_MODIFY_DRAW = "modifyDraw";
    private static final String ACTION_REMOVE_DRAW = "removeDraw";
    private static final String ACTION_CONFIRM_REMOVE_DRAW = "confirmRemoveDraw";
    private static final String ACTION_RAND_DRAW = "randDraw";

    // Infos
    private static final String INFO_DRAW_CREATED = "draw.info.draw.created";
    private static final String INFO_DRAW_UPDATED = "draw.info.draw.updated";
    private static final String INFO_DRAW_REMOVED = "draw.info.draw.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private Draw _draw;
    private List<Integer> _listIdDraws;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DRAWS, defaultView = true )
    public String getManageDraws( HttpServletRequest request )
    {
        _draw = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdDraws.isEmpty( ) )
        {
        	_listIdDraws = DrawHome.getIdDrawsList(  );
        }
        
        Map<String, Object> model = getPaginatedListModel( request, MARK_DRAW_LIST, _listIdDraws, JSP_MANAGE_DRAWS );
        
        
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_RAND_DRAW ) );


        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DRAWS, TEMPLATE_MANAGE_DRAWS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<Draw> getItemsFromIds( List<Integer> listIds ) 
	{
		List<Draw> listDraw = DrawHome.getDrawsListByIds( listIds );
		
		// keep original order
        return listDraw.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdDraws list
    */
    public void resetListId( )
    {
    	_listIdDraws = new ArrayList<>( );
    }

    /**
     * Returns the form to create a draw
     *
     * @param request The Http request
     * @return the html code of the draw form
     */
    @View( VIEW_CREATE_DRAW )
    public String getCreateDraw( HttpServletRequest request )
    {
        _draw = ( _draw != null ) ? _draw : new Draw(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DRAW, _draw );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_DRAW ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DRAW, TEMPLATE_CREATE_DRAW, model );
    }

    /**
     * Process the data capture form of a new draw
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_DRAW )
    public String doCreateDraw( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _draw, request, getLocale( ) );
        

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_DRAW ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _draw, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DRAW );
        }

        DrawHome.create( _draw );
        addInfo( INFO_DRAW_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }
    
    /**
     * Process the data capture form of a new draw
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_RAND_DRAW )
    public String doRandDraw( HttpServletRequest request ) throws AccessDeniedException
    {
       
       
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_RAND_DRAW ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        
       List<Integer> listUser=new ArrayList();
       
       List<User> listUserD=UserHome.getUsersList();
       
       boolean bAllDisabbled=true;
       for(User user:listUserD)
       {
    	   if(user.getStatus()==0)
    	   {
    		   bAllDisabbled=false;
    	   }else
    	   {
    		   listUser.add(user.getId());
    	   }
       }
       if(bAllDisabbled)
       {
    	   for(User user:listUserD)
           {
    		  user.setStatus(0);
    		  UserHome.update(user);
    		  listUser.add(user.getId());
           }
       }
       
        _draw = new Draw();
        Random rand = new Random();
        int nombre;
        do {
        	nombre = rand.nextInt(listUser.size()+1); //génère un nombre de la taille de la liste
        }
        while(nombre==0);
        _draw.setIduser(nombre);
        //User user = UserHome.findByPrimaryKey(nombre).get();
        
        Optional<User> userOpt=UserHome.findByPrimaryKey(nombre);
        
        if(userOpt.isPresent())
        {
        	User user=userOpt.get();
        	user.setStatus(1);
        	UserHome.update(user);
        }
        
        
        
        
        
        long millis=System.currentTimeMillis();  
        java.sql.Date date=new java.sql.Date(millis);  
        _draw.setDatecreation(date);
        

        DrawHome.create( _draw );
        addInfo( INFO_DRAW_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }

    /**
     * Manages the removal form of a draw whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DRAW )
    public String getConfirmRemoveDraw( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DRAW ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DRAW ) );
        url.addParameter( PARAMETER_ID_DRAW, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DRAW, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a draw
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage draws
     */
    @Action( ACTION_REMOVE_DRAW )
    public String doRemoveDraw( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DRAW ) );
        
        
        DrawHome.remove( nId );
        addInfo( INFO_DRAW_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }

    /**
     * Returns the form to update info about a draw
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DRAW )
    public String getModifyDraw( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DRAW ) );

        if ( _draw == null || ( _draw.getId(  ) != nId ) )
        {
            Optional<Draw> optDraw = DrawHome.findByPrimaryKey( nId );
            _draw = optDraw.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }


        Map<String, Object> model = getModel(  );
        model.put( MARK_DRAW, _draw );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_DRAW ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DRAW, TEMPLATE_MODIFY_DRAW, model );
    }

    /**
     * Process the change form of a draw
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_DRAW )
    public String doModifyDraw( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _draw, request, getLocale( ) );
		
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_DRAW ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _draw, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DRAW, PARAMETER_ID_DRAW, _draw.getId( ) );
        }

        DrawHome.update( _draw );
        addInfo( INFO_DRAW_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }
}
