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
package org.grails.activiti.springsecurity

import org.springframework.context.ApplicationListener 
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.codehaus.groovy.grails.plugins.springsecurity.SecurityRequestHolder as SRH
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.grails.activiti.ActivitiConstants
import org.springframework.web.context.request.RequestContextHolder as RCH
import org.springframework.web.context.request.RequestAttributes as RA

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.2
*/
class InteractiveAuthenticationSuccessEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {
	void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		def sessionUsernameKey = CH.config.activiti.sessionUsernameKey?:ActivitiConstants.DEFAULT_SESSION_USERNAME_KEY
		def session = SRH.request.getSession(true)
		session[sessionUsernameKey] = event.authentication.name
	}
}
