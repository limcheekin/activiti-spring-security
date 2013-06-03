/* Copyright 2011-2012 the original author or authors.
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
import org.activiti.spring.ProcessEngineFactoryBean
import org.activiti.spring.SpringProcessEngineConfiguration
import org.grails.activiti.ActivitiConstants
import org.grails.activiti.ActivitiService
import org.grails.activiti.serializable.SerializableVariableType
import org.grails.activiti.springsecurity.InteractiveAuthenticationSuccessEventListener
import org.grails.activiti.springsecurity.SpringSecurityGroupManagerFactory
import org.grails.activiti.springsecurity.SpringSecurityUserManagerFactory

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class ActivitiSpringSecurityGrailsPlugin {
	def version = "0.5.0"
	def grailsVersion = "1.3.3 > *"

	def loadAfter = ['activiti']

	def author = "Lim Chee Kin"
	def authorEmail = "limcheekin@vobject.com"
	def title = "Activiti Spring Security Integration"
	def description = 'Integrates Spring Security to Activiti as custom IdentityService by implemented SpringSecurityIdentitySession.'

	def documentation = "http://grails.org/plugin/activiti-spring-security"

	def license = "APACHE"
	def issueManagement = [system: 'GitHub', url: 'https://github.com/limcheekin/activiti-spring-security/issues']
	def scm = [url: 'https://github.com/limcheekin/activiti-spring-security']

	def doWithSpring = {
		def disabledActiviti = System.getProperty("disabledActiviti")

		def config = application.config.activiti

		if (disabledActiviti || config.disabled) {
			return
		}

		println "Configuring Activiti Process Engine with Spring Security ..."
		interactiveAuthenticationSuccessEventListener(InteractiveAuthenticationSuccessEventListener)
		userManagerFactory(SpringSecurityUserManagerFactory)
		groupManagerFactory(SpringSecurityGroupManagerFactory)
		processEngineConfiguration(SpringProcessEngineConfiguration) {
			processEngineName = config.processEngineName?:ActivitiConstants.DEFAULT_PROCESS_ENGINE_NAME
			databaseType = config.databaseType?:ActivitiConstants.DEFAULT_DATABASE_TYPE
			databaseSchemaUpdate = config.databaseSchemaUpdate ? config.databaseSchemaUpdate.toString() : ActivitiConstants.DEFAULT_DATABASE_SCHEMA_UPDATE
			deploymentName = config.deploymentName?:ActivitiConstants.DEFAULT_DEPLOYMENT_NAME
			deploymentResources = config.deploymentResources?:ActivitiConstants.DEFAULT_DEPLOYMENT_RESOURCES
			jobExecutorActivate = config.jobExecutorActivate?:ActivitiConstants.DEFAULT_JOB_EXECUTOR_ACTIVATE
			history = config.history?:ActivitiConstants.DEFAULT_HISTORY
			mailServerHost = config.mailServerHost?:ActivitiConstants.DEFAULT_MAIL_SERVER_HOST
			mailServerPort = config.mailServerPort?:ActivitiConstants.DEFAULT_MAIL_SERVER_PORT
			mailServerUsername = config.mailServerUsername
			mailServerPassword = config.mailServerPassword
			mailServerDefaultFrom = config.mailServerDefaultFrom?:ActivitiConstants.DEFAULT_MAIL_SERVER_FROM
			customSessionFactories = [ref("userManagerFactory"), ref("groupManagerFactory")]
			dataSource = ref("dataSource")
			transactionManager = ref("transactionManager")

			// Define custom serializable types for fix issue with serialization
			customPreVariableTypes = [new SerializableVariableType()]
		}

		processEngine(ProcessEngineFactoryBean) { processEngineConfiguration = ref("processEngineConfiguration") }

		runtimeService(processEngine:"getRuntimeService")
		repositoryService(processEngine:"getRepositoryService")
		taskService(processEngine:"getTaskService")
		managementService(processEngine:"getManagementService")
		identityService(processEngine:"getIdentityService")
		historyService(processEngine:"getHistoryService")
		formService(processEngine:"getFormService")

		activitiService(ActivitiService) {
			runtimeService = ref("runtimeService")
			taskService = ref("taskService")
			identityService = ref("identityService")
			formService = ref("formService")
		}
		println "... finished configuring Activiti Process Engine with Spring Security."
	}
}
