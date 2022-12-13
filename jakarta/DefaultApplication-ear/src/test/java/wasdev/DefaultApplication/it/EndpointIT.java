/*******************************************************************************
 * (c) Copyright IBM Corporation 2019, 2022.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package wasdev.DefaultApplication.it;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class EndpointIT {

    public void testServlet(String url, int expectedStatus, String expectedResponse) throws Exception {
        HttpClient client = new HttpClient();

        GetMethod method = new GetMethod(url);

        try {
            int statusCode = client.executeMethod(method);

            assertEquals("Unexpected HTTP status code ", expectedStatus, statusCode);

            if (expectedResponse != null) {
                String response = method.getResponseBodyAsString(3000);
                assertTrue("Unexpected response body", response.contains(expectedResponse));
            }
        } finally {
            method.releaseConnection();
        }
    }

    @Test
    public void testRoot() throws Exception {
      testServlet("http://localhost:9080", HttpStatus.SC_OK, "Default Application");
    }


    @Test
    public void testHitCount() throws Exception {
      testServlet("http://localhost:9080/hitcount", HttpStatus.SC_OK, "Hit Count Demonstration");
    }

    @Test
    public void testSnoopRequiresAuthentication() throws Exception {
      testServlet("http://localhost:9080/snoop", 401, null);
    }
}
