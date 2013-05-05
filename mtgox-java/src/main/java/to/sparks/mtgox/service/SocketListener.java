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

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import to.sparks.mtgox.event.PacketEvent;

/**
 *
 * @author SparksG
 */
public class SocketListener implements IOCallback, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher = null;
    private Logger logger;

    public SocketListener(Logger logger) {
        this.logger = logger;
    }

//    @Override
//    public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
//        String sEvent = aEvent != null ? aEvent.toString() : "null";
//        String sPacket = aPacket != null ? aPacket.getUTF8() : "null";
//        logger.log(Level.FINE, "processPacket( Event: {0}  Packet: {1} )", new Object[]{sEvent, sPacket});
//        MtGoxPacket packet = new MtGoxPacket(aEvent, aPacket);
//        PacketEvent event = new PacketEvent(this, packet);
//        applicationEventPublisher.publishEvent(event);
//    }
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onDisconnect() {
        logger.log(Level.WARNING, "Disconnect");
    }

    @Override
    public void onConnect() {
        logger.log(Level.WARNING, "Connect");
    }

    @Override
    public void onMessage(String string, IOAcknowledge ioa) {
        logger.log(Level.WARNING, "Message: {0}", string);
    }

    @Override
    public void onMessage(JSONObject jsono, IOAcknowledge ioa) {
        PacketEvent event = new PacketEvent(this, jsono);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void on(String string, IOAcknowledge ioa, Object... os) {
        logger.log(Level.WARNING, "on: {0}", string);
    }

    @Override
    public void onError(SocketIOException sioe) {
        logger.log(Level.SEVERE, "SocketIOException: {0}", sioe.getMessage());
    }
}
