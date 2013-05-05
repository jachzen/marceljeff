/*
 * The MtGox-Java API is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The MtGox-Java API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with the MtGox-Java API .  If not, see <http://www.gnu.org/licenses/>.
 */
package to.sparks.mtgox.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;
import to.sparks.mtgox.model.OrderCancelResult;
import to.sparks.mtgox.net.HTTPAuthenticator;

/**
 * A simple implementation of a client for the MtGox HTTP API version 0.
 *
 * @author SparksG
 * @deprecated This class only exists because the Version 1 API does not contain
 * all the functions we need. It will be removed as these functions become
 * available on later releases of the MtGox HTTP API.
 */
@Deprecated
class HTTPClientV0Service extends HTTPAuthenticator {

    private JsonFactory factory = new JsonFactory();
    private ObjectMapper mapper = new ObjectMapper();

    public HTTPClientV0Service(final Logger logger, String apiKey, String secret) {
        super(logger, apiKey, secret);
    }

    public OrderCancelResult cancelOrder(HashMap<String, String> params) throws Exception {
        InputStream is = getMtGoxHTTPInputStream(UrlFactory.getUrlForCommand("", UrlFactory.Command.PrivateOrderCancel), params);
        JsonParser jp = factory.createJsonParser(is);
        JavaType topMost = mapper.getTypeFactory().constructType(OrderCancelResult.class);
        return mapper.readValue(jp, topMost);
    }
}
