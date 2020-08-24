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
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
import com.sugaronrest.ErrorResponse;
import com.sugaronrest.restapicalls.responses.ReadAvailableModulesResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

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


public class GetAvailableModules {

    /**
     *  Gets available module names [SugarCRM REST method - get_available_modules].
     *
     * @param url REST API Url.
     * @param sessionId Session identifier.
     * @return ReadAvailableModulesResponse object.
     * @throws Exception
     */
    public static ReadAvailableModulesResponse run(String url, String sessionId) throws Exception {

        ReadAvailableModulesResponse readAvailableModulesResponse = new ReadAvailableModulesResponse();
        ErrorResponse errorResponse = null;

        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = new String();
        String jsonResponse = new String();

        try {
            Map<String, Object> requestData = new LinkedHashMap<String, Object>();
            requestData.put("session", sessionId);
            requestData.put("filter", "all");

            String jsonRequestData = mapper.writeValueAsString(requestData);

            Form formData = new Form();
            formData.param("method", "get_available_modules");
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
                readAvailableModulesResponse = new ReadAvailableModulesResponse();
                errorResponse = ErrorResponse.format("An error has occurred!", "No data returned.");
                readAvailableModulesResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                readAvailableModulesResponse.setError(errorResponse);
            } else {

            	jsonResponse = response.readEntity(String.class);

                if (StringUtils.isNotBlank(jsonResponse)) {
                    // First check if we have an error
                    errorResponse = ErrorResponse.fromJson(jsonResponse);
                    if (errorResponse == null) {
                        readAvailableModulesResponse = mapper.readValue(jsonResponse, ReadAvailableModulesResponse.class);
                    }
                }

                if (readAvailableModulesResponse == null) {
                    readAvailableModulesResponse = new ReadAvailableModulesResponse();
                    readAvailableModulesResponse.setError(errorResponse);

                    readAvailableModulesResponse.setStatusCode(HttpStatus.SC_OK);
                    if (errorResponse != null) {
                        readAvailableModulesResponse.setStatusCode(errorResponse.getStatusCode());
                    }
                } else {
                    readAvailableModulesResponse.setStatusCode(HttpStatus.SC_OK);
                }
            }
            client.close();
        }
        catch (Exception exception) {
            readAvailableModulesResponse = new ReadAvailableModulesResponse();
            errorResponse = ErrorResponse.format(exception, exception.getMessage());
            readAvailableModulesResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            errorResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            readAvailableModulesResponse.setError(errorResponse);
        }

        readAvailableModulesResponse.setJsonRawRequest(jsonRequest);
        readAvailableModulesResponse.setJsonRawResponse(jsonResponse);

        return readAvailableModulesResponse;
    }
}
