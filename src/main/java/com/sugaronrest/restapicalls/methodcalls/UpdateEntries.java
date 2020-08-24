/**
 MIT License

 Copyright (c) 2017 Kola Oyewumi

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package com.sugaronrest.restapicalls.methodcalls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sugaronrest.ErrorResponse;
import com.sugaronrest.restapicalls.responses.UpdateEntriesResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class UpdateEntries {

    /**
     * Updates entries [SugarCRM REST method - set_entries].
     *
     *  @param url REST API Url.
     *  @param sessionId Session identifier.
     *  @param moduleName SugarCRM module name.
     *  @param entities The entity objects collection to create.
     *  @param selectFields Selected field list.
     *  @return UpdateEntriesResponse object.
     */
    public static UpdateEntriesResponse run(String url, String sessionId, String moduleName, List<Object> entities, List<String> selectFields)  {

        UpdateEntriesResponse updateEntriesResponse = null;
        ErrorResponse errorResponse = null;

        String jsonRequest = new String();
        String jsonResponse = new String();

//        ObjectMapper mapper = JsonObjectMapper.getMapper();
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> requestData = new LinkedHashMap<String, Object>();
            requestData.put("session", sessionId);
            requestData.put("module_name", moduleName);
            requestData.put("name_value_list", EntitiesToNameValueList(entities, selectFields));

            String jsonRequestData = mapper.writeValueAsString(requestData);

            Form formData = new Form();
            formData.param("method", "set_entries");
            formData.param("input_type", "json");
            formData.param("response_type", "json");
            formData.param("rest_data", requestData.toString());

            jsonRequest = mapper.writeValueAsString(requestData);

            formData.param("rest_data", jsonRequestData);

            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(url);
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.form(formData));

            if (response == null) {
                updateEntriesResponse = new UpdateEntriesResponse();
                errorResponse = ErrorResponse.format("An error has occurred!", "No data returned.");
                updateEntriesResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                updateEntriesResponse.setError(errorResponse);
            } else {

            	jsonResponse = response.readEntity(String.class);

                if (StringUtils.isNotBlank(jsonResponse)) {
                    // First check if we have an error
                    errorResponse = ErrorResponse.fromJson(jsonResponse);
                    if (errorResponse == null) {
                        updateEntriesResponse = mapper.readValue(jsonResponse, UpdateEntriesResponse.class);
                    }
                }

                if (updateEntriesResponse == null) {
                    updateEntriesResponse = new UpdateEntriesResponse();
                    updateEntriesResponse.setError(errorResponse);

                    updateEntriesResponse.setStatusCode(HttpStatus.SC_OK);
                    if (errorResponse != null) {
                        updateEntriesResponse.setStatusCode(errorResponse.getStatusCode());
                    }
                } else {
                    updateEntriesResponse.setStatusCode(HttpStatus.SC_OK);
                }
            }
            client.close();
        }
        catch (Exception exception) {
            updateEntriesResponse = new UpdateEntriesResponse();
            errorResponse = ErrorResponse.format(exception, exception.getMessage());
            updateEntriesResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            errorResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            updateEntriesResponse.setError(errorResponse);
        }

        updateEntriesResponse.setJsonRawRequest(jsonRequest);
        updateEntriesResponse.setJsonRawResponse(jsonResponse);

        return updateEntriesResponse;
    }

    /**
     * Formats and return selected fields.
     *
     * @param entities List of Java objects to update.
     * @param selectFields Selected fields.
     * @return Formatted selected fields.
     */
    private static List<Map<String, Object>> EntitiesToNameValueList(List<Object> entities, List<String> selectFields) {
        if (entities == null) {
            return null;
        }

        List<Map<String, Object>> mappedEntities = new ArrayList<Map<String, Object>>();
        boolean useSelectedFields = (selectFields != null) && (selectFields.size() > 0);

        ObjectMapper mapper = new ObjectMapper();

        for (Object entity : entities) {
            Map<String, Object> tempEntity = mapper.convertValue(entity, Map.class);
            Map<String, Object> mappedEntity = new HashMap<String, Object>();
            for (Map.Entry<String, Object> mapEntry : tempEntity.entrySet()) {

                String key = mapEntry.getKey();

                // The identifier must always be added.
                if (!key.equalsIgnoreCase("id")) {
                    if (useSelectedFields) {
                        if (!selectFields.contains(key)) {
                            continue;
                        }
                    }
                }

                Map<String, Object> namevalueDic = new HashMap<String, Object>();
                namevalueDic.put("name", key);
                namevalueDic.put("value", mapEntry.getValue());

                mappedEntity.put(key, namevalueDic);
            }

            mappedEntities.add(mappedEntity);
        }

        return mappedEntities;
    }
}
