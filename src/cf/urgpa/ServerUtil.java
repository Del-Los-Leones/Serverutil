package cf.urgpa;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public class ServerUtil extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			private int seconds = 0;

			@Override
			public void run() {
				int intervalSec = getMobInterval() * 60;
				seconds++;
				if (intervalSec - seconds == 30) {
					getServer().broadcastMessage(ChatColor.AQUA + "[MPP] "
							+ ChatColor.WHITE + "잠시 후에 모든 엔티티가 제거됩니다.");
				}
				if (intervalSec - seconds == 0) {
					List<World> worlds = getServer().getWorlds();
					int cleared = 0;
					for (World world : worlds) {
						for (Entity e : world.getEntities()) {
							if (e instanceof Item || (e instanceof LivingEntity && !(e instanceof HumanEntity)
									&& !(e instanceof Animals) && !(e instanceof NPC))) {
								e.remove();
								cleared++;
							}
						}
					}
					seconds = 0;
					getServer().broadcastMessage(ChatColor.AQUA + "[MPP] "
							+ ChatColor.WHITE + cleared + "개의 엔티티가 제거되었습니다.");
				}
			}
		}, 0, 20);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			private int seconds = 0;

			@Override
			public void run() {
				if (seconds == 0)
					getServer().broadcastMessage(ChatColor.AQUA + "[MPP] "
							+ ChatColor.WHITE + "자동재부팅 기능이 활성화되었습니다.");
				seconds += 1;
				int intervalSec = getInterval() * 60;
				if (intervalSec - seconds == 60 * 30) {
					alertLeft(30);
				}
				if (intervalSec - seconds == 60 * 5) {
					alertLeft(5);
				}
				if (intervalSec - seconds == 3) {
					getServer().broadcastMessage(ChatColor.AQUA + "[MPP] "
							+ ChatColor.WHITE + "서버 재부팅 중..");
				}
				if (intervalSec - seconds == 0)
					getServer().shutdown();
			}

			public void alertLeft(int minute) {
				String msg = getMessage();
				msg = msg.replace("%time%", String.valueOf(minute));
				getServer().broadcastMessage(ChatColor.AQUA + "[MPP] " + ChatColor.WHITE + msg);
			}
		}, 0, 20);
	}

	public String getMessage() {
		YamlConfiguration conf = (YamlConfiguration) getConfig();
		return (String) conf.get("message");
	}

	public int getMobInterval() {
		YamlConfiguration conf = (YamlConfiguration) getConfig();
		return (int) conf.get("mob-interval");
	}

	public int getInterval() {
		YamlConfiguration conf = (YamlConfiguration) getConfig();
		return (int) conf.get("interval");
	}
}
