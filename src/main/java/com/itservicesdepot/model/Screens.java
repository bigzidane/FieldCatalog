package com.itservicesdepot.model;

import java.util.List;

public class Screens extends BaseModel {
	private static final long serialVersionUID = -6446140089620160690L;
	
	private List<Screen> screens;

	public Screens() {
		
	}
	
	public Screens(List<Screen> screens) {
		this.screens = screens;
	}
	
	public List<Screen> getScreens() {
		return screens;
	}

	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}

}
