package org.osrs.min.loading.stub;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.Map;

public class OSStub implements AppletStub {
    private Map<String, String> parameters;
    private String codeBase;

    public OSStub(String codeBase, Map<String, String> parameters) {
        this.codeBase = codeBase;
        this.parameters = parameters;
    }

    @Override
    public void appletResize(int w, int h) {

    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(codeBase);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public boolean isActive() {
        return false;
    }

}
