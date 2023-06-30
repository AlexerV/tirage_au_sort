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
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.draw.business.User;
import fr.paris.lutece.plugins.draw.business.UserHome;

/**
 * This class provides the user interface to manage User features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageUsers.jsp", controllerPath = "jsp/admin/plugins/draw/", right = "DRAW_MANAGEMENT" )
public class UserJspBean extends AbstractManageDrawJspBean <Integer, User>
{
    // Templates
    private static final String TEMPLATE_MANAGE_USERS = "/admin/plugins/draw/manage_users.html";
    private static final String TEMPLATE_CREATE_USER = "/admin/plugins/draw/create_user.html";
    private static final String TEMPLATE_MODIFY_USER = "/admin/plugins/draw/modify_user.html";

    // Parameters
    private static final String PARAMETER_ID_USER = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_USERS = "draw.manage_users.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_USER = "draw.modify_user.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_USER = "draw.create_user.pageTitle";

    // Markers
    private static final String MARK_USER_LIST = "user_list";
    private static final String MARK_USER = "user";

    private static final String JSP_MANAGE_USERS = "jsp/admin/plugins/draw/ManageUsers.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_USER = "draw.message.confirmRemoveUser";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "draw.model.entity.user.attribute.";

    // Views
    private static final String VIEW_MANAGE_USERS = "manageUsers";
    private static final String VIEW_CREATE_USER = "createUser";
    private static final String VIEW_MODIFY_USER = "modifyUser";

    // Actions
    private static final String ACTION_CREATE_USER = "createUser";
    private static final String ACTION_MODIFY_USER = "modifyUser";
    private static final String ACTION_REMOVE_USER = "removeUser";
    private static final String ACTION_CONFIRM_REMOVE_USER = "confirmRemoveUser";

    // Infos
    private static final String INFO_USER_CREATED = "draw.info.user.created";
    private static final String INFO_USER_UPDATED = "draw.info.user.updated";
    private static final String INFO_USER_REMOVED = "draw.info.user.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private User _user;
    private List<Integer> _listIdUsers;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_USERS, defaultView = true )
    public String getManageUsers( HttpServletRequest request )
    {
        _user = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdUsers.isEmpty( ) )
        {
        	_listIdUsers = UserHome.getIdUsersList(  );
        }
        
        Map<String, Object> model = getPaginatedListModel( request, MARK_USER_LIST, _listIdUsers, JSP_MANAGE_USERS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_USERS, TEMPLATE_MANAGE_USERS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<User> getItemsFromIds( List<Integer> listIds ) 
	{
		List<User> listUser = UserHome.getUsersListByIds( listIds );
		
		// keep original order
        return listUser.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdUsers list
    */
    public void resetListId( )
    {
    	_listIdUsers = new ArrayList<>( );
    }

    /**
     * Returns the form to create a user
     *
     * @param request The Http request
     * @return the html code of the user form
     */
    @View( VIEW_CREATE_USER )
    public String getCreateUser( HttpServletRequest request )
    {
        _user = ( _user != null ) ? _user : new User(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_USER, _user );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_USER ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_USER, TEMPLATE_CREATE_USER, model );
    }

    /**
     * Process the data capture form of a new user
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_USER )
    public String doCreateUser( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _user, request, getLocale( ) );
        

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_USER ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _user, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_USER );
        }

        UserHome.create( _user );
        addInfo( INFO_USER_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_USERS );
    }

    /**
     * Manages the removal form of a user whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_USER )
    public String getConfirmRemoveUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_USER ) );
        url.addParameter( PARAMETER_ID_USER, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_USER, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a user
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage users
     */
    @Action( ACTION_REMOVE_USER )
    public String doRemoveUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
        
        
        UserHome.remove( nId );
        addInfo( INFO_USER_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_USERS );
    }

    /**
     * Returns the form to update info about a user
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_USER )
    public String getModifyUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );

        if ( _user == null || ( _user.getId(  ) != nId ) )
        {
            Optional<User> optUser = UserHome.findByPrimaryKey( nId );
            _user = optUser.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }


        Map<String, Object> model = getModel(  );
        model.put( MARK_USER, _user );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_USER ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_USER, TEMPLATE_MODIFY_USER, model );
    }

    /**
     * Process the change form of a user
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_USER )
    public String doModifyUser( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _user, request, getLocale( ) );
		
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_USER ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _user, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_USER, PARAMETER_ID_USER, _user.getId( ) );
        }

        UserHome.update( _user );
        addInfo( INFO_USER_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_USERS );
    }
}
