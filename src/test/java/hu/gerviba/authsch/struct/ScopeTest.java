package hu.gerviba.authsch.struct;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ScopeTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testByScope() throws Exception {
        assertEquals(Scope.BASIC, Scope.byScope("basic"));
        assertEquals(Scope.DISPLAY_NAME, Scope.byScope("displayName"));
        assertEquals(Scope.SURNAME, Scope.byScope("sn"));
        assertEquals(Scope.GIVEN_NAME, Scope.byScope("givenName"));
        assertEquals(Scope.MAIL, Scope.byScope("mail"));
        assertEquals(Scope.NEPTUN_CODE, Scope.byScope("niifPersonOrgID"));
        assertEquals(Scope.LINKED_ACCOUNTS, Scope.byScope("linkedAccounts"));
        assertEquals(Scope.EDU_PERSON_ENTILEMENT, Scope.byScope("eduPersonEntitlement"));
        assertEquals(Scope.ROOM_NUMBER, Scope.byScope("roomNumber"));
        assertEquals(Scope.MOBILE, Scope.byScope("mobile"));
        assertEquals(Scope.COURSES, Scope.byScope("niifEduPersonAttendedCourse"));
        assertEquals(Scope.ENTRANTS, Scope.byScope("entrants"));
        assertEquals(Scope.ACTIVE_DIRECTORY_MEMBERSHIP, Scope.byScope("admembership"));
        assertEquals(Scope.BME_UNIT_SCOPE, Scope.byScope("bmeunitscope"));
        
        assertEquals(Scope.BASIC, Scope.byScope("unknown"));
        assertEquals(Scope.BASIC, Scope.byScope(null));
    }
    
    @Test
    public void testListFromString() throws Exception {
        assertEquals(Arrays.asList(Scope.BASIC, Scope.GIVEN_NAME, Scope.ACTIVE_DIRECTORY_MEMBERSHIP), 
                Scope.listFromString(" ", "basic givenName admembership"));
    }
    
}
