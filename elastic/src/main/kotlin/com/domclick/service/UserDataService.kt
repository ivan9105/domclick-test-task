package com.domclick.service

import com.domclick.entity.UserData
import com.domclick.repository.UserDataRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.script.Script
import org.elasticsearch.script.ScriptType
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders.nested
import org.elasticsearch.search.aggregations.Aggregations
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class UserDataService(
        private val repository: UserDataRepository,
        private val elasticsearchTemplate: ElasticsearchTemplate
) {

    companion object {
        private val PAINLESS_SCRIPT_LANG = "painless"
    }

    fun save(userData: UserData) = repository.save(userData)

    fun findById(id: String) = repository.findById(id).orElse(null)

    fun findAll() = repository.findAll()

    fun findByFirstName(firstName: String, pageable: Pageable) = repository.findByFirstName(firstName, pageable)

    fun findByTagValue(tagValue: String, pageable: Pageable) = repository.findByTagValue(tagValue, pageable)

    fun findByTagValueAndFirstName(tagValue: String, firstName: String, pageable: Pageable) =
            repository.findByTagValueAndFirstName(tagValue, firstName, pageable)

    fun count() = repository.count()

    fun delete(id: String) {
        val userData = findById(id) ?: return
        return repository.delete(userData)
    }

    fun delete(userData: UserData) = repository.delete(userData)

    fun getAvgBalanceByFirstName(firstName: String): BigDecimal? {
        val avgAccountBalanceSubAggregation = AggregationBuilders
                .avg("avg_accounts_balance")
                .field("accounts.balance")

        val avgBalanceByFirstNameQuery = getAvgAccountBalanceAggregationQuery(
                firstName,
                avgAccountBalanceSubAggregation
        )

        var avg: BigDecimal? = null

        elasticsearchTemplate.query(avgBalanceByFirstNameQuery) {
            val aggregations = it.aggregations ?: return@query
            avg = extractAvgValue(aggregations)
        }

        return avg
    }

    fun getAvgBalanceByFirstNamePainless(firstName: String): BigDecimal? {
        val avgAccountBalanceSubAggregation = AggregationBuilders
                .avg("avg_accounts_balance")
                .script(
                        Script(
                                ScriptType.INLINE,
                                PAINLESS_SCRIPT_LANG,
                                "doc['accounts.balance']",
                                emptyMap()
                        )
                )

        val avgBalanceByFirstNameQuery = getAvgAccountBalanceAggregationQuery(
                firstName,
                avgAccountBalanceSubAggregation
        )

        var avg: BigDecimal? = null

        elasticsearchTemplate.query(avgBalanceByFirstNameQuery) {
            val aggregations = it.aggregations ?: return@query
            avg = extractAvgValue(aggregations)
        }

        return avg
    }

    private fun getAvgAccountBalanceAggregationQuery(
            firstName: String,
            avgAccountBalanceSubAggregation: AvgAggregationBuilder
    ): NativeSearchQuery {
        return NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("firstName", firstName))
                .withSearchType(SearchType.DEFAULT)
                .withIndices("domclick")
                .withTypes("user_data")
                .addAggregation(
                        nested("accounts", "accounts")
                                .subAggregation(avgAccountBalanceSubAggregation)
                ).build()
    }

    private fun extractAvgValue(aggregations: Aggregations): BigDecimal? {
        val nestedAggregation = aggregations.asMap()["accounts"] as InternalNested
        val resultStr = nestedAggregation.aggregations.asMap["avg_accounts_balance"].toString()
        val jsonObject = Gson().fromJson(resultStr, JsonObject::class.java)
        return jsonObject.get("avg_accounts_balance").asJsonObject.get("value").asBigDecimal
    }
}