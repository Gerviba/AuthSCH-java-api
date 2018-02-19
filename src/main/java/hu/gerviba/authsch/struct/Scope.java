/**
 * 
 * "THE BEER-WARE LICENSE" (Revision 42):
 * 
 *  <gerviba@gerviba.hu> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return.       Szabó Gergely
 * 
 */
package hu.gerviba.authsch.struct;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Access Scope
 * @author Gerviba
 * @see https://git.sch.bme.hu/kszk/authsch/wikis/api
 */
public enum Scope {
    /**
     * AuthSCH-s azonosító (varchar, maximum 24 karakter). 
     * Belépéskor a kiadásához nem szükséges a felhasználó jóváhagyása.
     */
    BASIC("basic"),
    /**
     * Név
     */
    DISPLAY_NAME("displayName"),
    /**
     * Vezetéknév
     */
    SURNAME("sn"),
    /**
     * Keresztnév
     */
    GIVEN_NAME("givenName"),
    /**
     * E-mail cím
     */
    MAIL("mail"),
    /**
     * Neptun kód (csak abban az esetben, ha a felhasználónak be van kötve a BME címtár 
     * azonosítója is, egyébként null-t ad vissza). Fokozottan védett információ, 
     * ami azt jelenti, hogy alapból nem kérhető le (invalid scope hibával kerül 
     * visszatérésre az ezt tartalmazó engedélykérés), csak indokolt esetben, központi 
     * engedélyezés után használható (ehhez adj fel egy ticketet a support.sch.bme.hu 
     * oldalon, amelyben leírod hogy mihez és miért van rá szükséged.
     */
    NEPTUN_CODE("niifPersonOrgID"),
    /**
     * Kapcsolt accountok, kulcs - érték párokban. Lehetséges kulcsok:
     * <li> bme: szám@bme.hu
     * <li> schacc: schacc username
     * <li> vir: vir id (integer)
     * <li> virUid: vir username
     */
    LINKED_ACCOUNTS("linkedAccounts"),
    /**
     * Körtagságok (itt az adott körnél a status csak egy értéket vehet fel, 
     * mégpedig a körvezető / tag / öregtag közül valamelyiket, ebben a prioritási sorrendben)
     * @see PersonEntilement
     */
    EDU_PERSON_ENTILEMENT("eduPersonEntitlement"),
    /**
     * Felhasználó szobaszáma (ha kollégista, akkor a kollégium neve és a szobaszám található 
     * meg benne, ha nem kollégista, akkor pedig null-t ad vissza). Amennyiben a felhasználó 
     * nem rendelkezik SCH Accounttal, szintén null-t ad eredményül. 
     * @deprecated Határozatlan ideig nem elérhető jogi okokból.
     */
    @Deprecated
    ROOM_NUMBER("roomNumber"),
    /**
     * Mobilszám a VIR-ből
     */
    MOBILE("mobile"),
    /**
     * Az adott félévben hallgatott tárgyak
     */
    COURSES("niifEduPersonAttendedCourse"),
    /**
     * Közösségi belépők a VIR-ről, február és július között az őszi, egyébként (tehát 
     * augusztustól januárig) a tavaszi belépők
     * @see Entrant
     */
    ENTRANTS("entrants"),
    /**
     * Csoporttagságok a KSZK-s Active Directoryban
     */
    ACTIVE_DIRECTORY_MEMBERSHIP("admembership"),
    /**
     * Egyetemi jogviszony, jelenlegi lehetséges értékek: 
     * BME, BME_VIK, BME_VIK_ACTIVE, BME_VIK_NEWBIE
     * @see BMEUnitScope
     */
    BME_UNIT_SCOPE("bmeunitscope");
    
    private final String scope;
    
    private Scope(String scope) {
        this.scope = scope;
    }

    public static Scope byScope(String scope) {
        for (Scope s : values())
            if (s.getScope().equals(scope))
                return s;
        return BASIC;
    }
    
    public String getScope() {
        return scope;
    }
    
    public static String buildForUrl(List<Scope> scopes) {
        return String.join("+", scopes.stream()
                .map(x -> x.scope).collect(Collectors.toList()));
    }
    
    public static String buildForUrl(Scope... scopes) {
        return String.join("+", Arrays.asList(scopes).stream()
                .map(x -> x.scope).collect(Collectors.toList()));
    }

    public static List<Scope> listFromString(String delimiter, String scopes) {
        return Arrays.asList(scopes.split(delimiter)).stream()
                .map(x -> Scope.byScope(x))
                .collect(Collectors.toList());
    }
    
}
