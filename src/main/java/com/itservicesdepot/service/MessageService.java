/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Message;
import com.itservicesdepot.model.Result;

public interface MessageService {

	public List<Message> getMessages();

	public Message getMessage(int i);

	public Message getMessage(String name);

	public Result addMessage(Message message);

	public Result deleteMessage(Message message);

	public Result updateMessage(Message message);

}
