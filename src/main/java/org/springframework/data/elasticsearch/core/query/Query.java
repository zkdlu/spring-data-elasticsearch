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
package org.springframework.data.elasticsearch.core.query;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.lang.Nullable;

/**
 * Query
 *
 * @author Rizwan Idrees
 * @author Mohsin Husen
 * @author Mark Paluch
 * @author Alen Turkovic
 * @author Sascha Woo
 * @author Christoph Strobl
 * @author Farid Azaza
 * @author Peter-Josef Meisch
 * @author Peer Mueller
 */
public interface Query {

	int DEFAULT_PAGE_SIZE = 10;
	Pageable DEFAULT_PAGE = PageRequest.of(0, DEFAULT_PAGE_SIZE);

	/**
	 * Get a {@link Query} that matches all documents in the index.
	 *
	 * @return new instance of {@link Query}.
	 * @since 3.2
	 * @see QueryBuilders#matchAllQuery()
	 */
	static Query findAll() {
		return new StringQuery(QueryBuilders.matchAllQuery().toString());
	}

	/**
	 * restrict result to entries on given page. Corresponds to the 'start' and 'rows' parameter in elasticsearch
	 *
	 * @param pageable
	 * @return
	 */
	<T extends Query> T setPageable(Pageable pageable);

	/**
	 * Get page settings if defined
	 *
	 * @return
	 */
	Pageable getPageable();

	/**
	 * Add {@link org.springframework.data.domain.Sort} to query
	 *
	 * @param sort
	 * @return
	 */
	<T extends Query> T addSort(Sort sort);

	/**
	 * @return null if not set
	 */
	@Nullable
	Sort getSort();

	/**
	 * Add fields to be added as part of search request
	 *
	 * @param fields
	 */
	void addFields(String... fields);

	/**
	 * Get fields to be returned as part of search request
	 *
	 * @return
	 */
	List<String> getFields();

	/**
	 * Add source filter to be added as part of search request
	 *
	 * @param sourceFilter
	 */
	void addSourceFilter(SourceFilter sourceFilter);

	/**
	 * Get SourceFilter to be returned to get include and exclude source fields as part of search request.
	 *
	 * @return SourceFilter
	 */
	@Nullable
	SourceFilter getSourceFilter();

	/**
	 * Get minimum score
	 *
	 * @return
	 */
	float getMinScore();

	/**
	 * Get if scores will be computed and tracked, regardless of whether sorting on a field. Defaults to <tt>false</tt>.
	 *
	 * @return
	 * @since 3.1
	 */
	boolean getTrackScores();

	/**
	 * Get Ids
	 *
	 * @return
	 */
	@Nullable
	Collection<String> getIds();

	/**
	 * Get route
	 *
	 * @return
	 */
	@Nullable
	String getRoute();

	/**
	 * Type of search
	 *
	 * @return
	 */
	SearchType getSearchType();

	/**
	 * Get indices options
	 *
	 * @return null if not set
	 */
	@Nullable
	IndicesOptions getIndicesOptions();

	/**
	 * Get preference
	 *
	 * @return
	 * @since 3.2
	 */
	@Nullable
	String getPreference();

	/**
	 * Add preference filter to be added as part of search request
	 *
	 * @param preference
	 * @since 3.2
	 */
	void setPreference(String preference);

	/**
	 * @return true if the query has a limit on the max number of results.
	 * @since 4.0
	 */
	default boolean isLimiting() {
		return false;
	}

	/**
	 * return the max of results. Must not return null when {@link #isLimiting()} returns true.
	 *
	 * @since 4.0
	 */
	@Nullable
	default Integer getMaxResults() {
		return null;
	}

	/**
	 * Sets the {@link HighlightQuery}.
	 *
	 * @param highlightQuery the query to set
	 * @since 4.0
	 */
	void setHighlightQuery(HighlightQuery highlightQuery);

