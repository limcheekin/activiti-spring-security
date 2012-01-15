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

import grails.test.*

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class SpringSecurityIdentitySessionTests extends GrailsUnitTestCase {
	def identityService
	def springSecurityService
	
	protected void setUp() {
		super.setUp()
	}
	
	protected void tearDown() {
		super.tearDown()
	}
	
	void testSelectAllUsers() {
		def users = [
			new User(
			username: 'admin',
			email: 'admin@activiti.org',
			firstName: 'Admin',
			lastName: 'User',
			password: springSecurityService.encodePassword('admin'),
			enabled: true).save(failOnError: true),
			new User(
			username: 'admin2',
			email: 'admin2@activiti.org',
			firstName: 'Admin2',
			lastName: 'User2',
			password: springSecurityService.encodePassword('admin2'),
			enabled: true).save(failOnError: true)
		]
		assert users.containsAll(identityService.createUserQuery().orderByUserLastName().asc().orderByUserFirstName().asc().list())
	}
	
	void testSelectAllUsersCount() {
		def users = [
			new User(
			username: 'admin',
			email: 'admin@activiti.org',
			firstName: 'Admin',
			lastName: 'User',
			password: springSecurityService.encodePassword('admin'),
			enabled: true).save(failOnError: true),
			new User(
		  username: 'admin2',
			email: 'admin2@activiti.org',
			firstName: 'Admin2',
			lastName: 'User2',
			password: springSecurityService.encodePassword('admin2'),
			enabled: true).save(failOnError: true)
		]
		assertEquals "users.size()", users.size(), identityService.createUserQuery().count()
	}
	
	void testFindGroupsByUser() {
		def user = new User(
			  username: 'admin',
				email: 'admin@activiti.org',
				firstName: 'Admin',
				lastName: 'User',
				password: springSecurityService.encodePassword('admin'),
				enabled: true).save(failOnError: true)
		def role1 = new Role(authority: "ROLE_USER", name: "Role User")
		role1.id = "ROLE_USER"
		role1.save(failOnError: true)
		def role2 = new Role(authority: "ROLE_MANAGER", name: "Role Manager")
		role2.id = "ROLE_MANAGER"
		role2.save(failOnError: true)
		UserRole.create(user, role1)
		UserRole.create(user, role2)
		assert [role1, role2].containsAll(identityService.createGroupQuery().groupMember(user.id).orderByGroupId().asc().list())
	}
	
	void testFindGroupsByUserCount() {
		def user = new User(
			  username: 'admin',
				email: 'admin@activiti.org',
				firstName: 'Admin',
				lastName: 'User',
				password: springSecurityService.encodePassword('admin'),
				enabled: true).save(failOnError: true)
		def role1 = new Role(authority: "ROLE_USER", name: "Role User")
		role1.id = "ROLE_USER"
		role1.save(failOnError: true)
		def role2 = new Role(authority: "ROLE_MANAGER", name: "Role Manager")
		role2.id = "ROLE_MANAGER"
		role2.save(failOnError: true)
		UserRole.create(user, role1)
		UserRole.create(user, role2)
		assertEquals 2, identityService.createGroupQuery().groupMember(user.id).count()
	}
	
	void testFindUsersByGroup() {
		def users = [
			new User(
			username: 'admin',
			email: 'admin@activiti.org',
			firstName: 'Admin',
			lastName: 'User',
			password: springSecurityService.encodePassword('admin'),
			enabled: true).save(failOnError: true),
			new User(
			username: 'admin2',
			email: 'admin2@activiti.org',
			firstName: 'Admin2',
			lastName: 'User2',
			password: springSecurityService.encodePassword('admin2'),
			enabled: true).save(failOnError: true),
			new User(
			username: 'admin3',
			email: 'admin3@activiti.org',
			firstName: 'Admin3',
			lastName: 'User3',
			password: springSecurityService.encodePassword('admin3'),
			enabled: true).save(failOnError: true)
		]
		def role = new Role(authority: "ROLE_USER", name: "Role User")
		role.id = "ROLE_USER"
		role.save(failOnError: true)
		UserRole.create(users[0], role)
		UserRole.create(users[2], role)
		assert [users[0], users[2]].containsAll(identityService.createUserQuery().memberOfGroup(role.id.toString()).orderByUserId().asc().list())
	}
}
