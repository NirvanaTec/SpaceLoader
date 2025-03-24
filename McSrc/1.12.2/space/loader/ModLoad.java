/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.loader;

import net.minecraftforge.fml.common.Mod;
import space.Core;

@Mod(modid = "space", name = "Space", version = "1.0", acceptableRemoteVersions = "*")
public class ModLoad {
    public ModLoad() {
        Core.mode = true;
        new Core().initialize();
    }
}
