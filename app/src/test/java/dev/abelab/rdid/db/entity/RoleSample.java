package dev.abelab.rdid.db.entity;

/**
 * Role Sample Builder
 */
public class RoleSample extends AbstractSample {

	public static RoleSampleBuilder builder() {
		return new RoleSampleBuilder();
	}

	public static class RoleSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String name = SAMPLE_STR;

		public RoleSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public RoleSampleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public Role build() {
			return Role.builder() //
				.id(this.id) //
				.name(this.name) //
				.build();
		}

	}

}
