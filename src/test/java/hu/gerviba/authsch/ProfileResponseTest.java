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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import hu.gerviba.authsch.response.ProfileDataResponse;
import hu.gerviba.authsch.struct.BMEUnitScope;

public class ProfileResponseTest {
    
    @Test
    public void testProfileResponse() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        ProfileDataResponse profile = api.mapProfileDataResponse("{"     
                + "\"internal_id\":\"00000000-592c-6864-8dd9-000000000000\","
                + "\"displayName\":\"JUnit Bela\","
                + "\"sn\":\"JUnit\","
                + "\"givenName\":\"Bela\","
                + "\"mail\":\"juint@gmail.com\","
                + "\"linkedAccounts\":{\"bme\":\"123456@bme.hu\",\"schacc\":\"junit\",\"vir\":12345,"
                + "\"virUid\":\"JUnit\"},"
                + "\"eduPersonEntitlement\":[{\"id\":123,\"name\":\"Kor\",\"status\":\"tag\","
                + "\"start\":\"2018-01-03\",\"end\":null}],"
                + "\"mobile\":\"+36301234567\","
                + "\"niifEduPersonAttendedCourse\":\"BMEGT42A001;BMEGT52A001;BMEGT70BS2B\","
                + "\"entrants\":[{\"groupId\":123,\"groupName\":\"Kor\",\"entrantType\":\"KB\"}],"
                + "\"admembership\":[\"junit@sch.hu\",\"juint@balu.sch.bme.hu\",\"LinuxTerminal\","
                + "\"CERTSVC_DCOM_ACCESS\",\"Users\"],"
                + "\"bmeunitscope\":[\"BME\",\"BME_ACTIVE\",\"BME_VIK\",\"BME_VIK_ACTIVE\"]}");
        
        System.out.println(profile);
        assertEquals("00000000-592c-6864-8dd9-000000000000", profile.getInternalId().toString());
        assertEquals("JUnit Bela", profile.getDisplayName());
        assertEquals("JUnit", profile.getSurname());
        assertEquals("juint@gmail.com", profile.getMail());
        assertEquals("+36301234567", profile.getMobile());
        
        assertEquals("123456@bme.hu", profile.getLinkedAccounts().get("bme"));
        assertEquals("junit", profile.getLinkedAccounts().get("schacc"));
        assertEquals("12345", profile.getLinkedAccounts().get("vir"));
        assertEquals("JUnit", profile.getLinkedAccounts().get("virUid"));
        
        assertEquals(1, profile.getEduPersonEntitlements().size());
        assertEquals(123, profile.getEduPersonEntitlements().get(0).getId());
        assertEquals("Kor", profile.getEduPersonEntitlements().get(0).getName());
        assertEquals("tag", profile.getEduPersonEntitlements().get(0).getStatus());
        assertEquals("2018-01-03", profile.getEduPersonEntitlements().get(0).getStart());
        assertEquals(null, profile.getEduPersonEntitlements().get(0).getEnd());
        
        assertEquals(1, profile.getEntrants().size());
        assertEquals(123, profile.getEntrants().get(0).getGroupId());
        assertEquals("Kor", profile.getEntrants().get(0).getGroupName());
        assertEquals("KB", profile.getEntrants().get(0).getEntrantType());
        
