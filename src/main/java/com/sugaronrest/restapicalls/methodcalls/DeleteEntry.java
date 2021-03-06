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
import com.sugaronrest.restapicalls.responses.DeleteEntryResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class DeleteEntry {

    /**
     * Deletes entry [SugarCRM REST method -set_entry (sets deleted to 1].
     *
     *  @param url REST API Url.
     *  @param sessionId Session identifier.
     *  @param moduleName SugarCRM module name.
     *  @param id The entity identifier.
     *  @return DeleteEntryResponse object.
     */
    public static DeleteEntryResponse run(String url, String sessionId, String moduleName, String id)  {

        DeleteEntryResponse deleteEntryResponse = null;
        ErrorResponse errorResponse = null;

        String jsonRequest = new String();
        String jsonResponse = new String();

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> requestData = new LinkedHashMap<String, Object>();
            requestData.put("session", sessionId);
            requestData.put("module_name", moduleName);
            requestData.put("name_value_list", DeleteDataToNameValueList(id));

            String jsonRequestData = mapper.writeValueAsString(requestData);

            Form formData = new Form();
            formData.param("method", "set_entry");
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
                deleteEntryResponse = new DeleteEntryResponse();
                errorResponse = ErrorResponse.format("An error has occurred!", "No data returned.");
                deleteEntryResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                deleteEntryResponse.setError(errorResponse);
            } else {

//                jsonResponse = response.getBody().toString();
            	jsonResponse = response.readEntity(String.class);

                if (StringUtils.isNotBlank(jsonResponse)) {
                    // First check if we have an error
                    errorResponse = ErrorResponse.fromJson(jsonResponse);
                    if (errorResponse == null) {
                        deleteEntryResponse = mapper.readValue(jsonResponse, DeleteEntryResponse.class);
                    }
                }

                if (deleteEntryResponse == null) {
                    deleteEntryResponse = new DeleteEntryResponse();
                    deleteEntryResponse.setError(errorResponse);

                    deleteEntryResponse.setStatusCode(HttpStatus.SC_OK);
                    if (errorResponse != null) {
                        deleteEntryResponse.setStatusCode(errorResponse.getStatusCode());
                    }
                } else {
                    deleteEntryResponse.setStatusCode(HttpStatus.SC_OK);
                }
            }
            client.close();
        }
        catch (Exception exception) {
            deleteEntryResponse = new DeleteEntryResponse();
            errorResponse = ErrorResponse.format(exception, exception.getMessage());
            deleteEntryResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            errorResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            deleteEntryResponse.setError(errorResponse);
        }

        deleteEntryResponse.setJsonRawRequest(jsonRequest);
        deleteEntryResponse.setJsonRawResponse(jsonResponse);

        return deleteEntryResponse;
    }

    /**
     *  Formats request parameter.
     *
     * @param id The identifier of entity to delete.
     * @return
     */
    private static Map<String, Object> DeleteDataToNameValueList(String id) {
        Map<String, Object> namevalueList = new HashMap<String, Object>();

        Map<String, Object> namevalueDic = new HashMap<String, Object>();
        namevalueDic.put("name", "id");
        namevalueDic.put("value", id);
        namevalueList.put("id", namevalueDic);

        namevalueDic = new HashMap<String, Object>();
        namevalueDic.put("name", "deleted");
        namevalueDic.put("value", 1);
        namevalueList.put("name", namevalueDic);

        return namevalueList;
    }
}
