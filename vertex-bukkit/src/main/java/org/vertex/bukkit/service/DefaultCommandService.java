package org.vertex.bukkit.service;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.vertex.core.command.CmdParam;
import org.vertex.core.command.Command;
import org.vertex.core.command.CommandSpec;
import org.vertex.core.conversor.TypeConversor;
import org.vertex.core.service.ICommandService;
import org.vertex.core.util.Pair;
import org.vertex.core.util.Registry;
import org.vertex.bukkit.BukkitPluginContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCommandService implements ICommandService {
    private Registry<String, CommandSpec> commands = new Registry<>();


    public void register(Registry<String, TypeConversor<?,?>> conversors) {
        Set<String> root = commands.keys().stream().map(x ->  x.split(" ")[0])
                .collect(Collectors.toSet());
        for (String cmd : root) {
            BukkitPluginContainer.getCurrentPlugin().getCommand(cmd).setExecutor((sender, c, label, args) -> {
                String fullCmd = cmd + " " + String.join(" ", args).trim();
                System.out.println("Full cmd: " + fullCmd);
                StringBuilder current = new StringBuilder(cmd);

                CommandSpec found = null;
                if(commands.find(cmd).isPresent()) {
                    found = commands.find(cmd).get();
                }

                for(String arg : args) {
                    System.out.println("Found arg: " + arg);
                    current.append(" ").append(arg);
                    Optional<CommandSpec> next = commands.find(current.toString().trim());
                    if(next.isPresent()) {
                        found = next.get();
                    }
                }

                if(found == null) {
                    return true;
                }

                String[] newArgs = fullCmd.trim().substring(found.getPath().length()).trim().split(" ");
                if(newArgs[0].equals("")) {
                    newArgs = new String[0];
                }
                System.out.println("New args: " + Arrays.toString(newArgs));
                Command command = found.getCommand();

                if(command.playerOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command is only for players.");
                    return false;
                }

                if(!command.permission().equals("") && (sender.hasPermission(command.permission()) && !sender.isOp())) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission for that.");
                    return false;
                }

                AtomicInteger index = new AtomicInteger();
                Map<Integer, Command.CommandParam> paramMap = Stream.of(found.getCommand().params())
                        .collect(Collectors.toMap(i -> index.getAndIncrement(), Function.identity()));

                System.out.println("Param map:");
                paramMap.forEach((x,y) -> System.out.println(x + ":" + y.key()));

                List<CmdParam> params = Stream.of(found.getMethod().getParameters())
                        .filter(param -> param.isAnnotationPresent(CmdParam.class))
                        .map(param -> param.getAnnotation(CmdParam.class)).collect(Collectors.toList());

                System.out.println("Params:");
                params.forEach((x) -> System.out.println(x.value()));

                Object[] orderedArgs = new Object[newArgs.length + 1];
                orderedArgs[0] = sender;

                System.out.println("Tamanho dos ordered:");
                System.out.println(orderedArgs.length);

                int argsIndex = 1;

                for (CmdParam param : params) {
                    Pair<Integer, Command.CommandParam> correspondent = paramMap.entrySet().stream()
                            .filter(entry -> entry.getValue().key().equalsIgnoreCase(param.value()))
                            .map(entry -> new Pair<>(entry.getKey(), entry.getValue())).findFirst().get();

                    TypeConversor conversor = conversors.find(correspondent.getSecond().conversor()).get();

                    String passed = newArgs[correspondent.getFirst()];
                    Object value = conversor.convert(passed);
                    orderedArgs[argsIndex++] = value;
                    System.out.println("Valor passado: " + passed);
                    System.out.println("Classe do valor convertido: " + value.getClass().getName());
                }

                System.out.println("Tamanho: " + orderedArgs.length);

                try {
                    found.getMethod().invoke(null, orderedArgs);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    sender.sendMessage(ChatColor.RED + "Correct usage: /" + command.usage());
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
                            .reduce("", (s1, s2) -> s1 + " " + s2).trim();
                    commands.register(path, new CommandSpec(path, method, command));
                    System.out.println("Registered: " + path);
                });
    }

}
