package dev.abelab.rdid.helper.sample;

import java.util.Date;

import dev.abelab.rdid.db.entity.User;

/**
 * User Sample Builder
 */
public class UserSample extends AbstractSample {

	public static UserSampleBuilder builder() {
		return new UserSampleBuilder();
	}

	public static class UserSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String firstName = SAMPLE_STR;
		private String lastName = SAMPLE_STR;
		private String email = SAMPLE_STR;
		private String password = SAMPLE_STR;
		private Integer admissionYear = SAMPLE_INT;
		private Date createdAt = SAMPLE_DATE;
		private Date updatedAt = SAMPLE_DATE;

		public UserSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public UserSampleBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserSampleBuilder lastName(String lastName) {
			this.firstName = lastName;
			return this;
		}

		public UserSampleBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserSampleBuilder admissionYear(Integer admissionYear) {
			this.admissionYear = admissionYear;
			return this;
		}

		public UserSampleBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserSampleBuilder createdAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public UserSampleBuilder updatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public User build() {
			return User.builder() //
				.id(this.id) //
				.firstName(this.firstName) //
				.lastName(this.lastName) //
				.email(this.email) //
				.password(this.password) //
				.admissionYear(this.admissionYear) //
				.createdAt(this.createdAt) //
				.updatedAt(this.updatedAt) //
				.build();
		}

	}

}