        assertEquals(Arrays.asList("BMEGT42A001", "BMEGT52A001", "BMEGT70BS2B"), profile.getCourses());
        assertEquals(Arrays.asList("junit@sch.hu", "juint@balu.sch.bme.hu", "LinuxTerminal", 
                "CERTSVC_DCOM_ACCESS", "Users"), profile.getADMemberships());
        assertEquals(Arrays.asList(BMEUnitScope.BME, BMEUnitScope.BME_ACTIVE, BMEUnitScope.BME_VIK, 
                BMEUnitScope.BME_VIK_ACTIVE), profile.getBmeUnitScopes());
    }
    
    @Test
    public void testProfileResponseMore() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        ProfileDataResponse profile = api.mapProfileDataResponse("{"     
                + "\"internal_id\":\"00000000-592c-6864-8dd9-000000000000\","
                + "\"displayName\":\"JUnit Bela\","
                + "\"sn\":\"JUnit\","
                + "\"givenName\":\"Bela\","
                + "\"mail\":\"juint@gmail.com\","
                + "\"linkedAccounts\":{\"bme\":\"123456@bme.hu\",\"schacc\":\"junit\",\"vir\":12345,"
                + "\"virUid\":\"JUnit\"},"
                + "\"eduPersonEntitlement\":[{\"id\":123,\"name\":\"Kor\",\"status\":\"tag\","
                + "\"start\":\"2018-01-03\",\"end\":null},"
                + "{\"id\":791,\"name\":\"Circle\",\"status\":\"vez\","
                + "\"start\":\"2018-07-10\",\"end\":\"2018-07-12\"}],"
                + "\"mobile\":\"+36301234567\","
                + "\"niifEduPersonAttendedCourse\":\"BMEGT42A001;BMEGT52A001;BMEGT70BS2B\","
                + "\"entrants\":[{\"groupId\":123,\"groupName\":\"Kor\",\"entrantType\":\"KB\"},"
                + "{\"groupId\":456,\"groupName\":\"Test\",\"entrantType\":\"KDO\"}],"
                + "\"admembership\":[\"junit@sch.hu\",\"juint@balu.sch.bme.hu\",\"LinuxTerminal\","
                + "\"CERTSVC_DCOM_ACCESS\",\"Users\"],"
                + "\"bmeunitscope\":[\"BME\",\"BME_ACTIVE\",\"BME_VIK\",\"BME_VIK_ACTIVE\"]}");
        
        assertEquals("00000000-592c-6864-8dd9-000000000000", profile.getInternalId().toString());
        assertEquals("JUnit Bela", profile.getDisplayName());
        assertEquals("JUnit", profile.getSurname());
        assertEquals("juint@gmail.com", profile.getMail());
        assertEquals("+36301234567", profile.getMobile());
        
        assertEquals("123456@bme.hu", profile.getLinkedAccounts().get("bme"));
        assertEquals("junit", profile.getLinkedAccounts().get("schacc"));
        assertEquals("12345", profile.getLinkedAccounts().get("vir"));
        assertEquals("JUnit", profile.getLinkedAccounts().get("virUid"));
        
        assertEquals(2, profile.getEduPersonEntitlements().size());
        assertEquals(123, profile.getEduPersonEntitlements().get(0).getId());
        assertEquals("Kor", profile.getEduPersonEntitlements().get(0).getName());
        assertEquals("tag", profile.getEduPersonEntitlements().get(0).getStatus());
        assertEquals("2018-01-03", profile.getEduPersonEntitlements().get(0).getStart());
        assertEquals(null, profile.getEduPersonEntitlements().get(0).getEnd());
        
        assertEquals(791, profile.getEduPersonEntitlements().get(1).getId());
        assertEquals("Circle", profile.getEduPersonEntitlements().get(1).getName());
        assertEquals("vez", profile.getEduPersonEntitlements().get(1).getStatus());
        assertEquals("2018-07-10", profile.getEduPersonEntitlements().get(1).getStart());
        assertEquals("2018-07-12", profile.getEduPersonEntitlements().get(1).getEnd());
        
        assertEquals(2, profile.getEntrants().size());
        assertEquals(123, profile.getEntrants().get(0).getGroupId());
        assertEquals("Kor", profile.getEntrants().get(0).getGroupName());
        assertEquals("KB", profile.getEntrants().get(0).getEntrantType());
        
        assertEquals(456, profile.getEntrants().get(1).getGroupId());
        assertEquals("Test", profile.getEntrants().get(1).getGroupName());
        assertEquals("KDO", profile.getEntrants().get(1).getEntrantType());
        
        assertEquals(Arrays.asList("BMEGT42A001", "BMEGT52A001", "BMEGT70BS2B"), profile.getCourses());
        assertEquals(Arrays.asList("junit@sch.hu", "juint@balu.sch.bme.hu", "LinuxTerminal", 
                "CERTSVC_DCOM_ACCESS", "Users"), profile.getADMemberships());
        assertEquals(Arrays.asList(BMEUnitScope.BME, BMEUnitScope.BME_ACTIVE, BMEUnitScope.BME_VIK, 
                BMEUnitScope.BME_VIK_ACTIVE), profile.getBmeUnitScopes());
    }
    
    @Test
    public void testProfileResponseEmpty() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        ProfileDataResponse profile = api.mapProfileDataResponse("{"     
                + "\"internal_id\":\"00000000-592c-6864-8dd9-000000000000\","
                + "\"displayName\":\"JUnit Bela\","
                + "\"sn\":\"JUnit\","
                + "\"mail\":\"juint@gmail.com\","
                + "\"mobile\":null}");

        assertEquals(null, profile.getGivenName());
        assertEquals(0, profile.getLinkedAccounts().size());
        assertEquals(0, profile.getEduPersonEntitlements().size());
        assertEquals(0, profile.getEntrants().size());
        assertEquals(null, profile.getMobile());
        assertEquals(Arrays.asList(), profile.getCourses());
        assertEquals(Arrays.asList(), profile.getADMemberships());
        assertEquals(Arrays.asList(), profile.getBmeUnitScopes());
    }
    
}
