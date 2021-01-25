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

package com.districtmeps.bot.sendpic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class SendPic {

    public static void start(GuildMemberJoinEvent event){

		System.out.println("starting");
		String id = event.getUser().getId();
		JFrame f = new JFrame();
		Component p = new Pic(event);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(new Dimension(1053 + 17, 742));
		f.add(p);
		f.setVisible(true);

		BufferedImage bi = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bi.createGraphics();
		System.out.println(p.getWidth() + "  " + p.getHeight() + " " + f.getWidth() + " " + f.getHeight());
		p.print(graphics);
		graphics.dispose();
		f.dispose();



		File file = new File("./media/joined/" + id + ".png");
		try {
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("done");

		//Message msg = null;
		File send = new File("./media/joined/" + id + ".png");
		if (send.exists()) {
            event.getGuild().getSystemChannel().sendFile(send, send.getName()).queue();
            
		} else {
			System.out.println("THIS FILE DOES NOT EXIST");
		}

		System.out.println("done");
    }
    
}