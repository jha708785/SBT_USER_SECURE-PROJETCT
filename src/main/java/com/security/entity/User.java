package com.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class User {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(length = 30)
	    private String firstName;

	    @Column(length = 30)
	    private String lastName;

	    @Column(length = 100)
	    private String email;

	    private String password;

	    @Transient
	    private String confirmPassword;

	   
	    private String role;
	    
	    private String vcode;

	    public String getVcode() {
			return vcode;
		}

		public void setVcode(String vcode) {
			this.vcode = vcode;
		}

		/**
	     * the user by default is not enable, until he activates his account.
	     */
	    @Column(name = "enabled")
	    private boolean enabled; // by default is false, until the user activates his account via email verification.

	    private boolean accountNonLocked; // by default is true, until the user is blocked by the admin.

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}

		public String getRole() {
			return role;
		}

		

		public void setRole(String role) {
			this.role = role;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isAccountNonLocked() {
			return accountNonLocked;
		}

		public void setAccountNonLocked(boolean accountNonLocked) {
			this.accountNonLocked = accountNonLocked;
		}

	   /* @OneToMany(mappedBy = "user")
	    private List<Token> tokens;*/

}
