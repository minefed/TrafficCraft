# Wikidata request identification

Minecraft 1.20.4 / Fabric, TrafficCraft 1.1.3 and bundled DragonLib 2.2.24.

The optional traffic-light and tram-signal Wikipedia links failed to load because
DragonLib opened the Wikidata URL with Java's generic User-Agent. Both Q8004 and
Q2354774 returned HTTP 403 in the game. Requests with a descriptive agent and a
contact URL succeeded, as required by the
[official Wikimedia API policy](https://www.mediawiki.org/wiki/API:Etiquette#The_User-Agent_header).

Minefed's release policy retains the official TrafficCraft JAR because embedded
dependency asset redistribution has not been verified. Consequently, the repair
is shipped in the independently authored `minefed-client-compat` add-on, alongside
the PTS/PFM fixes. This repository must not register a second redirect, which
would conflict when the source-built TrafficCraft and the add-on are used together.

The add-on redirects exactly one `URL.openStream` instruction in the bundled
WikipediaArticle loader. Only HTTPS requests to `www.wikidata.org` receive its
descriptive User-Agent and 10-second connection/read timeouts. Other URLs and
DragonLib's exception/fallback behavior are unchanged. No global HTTP property
is changed and the official TrafficCraft/DragonLib JARs are not modified.

Validation: the target and single invocation were checked in the actual bundled
2.2.24 bytecode. The redirect handler compiled with Java 17 and returned valid
sitelink data for both IDs against the live API. The parent modpack's optional
`audit.verifyWikipedia` probe checks that the actual Mixin-loaded game populates
both article language maps. The API-dependent check is separate from offline
inventory rendering tests.

All existing GPL and bundled dependency notices are retained.
