/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.loader;

import space.Core;

public class InjectionEndpoint {

    public static void Load() {
        Core.mode = false;
        new space.Core().initialize();
    }
}
