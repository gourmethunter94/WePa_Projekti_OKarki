/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.domain.FluentList;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withName;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author ollik
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsTest extends FluentTest{
    
    @LocalServerPort
    private Integer port;
    
    @Test
    public void RedirectToHomeAndLogin(){
        goTo("http://localhost:" + port);
        
        assertTrue(pageSource().contains("Latest"));
        assertTrue(pageSource().contains("Home"));
        assertTrue(!pageSource().contains("Write"));
        
        $("input", withName("username")).first().fill().with("jour");
        $("input", withName("password")).first().fill().with("nalist");
        $("input", withName("submit")).first().click();
        
        assertTrue(pageSource().contains("Write"));
    }
    
    @Test
    public void AddNews(){
        goTo("http://localhost:" + port);
        
        assertTrue(!pageSource().contains("19asdKuKu&&>&newsLol123123Test"));
        assertTrue(pageSource().contains("Latest"));
        assertTrue(pageSource().contains("Home"));
        assertTrue(!pageSource().contains("Write"));
        
        $("input", withName("username")).first().fill().with("jour");
        $("input", withName("password")).first().fill().with("nalist");
        $("input", withName("submit")).first().click();
        
        assertTrue(pageSource().contains("Write"));
        
        $("a", withName("write")).first().click();
        
        assertTrue(pageSource().contains("Write new article"));
        
        $("input", withName("title")).first().fill().with("19asdKuKu&&>&newsLol123123Test");
        $("textarea", withName("content")).first().fill().with("random content test 123 asd lol this is kind of weird");
        $("input", withName("author")).first().fill().with("shabishabi asdasd");
        $("input", withName("postnews")).first().click();
        
        $("a", withName("19asdKuKu&&>&newsLol123123Test")).first().click();
        
        assertTrue(pageSource().contains("random content test 123 asd lol this is kind of weird"));
        assertTrue(pageSource().contains("shabishabi asdasd"));
    }
    
    
    @Test
    public void AddAndRemoveNews(){
        goTo("http://localhost:" + port);
        
        assertTrue(!pageSource().contains("19asd8989"));
        assertTrue(pageSource().contains("Latest"));
        assertTrue(pageSource().contains("Home"));
        assertTrue(!pageSource().contains("Write"));
        
        $("input", withName("username")).first().fill().with("ad");
        $("input", withName("password")).first().fill().with("min");
        $("input", withName("submit")).first().click();
        
        assertTrue(pageSource().contains("Write"));
        
        $("a", withName("write")).first().click();
        
        assertTrue(pageSource().contains("Write new article"));
        
        $("input", withName("title")).first().fill().with("19asd8989");
        $("textarea", withName("content")).first().fill().with("random content test 123 asd lol this is kind of weird");
        $("input", withName("author")).first().fill().with("shabishabi asdasd");
        $("input", withName("postnews")).first().click();
        
        $("a", withName("19asd8989")).first().click();
        
        assertTrue(pageSource().contains("19asd8989"));
        assertTrue(pageSource().contains("random content test 123 asd lol this is kind of weird"));
        assertTrue(pageSource().contains("shabishabi asdasd"));
        
        $("input", withName("remove")).first().click();
        
        assertTrue(!pageSource().contains("19asd8989"));
    }
    
}
