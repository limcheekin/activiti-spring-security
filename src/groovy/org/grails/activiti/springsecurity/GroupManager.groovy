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

import org.activiti.engine.identity.Group
import org.activiti.engine.identity.GroupQuery
import org.activiti.engine.impl.GroupQueryImpl
import org.activiti.engine.impl.Page
import org.activiti.engine.impl.context.Context
import org.activiti.engine.impl.persistence.entity.GroupEntity

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils as SSU
import grails.util.GrailsNameUtils as GNU
/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.4
 */
class GroupManager extends org.activiti.engine.impl.persistence.entity.GroupManager {
	
	static final Log LOG = LogFactory.getLog(GroupManager.class)
	
	Group createNewGroup(String groupId) {
		throw new UnsupportedOperationException("Please use ${getGroupDomainClass()}.save() to create Group.")
	}
	
	void insertGroup(Group group) {
		throw new UnsupportedOperationException("Please use ${getGroupDomainClass()}.save() to create Group.")
	}
	
	void updateGroup(Group updatedGroup) {
		throw new UnsupportedOperationException("Please use ${getGroupDomainClass()}.save() to update Group.")
	}
	
	void deleteGroup(String groupId) {
		throw new UnsupportedOperationException("Please use ${getGroupDomainClass()}.delete() to delete Group.")
	}
	
	GroupQuery createNewGroupQuery() {
		return new GroupQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired())
	}
	
	List<Group> findGroupsByUser(String userId) {
		LOG.debug "findGroupsByUser (${userId})"
		def user = getUserDomainClass()."findBy${getUsernameClassName()}"(userId)
		def groups = user?.authorities.toList()
		return groups
	}
	
	List<Group> findGroupByQueryCriteria(Object query, Page page) {
		LOG.debug "findGroupByQueryCriteria (${query.class.name}, $page)"
		List<Group> groups
		String queryString = createGroupQueryString(query)
		LOG.debug "queryString = $queryString"
		if (page) { // listPage()
			groups = getGroupJoinDomainClass().findAll(queryString, [offset:page.firstResult, max:page.maxResults]).collect{it.userGroup}
		} else { // list()
			groups = getGroupJoinDomainClass().findAll(queryString, Collections.emptyMap()).collect{it."${GNU.getPropertyName(getGroupDomainClassName())}"}
		}
		return groups
	}
	
	long findGroupCountByQueryCriteria(Object query) {
		LOG.debug "findGroupCountByQueryCriteria (${query.class.name})"
		String queryString = createGroupQueryString(query)
		LOG.debug "queryString = $queryString"
		return getGroupJoinDomainClass().executeQuery("select count(g) ${queryString}")[0]
	}
	
	private String createGroupQueryString(Object query) {
		FastStringWriter queryString = new FastStringWriter()
		queryString << "from ${getGroupJoinDomainClassName()} as g where 1=1"
		String groupPropertyName = GNU.getPropertyName(getGroupDomainClassName())
		if (query.id)
			queryString << " and g.${groupPropertyName}.id='${query.id}'"
		
		if (query.name) {
			queryString << " and g.${groupPropertyName}.name = '${query.name}'"
		}
		
		if (query.nameLike) {
			queryString << " and g.${groupPropertyName}.name like '${query.nameLike}'"
		}
		
		if (query.type) {
			queryString << " and g.${groupPropertyName}.type = '${query.type}'"
		}
		
		if (query.userId) {
			queryString << " and g.${GNU.getPropertyName(getUserDomainClassName())}.id = '${query.userId}'"
		}
		
		if (query.orderBy) {
			String orderBy = query.orderBy.toLowerCase().replace('_', '')
			orderBy = orderBy.replace('g', "g.${groupPropertyName}")
			queryString << " order by ${orderBy}"
		}
		return queryString.toString()
	}
	
	GroupEntity findGroupById(String groupId) {
		throw new UnsupportedOperationException("Please use ${getGroupDomainClass()}.get(id) to find Group by Id.")
	}
	
	private getGroupDomainClassName() {
		return SSU.securityConfig.authority.className
	}
	
	private getGroupDomainClass() {
		return AH.application.getDomainClass(getGroupDomainClassName()).clazz
	}
	
	private getGroupJoinDomainClassName() {
		return SSU.securityConfig.userLookup.authorityJoinClassName
	}
	
	private getGroupJoinDomainClass() {
		return AH.application.getDomainClass(getGroupJoinDomainClassName()).clazz
	}
	
	private getUserDomainClassName() {
		return SSU.securityConfig.userLookup.userDomainClassName
	}

	private getUserDomainClass() {
		return AH.application.getDomainClass(getUserDomainClassName()).clazz
	}
	
	private getUsernamePropertyName() {
		return SSU.securityConfig.userLookup.usernamePropertyName
	}
		
	private getUsernameClassName() {
		return GNU.getClassNameRepresentation(getUsernamePropertyName())
	}
}
