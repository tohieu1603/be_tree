package com.tree.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public final class MarkdownUtil {
    private static final Parser parser;
    private static final HtmlRenderer renderer;
    private static final FlexmarkHtmlConverter htmlConverter;

    static {
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
        htmlConverter = FlexmarkHtmlConverter.builder().build();
    }

    private MarkdownUtil() {}

    public static String markdownToHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    public static String htmlToMarkdown(String html) {
        if (html == null || html.isBlank()) {
            return "";
        }
        return htmlConverter.convert(html);
    }
}
