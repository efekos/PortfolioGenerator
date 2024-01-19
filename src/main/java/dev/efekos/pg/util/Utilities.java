package dev.efekos.pg.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utilities class used for various different methods inside it.
 */
public class Utilities {

    /**
     * Converts a markdown string to html string using CommonMark.
     *
     * @param markdown Markdown input string.
     * @return HTML output string.
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = new Parser.Builder().build();
        Node node = parser.parse(markdown);
        return HtmlRenderer.builder().build().render(node);
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
