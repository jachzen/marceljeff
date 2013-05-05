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

import io.socket.SocketIO;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import to.sparks.mtgox.MtGoxWebsocketClient;
import to.sparks.mtgox.event.DepthEvent;
import to.sparks.mtgox.event.PacketEvent;
import to.sparks.mtgox.event.TickerEvent;
import to.sparks.mtgox.event.TradeEvent;
import to.sparks.mtgox.model.CurrencyInfo;
import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.OpPrivateDepth;
import to.sparks.mtgox.model.OpPrivateTicker;
import to.sparks.mtgox.model.OpPrivateTrade;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.model.Trade;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author SparksG
 */
class WebsocketClientService implements Runnable, MtGoxWebsocketClient, ApplicationEventPublisherAware, ApplicationListener<PacketEvent> {

    private final static String SOCKET_IO_SERVER =  "http://socketio.mtgox.com/mtgox";// "http://socketio-beta.mtgox.com/mtgox"; // "http://socketio.mtgox.com/mtgox";
    private ApplicationEventPublisher applicationEventPublisher = null;
    private Logger logger;
//    private BaseWebSocketClient websocket;
    SocketIO socket;
    private SimpleAsyncTaskExecutor taskExecutor;
    private Map<String, CurrencyInfo> currencyCache;
    private HTTPClientV1Service httpAPIV1;
    private SocketListener socketListener;
    // private ReliabilityOptions reliability;

    public WebsocketClientService(Logger logger, SimpleAsyncTaskExecutor taskExecutor, HTTPClientV1Service httpAPIV1, SocketListener socketListener) {
        this(logger, taskExecutor, httpAPIV1, socketListener, true);
    }

    public WebsocketClientService(Logger logger, SimpleAsyncTaskExecutor taskExecutor, HTTPClientV1Service httpAPIV1, SocketListener socketListener, boolean autoRestartSocket) {
        this.logger = logger;
        this.taskExecutor = taskExecutor;
        this.httpAPIV1 = httpAPIV1;
		currencyCache = new HashMap<String, CurrencyInfo>();
        currencyCache.put("BTC", CurrencyInfo.BitcoinCurrencyInfo);
        this.socketListener = socketListener;
        //      reliability = new ReliabilityOptions(autoRestartSocket, 10000L, 30000L, Integer.MAX_VALUE, Integer.MAX_VALUE);
//        websocket = new BaseWebSocketClient(reliability);
    }

    public void init() {
        taskExecutor.execute(this);
    }

    public void destroy() {
        try {
            if (socket != null) {
                socket.disconnect();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            socket = null;
        }
    }

    @Override
    public void shutdown() {
//        taskExecutor.shutdown();
    }

    /*
     * Close and reopen the websocket. Use a Spring scheduler to call this every
     * 15 minutes or so.
     */
    public void recycleWebsocketConnection() {
//        try {
            logger.info("Recycle websocket.");

            destroy();
//
//            socket = new SocketIO(SOCKET_IO_SERVER);
//            socket.connect(socketListener);

            init();
//        } catch (MalformedURLException ex) {
//            logger.log(Level.SEVERE, null, ex);
//        }
    }

    private CurrencyInfo getCachedCurrencyInfo(String currencyCode) {
        CurrencyInfo ci = null;

        if (!currencyCache.containsKey(currencyCode)) {
            try {
                ci = httpAPIV1.getCurrencyInfo(currencyCode);
                currencyCache.put(currencyCode, ci);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        ci = currencyCache.get(currencyCode);
        return ci;
    }

    public void tradeEvent(Trade trade) {
        if (applicationEventPublisher != null) {
            CurrencyInfo ci = getCachedCurrencyInfo(trade.getPrice_currency());
            if (ci != null) {
                trade.setCurrencyInfo(ci);
            }
            TradeEvent event = new TradeEvent(this, trade);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void tickerEvent(Ticker ticker) {
        if (applicationEventPublisher != null) {
            CurrencyInfo ci = getCachedCurrencyInfo(ticker.getCurrencyCode());
            if (ci != null) {
                ticker.setCurrencyInfo(ci);
            }
            TickerEvent event = new TickerEvent(this, ticker);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void depthEvent(Depth depth) {
        if (applicationEventPublisher != null) {
            DepthEvent event = new DepthEvent(this, depth);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public void run() {
        try {
//socket = new SocketIO("http://socketio-beta.mtgox.com/mtgox");
            socket = new SocketIO(SOCKET_IO_SERVER);
            socket.connect(socketListener);

//            websocket.addListener(socketListener);
//            websocket.open("ws://socketio-beta.mtgox.com");
            logger.info("WebSocket API Client started.");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(PacketEvent event) {
        JSONObject op = (JSONObject) event.getPayload();

        try {
            // logger.fine(aPacket.getUTF8());

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper();

//                    JsonParser jp = factory.createJsonParser(aPacket.getUTF8());
//                    DynaBean op = mapper.readValue(jp, DynaBean.class);

            if (op.get("op") != null && op.get("op").equals("private")) {
                String messageType = op.get("private").toString();
                if (messageType.equalsIgnoreCase("ticker")) {
                    OpPrivateTicker opPrivateTicker = mapper.readValue(factory.createJsonParser(op.toString()), OpPrivateTicker.class);
                    Ticker ticker = opPrivateTicker.getTicker();
                    tickerEvent(ticker);
                    logger.log(Level.FINE, "Ticker: last: {0}", new Object[]{ticker.getLast().toPlainString()});
                } else if (messageType.equalsIgnoreCase("depth")) {
                    OpPrivateDepth opPrivateDepth = mapper.readValue(factory.createJsonParser(op.toString()), OpPrivateDepth.class);
                    Depth depth = opPrivateDepth.getDepth();
                    depthEvent(depth);
                    logger.log(Level.FINE, "Depth total volume: {0}", new Object[]{depth.getTotalVolume().toPlainString()});
                } else if (messageType.equalsIgnoreCase("trade")) {
                    OpPrivateTrade opPrivateTrade = mapper.readValue(factory.createJsonParser(op.toString()), OpPrivateTrade.class);
                    Trade trade = opPrivateTrade.getTrade();
                    tradeEvent(trade);
                    logger.log(Level.FINE, "Trade currency: {0}", new Object[]{trade.getPrice_currency()});
                } else {
                    logger.log(Level.WARNING, "Unknown private operation: {0}", new Object[]{op.toString()});
                }

                // logger.log(Level.INFO, "messageType: {0}, payload: {1}", new Object[]{messageType, dataPayload});
            } else {
                logger.log(Level.WARNING, "Unknown operation: {0}, payload: {1}", new Object[]{op.get("op"), op.toString()});
                // TODO:  Process the following types
                // subscribe
                // unsubscribe
                // remark
                // result
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }
}
