package org.osrs.min.loading;

import org.osrs.min.api.accessors.Client;
import org.osrs.min.canvas.screen.ScreenOverlay;
import org.osrs.min.canvas.screen.overlays.BasicInfoDebugger;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.injectors.Interaction;
import org.osrs.min.loading.injectors.Invokers;
import org.osrs.min.loading.injectors.PacketMeta;
import org.osrs.min.loading.params.OSParams;
import org.osrs.min.loading.stub.OSStub;
import org.osrs.min.script.ScriptEngine;
import org.osrs.min.threads.CanvasListener;
import org.osrs.min.ui.BotMenu;
import org.osrs.min.utils.Utils;
import org.parabot.api.io.WebUtil;
import org.parabot.core.Context;
import org.parabot.core.Directories;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.servers.ServerManifest;
import org.parabot.environment.servers.ServerProvider;
import org.parabot.environment.servers.Type;

import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@ServerManifest(author = "Ethan", name = "OldSchool RuneScape", type = Type.INJECTION, version = 1.0)
public class Loader extends ServerProvider {

    private File targetJar;
    private File accessorsJar;
    private List<ScreenOverlay> overlays;
    private OSParams parameters;
    private ScriptEngine scriptEngine = new ScriptEngine();
    private XMLHookParser xmlHookParser;

    public static Client getClient() {
        return (Client) Context.getInstance().getClient();
    }

    @Override
    public URL getJar() {
        parameters = new OSParams(18);
        while (parameters.getJarLink() == null || parameters.getJarLink().length() < 5) {
            Time.sleep(200);
        }
        targetJar = new File(Directories.getCachePath(), "osrs.jar");
        try {
            if (!targetJar.exists()) {
                final URL url = new URL("http://oldschool" + parameters.getWorldId() + ".runescape.com/" + parameters.getJarLink());
                WebUtil.downloadFile(url, targetJar, VerboseLoader.get());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return WebUtil.toURL(targetJar);
    }

    @Override
    public Applet fetchApplet() {
        try {
            Utils.getInstance().addToSystemClassLoader(getAccessors());
            final Context context = Context.getInstance();
            handleCustomInjections(xmlHookParser);
            final ASMClassLoader classLoader = context.getASMClassLoader();
            final Class<?> clientClass = classLoader.loadClass("client");
            Object instance = clientClass.newInstance();
            Applet applet = (Applet) instance;
            applet.setStub(new OSStub(parameters.get("codebase"), parameters.getParameters()));
            new CanvasListener(scriptEngine, applet, getOverlays()).start();
            return applet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A ugly way to handle custom injections, needs to be re-done.
     * <p>
     * We handle out injection for interaction, invokers, packets.
     *
     * @author Ethan
     */
    private void handleCustomInjections(XMLHookParser xmlHookParser) {
        new Interaction(xmlHookParser);
        new Invokers(xmlHookParser);
        new PacketMeta(xmlHookParser);
    }

    /**
     * Grabs the current HookFile for server.
     * <p>
     * We have to extend Parabot's HookFile & override getInputStream because loading hooks from URL isn't fully supported in Parabot ATM.
     * They attach the accounts API key to end of URL.
     *
     * @author Ethan
     */
    @Override
    public HookFile getHookFile() {
        try {
            File hook = new File("C:\\Users\\Ethan\\Desktop\\Parabot\\file.xml");
            if (hook.exists()) {
                final org.osrs.min.loading.hooks.HookFile hookFile = new org.osrs.min.loading.hooks.HookFile(hook, HookFile.TYPE_XML);
                xmlHookParser = new XMLHookParser(hookFile);
                return hookFile;
            } else {

                final org.osrs.min.loading.hooks.HookFile hookFile = new org.osrs.min.loading.hooks.HookFile(new URL("https://parabot-osrs.000webhostapp.com/hooks.xml"), HookFile.TYPE_XML);
                xmlHookParser = new XMLHookParser(hookFile);
                return hookFile;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void injectHooks() {
        AddInterfaceAdapter.setAccessorPackage("org/osrs/min/api/accessors/");
        super.injectHooks();
    }

    @Override
    public void initScript(Script script) {
        scriptEngine.setScript(script);
        scriptEngine.init();
    }

    @Override
    public void unloadScript(Script script) {
        if (scriptEngine == null)
            return;
        scriptEngine.unload();
    }

    @Override
    public void addMenuItems(JMenuBar bar) {
        new BotMenu(bar, getOverlays());
    }

    private List<ScreenOverlay> getOverlays() {
        if (overlays == null) {
            overlays = new ArrayList<>();
            overlays.add(new BasicInfoDebugger());
        }
        return overlays;
    }

    private File getAccessors() {
        accessorsJar = new File(org.parabot.core.Directories.getCachePath() + File.separator + "OSRSAccessors.jar");
        try {
            if (!accessorsJar.exists()) {
                final URL url = new URL("https://parabot-osrs.000webhostapp.com/OSRSAccessors.jar");
                WebUtil.downloadFile(url, accessorsJar, VerboseLoader.get());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return accessorsJar;
    }
}
