package hu.gerviba.authsch;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import hu.gerviba.authsch.struct.Scope;

public class AuthSchAPITest {

    @Test
    public void testUrlGenerationAll() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        api.setClientIdentifier("cid");
        assertEquals("https://auth.sch.bme.hu/site/login?response_type=code"
                + "&client_id=cid&state=unique&scope=basic+displayName+sn"
                + "+givenName+mail+niifPersonOrgID+linkedAccounts+eduPersonEntitlement"
                + "+roomNumber+mobile+niifEduPersonAttendedCourse+entrants+admembership"
                + "+bmeunitscope", api.generateLoginUrl("unique", Scope.values()));
    }

    @Test
    public void testUrlGenerationSpecified() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        assertEquals("https://auth.sch.bme.hu/site/login?response_type=code&client_id=testclient"
                + "&state=unique&scope=basic+givenName+mail+bmeunitscope", 
                api.generateLoginUrl("unique", Scope.BASIC, Scope.GIVEN_NAME, Scope.MAIL, Scope.BME_UNIT_SCOPE));
    }
    
    @Test
    public void testUrlGenerationFormList() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        List<Scope> scopes = Arrays.asList(Scope.BASIC, Scope.GIVEN_NAME, Scope.MAIL, Scope.COURSES);
        assertEquals("https://auth.sch.bme.hu/site/login?response_type=code&client_id=testclient"
                + "&state=unique&scope=basic+givenName+mail+niifEduPersonAttendedCourse", 
                api.generateLoginUrl("unique", scopes));
    }
    
    @Test
    public void testSettersGetters() throws Exception {
        AuthSchAPI api = new AuthSchAPI();
        api.setApiUrlBase("a");
        assertEquals("a", api.getApiUrlBase());
        api.setLoginUrlBase("b");
        assertEquals("b", api.getLoginUrlBase());
        api.setTokenUrlBase("c");
        assertEquals("c", api.getTokenUrlBase());
    }
    
}
