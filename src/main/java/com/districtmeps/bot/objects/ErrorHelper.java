/**
 *      Copyright 2020-2021 Daniel Sanchez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.districtmeps.bot.objects;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

/**
 * ErrorHelper
 */
public class ErrorHelper {

    public static void MessageAdmin(User user, String location, String message) {

        user.openPrivateChannel().queue((channel) -> {
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Happened at `" + location + "`");
            builder.setDescription("\n" + message);
            channel.sendMessage(builder.build()).queue();
        });
    }
}