/**
 * 
 * "THE BEER-WARE LICENSE" (Revision 42):
 * 
 *  <gerviba@gerviba.hu> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return.       Szabó Gergely
 * 
 */
package hu.gerviba.authsch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hu.gerviba.authsch.response.AuthResponse;
import hu.gerviba.authsch.response.ProfileDataResponse;
import hu.gerviba.authsch.response.ProfileDataResponse.ProfileDataResponseBuilder;
import hu.gerviba.authsch.struct.BMEUnitScope;
import hu.gerviba.authsch.struct.Entrant;
import hu.gerviba.authsch.struct.PersonEntitlement;
import hu.gerviba.authsch.struct.Scope;

/**
 * <h1>How to init:</h1>
 * Set client id and key with {@link #setClientIdentifier(String)} and {@link #setClientKey(String)}.
 * Store it in application scope.
 * 
 * @author Gerviba
 * @see {@link #validateAuthentication(String)}
 * @see {@link #refreshToken(String)}
 * @see {@link #getProfile(String)}
 */
public class AuthSchAPI implements Serializable {
    
    private static final long serialVersionUID = 3441712708900902459L;
    
    private String tokenUrlBase = "https://auth.sch.bme.hu/oauth2/token";
    private String loginUrlBase = "https://auth.sch.bme.hu/site/login";
    private String apiUrlBase = "https://auth.sch.bme.hu/api";
    private String clientIdentifier = "testclient";
    private String clientKey = "testpass";
    
    public AuthSchAPI() {}
    
    /**
     * Token endpoint url
     * @return Default: https://auth.sch.bme.hu/oauth2/token
     */
    public String getTokenUrlBase() {
        return tokenUrlBase;
    }

    /**
     * Sets token endpoint url
     * @param tokenUrlBase (default: https://auth.sch.bme.hu/oauth2/token)
     */
    public void setTokenUrlBase(String tokenUrlBase) {
        this.tokenUrlBase = tokenUrlBase;
    }
    
    /**
     * Login base url
     * @return Default: https://auth.sch.bme.hu/site/login
     */
    public String getLoginUrlBase() {
        return loginUrlBase;
    }

    /**
     * Sets login base url
     * @param tokenUrlBase (default: https://auth.sch.bme.hu/site/login)
     */
    public void setLoginUrlBase(String loginUrlBase) {
        this.loginUrlBase = loginUrlBase;
    }

    /**
     * API endpoint base url
     * @return Default: https://auth.sch.bme.hu/api
     */
    public String getApiUrlBase() {
        return apiUrlBase;
    }

    /**
     * Sets API endpoint base url
     * @param tokenUrlBase (default: https://auth.sch.bme.hu/api)
     */
    public void setApiUrlBase(String apiUrlBase) {
        this.apiUrlBase = apiUrlBase;
    }

    /**
     * Sets client's identifier
     * @param clientIdentifier about 20 digit numbers
     */
    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    /**
     * Sets client's secret key
     * @param clientKey about 80 chars [a-zA-Z0-9]
     */
    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    /**
     * Validate authentication
     * @param code Received `code` value from HTTPS parameters
     * @throws AuthSchResponseException
     */
    public AuthResponse validateAuthentication(String code) {
        return httpPost("grant_type=authorization_code&code=" + code, false);
    }

    /**
     * Validate authentication
     * @param code Received `code` value from HTTPS parameters
     * @throws AuthSchResponseException
     */
    public AuthResponse refreshToken(String refreshToken) {
        return httpPost("grant_type=refresh_token&refresh_token=" + refreshToken, true);
    }
    
    /**
     * Login URL generator
     * @param uniqueId A unique identifier for the user. Must be hashed! (eg. sha256(JSESSIONID))
     * @param scopes A list of used scopes
     * @return Generated login url
     */
    public String generateLoginUrl(String uniqueId, List<Scope> scopes) {
        return String.format("%s?response_type=code&client_id=%s&state=%s&scope=%s", 
                loginUrlBase, clientIdentifier, uniqueId, Scope.buildForUrl(scopes));
    }

    /**
     * Login URL generator
     * @param uniqueId A unique identifier for the user. Must be hashed! (eg. sha256(JSESSIONID))
     * @param scopes A list of used scopes
     * @return Generated login url
     */
    public String generateLoginUrl(String uniqueId, Scope... scopes) {
        return String.format("%s?response_type=code&client_id=%s&state=%s&scope=%s", 
                loginUrlBase, clientIdentifier, uniqueId, Scope.buildForUrl(scopes));
    }
    
