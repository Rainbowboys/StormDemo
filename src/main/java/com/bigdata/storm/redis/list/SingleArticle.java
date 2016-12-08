package com.bigdata.storm.redis.list;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 访问单个新闻页：
 * http://www.huxiu.com/article/102062/1.html 需要：标题，内容
 * */
public class SingleArticle {

    public static void main(String[] args) throws IOException {
        // 第一步：访问页面
        String url = "http://www.huxiu.com/article/102062/1.html";

        Document document = Jsoup.connect(url).get();
        //获取文章标题
        Elements elements = document.getElementsByTag("title");

        System.out.println(elements.get(0).text());

        Elements select = document.select("div  #article_content");
        System.out.println(select.get(0).text());
        String substring = url.substring(29, url.lastIndexOf("."));
        System.out.println(substring);


    }
}
