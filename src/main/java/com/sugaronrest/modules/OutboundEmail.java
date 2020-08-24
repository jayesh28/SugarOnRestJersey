/**
* <auto-generated />
* This file was generated by a StringTemplate 4 template.
* Don't change it directly as your change would get overwritten. Instead, make changes
* to the .stg file (i.e. the StringTemplate 4 template file) and save it to regenerate this file.
*
* For more infor on StringTemplate 4 template please go to -
* https://github.com/antlr/antlrcs
*
* @author  Kola Oyewumi
* @version 1.0.0
* @since   2017-01-03
*
* A class which represents the outbound_email table.
*/

package com.sugaronrest.modules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sugaronrest.restapicalls.Module;
import com.sugaronrest.restapicalls.CustomDateDeserializer;
import com.sugaronrest.restapicalls.CustomDateSerializer;


@Module(name = "", tablename = "outbound_email")
@JsonRootName(value = "outbound_email")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboundEmail {
    public String getId() {
        return id;
    }

    public void setId(String value) {
        id = value;
    }
    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }
    public String getType() {
        return type;
    }

    public void setType(String value) {
        type = value;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        userId = value;
    }
    public String getMailSendtype() {
        return mailSendtype;
    }

    public void setMailSendtype(String value) {
        mailSendtype = value;
    }
    public String getMailSmtptype() {
        return mailSmtptype;
    }

    public void setMailSmtptype(String value) {
        mailSmtptype = value;
    }
    public String getMailSmtpserver() {
        return mailSmtpserver;
    }

    public void setMailSmtpserver(String value) {
        mailSmtpserver = value;
    }
    public Integer getMailSmtpport() {
        return mailSmtpport;
    }

    public void setMailSmtpport(Integer value) {
        mailSmtpport = value;
    }
    public String getMailSmtpuser() {
        return mailSmtpuser;
    }

    public void setMailSmtpuser(String value) {
        mailSmtpuser = value;
    }
    public String getMailSmtppass() {
        return mailSmtppass;
    }

    public void setMailSmtppass(String value) {
        mailSmtppass = value;
    }
    public Integer getMailSmtpauthReq() {
        return mailSmtpauthReq;
    }

    public void setMailSmtpauthReq(Integer value) {
        mailSmtpauthReq = value;
    }
    public Integer getMailSmtpssl() {
        return mailSmtpssl;
    }

    public void setMailSmtpssl(Integer value) {
        mailSmtpssl = value;
    }

    @JsonProperty("id")
    private String id;
  
    @JsonProperty("name")
    private String name;
  
    @JsonProperty("type")
    private String type;
  
    @JsonProperty("user_id")
    private String userId;
  
    @JsonProperty("mail_sendtype")
    private String mailSendtype;
  
    @JsonProperty("mail_smtptype")
    private String mailSmtptype;
  
    @JsonProperty("mail_smtpserver")
    private String mailSmtpserver;
  
    @JsonProperty("mail_smtpport")
    private Integer mailSmtpport;
  
    @JsonProperty("mail_smtpuser")
    private String mailSmtpuser;
  
    @JsonProperty("mail_smtppass")
    private String mailSmtppass;
  
    @JsonProperty("mail_smtpauth_req")
    private Integer mailSmtpauthReq;
  
    @JsonProperty("mail_smtpssl")
    private Integer mailSmtpssl;
  
}
