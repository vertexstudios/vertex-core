package net.threadly.core.testplugin;

import net.threadly.core.PluginBase;
import net.threadly.core.service.ICommandService;

public class Main extends PluginBase {

    private static Main instance;

    @Override
    public void init() {
        ICommandService service = serviceManager.getService(ICommandService.class).get();
        service.load(Commands.class);

        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }
}
