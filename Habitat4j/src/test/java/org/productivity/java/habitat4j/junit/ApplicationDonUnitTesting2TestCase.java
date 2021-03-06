package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.junit.examples.propertybean.SampleLDAPBean;

/**
 * This class consists of JUnit tests for deploying Application D on Unit Testing Server #2
 * with Habitat4J.
 * 
 * <p>## LICENSE INFORMATION ##</p>
 * 
 * <p>This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.</p>
 *
 * <p>This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 
 * USA</p>
 * 
 * @author Justin Yunke &lt;habitat4j@productivity.org&gt;
 * @version $Id: ApplicationDonUnitTesting2TestCase.java,v 1.8 2004/08/20 11:00:03 cvs Exp $
 */
public class ApplicationDonUnitTesting2TestCase extends Habitat4JBaseTestCase implements Habitat4JConstants {
	private static final String APPLICATION_NAME			= "ApplicationD";
	private static final String SERVER_NAME					= "servers/unit/server-2/";
	
	public static final String[] TEST_LIST = {
		"testServerIdentity",
		"testLDAPonUnitServer2"
	};
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		super.setUp(SERVER_NAME,APPLICATION_NAME);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void doLoadFromResource() throws PropertyListHandlerException {
		String applicationPropertyPackage = DATA_EXAMPLES_RESOURCE_PATH + "apps/" + APPLICATION_NAME;
		
		PropertyListManager.loadPropertyListFromResource(applicationPropertyPackage + "/properties.xml");
		PropertyListManager.loadPropertyListFromResource(applicationPropertyPackage + "/properties-ldap.xml");
		
		logger.info(PropertyListManager.getPropertyList().toString());
		logger.info(PropertyListManager.getPropertyList("ldap").toString());
	}
	
	public void testServerIdentity() {
		try {
			doLoadFromResource();

			assertEquals(APPLICATION_NAME,ServerIdentityManager.getApplicationName());
			
			assertEquals("Unit",ServerIdentityManager.getEnvironment());
			assertEquals("server-unit-2",ServerIdentityManager.getInstanceName());
			assertEquals("Unit Testing Server #2",ServerIdentityManager.getInstanceDescription());
			assertEquals("2",ServerIdentityManager.getInstanceEnumeration());
			
			assertEquals(2,ServerIdentityManager.getRoles().size());
			
			assertNotNull(ServerIdentityManager.getRole("appServer"));		
			assertEquals("J2EE",ServerIdentityManager.getRole("appServer").getType());
			assertEquals("JBOSS",ServerIdentityManager.getRole("appServer").getVendor());
			assertEquals("JBOSS J2EE Server",ServerIdentityManager.getRole("appServer").getProduct());
			assertEquals("3.2",ServerIdentityManager.getRole("appServer").getVersion());
			assertEquals("3",ServerIdentityManager.getRole("appServer").getPatchLevel());

			assertNotNull(ServerIdentityManager.getRole("dbServer"));		
			assertEquals("MySQL",ServerIdentityManager.getRole("dbServer").getType());
			assertEquals("MySQL AB",ServerIdentityManager.getRole("dbServer").getVendor());
			assertEquals("MySQL SQL Server",ServerIdentityManager.getRole("dbServer").getProduct());
			assertEquals("4.0",ServerIdentityManager.getRole("dbServer").getVersion());
			assertEquals("20",ServerIdentityManager.getRole("dbServer").getPatchLevel());

			assertEquals(0,ServerIdentityManager.getPragmaDefinitions().size());
			assertNull(ServerIdentityManager.getPragmaDefinition("bogusPragmaDefinition"));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
		
		logger.info(ServerIdentityManager.toDisplayString());
	}

	public void testLDAPonUnitServer2() {
		try {
			doLoadFromResource();
			
			SampleLDAPBean ldap = (SampleLDAPBean) PropertyListManager.getPropertyBean("ldap","login.ldap.server");

			assertNotNull(ldap);
			assertEquals(ldap.getHost(),"ldap-unit-0.bogus.productivity.org");
			assertEquals(ldap.getPort(),389);
			assertEquals(ldap.getService(),"ldap");
			assertNull(ldap.getDescription());
			assertEquals(ldap.getBaseDN(),"o=habitat4j.org,ou=unit");
			assertEquals(ldap.getAuthUser(),"unitacct");
			assertEquals(ldap.getAuthPass(),"unitpass");

			String test = PropertyListManager.getProperty("ldap","test.context.not.in.server.identity");
			assertTrue(!"true".equals(test));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public ApplicationDonUnitTesting2TestCase(String arg0) {
		super(arg0);
	}
}
