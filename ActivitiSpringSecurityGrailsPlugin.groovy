/* Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.grails.activiti.ActivitiConstants

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class ActivitiSpringSecurityGrailsPlugin {
	// the plugin version
	def version = "0.4.6"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3.3 > *"
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]
	
	// TODO Fill in these fields
	def author = "Lim Chee Kin"
	def authorEmail = "limcheekin@vobject.com"
	def title = "Activiti Spring Security Integration"
	def description = '''\
The plugin integrates Spring Security to Activiti as custom IdentityService by implemented SpringSecurityIdentitySession.
'''
	
	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/activiti-spring-security"
	
	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before 
	}
	
	def doWithSpring = {
		def disabledActiviti = System.getProperty("disabledActiviti")
		
		if (!disabledActiviti && !CH.config.activiti.disabled) {
			println "Activiti Process Engine with Spring Security Initialization ..."
			interactiveAuthenticationSuccessEventListener(org.grails.activiti.springsecurity.InteractiveAuthenticationSuccessEventListener)
			userManagerFactory(org.grails.activiti.springsecurity.SpringSecurityUserManagerFactory)
			groupManagerFactory(org.grails.activiti.springsecurity.SpringSecurityGroupManagerFactory)
			processEngineConfiguration(org.activiti.spring.SpringProcessEngineConfiguration) {
				processEngineName = CH.config.activiti.processEngineName?:ActivitiConstants.DEFAULT_PROCESS_ENGINE_NAME
				databaseType = CH.config.activiti.databaseType?:ActivitiConstants.DEFAULT_DATABASE_TYPE
				databaseSchemaUpdate = CH.config.activiti.databaseSchemaUpdate ? CH.config.activiti.databaseSchemaUpdate.toString() : ActivitiConstants.DEFAULT_DATABASE_SCHEMA_UPDATE
				deploymentName = CH.config.activiti.deploymentName?:ActivitiConstants.DEFAULT_DEPLOYMENT_NAME
				deploymentResources = CH.config.activiti.deploymentResources?:ActivitiConstants.DEFAULT_DEPLOYMENT_RESOURCES
				jobExecutorActivate = CH.config.activiti.jobExecutorActivate?:ActivitiConstants.DEFAULT_JOB_EXECUTOR_ACTIVATE
				history = CH.config.activiti.history?:ActivitiConstants.DEFAULT_HISTORY
				mailServerHost = CH.config.activiti.mailServerHost?:ActivitiConstants.DEFAULT_MAIL_SERVER_HOST
				mailServerPort = CH.config.activiti.mailServerPort?:ActivitiConstants.DEFAULT_MAIL_SERVER_PORT
				mailServerUsername = CH.config.activiti.mailServerUsername
				mailServerPassword = CH.config.activiti.mailServerPassword
				mailServerDefaultFrom = CH.config.activiti.mailServerDefaultFrom?:ActivitiConstants.DEFAULT_MAIL_SERVER_FROM
				customSessionFactories = [ref("userManagerFactory"), ref("groupManagerFactory")]
				dataSource = ref("dataSource")
				transactionManager = ref("transactionManager")
			}
			
			processEngine(org.activiti.spring.ProcessEngineFactoryBean) { processEngineConfiguration = ref("processEngineConfiguration") }
			
			runtimeService(processEngine:"getRuntimeService")
			repositoryService(processEngine:"getRepositoryService")
			taskService(processEngine:"getTaskService")
			managementService(processEngine:"getManagementService")
			identityService(processEngine:"getIdentityService")
			historyService(processEngine:"getHistoryService")
			formService(processEngine:"getFormService")
			
			activitiService(org.grails.activiti.ActivitiService) {
				runtimeService = ref("runtimeService")
				taskService = ref("taskService")
				identityService = ref("identityService")
				formService = ref("formService")
			}
		}
	}
	def doWithDynamicMethods = { ctx ->
		// TODO Implement registering dynamic methods to classes (optional)
	}
	
	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)
	}
	
	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}
	
	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}
}
