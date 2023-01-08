package net.threadly.core.service.impl;

import net.threadly.core.PluginBase;
import net.threadly.core.command.CmdParam;
import net.threadly.core.command.Command;
import net.threadly.core.command.CommandSpec;
import net.threadly.core.conversor.TypeConversor;
import net.threadly.core.service.ICommandService;
import net.threadly.core.service.IService;
import net.threadly.core.util.Pair;
import net.threadly.core.util.Registry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCommandService implements ICommandService {
    private Registry<String, CommandSpec> commands = new Registry<>();

    public Optional<CommandSpec> getCommand(String input) {
        Optional<CommandSpec> found = Optional.empty();
        String current = "";
        for (String arg : input.split(" ")) {
            if (!commands.keys().contains(current + " " + arg)) {
                break;
            }
            found = Optional.of(commands.find(current + " " + arg).get());
        }
        return found;
    }

    public void register(JavaPlugin plugin, Registry<String, TypeConversor<?,?>> conversors) {
        Set<String> root = commands.keys().stream().map(x -> x.split(" ")[0])
                .collect(Collectors.toSet());
        for (String cmd : root) {
            plugin.getCommand(cmd).setExecutor((sender, c, label, args) -> {
                String current = cmd + " " + String.join(" ", args);

                Optional<CommandSpec> found = getCommand(current);
                if (!found.isPresent()) {
                    return true;
                }

                CommandSpec spec = found.get();

                String[] newArgs = current.substring(spec.getPath().length() + 1, spec.getPath().length() - 1).trim().split(" ");
                Command command = spec.getCommand();

                if(command.playerOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command is only for players.");
                    return false;
                }

                if(command.permission().equals("") && sender.hasPermission(command.permission())) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission for that.");
                    return false;
                }

                AtomicInteger index = new AtomicInteger();
                Map<Integer, Command.CommandParam> paramMap = Stream.of(spec.getCommand().params())
                        .collect(Collectors.toMap(i -> index.getAndIncrement(), Function.identity()));

                List<CmdParam> params = Stream.of(spec.getMethod().getParameters())
                        .filter(param -> param.isAnnotationPresent(CmdParam.class))
                        .map(param -> param.getAnnotation(CmdParam.class)).collect(Collectors.toList());

                Object[] orderedArgs = new Object[newArgs.length + 1];
                orderedArgs[0] = sender;

                int argsIndex = 1;

                for (CmdParam param : params) {
                    Pair<Integer, Command.CommandParam> correspondent = paramMap.entrySet().stream()
                            .filter(entry -> entry.getValue().key().equalsIgnoreCase(param.value()))
                            .map(entry -> new Pair<>(entry.getKey(), entry.getValue())).findFirst().get();

                    TypeConversor conversor = conversors.find(correspondent.getSecond().conversor()).get();

                    String passed = newArgs[correspondent.getFirst()];
                    Object value = conversor.convert(passed);
                    orderedArgs[argsIndex++] = value;
                }

                try {
                    spec.getMethod().invoke(null, orderedArgs);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }


                return false;
            });
        }
    }

    public void load(Class<?> clazz) {
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Command.class) && Modifier.isStatic(method.getModifiers()))
                .forEach(method -> {
                    Command command = method.getAnnotation(Command.class);
                    String path = Arrays.stream(command.usage().split(" "))
                            .filter(word -> !word.contains("<") && !word.contains(">") && !word.contains("[") && !word.contains("]"))
                            .reduce("", String::concat);
                    commands.register(path, new CommandSpec(path, method, command));
                });
    }

}
