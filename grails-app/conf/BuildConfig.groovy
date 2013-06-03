grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenRepo "https://maven.alfresco.com/nexus/content/groups/public/"
	}

	plugins {
		compile ":activiti:5.12.1"
		compile ":spring-security-core:1.2.7.3"

		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}

// grails.plugin.location.activiti="../grails-activiti-plugin"