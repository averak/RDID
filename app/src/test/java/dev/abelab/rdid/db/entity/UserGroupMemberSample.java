package dev.abelab.rdid.db.entity;

/**
 * UserGroupMember Sample Builder
 */
public class UserGroupMemberSample extends AbstractSample {

	public static UserGroupMemberSampleBuilder builder() {
		return new UserGroupMemberSampleBuilder();
	}

	public static class UserGroupMemberSampleBuilder {

		private Integer userId = SAMPLE_INT;
		private Integer groupId = SAMPLE_INT;

		public UserGroupMemberSampleBuilder userId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public UserGroupMemberSampleBuilder groupId(Integer groupId) {
			this.groupId = groupId;
			return this;
		}

		public UserGroupMember build() {
			return UserGroupMember.builder() //
				.userId(this.userId) //
				.groupId(this.groupId) //
				.build();
		}

	}

}
