package dev.efekos.pg.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class Utilities {
    public static String markdownToHtml(String markdown){
        Parser parser = new Parser.Builder().build();
        Node node = parser.parse(markdown);
        return HtmlRenderer.builder().build().render(node);
    }
}
