package com.example.crawling;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;


@SpringBootApplication
public class CrawlingApplication {

    private static final Logger log = LoggerFactory.getLogger(CrawlingApplication.class);
    private static final int First = 1;
    private static final int Last = 32;
    private static final String platform = "Inflearn";
    public static void main(String[] args) throws IOException {

   /*     try {
            //개발 강의 전부 순회.
            for(int i= First; i<= Last; i++) {
                final String infUrl = "http://www.inflearn.com/courses/it-programming?order=seq&page=" + i;
                Connection conn = Jsoup.connect(infUrl);
                Document doc = conn.get();
            }
        }*/
        SpringApplication.run(CrawlingApplication.class, args);

        final String url = "https://www.inflearn.com/courses/it-programming";

        Connection conn = Jsoup.connect(url);

        // 썸네일 링크 크롤링.
        try {
            Document doc = conn.get();
            Elements imageUrl = doc.getElementsByClass("swiper-lazy");


            for (Element element : imageUrl) {
                System.out.println(element.attr("abs:src")); // html 태그 삭제.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 강의 제목 크롤링.
        try {
            Document doc = conn.get();
            Elements titleUrl = doc.select("div.card-content>div.course_title");

            for (int j = 0; j < titleUrl.size(); j++) {
                final String title = titleUrl.get(j).text();
                System.out.println("강의 제목: " + title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 강의 금액 크롤링.
        try {
            Document doc = conn.get();
            Elements priceUrl = doc.getElementsByClass("price");

            for (int j = 0; j < priceUrl.size(); j++) {
                final String price = priceUrl.get(j).text();
                final String realPrice = getReal(price);
                final String salePrice = getSale(price);

                final int realIntPrice = toInt(removeNum(realPrice));
                final int saleIntPrice = toInt(removeNum(salePrice));

                System.out.println("가격 : " + realIntPrice);
                System.out.println("할인 가격: " + saleIntPrice);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // 강의 링크 크롤링.     gradle 업데이트 과정에서 오류가 발생해서, 현재 구동 x.
        try {
            Document doc = conn.get();
            Elements linkUrl = doc.select("a.course_card_front");

            for (int j = 0; j < linkUrl.size(); j++) {
                final String lUrl = linkUrl.get(j).attr("abs:href");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // 강의 평점 크롤링.
        Connection innerConn = Jsoup.connect(url);
        Document innerDoc = innerConn.get();
        try {
            Element rateUrl = innerDoc.selectFirst("div.dashboard-star_num");
            final float rate = Objects.isNull(rateUrl)
                    ? toFloat("0")
                    : toFloat(rateUrl.text());
            System.out.println("평점:" + rate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Document doc = conn.get();
            Elements insUrl = doc.getElementsByClass("instructor");
            Elements desUrl = doc.select("p.course_description");
            Elements skillUrl = doc.select("div.course_skills > span");

            for (int j = 0; j < insUrl.size(); j++) {
                final String instructor = insUrl.get(j).text();
                final String description = desUrl.get(j).text();

                final String skills = removeWhiteSpace(skillUrl.get(j).text());

                System.out.println("강의자 :" + instructor);
                System.out.println("강의 부가설명 : " + description);
                System.out.println("기술 스택 : " + skills);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getReal(final String price) {
        final String[] priceArr = price.split(" ");
        return priceArr[0];
    }

    private static String getSale(final String price) {
        final String[] priceArr = price.split(" ");
        return (priceArr.length == 1) ? price : priceArr[1];
    }
    private static String removeNum(final String str) {
        return str.replaceAll("\\W", "");
    }
    private static int toInt(final String str) {
        return Integer.parseInt(str);
    }

    private static float toFloat(final String str) {
        return Float.parseFloat(str);
    }

    private static String removeWhiteSpace(final String str) {
        return str.replaceAll("\\s","");
    }
}
