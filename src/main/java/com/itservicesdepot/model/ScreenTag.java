/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;
 

public class ScreenTag {
     
    private TagCloudModel model;
     
    public ScreenTag() {
    	init();
    }
    public void init() {
        model = new DefaultTagCloudModel();
        model.addTag(new DefaultTagCloudItem("Transformers", 1));
        model.addTag(new DefaultTagCloudItem("RIA", "#", 1));
        model.addTag(new DefaultTagCloudItem("AJAX", 1));
        model.addTag(new DefaultTagCloudItem("jQuery", "#", 1));
        model.addTag(new DefaultTagCloudItem("NextGen", 1));
        model.addTag(new DefaultTagCloudItem("JSF 2.0", "#", 1));
        model.addTag(new DefaultTagCloudItem("FCB", 1));
        model.addTag(new DefaultTagCloudItem("Mobile",  1));
        model.addTag(new DefaultTagCloudItem("Themes", "#", 1));
        model.addTag(new DefaultTagCloudItem("Rocks", "#", 1));
    }
 
    public TagCloudModel getModel() {
        return model;
    }
}