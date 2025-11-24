/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.loader;

import space.Core;

/**
 * 类名 硬性要求 Loader + Injection (硬性)
 * Loader - run = this.getClass().getName() + "Injection";
 */
public class LoaderInjection {

    /**
     * 模组加载入口
     * public static void 为 Loader.java 硬性要求
     */
    public static void load() {
        new Core().initialize();
    }

}
