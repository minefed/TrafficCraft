/* Copyright (c) 2026 Minefed. SPDX-License-Identifier: GPL-3.0-only */
package de.mrjulsen.trafficcraft.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Identify the optional Wikidata request as required by Wikimedia's API policy. */
@Mixin(targets = "de.mrjulsen.mcdragonlib.util.Wikipedia$WikipediaArticle", remap = false)
public class WikipediaRequestMixin {
    @Redirect(method = "lambda$new$0(Ljava/lang/String;)V", remap = false,
        at = @At(value = "INVOKE", target = "Ljava/net/URL;openStream()Ljava/io/InputStream;", remap = false),
        require = 1, expect = 1, allow = 1)
    private InputStream minefed$identifiedWikipediaRequest(URL url) throws IOException {
        if (!"https".equals(url.getProtocol()) || !"www.wikidata.org".equals(url.getHost()))
            return url.openStream();
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent",
            "Minefed-TrafficCraft/1.1.3 (https://github.com/minefed/TrafficCraft)");
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);
        return connection.getInputStream();
    }
}
