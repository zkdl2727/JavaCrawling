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
import java.util.Objects;


@SpringBootApplication
public class CrawlingApplication {

    private static final Logger log = LoggerFactory.getLogger(CrawlingApplication.class);
    private static final int First = 1;
    private static final int Last = 32;
    private static final String platform = "Inflearn";
    public static void main(String[] args) throws IOException {

        SpringApplication.run(CrawlingApplication.class, args);
        try {
            //개발 강의 전부 순회.
            for (int i = First; i <= Last; i++) {
                final String infUrl = "http://www.inflearn.com/courses/it-programming?order=seq&page=" + i;
                Connection conn = Jsoup.connect(infUrl);
                Document doc = conn.get();


                // 크롤링 항목 리스트
                // - 썸네일 링크, 강의 제목, 가격, 평점, 강의자, 강의 링크, 수강자 수, 강의 세션 개수, 시간, 플랫폼.

                Elements imageUrl = doc.getElementsByClass("swiper-lazy");
                Elements titleUrl = doc.select("div.card-content>div.course_title");
                Elements priceUrl = doc.getElementsByClass("price");
                Elements instructUrl = doc.getElementsByClass("instructor");
                Elements linkUrl = doc.select("a.course_card_front");
                Elements descriptionUrl = doc.select("p.course_description");
                Elements skillUrl = doc.select("div.course_skills>span");
                String[] imageUrls = new String[imageUrl.size()];

                int setIdx = 0;
                int getIdx = 0;

                for (Element e : imageUrl) {
                    imageUrls[setIdx++] = e.attr("abs:src");
                }

                for (int j = 0; j < titleUrl.size(); j++) {
                    final String title = titleUrl.get(j).text();
                    final String price = priceUrl.get(j).text();
                    final String realPrice = getReal(price);
                    final String salePrice = getSale(price);
                    final int realIntPrice = toInt(removeNum(realPrice));
                    final int saleIntPrice = toInt(removeNum(salePrice));

                    final String cur = String.valueOf(price.charAt(0));
                    final String instructor = instructUrl.get(j).text();

                    final String url = linkUrl.get(j).attr("abs:href");
                    final String description = descriptionUrl.get(j).text();
                    final String skills = removeWhiteSpace(skillUrl.get(j).text());

                    System.out.println("썸네일:" + imageUrls[j]);
                    System.out.println("강의 제목:" + title);
                    System.out.println("가격:" + realIntPrice);
                    System.out.println("할인 가격: " + saleIntPrice);
                    System.out.println("원화:" + cur);
                    System.out.println("강의자:" + instructor);
                    System.out.println("강의 링크:" + description);
                    System.out.println("기술 스택:" + skills);

                    // 강의 링크 내부 변수
                    Connection innconn = Jsoup.connect(url);
                    Document inndoc = innconn.get();

                    // 평점
                    Element rateUrl = inndoc.selectFirst("div.dashboard-star__num");
                    final float rate = Objects.isNull(rateUrl)
                            ? toFloat("0")
                            : toFloat(rateUrl.text());
                    System.out.println("평점:" + rate);

                    //수강자 수
                    Elements listenUrl = inndoc.select("div.cd-header__info-cover");
                    final String listener = Objects.isNull(listenUrl)
                            ? inndoc.selectFirst("span>strong").text()
                            : inndoc.select("div.cd-header__info-cover>span>strong").get(1).text();
                    System.out.println("수강자 수 :" + removeNum(listener));

                    //강의 세션 개수
                    final String course = inndoc.selectFirst("span.cd-curriculum__sub-title").text();
                    System.out.println("강의 세션 개수: " + getSessionCount(course));
                    final int sessionCnt = Integer.parseInt(getSessionCount(course));
                    System.out.println();
                }
            }
        } catch (IOException e) {
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

    private static String stripHtml(final String html) {
        return Jsoup.clean(html, Whitelist.none());
    }

    private static String removeBracket(final String str) {
        return str.replaceAll("^[(]|[)]$", "");
    }
    private static String getSessionCount(final String course) {
        return removeNum(course.substring(0,course.indexOf("개")));
    }
}



       /* 이전 코드.

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
    }*/