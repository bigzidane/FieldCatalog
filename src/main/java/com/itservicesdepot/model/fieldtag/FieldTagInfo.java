/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model.fieldtag;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FieldTagInfo {

    @Column(name="TAG_LEFT", nullable=true)
    private double leftTagPosition;

    @Column(name="TAG_TOP", nullable=true)
    private double topTagPosition;

    @Column(name="TAG_WIDTH", nullable=true)
    private double tagWidth;

    @Column(name="TAG_HEIGHT", nullable=true)
    private double tagHeight;

    public double getLeftTagPosition() {
        return leftTagPosition;
    }

    public void setLeftTagPosition(double leftTagPosition) {
        this.leftTagPosition = leftTagPosition;
    }

    public double getTopTagPosition() {
        return topTagPosition;
    }

    public void setTopTagPosition(double topTagPosition) {
        this.topTagPosition = topTagPosition;
    }

    public double getTagWidth() {
        return tagWidth;
    }

    public void setTagWidth(double tagWidth) {
        this.tagWidth = tagWidth;
    }

    public double getTagHeight() {
        return tagHeight;
    }

    public void setTagHeight(double tagHeight) {
        this.tagHeight = tagHeight;
    }

}