    /**
     * Load profile info
     * @param accessToken
     * @throws AuthSchResponseException
     */
    public ProfileDataResponse getProfile(String accessToken) {
        return httpGet("profile", accessToken);
    }
    
    private ProfileDataResponse httpGet(String service, String accessToken) {
        URL obj = newUrl(apiUrlBase + "/" + service + "/?access_token=" + accessToken);
        HttpsURLConnection con = newGetConnection(obj);
        setGetHeaders(con);
        processResponseCode(con);
        
        return mapProfileDataResponse(readData(con));
    }

    private HttpsURLConnection newGetConnection(URL obj) {
        HttpsURLConnection con;
        try {
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
        } catch (IOException e) {
            throw new AuthSchResponseException("Failed to open connection", e);
        }
        return con;
    }
    
    private void setGetHeaders(HttpsURLConnection con) {
        con.setRequestProperty("User-Agent", System.getProperty("authsch.useragent", "AuthSchJavaAPI"));
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((clientIdentifier + ":" + clientKey).getBytes()));
        
        con.setDoOutput(true);
    }
    
    @SuppressWarnings("deprecation")
    ProfileDataResponse mapProfileDataResponse(String rawJson) {
        JsonParser jsonParser = new JsonParser();
        
        try {
            JsonObject obj = jsonParser.parse(rawJson).getAsJsonObject();
            ProfileDataResponseBuilder response = ProfileDataResponse.newBuilder();
            
            response.setInternalId(UUID.fromString(obj.get("internal_id").getAsString()));
            
            if (obj.get(Scope.DISPLAY_NAME.getScope()) != null 
                    && !obj.get(Scope.DISPLAY_NAME.getScope()).isJsonNull())
                response.setDisplayName(obj.get(Scope.DISPLAY_NAME.getScope()).getAsString());
            
            if (obj.get(Scope.SURNAME.getScope()) != null 
                    && !obj.get(Scope.SURNAME.getScope()).isJsonNull())
                response.setSurname(obj.get(Scope.SURNAME.getScope()).getAsString());
            
            if (obj.get(Scope.GIVEN_NAME.getScope()) != null 
                    && !obj.get(Scope.GIVEN_NAME.getScope()).isJsonNull())
                response.setGivenName(obj.get(Scope.GIVEN_NAME.getScope()).getAsString());
            
            if (obj.get(Scope.MAIL.getScope()) != null 
                    && !obj.get(Scope.MAIL.getScope()).isJsonNull())
                response.setMail(obj.get(Scope.MAIL.getScope()).getAsString());
            
            if (obj.get(Scope.LINKED_ACCOUNTS.getScope()) != null 
                    && !obj.get(Scope.LINKED_ACCOUNTS.getScope()).isJsonNull()) {
                
                for (Map.Entry<String, JsonElement> element : obj.get(Scope.LINKED_ACCOUNTS.getScope())
                        .getAsJsonObject().entrySet())
                    response.addLinkedAccount(element.getKey(), element.getValue().getAsString());
            }
            
            if (obj.get(Scope.EDU_PERSON_ENTILEMENT.getScope()) != null 
                    && !obj.get(Scope.EDU_PERSON_ENTILEMENT.getScope()).isJsonNull()) {
                
                for (JsonElement element : obj.get(Scope.EDU_PERSON_ENTILEMENT.getScope()).getAsJsonArray()) {
                    JsonObject entrant = element.getAsJsonObject();
                    response.addEduPersonEntitlement(new PersonEntitlement(
                            entrant.get("id").getAsInt(), 
                            entrant.get("name").getAsString(), 
                            entrant.get("status").getAsString(), 
                            entrant.get("start").getAsString(), 
                            entrant.get("end").isJsonNull() ? null : entrant.get("end").getAsString()));
                }
            }
            
            if (obj.get(Scope.ROOM_NUMBER.getScope()) != null 
                    && !obj.get(Scope.ROOM_NUMBER.getScope()).isJsonNull())
                response.setRoomNumber(obj.get(Scope.ROOM_NUMBER.getScope()).getAsString());
            
            if (obj.get(Scope.MOBILE.getScope()) != null 
                    && !obj.get(Scope.MOBILE.getScope()).isJsonNull())
                response.setMobile(obj.get(Scope.MOBILE.getScope()).getAsString());
            
            if (obj.get(Scope.COURSES.getScope()) != null 
                    && !obj.get(Scope.COURSES.getScope()).isJsonNull()) {
                for (String course : obj.get(Scope.COURSES.getScope()).getAsString().split(";"))
                    response.addCourse(course);
            }
            
            if (obj.get(Scope.ENTRANTS.getScope()) != null
                    && !obj.get(Scope.ENTRANTS.getScope()).isJsonNull()) {
                
                for (JsonElement element : obj.get(Scope.ENTRANTS.getScope()).getAsJsonArray()) {
                    JsonObject entrant = element.getAsJsonObject();
                    response.addEntrant(new Entrant(
                            entrant.get("groupId").getAsInt(), 
                            entrant.get("groupName").getAsString(), 
                            entrant.get("entrantType").getAsString()));
                }
            }
            
            if (obj.get(Scope.ACTIVE_DIRECTORY_MEMBERSHIP.getScope()) != null 
                    && !obj.get(Scope.ACTIVE_DIRECTORY_MEMBERSHIP.getScope()).isJsonNull()) {
                
                for (JsonElement element : obj.get(Scope.ACTIVE_DIRECTORY_MEMBERSHIP.getScope()).getAsJsonArray())
                    response.addADMembership(element.getAsString());
            }
            
            if (obj.get(Scope.BME_UNIT_SCOPE.getScope()) != null 
                    && !obj.get(Scope.BME_UNIT_SCOPE.getScope()).isJsonNull()) {
                
                for (JsonElement element : obj.get(Scope.BME_UNIT_SCOPE.getScope()).getAsJsonArray())
                    response.addBmeUnitScope(BMEUnitScope.valueOf(element.getAsString()));
                
            }
            
            return response.build();
        } catch (NullPointerException e) {
            throw new AuthSchResponseException("Failed to parse auth response", e);
        }
    }
    
    private AuthResponse httpPost(String parameters, boolean reAuth) {
        URL obj = newUrl(tokenUrlBase);
        HttpsURLConnection con = newPostConnection(obj);
        setPostHeaders(con);
        writePostParameters(parameters, con);
        processResponseCode(con);
        String response = readData(con);
        
        return reAuth 
                ? mapAuthResponse(response, parameters) 
                : mapAuthResponse(response);
    }
    
    private URL newUrl(String url) {
        URL obj;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            throw new AuthSchResponseException("Failed to create new URL", e);
        }
        return obj;
    }
    
    private HttpsURLConnection newPostConnection(URL obj) {
        HttpsURLConnection con;
        try {
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
        } catch (IOException e) {
            throw new AuthSchResponseException("Failed to open connection", e);
        }
        return con;
    }
    
    private void setPostHeaders(HttpsURLConnection con) {
        con.setRequestProperty("User-Agent", System.getProperty("authsch.useragent", "AuthSchJavaAPI"));
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder()
        .encodeToString((clientIdentifier + ":" + clientKey).getBytes()));
        
        con.setDoOutput(true);
    }
    

    private void writePostParameters(String parameters, HttpsURLConnection con) {
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(parameters);
            wr.flush();
        } catch (IOException e) {
            throw new AuthSchResponseException("Failed to write post data", e);
        }
    }

    private String readData(HttpsURLConnection con) {
        String inputLine, response = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            while ((inputLine = in.readLine()) != null)
                response += inputLine;
        } catch (IOException e) {
            throw new AuthSchResponseException("Failed to write post data", e);
        }
        return response;
    }

    private int processResponseCode(HttpsURLConnection con) {
        int responseCode = -1;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            throw new AuthSchResponseException("Failed to get response code", e);
        }
        
        if (responseCode != 200)
            throw new AuthSchResponseException("HTTP response code: " + responseCode);
        return responseCode;
    }
    
    AuthResponse mapAuthResponse(String rawJson) {
        JsonParser jsonParser = new JsonParser();
        
        try {
            JsonObject obj = jsonParser.parse(rawJson).getAsJsonObject();
            return new AuthResponse(
                    obj.get("access_token").getAsString(), 
                    obj.get("expires_in").getAsLong(), 
                    obj.get("token_type").getAsString(), 
                    Scope.listFromString(" ", obj.get("scope").getAsString()),
                    obj.get("refresh_token").getAsString());
        } catch (NullPointerException e) {
            throw new AuthSchResponseException("Failed to parse auth response", e);
        }
    }
    
    AuthResponse mapAuthResponse(String rawJson, String parameters) {
        JsonParser jsonParser = new JsonParser();
        
        try {
            JsonObject obj = jsonParser.parse(rawJson).getAsJsonObject();
            return new AuthResponse(
                    obj.get("access_token").getAsString(), 
                    obj.get("expires_in").getAsLong(), 
                    obj.get("token_type").getAsString(), 
                    Scope.listFromString(" ", obj.get("scope").getAsString()),
                    parameters.substring(parameters.lastIndexOf('=') + 1));
        } catch (NullPointerException e) {
            throw new AuthSchResponseException("Failed to parse auth response", e);
        }
    }
    
}

