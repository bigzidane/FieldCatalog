package com.itservicesdepot.test.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.service.CustFieldService;
import com.itservicesdepot.service.EmailService;
import com.itservicesdepot.service.EventService;
import com.itservicesdepot.service.GroupService;
import com.itservicesdepot.service.MessageService;
import com.itservicesdepot.service.RoleService;
import com.itservicesdepot.service.TagService;
import com.itservicesdepot.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
@Transactional
public class SystemTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private CustFieldService custFieldService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private TagService tagService;
	
	@Test
	public void main() {
		System.out.println("This is the main test of System");
	}
}