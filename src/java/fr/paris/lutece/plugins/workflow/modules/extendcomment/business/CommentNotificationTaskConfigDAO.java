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
package fr.paris.lutece.plugins.workflow.modules.extendcomment.business;

import fr.paris.lutece.plugins.workflow.modules.extendcomment.service.WorkflowCommentPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * 
 * CommentNotificationTaskConfigDAO
 *
 */
public class CommentNotificationTaskConfigDAO implements ITaskConfigDAO<CommentNotificationTaskConfig>
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_task, message, subject, sender_name, sender_email FROM task_forms_extend_comment_notification_cf WHERE id_task = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO task_forms_extend_comment_notification_cf ( id_task, message, subject, sender_name, sender_email ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM task_forms_extend_comment_notification_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE task_forms_extend_comment_notification_cf SET id_task = ?, message = ?, subject = ?, sender_name = ?, sender_email = ? WHERE id_task = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( CommentNotificationTaskConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, WorkflowCommentPlugin.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, config.getIdTask( ) );

            daoUtil.setString( ++nIndex, config.getMessage( ) );
            daoUtil.setString( ++nIndex, config.getSubject( ) );
            daoUtil.setString( ++nIndex, config.getSenderName( ) );
            daoUtil.setString( ++nIndex, config.getSenderEmail( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( CommentNotificationTaskConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, WorkflowCommentPlugin.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, config.getIdTask( ) );
            daoUtil.setString( ++nIndex, config.getMessage( ) );
            daoUtil.setString( ++nIndex, config.getSubject( ) );
            daoUtil.setString( ++nIndex, config.getSenderName( ) );
            daoUtil.setString( ++nIndex, config.getSenderEmail( ) );

            daoUtil.setInt( ++nIndex, config.getIdTask( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentNotificationTaskConfig load( int nIdTask )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, WorkflowCommentPlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeQuery( );

            CommentNotificationTaskConfig commentNotificationTaskConfig = null;

            if ( daoUtil.next( ) )
            {
                commentNotificationTaskConfig = new CommentNotificationTaskConfig( );
                int nIndex = 0;

                commentNotificationTaskConfig.setIdTask( daoUtil.getInt( ++nIndex ) );
                commentNotificationTaskConfig.setMessage( daoUtil.getString( ++nIndex ) );
                commentNotificationTaskConfig.setSubject( daoUtil.getString( ++nIndex ) );
                commentNotificationTaskConfig.setSenderName( daoUtil.getString( ++nIndex ) );
                commentNotificationTaskConfig.setSenderEmail( daoUtil.getString( ++nIndex ) );
            }

            return commentNotificationTaskConfig;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, WorkflowCommentPlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeUpdate( );
        }
    }
}