	/**
	 * @return the optional set {@link HighlightQuery}.
	 * @since 4.0
	 */
	default Optional<HighlightQuery> getHighlightQuery() {
		return Optional.empty();
	}

	/**
	 * Sets the flag whether to set the Track_total_hits parameter on queries {@see <a href=
	 * "https://www.elastic.co/guide/en/elasticsearch/reference/7.0/search-request-track-total-hits.html">Elasticseacrh
	 * documentation</>}
	 *
	 * @param trackTotalHits the value to set.
	 * @since 4.0
	 */
	void setTrackTotalHits(@Nullable Boolean trackTotalHits);

	/**
	 * Sets the flag whether to set the Track_total_hits parameter on queries {@see <a href=
	 * "https://www.elastic.co/guide/en/elasticsearch/reference/7.0/search-request-track-total-hits.html">Elasticseacrh
	 * documentation</>}
	 *
	 * @return the set value.
	 * @since 4.0
	 */
	@Nullable
	Boolean getTrackTotalHits();

	/**
	 * Sets the maximum value up to which total hits are tracked. Only relevant if #getTrackTotalHits is {@literal null}
	 *
	 * @param trackTotalHitsUpTo max limit for trackTotalHits
	 * @since 4.1
	 */
	void setTrackTotalHitsUpTo(@Nullable Integer trackTotalHitsUpTo);

	/**
	 * Gets the maximum value up to which total hits are tracked. Only relevant if #getTrackTotalHits is {@literal null}
	 *
	 * @return max limit for trackTotalHits
	 * @since 4.1
	 */
	@Nullable
	Integer getTrackTotalHitsUpTo();

	/**
	 * For queries that are used in delete request, these are internally handled by Elasticsearch as scroll/bulk delete
	 * queries. Must not return {@literal null} when {@link #hasScrollTime()} returns {@literal true}.
	 *
	 * @return the scrolltime settings
	 * @since 4.0
	 */
	@Nullable
	Duration getScrollTime();

	/**
	 * For queries that are used in delete request, these are internally handled by Elasticsearch as scroll/bulk delete
	 * queries.
	 *
	 * @param scrollTime the scrolltime settings
	 * @since 4.0
	 */
	void setScrollTime(@Nullable Duration scrollTime);

	/**
	 * @return {@literal true} if scrollTimeMillis is set.
	 * @since 4.0
	 */
	default boolean hasScrollTime() {
		return getScrollTime() != null;
	}

	/**
	 * Get the query timeout.
	 *
	 * @return null if not set
	 * @since 4.2
	 */
	@Nullable
	TimeValue getTimeout();

	/**
	 * @return {@literal true} when the query has the explain parameter set, defaults to {@literal false}
	 * @since 4.2
	 */
	default boolean getExplain() {
		return false;
	}

	/**
	 * Sets the setSearchAfter objects for this query.
	 *
	 * @param searchAfter the setSearchAfter objects. These are obtained with {@link SearchHit#getSortValues()} from a
	 *          search result.
	 * @since 4.2
	 */
	void setSearchAfter(@Nullable List<Object> searchAfter);

	/**
	 * @return the search_after objects.
	 * @since 4.2
	 */
	@Nullable
	List<Object> getSearchAfter();

	/**
	 * Adds a {@link RescorerQuery}.
	 *
	 * @param rescorerQuery the query to add to the list of rescorer queries, must not be {@literal null}
	 * @since 4.2
	 */
	void addRescorerQuery(RescorerQuery rescorerQuery);

	/**
	 * Sets the {@link RescorerQuery}.
	 *
	 * @param rescorerQueryList list of rescorer queries set, must not be {@literal null}.
	 * @since 4.2
	 */
	void setRescorerQueries(List<RescorerQuery> rescorerQueryList);

	/**
	 * get the list of {@link RescorerQuery}s
	 *
	 * @since 4.2
	 */
	default List<RescorerQuery> getRescorerQueries() {
		return Collections.emptyList();
	}
}
