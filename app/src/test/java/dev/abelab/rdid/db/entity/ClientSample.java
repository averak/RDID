package dev.abelab.rdid.db.entity;

/**
 * Client Sample Builder
 */
public class ClientSample extends AbstractSample {

	public static ClientSampleBuilder builder() {
		return new ClientSampleBuilder();
	}

	public static class ClientSampleBuilder {

		private String id = SAMPLE_STR;
		private String name = SAMPLE_STR;

		public ClientSampleBuilder id(String id) {
			this.id = id;
			return this;
		}

		public ClientSampleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public Client build() {
			return Client.builder() //
				.id(this.id) //
				.name(this.name) //
				.build();
		}

	}

}
