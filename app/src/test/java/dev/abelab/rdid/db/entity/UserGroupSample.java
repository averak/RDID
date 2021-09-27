package dev.abelab.rdid.db.entity;

/**
 * UserGroup Sample Builder
 */
public class UserGroupSample extends AbstractSample {

	public static UserGroupSampleBuilder builder() {
		return new UserGroupSampleBuilder();
	}

	public static class UserGroupSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String name = SAMPLE_STR;
		private String description = SAMPLE_STR;

		public UserGroupSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public UserGroupSampleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public UserGroupSampleBuilder description(String description) {
			this.description = description;
			return this;
		}

		public UserGroup build() {
			return UserGroup.builder() //
				.id(this.id) //
				.name(this.name) //
				.description(this.description) //
				.build();
		}

	}

}
