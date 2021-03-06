package org.gluu.casa.core.ldap;

import com.unboundid.ldap.sdk.persist.FilterUsage;
import com.unboundid.ldap.sdk.persist.LDAPField;
import com.unboundid.ldap.sdk.persist.LDAPObject;

/**
 * This class provides an implementation of an object that can be used to
 * represent gluuPerson objects in the directory.
 * It was generated by the generate-source-from-schema tool provided with the
 * UnboundID LDAP SDK for Java.  It may be customized as desired to better suit
 * your needs.
 */
@LDAPObject(structuralClass="gluuPerson",
        superiorClass="top")
public class PersonPreferences extends BaseLdapPerson {

    // The field used for optional attribute oxPreferredMethod.
    @LDAPField(filterUsage= FilterUsage.ALWAYS_ALLOWED)
    private String[] oxPreferredMethod;


    // The field used for optional attribute oxPreferredMethod.
    @LDAPField
    private String[] oxStrongAuthPolicy;


    // The field used for optional attribute oxPreferredMethod.
    @LDAPField
    private String[] oxTrustedDevicesInfo;

    /**
     * Retrieves the first value for the field associated with the
     * oxPreferredMethod attribute, if present.
     *
     * @return  The first value for the field associated with the
     *          oxPreferredMethod attribute, or
     *          {@code null} if that attribute was not present in the entry or
     *          does not have any values.
     */
    public String getPreferredMethod()
    {
        if ((oxPreferredMethod == null) ||
                (oxPreferredMethod.length == 0))
        {
            return null;
        }
        else
        {
            return oxPreferredMethod[0];
        }
    }

    /**
     * Retrieves the first value for the field associated with the
     * oxStrongAuthPolicy attribute, if present.
     *
     * @return  The first value for the field associated with the
     *          oxStrongAuthPolicy attribute, or
     *          {@code null} if that attribute was not present in the entry or
     *          does not have any values.
     */
    public String getStrongAuthPolicy()
    {
        if ((oxStrongAuthPolicy == null) ||
                (oxStrongAuthPolicy.length == 0))
        {
            return null;
        }
        else
        {
            return oxStrongAuthPolicy[0];
        }
    }

    /**
     * Retrieves the first value for the field associated with the
     * oxTrustedDevicesInfo attribute, if present.
     *
     * @return  The first value for the field associated with the
     *          oxTrustedDevicesInfo attribute, or
     *          {@code null} if that attribute was not present in the entry or
     *          does not have any values.
     */
    public String getTrustedDevicesInfo()
    {
        if ((oxTrustedDevicesInfo == null) ||
                (oxTrustedDevicesInfo.length == 0))
        {
            return null;
        }
        else
        {
            return oxTrustedDevicesInfo[0];
        }
    }

    /**
     * Sets the values for the field associated with the
     * oxPreferredMethod attribute.
     *
     * @param  v  The values for the field associated with the
     *            oxPreferredMethod attribute.
     */
    public void setPreferredMethod(final String... v)
    {
        this.oxPreferredMethod = v;
    }

    /**
     * Sets the values for the field associated with the
     * oxStrongAuthPolicy attribute.
     *
     * @param  v  The values for the field associated with the
     *            oxStrongAuthPolicy attribute.
     */
    public void setStrongAuthPolicy(final String... v)
    {
        this.oxStrongAuthPolicy = v;
    }

    /**
     * Sets the values for the field associated with the
     * oxTrustedDevicesInfo attribute.
     *
     * @param  v  The values for the field associated with the
     *            oxTrustedDevicesInfo attribute.
     */
    public void setTrustedDevices(final String... v)
    {
        this.oxTrustedDevicesInfo = v;
    }

}
