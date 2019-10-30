package org.osrs.min.loading.hooks;

import org.parabot.api.io.WebUtil;
import org.parabot.core.forum.AccountManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Creates HookFile for server.
 * <p>
 * We have to extend Parabot's HookFile & override getInputStream because loading hooks from URL isn't fully supported in Parabot ATM.
 * They attach the accounts API key to end of URL.
 *
 * @author Ethan
 */
public class HookFile extends org.parabot.core.asm.hooks.HookFile {

    private boolean isLocal;
    private URL url;

    public HookFile(File file, int type) throws MalformedURLException {
        super(file, type);
        this.isLocal = true;
    }

    public HookFile(URL url, int type) {
        super(url, type);
        this.url = url;
    }

    @Override
    public InputStream getInputStream(AccountManager manager) {
        if (isLocal) {
            return this.getInputStream();
        } else {
            try {
                return WebUtil.getConnection(url).getInputStream();
            } catch (IOException e) {
                return null;
            }
        }
    }
}
