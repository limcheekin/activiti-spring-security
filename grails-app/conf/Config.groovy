// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
	
	  debug  'org.grails.activiti.springsecurity'
}

grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.grails.activiti.springsecurity.User'
grails.plugins.springsecurity.userLookup.usernamePropertyName = 'email'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.grails.activiti.springsecurity.UserRole'
grails.plugins.springsecurity.authority.className = 'org.grails.activiti.springsecurity.Role'
grails.plugins.springsecurity.requestMap.className = 'org.grails.activiti.springsecurity.RequestMap'
grails.plugins.springsecurity.securityConfigType = grails.plugins.springsecurity.SecurityConfigType.Requestmap

grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

// Added by the Grails Activiti plugin:
activiti {
    processEngineName = "activiti-engine-default"
	  databaseType = "h2" 
	  deploymentName = appName
	  deploymentResources = ["file:./grails-app/conf/**/*.bpmn*.xml", 
	                         "file:./grails-app/conf/**/*.png", 
	                         "file:./src/taskforms/**/*.form"]
	  jobExecutorActivate = false
	  mailServerHost = "smtp.yourserver.com"
	  mailServerPort = "25"
	  mailServerUsername = ""
	  mailServerPassword = ""
	  mailServerDefaultFrom = "username@yourserver.com"
	  history = "audit" // "none", "activity", "audit" or "full"
	  sessionUsernameKey = "username"
	  useFormKey = true
}

environments {
    development {
        activiti {
			  processEngineName = "activiti-engine-dev"
			  databaseSchemaUpdate = true // true, false or "create-drop"	  
        }
    }
    test {
        activiti {
			  processEngineName = "activiti-engine-test"
			  databaseSchemaUpdate = true
	      mailServerPort = "5025"			  
        }
    }	
    production {
        activiti {
			  processEngineName = "activiti-engine-prod"
			  databaseSchemaUpdate = false
			  jobExecutorActivate = true
        }
    }
}	

