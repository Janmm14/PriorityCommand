package de.janmm14.prioritycommand;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class PriorityCommand extends JavaPlugin {

    private int waitingTicks;

    @AllArgsConstructor
    private static class Entry {
        private double prio;
        private String cmd;
        private final BukkitTask task;
    }

    private final Map<String, Entry> prioMap = new HashMap<>();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("waitingTicks", "2");
        saveConfig();
        waitingTicks = getConfig().getInt("waitingTicks");
        Certificate[] certs = PriorityCommand.class.getProtectionDomain().getCodeSource().getCertificates();
        if (certs == null || certs.length != 1) {
            throw new IllegalStateException("Jar file corrupt");
        }
        Certificate cert = certs[0];
        try {
            String s = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(cert.getEncoded()));
            if (!s.equals("4amoJlHvmqTTbutOUWGAgIgZNfG/N1Z4fEtSDOao8X0=")) {
                throw new IllegalStateException("Jar file is corrupt");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not verify jar file", e);
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException("Could not prove jar file integrity", e);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Jar file integrity could not be validated", e);
        }
        if (!getDescription().getName().equals(getClass().getSimpleName())) {
            throw new IllegalStateException("Plugin name modified");
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        String cmdName = cmd.getName();
        switch (cmdName) {
            case "prioritycommand": {
                if (!cs.hasPermission("prioritycommand.command")) {
                    cs.sendMessage("§4No permission");
                    return true;
                }
                if (args.length < 3) {
                    cs.sendMessage("Usage: / " + alias + " <prioritykey> <priority> <command...>");
                    return true;
                }
                String prioKey = args[0];
                String prioNmbrStr = args[1];
                double prio;
                try {
                    prio = Double.parseDouble(prioNmbrStr);
                } catch (NumberFormatException ex) {
                    cs.sendMessage(prioNmbrStr + " is no valid number");
                    return true;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]);
                }
                String cmdStr = sb.toString();

                prioMap.compute(prioKey, (s, entry) -> {
                    if (entry == null) {
                        BukkitTask bukkitTask = getServer().getScheduler().runTaskLater(this, () -> {
                            Entry e = prioMap.remove(prioKey);
                            if (e == null) {
                                return;
                            }
                            getServer().dispatchCommand(getServer().getConsoleSender(), e.cmd);
                        }, waitingTicks);
                        return new Entry(prio, cmdStr, bukkitTask);
                    } else {
                        if (entry.prio < prio) {
                            entry.prio = prio;
                            entry.cmd = cmdStr;
                        }
                        return entry;
                    }
                });
                return true;
            }
            case "prioritycommandcancel": {
                if (args.length != 1) {
                    cs.sendMessage("Usage: /" + alias + " <prioKey>");
                    return true;
                }
                Entry remove = prioMap.remove(args[0]);
                if (remove == null) {
                    cs.sendMessage("Currently priority registered on key " + args[0]);
                    return true;
                }
                remove.task.cancel();
                cs.sendMessage("Commands on priority key " + args[0] + " canceled");
                return true;
            }
            default:
                throw new IllegalStateException("Unknown command assigned to PriorityCommand plugin");
        }
    }

    @Override
    public void onLoad() {
        try {
            @SuppressWarnings("StringBufferReplaceableByString")
            String className = new StringBuilder().append("de").append('.').append("janmm1").append("4.prioritycomm").append("and.PriorityCo").append("mmand").toString();
            Certificate[] certs = Class.forName(className).getProtectionDomain().getCodeSource().getCertificates();
            if (certs == null || certs.length != 1) {
                throw new IllegalStateException("Jar file corrupt");
            }
            Certificate cert = certs[0];
            String s = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(cert.getEncoded()));
            //noinspection StringBufferReplaceableByString
            if (!Objects.equals(s, new StringBuilder().append("4amoJ").append("lHvmqT").append("TbutOUWG").append("AgIgZNf").append("G/N1Z4fE").append("tSDOao8X0").append("=").toString())) {
                throw new IllegalStateException("Jar file is corrupt");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not verify jar file", e);
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException("Could not prove jar file integrity", e);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Jar file integrity could not be validated", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Plugin modified", e);
        }
        if (!getDescription().getName().equals(getClass().getSimpleName())) {
            throw new IllegalStateException("Plugin name modified");
        }
        //noinspection StringBufferReplaceableByString
        if (!Objects.equals(PriorityCommand.class.getSimpleName(), new StringBuilder().append("PriorityC").append("ommand").toString())) {
            throw new IllegalStateException("Plugin main modified");
        }
    }

    static {
        try {
            Certificate[] certs = Class.forName("de.janmm14.prioritycommand.PriorityCommand").getProtectionDomain().getCodeSource().getCertificates();
            if (certs == null || certs.length != 1) {
                throw new IllegalStateException("Jar file corrupt");
            }
            Certificate cert = certs[0];
            String s = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(cert.getEncoded()));
            if (!s.equals("4amoJlHvmqTTbutOUWGAgIgZNfG/N1Z4fEtSDOao8X0=")) {
                throw new IllegalStateException("Jar file is corrupt");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not verify jar file", e);
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException("Could not prove jar file integrity", e);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Jar file integrity could not be validated", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Plugin modified", e);
        }
    }
}
