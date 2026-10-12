/*
 * Copyright (c) 2002-2026, City of Paris
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

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.plugins.workflow.modules.extendcomment.business.CommentNotificationTaskConfig;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflowcore.business.task.TaskType;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.config.TaskConfigService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

/**
 * Produces the task type and the configuration service of the comment notification task.
 */
@ApplicationScoped
public class CommentNotificationTaskProducer
{
    private static final String PROPERTY_PREFIX = "workflow-extendcomment.taskCommentNotification.";

    /**
     * Produces the comment notification task type.
     *
     * @param strKey
     *            the task type key
     * @param strTitleI18nKey
     *            the i18n key of the task title
     * @param strBeanName
     *            the name of the task bean
     * @param strConfigBeanName
     *            the name of the task configuration bean
     * @param bConfigRequired
     *            whether the task needs a configuration
     * @param bFormTaskRequired
     *            whether the task displays a form
     * @param bTaskForAutomaticAction
     *            whether the task can run in an automatic action
     * @return the task type
     */
    @Produces
    @ApplicationScoped
    @Named( WorkflowCommentPlugin.BEAN_TASK_TYPE )
    public ITaskType produceTaskType( @ConfigProperty( name = PROPERTY_PREFIX + "key" ) String strKey,
            @ConfigProperty( name = PROPERTY_PREFIX + "titleI18nKey" ) String strTitleI18nKey,
            @ConfigProperty( name = PROPERTY_PREFIX + "beanName" ) String strBeanName,
            @ConfigProperty( name = PROPERTY_PREFIX + "configBeanName" ) String strConfigBeanName,
            @ConfigProperty( name = PROPERTY_PREFIX + "configRequired", defaultValue = "true" ) boolean bConfigRequired,
            @ConfigProperty( name = PROPERTY_PREFIX + "formTaskRequired", defaultValue = "false" ) boolean bFormTaskRequired,
            @ConfigProperty( name = PROPERTY_PREFIX + "taskForAutomaticAction", defaultValue = "true" ) boolean bTaskForAutomaticAction )
    {
        TaskType taskType = new TaskType( );
        taskType.setKey( strKey );
        taskType.setTitleI18nKey( strTitleI18nKey );
        taskType.setBeanName( strBeanName );
        taskType.setConfigBeanName( strConfigBeanName );
        taskType.setConfigRequired( bConfigRequired );
        taskType.setFormTaskRequired( bFormTaskRequired );
        taskType.setTaskForAutomaticAction( bTaskForAutomaticAction );

        return taskType;
    }

    /**
     * Produces the configuration service of the comment notification task.
     *
     * @param taskConfigDAO
     *            the configuration DAO
     * @return the configuration service
     */
    @Produces
    @ApplicationScoped
    @Named( WorkflowCommentPlugin.BEAN_CONFIG )
    public ITaskConfigService produceTaskConfigService(
            @Named( WorkflowCommentPlugin.BEAN_CONFIG_DAO ) ITaskConfigDAO<CommentNotificationTaskConfig> taskConfigDAO )
    {
        TaskConfigService taskConfigService = new TaskConfigService( );
        taskConfigService.setTaskConfigDAO( (ITaskConfigDAO) taskConfigDAO );

        return taskConfigService;
    }
}
