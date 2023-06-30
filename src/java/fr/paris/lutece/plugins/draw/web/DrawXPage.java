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

import fr.paris.lutece.plugins.draw.business.Draw;
import fr.paris.lutece.plugins.draw.business.DrawHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest; 


/**
 * This class provides the user interface to manage Draw xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "draw" , pageTitleI18nKey = "draw.xpage.draw.pageTitle" , pagePathI18nKey = "draw.xpage.draw.pagePathLabel" )
public class DrawXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_DRAWS = "/skin/plugins/draw/manage_draws.html";
    private static final String TEMPLATE_CREATE_DRAW = "/skin/plugins/draw/create_draw.html";
    private static final String TEMPLATE_MODIFY_DRAW = "/skin/plugins/draw/modify_draw.html";
    
    // Parameters
    private static final String PARAMETER_ID_DRAW = "id";
    
    // Markers
    private static final String MARK_DRAW_LIST = "draw_list";
    private static final String MARK_DRAW = "draw";
    
    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_DRAW = "draw.message.confirmRemoveDraw";
    
    // Views
    private static final String VIEW_MANAGE_DRAWS = "manageDraws";
    private static final String VIEW_CREATE_DRAW = "createDraw";
    private static final String VIEW_MODIFY_DRAW = "modifyDraw";

    // Actions
    private static final String ACTION_CREATE_DRAW = "createDraw";
    private static final String ACTION_MODIFY_DRAW = "modifyDraw";
    private static final String ACTION_REMOVE_DRAW = "removeDraw";
    private static final String ACTION_CONFIRM_REMOVE_DRAW = "confirmRemoveDraw";

    // Infos
    private static final String INFO_DRAW_CREATED = "draw.info.draw.created";
    private static final String INFO_DRAW_UPDATED = "draw.info.draw.updated";
    private static final String INFO_DRAW_REMOVED = "draw.info.draw.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private Draw _draw;
    
    /**
     * return the form to manage draws
     * @param request The Http request
     * @return the html code of the list of draws
     */
    @View( value = VIEW_MANAGE_DRAWS, defaultView = true )
    public XPage getManageDraws( HttpServletRequest request )
    {
        _draw = null;
        List<Draw> listDraws = DrawHome.getDrawsList(  );
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_DRAW_LIST, listDraws );
        
        return getXPage( TEMPLATE_MANAGE_DRAWS, getLocale( request ), model );
    }

    /**
     * Returns the form to create a draw
     *
     * @param request The Http request
     * @return the html code of the draw form
     */
    @View( VIEW_CREATE_DRAW )
    public XPage getCreateDraw( HttpServletRequest request )
    {
        _draw = ( _draw != null ) ? _draw : new Draw(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DRAW, _draw );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_DRAW ) );

        return getXPage( TEMPLATE_CREATE_DRAW, getLocale( request ), model );
    }

    /**
     * Process the data capture form of a new draw
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_DRAW )
    public XPage doCreateDraw( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _draw, request, getLocale( request ) );

		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_DRAW ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _draw ) )
        {
            return redirectView( request, VIEW_CREATE_DRAW );
        }

        DrawHome.create( _draw );
        addInfo( INFO_DRAW_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }

    /**
     * Manages the removal form of a draw whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException {@link fr.paris.lutece.portal.service.message.SiteMessageException}
     */
    @Action( ACTION_CONFIRM_REMOVE_DRAW )
    public XPage getConfirmRemoveDraw( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DRAW ) );

        UrlItem url = new UrlItem( getActionFullUrl( ACTION_REMOVE_DRAW ) );
        url.addParameter( PARAMETER_ID_DRAW, nId );
        
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_DRAW, SiteMessage.TYPE_CONFIRMATION, url.getUrl(  ) );
        return null;
    }

    /**
     * Handles the removal form of a draw
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage draws
     */
    @Action( ACTION_REMOVE_DRAW )
    public XPage doRemoveDraw( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DRAW ) );
        DrawHome.remove( nId );
        addInfo( INFO_DRAW_REMOVED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }

    /**
     * Returns the form to update info about a draw
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DRAW )
    public XPage getModifyDraw( HttpServletRequest request )
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

        return getXPage( TEMPLATE_MODIFY_DRAW, getLocale( request ), model );
    }

    /**
     * Process the change form of a draw
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_DRAW )
    public XPage doModifyDraw( HttpServletRequest request ) throws AccessDeniedException
    {     
        populate( _draw, request, getLocale( request ) );
		

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_DRAW ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _draw ) )
        {
            return redirect( request, VIEW_MODIFY_DRAW, PARAMETER_ID_DRAW, _draw.getId( ) );
        }

        DrawHome.update( _draw );
        addInfo( INFO_DRAW_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_DRAWS );
    }
}
