package org.vertex.bukkit;

import org.vertex.core.service.ICommandService;

public class Main extends VertexBukkitPlugin {

    @Override
    public void init() {
        ICommandService commandService = this.serviceManager.getService(ICommandService.class).get();
        commandService.load(TestCMD.class);
    }
}
