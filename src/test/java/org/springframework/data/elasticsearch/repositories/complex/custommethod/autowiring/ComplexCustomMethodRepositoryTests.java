/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.elasticsearch.repositories.complex.custommethod.autowiring;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.elasticsearch.annotations.FieldType.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.junit.jupiter.ElasticsearchRestTemplateConfiguration;
import org.springframework.data.elasticsearch.junit.jupiter.SpringIntegrationTest;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.utils.IndexInitializer;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Artur Konczak
 * @author Mohsin Husen
 * @author Peter-Josef Meisch
 */
@SpringIntegrationTest
@ContextConfiguration(classes = { ComplexCustomMethodRepositoryTests.Config.class })
public class ComplexCustomMethodRepositoryTests {

	@Configuration
	@Import({ ElasticsearchRestTemplateConfiguration.class })
	@EnableElasticsearchRepositories(considerNestedRepositories = true)
	static class Config {}

	@Autowired private ComplexElasticsearchRepository complexRepository;

	@Autowired ElasticsearchOperations operations;

	private IndexOperations indexOperations;

	@BeforeEach
	public void before() {
		indexOperations = operations.indexOps(SampleEntity.class);
		IndexInitializer.init(indexOperations);
	}

	@AfterEach
	void after() {
		indexOperations.delete();
	}

	@Test
	public void shouldExecuteComplexCustomMethod() {

		// given

		// when
		String result = complexRepository.doSomethingSpecial();

		// then
		assertThat(result).isEqualTo("2+2=4");
	}

	@Document(indexName = "test-index-sample-repositories-complex-custommethod-autowiring", replicas = 0,
			refreshInterval = "-1")
	static class SampleEntity {
		@Nullable
		@Id private String id;
		@Nullable @Field(type = Text, store = true, fielddata = true) private String type;
		@Nullable @Field(type = Text, store = true, fielddata = true) private String message;

		@Nullable
		public String getId() {
			return id;
		}

		public void setId(@Nullable String id) {
			this.id = id;
		}

		@Nullable
		public String getType() {
			return type;
		}

		public void setType(@Nullable String type) {
			this.type = type;
		}

		@Nullable
		public String getMessage() {
			return message;
		}

		public void setMessage(@Nullable String message) {
			this.message = message;
		}
	}

}
