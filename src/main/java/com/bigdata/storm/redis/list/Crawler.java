package com.bigdata.storm.redis.list;

import com.bigdata.storm.redis.InitRedisConnection;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Rainbow on 2016/12/7.
 * 小爬虫
 */
public class Crawler {
    //定义待爬 list url
    private static String redisUrlsWillKey = "crawler:urls:will";

    public static void main(String[] args) throws Exception {
        String startUrl = "http://www.huxiu.com/";
        String domain = "http://www.huxiu.com";

        getUrl(startUrl, domain);
        CrawlerUrl();


    }

    private static void CrawlerUrl() throws Exception {
        Jedis jedis = InitRedisConnection.getConnection();

        while (true) {
            String url = jedis.lpop(redisUrlsWillKey);
            Article article = getArticle(url);
            System.out.println(article);
        }


    }

    private static Article getArticle(String url) throws Exception {

        Article article = new Article();
        Document document = Jsoup.connect(url).get();
        Elements author_name = document.getElementsByClass("author-name");
        article.setAuthor(StringUtils.isBlank(author_name.text()) ? "xiaozhang" : author_name.text());
        Elements article_time = document.getElementsByClass("article-time");
        article.setDate(StringUtils.isBlank(article_time.text())?new Date():DateUtil.getDate(article_time.text()));
        // 抽取文章标题
        Elements title = document.getElementsByTag("title");
        article.setTitle(title.text());

        //http://www.huxiu.com/article/173650.html?f=retweeted
        article.setId(url.substring(29,url.lastIndexOf(".")));
        Elements elements = document.select("div #article_content");
        article.setContent(elements.get(0).text());
        return article;
    }

    private static void getUrl(String startUrl, String domain) throws IOException {
        Jedis jedis = InitRedisConnection.getConnection();
        Document document = Jsoup.connect(startUrl).get();
        Elements elements = document.getElementsByAttribute("href");
        for (Element element : elements) {
            String url = element.attr("href");
            if (url.contains("article")) {
                String urlString = domain + url;
                jedis.lpush(redisUrlsWillKey, urlString);
                System.out.println(urlString);
            }
        }
    }
}
