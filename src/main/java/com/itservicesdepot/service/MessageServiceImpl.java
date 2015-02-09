package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.MessageDAO;
import com.itservicesdepot.model.Message;
import com.itservicesdepot.model.Result;

@Service("messageService")
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDAO;
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result addMessage(Message message) {
		Result result = new Result();
		
		messageDAO.addMessage(message);
		
		return result;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result updateMessage(Message message) {
		Result result = new Result();
		
		messageDAO.updateMessage(message);
		
		return result;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result deleteMessage(Message message) {
		Result result = new Result();
		
		int usedCount = this.messageDAO.getMessageUsedCount(message.getId());
		if (usedCount > 0) {
			result.setCode(ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION);
		}
		else {
			messageDAO.deleteMessage(message);
		}
		
		return result;
	}

	@Override
	public List<Message> getMessages() {
		return messageDAO.getMessages();
	}

	@Override
	public Message getMessage(int id) {
		return messageDAO.getMessage(id);
	}

	@Override
	public Message getMessage(String code) {
		return messageDAO.getMessage(code);
	}
}
