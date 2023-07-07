package org.vertex.bukkit.gui.pipeline;

import org.vertex.bukkit.gui.Container;

public class ContainerClosingPipeline extends ContainerPipeline {
    
    public static ContainerPipeline create() {
        return new ContainerPipeline().then(Container::dispose);
    }

}
