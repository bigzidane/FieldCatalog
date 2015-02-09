package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.GroupDAO;
import com.itservicesdepot.model.Group;
import com.itservicesdepot.model.Result;

@Service("groupService")
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDAO groupDAO;
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result addGroup(Group group) {
		Result result = new Result();
		
		result.setId(groupDAO.addGroup(group));
		
		return result;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result updateGroup(Group group) {
		Result result = new Result();
		
		groupDAO.updateGroup(group);
		
		return result;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result deleteGroup(Group group) {
		Result result = new Result();
	
		int usedCount = this.groupDAO.getGroupUsedCount(group.getId());
		if (usedCount > 0) {
			result.setCode(ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION);
		}
		else {
			groupDAO.deleteGroup(group);
		}
		
		return result;
	}

	@Override
	public List<Group> getGroups() {
		return groupDAO.getGroups();
	}

	@Override
	public Group getGroup(int id) {
		return groupDAO.getGroup(id);
	}

	@Override
	public Group getGroup(String name) {
		return groupDAO.getGroup(name);
	}
}
