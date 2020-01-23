/**
 *      Copyright 2020 Daniel Sanchez
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

package com.districtmeps.bot.commands.owner;

import java.util.List;

import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.objects.ICommand;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * EvalCommand
 */
public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;

    public EvalCommand(){
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.core.utils.*\n";
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!event.getAuthor().getId().equals(Config.getInstance().getString("owner"))){
            return;
        }

        if(args.isEmpty()){
            event.getChannel().sendMessage("Missing Args").queue();
            return;
        }

        try{
            engine.setProperty("args", args);
            engine.setProperty("event", event);
            engine.setProperty("message", event.getMessage());
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("jda", event.getJDA());
            engine.setProperty("guild", event.getGuild());
            engine.setProperty("member", event.getMember());

            String script = imports + event.getMessage().getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            event.getChannel().sendMessage(out == null ? "Executed without error" : out.toString()).queue();

        } catch (Exception e){
            event.getChannel().sendMessage(e.getMessage()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Takes groovy code and evaluates it";
    }

    @Override
    public String getInvoke() {
        return "eval";
    }

    @Override
    public int getType() {
        return 1;
    }

    
}