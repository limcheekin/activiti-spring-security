grails.project.work.dir = 'target'

grails.project.dependency.resolution = {
	inherits('global') {
		excludes 'hibernate', 'tomcat'
	}

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenCentral()
		mavenRepo "https://maven.alfresco.com/nexus/content/groups/public/"
		mavenRepo 'http://repo.spring.io/milestone'
	}

	plugins {
		compile ":activiti:5.12.1"
		compile ':spring-security-core:2.0-RC2'

		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}

// grails.plugin.location.activiti="../grails-activiti-plugin"