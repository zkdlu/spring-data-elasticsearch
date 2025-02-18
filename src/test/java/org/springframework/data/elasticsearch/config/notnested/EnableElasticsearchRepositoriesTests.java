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
package org.springframework.data.elasticsearch.config.notnested;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.elasticsearch.annotations.FieldType.*;

import java.lang.Double;
import java.lang.Long;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.junit.jupiter.ElasticsearchRestTemplateConfiguration;
import org.springframework.data.elasticsearch.junit.jupiter.SpringIntegrationTest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.utils.IndexInitializer;
import org.springframework.data.repository.Repository;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Rizwan Idrees
 * @author Mohsin Husen
 * @author Kevin Leturc
 * @author Gad Akuka
 * @author Peter-Josef Meisch
 */
@SpringIntegrationTest
@ContextConfiguration(classes = { EnableElasticsearchRepositoriesTests.Config.class })
public class EnableElasticsearchRepositoriesTests implements ApplicationContextAware {

	@Nullable ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.context = applicationContext;
	}

	@Configuration
	@Import({ ElasticsearchRestTemplateConfiguration.class })
	@EnableElasticsearchRepositories
	static class Config {}

	@Autowired ElasticsearchOperations operations;
	private IndexOperations indexOperations;

	@Autowired private SampleElasticsearchRepository repository;

	@Autowired(required = false) private SampleRepository nestedRepository;

	interface SampleRepository extends Repository<SampleEntity, Long> {}

	@BeforeEach
	public void before() {
		indexOperations = operations.indexOps(SampleEntity.class);
		IndexInitializer.init(indexOperations);
	}

	@AfterEach
	void tearDown() {
		operations.indexOps(IndexCoordinates.of("test-index-sample-config-not-nested")).delete();
		operations.indexOps(IndexCoordinates.of("test-index-uuid-keyed-config-not-nested")).delete();
	}

	@Test
	public void bootstrapsRepository() {

		assertThat(repository).isNotNull();
	}

	@Test
	public void shouldScanSelectedPackage() {

		// given

		// when
		String[] beanNamesForType = context.getBeanNamesForType(ElasticsearchRepository.class);

		// then
		assertThat(beanNamesForType).containsExactlyInAnyOrder("sampleElasticsearchRepository",
				"sampleUUIDKeyedElasticsearchRepository");
	}

	@Test
	public void hasNotNestedRepository() {

		assertThat(nestedRepository).isNull();
	}

	@Document(indexName = "test-index-sample-config-not-nested", replicas = 0, refreshInterval = "-1")
	static class SampleEntity {
		@Nullable @Id private String id;
		@Nullable @Field(type = Text, store = true, fielddata = true) private String type;
		@Nullable @Field(type = Text, store = true, fielddata = true) private String message;
		@Nullable private int rate;
		@Nullable @ScriptedField private Double scriptedRate;
		@Nullable private boolean available;
		@Nullable private String highlightedMessage;
		@Nullable private GeoPoint location;
		@Nullable @Version private Long version;

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

		public int getRate() {
			return rate;
		}

		public void setRate(int rate) {
			this.rate = rate;
		}

		@Nullable
		public java.lang.Double getScriptedRate() {
			return scriptedRate;
		}

		public void setScriptedRate(@Nullable java.lang.Double scriptedRate) {
			this.scriptedRate = scriptedRate;
		}

		public boolean isAvailable() {
			return available;
		}

		public void setAvailable(boolean available) {
			this.available = available;
		}

		@Nullable
		public String getHighlightedMessage() {
			return highlightedMessage;
		}

		public void setHighlightedMessage(@Nullable String highlightedMessage) {
			this.highlightedMessage = highlightedMessage;
		}

		@Nullable
		public GeoPoint getLocation() {
			return location;
		}

		public void setLocation(@Nullable GeoPoint location) {
			this.location = location;
		}

		@Nullable
		public java.lang.Long getVersion() {
			return version;
		}

		public void setVersion(@Nullable java.lang.Long version) {
			this.version = version;
		}
	}

	@Document(indexName = "test-index-uuid-keyed-config-not-nested", replicas = 0, refreshInterval = "-1")
	static class SampleEntityUUIDKeyed {
		@Nullable @Id private UUID id;
		@Nullable private String type;
		@Nullable @Field(type = FieldType.Text, fielddata = true) private String message;
		@Nullable private int rate;
		@Nullable @ScriptedField private Long scriptedRate;
		@Nullable private boolean available;
		@Nullable private String highlightedMessage;
		@Nullable private GeoPoint location;
		@Nullable @Version private Long version;

		@Nullable
		public UUID getId() {
			return id;
		}

		public void setId(@Nullable UUID id) {
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

		public int getRate() {
			return rate;
		}

		public void setRate(int rate) {
			this.rate = rate;
		}

		@Nullable
		public java.lang.Long getScriptedRate() {
			return scriptedRate;
		}

		public void setScriptedRate(@Nullable java.lang.Long scriptedRate) {
			this.scriptedRate = scriptedRate;
		}

		public boolean isAvailable() {
			return available;
		}

		public void setAvailable(boolean available) {
			this.available = available;
		}

		@Nullable
		public String getHighlightedMessage() {
			return highlightedMessage;
		}

		public void setHighlightedMessage(@Nullable String highlightedMessage) {
			this.highlightedMessage = highlightedMessage;
		}

		@Nullable
		public GeoPoint getLocation() {
			return location;
		}

		public void setLocation(@Nullable GeoPoint location) {
			this.location = location;
		}

		@Nullable
		public java.lang.Long getVersion() {
			return version;
		}

		public void setVersion(@Nullable java.lang.Long version) {
			this.version = version;
		}
	}
}
