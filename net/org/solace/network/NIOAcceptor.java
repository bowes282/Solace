/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.PlayerLoginDecipher;

/**
 *
 * @author KleptO
 */
public class NIOAcceptor extends Thread {

    @Override
    public void run() {
        while (NIOServer.channel().isOpen()) {
            try {
                SocketChannel channel = NIOServer.channel().accept();
                channel.configureBlocking(false);
                RSChannelContext channelContext = new RSChannelContext(channel,
                        new PlayerLoginDecipher());
                for (int i = 0; i < 3; i++) {
                    try {
                        channelContext.decoder().decode(channelContext);
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                        channel.close();
                    }
                }
                if (!channel.isOpen()) {
                    if (channelContext.player() != null) {
                        Game.getSingleton().deregister(channelContext.player());
                    }
                    continue;
                }
                channel.register(NIOSelector.selector(), SelectionKey.OP_READ,
                        channelContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
