package dev.abelab.rdid.db.entity;

/**
 * UserGroupRole Sample Builder
 */
public class UserGroupRoleSample extends AbstractSample {

	public static UserGroupRoleSampleBuilder builder() {
		return new UserGroupRoleSampleBuilder();
	}

	public static class UserGroupRoleSampleBuilder {

		private Integer groupId = SAMPLE_INT;
		private Integer clientId = SAMPLE_INT;
		private Integer roleId = SAMPLE_INT;

		public UserGroupRoleSampleBuilder groupId(Integer groupId) {
			this.groupId = groupId;
			return this;
		}

		public UserGroupRoleSampleBuilder clientId(Integer clientId) {
			this.clientId = clientId;
			return this;
		}

		public UserGroupRoleSampleBuilder roleId(Integer roleId) {
			this.roleId = roleId;
			return this;
		}

		public UserGroupRole build() {
			return UserGroupRole.builder() //
				.groupId(this.groupId) //
				.clientId(this.clientId) //
				.roleId(this.roleId) //
				.build();
		}

	}

}
