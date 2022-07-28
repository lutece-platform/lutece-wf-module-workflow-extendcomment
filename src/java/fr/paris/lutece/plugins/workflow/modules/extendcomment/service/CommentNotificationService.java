/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.plugins.workflow.modules.extendcomment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.mylutece.modules.cacheuserattribute.service.CacheUserAttributeService;
import fr.paris.lutece.plugins.workflow.modules.extendcomment.business.CommentNotificationTaskConfig;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * CommentNotificationService
 *
 */
public class CommentNotificationService implements ICommentNotificationService
{
    // MARKS
    private static final String MARK_DATE_CREATION = "date_creation";
    private static final String MARK_COMMENT = "comment";
    private static final String MARK_RESOURCE_ID = "resourceId";
    private static final String MARK_FIRSTNAME = "firstname";
    private static final String MARK_URL_FO = "url_response_fo";

    // PROPERTIES
    private static final String PROPERTY_CREATION_DATE = "module.workflow.extendcomment.task_comment_notification_config.markers.creation_date";
    private static final String PROPERTY_COMMENT = "module.workflow.extendcomment.task_comment_notification_config.markers.comment_content";
    private static final String PROPERTY_RESOURCE_ID = "module.workflow.extendcomment.task_comment_notification_config.markers.resource_id";
    private static final String PROPERTY_FIRSTNAME = "module.workflow.extendcomment.task_comment_notification_config.markers.firstname";
    private static final String PROPERTY_LABEL_URL_RESPONSE_FO = "module.workflow.extendcomment.task_comment_notification_config.markers.url_response_fo";    
    private static final String PROPERTY_URL_RESPONSE_FO = "workflow-extendcomment.formResponse.url.fo";
    private static final String PROPERTY_LUTECE_PROD_URL = "lutece.prod.url";
    private static final String PROPERTY_ID_FIRSTNAME_ATTRIBUTE = "workflow-extendcomment.cacheuserattribute.attributeId.firstname";

    @Inject
    @Named( CommentService.BEAN_SERVICE )
    ICommentService _commentService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getAvailableMarkers( )
    {
        Locale locale = I18nService.getDefaultLocale( );

        ReferenceList refList = new ReferenceList( );
        
        refList.addItem( MARK_DATE_CREATION, I18nService.getLocalizedString( PROPERTY_CREATION_DATE, locale ) );
        refList.addItem( MARK_COMMENT, I18nService.getLocalizedString( PROPERTY_COMMENT, locale ) );
        refList.addItem( MARK_RESOURCE_ID, I18nService.getLocalizedString( PROPERTY_RESOURCE_ID, locale ) );
        refList.addItem( MARK_FIRSTNAME, I18nService.getLocalizedString( PROPERTY_FIRSTNAME, locale ) );
        refList.addItem( MARK_URL_FO, I18nService.getLocalizedString( PROPERTY_LABEL_URL_RESPONSE_FO, locale ) );
        
        return refList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getAvailableMarkersValues( Comment comment )
    {
        Map<String, Object> markers = new HashMap<>( );
        
        markers.put( MARK_DATE_CREATION, comment.getDateComment( ) );
        markers.put( MARK_COMMENT, comment.getComment( ) );
        markers.put( MARK_RESOURCE_ID, comment.getIdExtendableResource( ) );
        markers.put( MARK_FIRSTNAME, getFirstnameAuthor( comment ) );
        markers.put( MARK_URL_FO, AppPropertiesService.getProperty( PROPERTY_LUTECE_PROD_URL ) +  AppPropertiesService.getProperty( PROPERTY_URL_RESPONSE_FO ) 
            + comment.getIdExtendableResource( ) );

        return markers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommentAuthor( ResourceHistory resourceHistory, CommentNotificationTaskConfig config )
    {
        Comment comment = _commentService.findByPrimaryKey( resourceHistory.getIdResource( ) );

        if ( comment != null )
        {
            Map<String, Object> model = getAvailableMarkersValues( comment );
            
            HtmlTemplate html = AppTemplateService.getTemplateFromStringFtl( StringUtils.EMPTY, config.getMessage( ), null, model, false );

            MailService.sendMailHtml( StringUtils.EMPTY, StringUtils.EMPTY, comment.getEmail( ), config.getSenderName( ), config.getSenderEmail( ),
                    config.getSubject( ), html.getHtml( ) );
        }
    }
    
    /**
     * Return firstname with CacheUserAttributeService
     * @param comment
     * @return firstname
     */
    private String getFirstnameAuthor( Comment comment )
    {
        int nIdEmailAttribute = AppPropertiesService.getPropertyInt( PROPERTY_ID_FIRSTNAME_ATTRIBUTE, 3 );
        List<String> listUsersId = new ArrayList<>();
        listUsersId.add( comment.getLuteceUserName( ) );
        
        Map<String, String> listAttribues = CacheUserAttributeService.getCachedAttributesByListUserIdsAndAttributeId( listUsersId, nIdEmailAttribute );
        
        if ( !listAttribues.isEmpty( ) )
        {
            return listAttribues.get( comment.getLuteceUserName( ) );
        } else
        {
            return StringUtils.EMPTY;
        } 
    }
}
