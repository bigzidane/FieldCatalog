/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="PRODUCT_GROUP")
@AssociationOverrides({
	@AssociationOverride(name = "pk.product", 
		joinColumns = @JoinColumn(name = "PRODUCT_ID")),
	@AssociationOverride(name = "pk.group", 
		joinColumns = @JoinColumn(name = "GROUP_ID")) })
public class ProductGroup extends BaseModel {
	private static final long serialVersionUID = -8027086134333827335L;

	@EmbeddedId
	private ProductGroupPK pk = new ProductGroupPK();
	
	@Column(name="Level")
	private String level;
	
	@Transient
	public Product getProduct() {
		return getPk().getProduct();
	}
 
	public void setProduct(Product product) {
		getPk().setProduct(product);
	}
 
	@Transient
	public Group getGroup() {
		return getPk().getGroup();
	}
 
	public void setGroup(Group category) {
		getPk().setGroup(category);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		ProductGroup that = (ProductGroup) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null)
			return false;
 
		return true;
	}
 
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public ProductGroupPK getPk() {
		return pk;
	}
	public void setPk(ProductGroupPK pk) {
		this.pk = pk;
	}
}