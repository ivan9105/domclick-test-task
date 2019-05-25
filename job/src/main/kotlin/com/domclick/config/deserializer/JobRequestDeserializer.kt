package com.domclick.config.deserializer

import com.domclick.controller.dto.JobRequest
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode

class JobRequestDeserializer : StdDeserializer<JobRequest>(JobRequest::class.java) {

    companion object {
        private const val PARAMS_FIELD_NAME = "params"
        private const val TYPE_FIELD_NAME = "type"
        private const val INTERVAL_IN_SECONDS_FIELD_NAME = "intervalInSeconds"
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): JobRequest {
        val tree = (parser.codec as ObjectMapper).readTree<TreeNode>(parser)
        val params = tree[PARAMS_FIELD_NAME]
        val fieldNamesIterator = params.fieldNames()
        val paramsMap = mutableMapOf<String, Any>()

        while (fieldNamesIterator.hasNext()) {
            val paramName = fieldNamesIterator.next()
            val paramValue = getStringValue(params, paramName)
            paramsMap[paramName] = paramValue
        }

        return JobRequest(
                type = getStringValue(tree, TYPE_FIELD_NAME),
                intervalInSeconds = (tree[INTERVAL_IN_SECONDS_FIELD_NAME] as IntNode).intValue(),
                params = paramsMap
        )
    }

    private fun getStringValue(params: TreeNode, paramName: String) =
            (params[paramName] as TextNode).textValue()
}