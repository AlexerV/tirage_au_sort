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

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Email;
import java.io.Serializable;
/**
 * This is the business class for the object User
 */ 
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    private int _nStatus;
    
    public int getStatus() {
		return _nStatus;
	}

	public void setStatus(int _nStatus) {
		this._nStatus = _nStatus;
	}

	@NotEmpty( message = "#i18n{draw.validation.user.Name.notEmpty}" )
    private String _strName;
    
    @NotEmpty( message = "#i18n{draw.validation.user.Lastname.notEmpty}" )
    private String _strLastname;
    @Email( message = "#i18n{portal.validation.message.email}" )
    @NotEmpty( message = "#i18n{draw.validation.user.Email.notEmpty}" )
    @Size( max = 255 , message = "#i18n{draw.validation.user.Email.size}" ) 
    private String _strEmail;
    
    private int _nPhone;
    
    @NotEmpty( message = "#i18n{draw.validation.user.Adress.notEmpty}" )
    private String _strAdress;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the Name
     * @return The Name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * @param strName The Name
     */ 
    public void setName( String strName )
    {
        _strName = strName;
    }
    
    
    /**
     * Returns the Lastname
     * @return The Lastname
     */
    public String getLastname( )
    {
        return _strLastname;
    }

    /**
     * Sets the Lastname
     * @param strLastname The Lastname
     */ 
    public void setLastname( String strLastname )
    {
        _strLastname = strLastname;
    }
    
    
    /**
     * Returns the Email
     * @return The Email
     */
    public String getEmail( )
    {
        return _strEmail;
    }

    /**
     * Sets the Email
     * @param strEmail The Email
     */ 
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }
    
    
    /**
     * Returns the Phone
     * @return The Phone
     */
    public int getPhone( )
    {
        return _nPhone;
    }

    /**
     * Sets the Phone
     * @param nPhone The Phone
     */ 
    public void setPhone( int nPhone )
    {
        _nPhone = nPhone;
    }
    
    
    /**
     * Returns the Adress
     * @return The Adress
     */
    public String getAdress( )
    {
        return _strAdress;
    }

    /**
     * Sets the Adress
     * @param strAdress The Adress
     */ 
    public void setAdress( String strAdress )
    {
        _strAdress = strAdress;
    }
    
}
