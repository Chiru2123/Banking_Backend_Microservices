package com.banking.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customer_customers")
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer age;
    private String gender;
    private String panCardNum;
    private String aadharCardNum;
    private String image;
    private String kycStatus;
    private String kycDocs;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPanCardNum() {
		return panCardNum;
	}
	public void setPanCardNum(String panCardNum) {
		this.panCardNum = panCardNum;
	}
	public String getAadharCardNum() {
		return aadharCardNum;
	}
	public void setAadharCardNum(String aadharCardNum) {
		this.aadharCardNum = aadharCardNum;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getKycStatus() {
		return kycStatus;
	}
	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}
	public String getKycDocs() {
		return kycDocs;
	}
	public void setKycDocs(String kycDocs) {
		this.kycDocs = kycDocs;
	}
    
    
}