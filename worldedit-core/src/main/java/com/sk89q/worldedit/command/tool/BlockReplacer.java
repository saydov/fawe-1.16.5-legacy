/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.command.tool;

import com.boydti.fawe.config.Caption;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.block.BaseBlock;

/**
 * A mode that replaces one block.
 */
public class BlockReplacer implements DoubleActionBlockTool {

    private Pattern pattern;

    public BlockReplacer(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean canUse(Actor player) {
        return player.hasPermission("worldedit.tool.replacer");
    }

    @Override
    public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
        BlockBag bag = session.getBlockBag(player);

        try (EditSession editSession = session.createEditSession(player, "BlockReplacer")) {
            try {
                BlockVector3 position = clicked.toVector().toBlockPoint();
                editSession.setBlock(position, pattern);
            } catch (MaxChangedBlocksException ignored) {
            } finally {
                session.remember(editSession);
            }
        } finally {
            if (bag != null) {
                bag.flushChanges();
            }
        }

        return true;
    }


    @Override
    public boolean actSecondary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
        BaseBlock targetBlock = player.getWorld().getFullBlock(clicked.toVector().toBlockPoint());

        if (targetBlock != null) {
            pattern = targetBlock;
            player.print(Caption.of("worldedit.tool.repl.switched", targetBlock.getBlockType().getRichName()));
        }

        return true;
    }

}
