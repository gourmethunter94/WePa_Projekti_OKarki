/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.NewsObject;
import wad.repository.NewsObjectRepository;

/**
 *
 * @author ollik
 */
@Controller
public class NewsController {
    
    @Autowired
    NewsObjectRepository newsObjectRepository;
    
    @PostConstruct
    public void init() {
        NewsObject ne1 = new NewsObject();
        ne1.setTitle("User and Password");
        ne1.setText("Username: jour | Password: nalist");
        ne1.setDate(System.nanoTime());
        newsObjectRepository.save(ne1);
        
        NewsObject ne2 = new NewsObject();
        ne2.setTitle("Long text test");
        ne2.setText("My father took me in to the city, to see a marching! Son when you grow up will you be the saviour of the broken, the beaten and the damned!? Random words from a song I am currently listening. I need to make this far longer for this to actually work. This might take some time, but when it works, it might be quite rewarding.");
        ne2.setDate(System.nanoTime());
        newsObjectRepository.save(ne2);
    }

    @GetMapping("/news")
    public String home(Model model) {

        List<NewsObject> nol = newsObjectRepository.findAll();

        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((NewsObject) o2).getDate().compareTo(((NewsObject) o1).getDate());
            }
        });

        model.addAttribute("news", nol);

        return "/home.html";
    }

    @GetMapping("/write")
    public String getWrite() {
        return "/write.html";
    }

    @GetMapping("/news/{id}")
    public String getPage(Model model, @PathVariable Long id) {

        NewsObject no = newsObjectRepository.getOne(id);
        Long current = null;

        if (no != null) {

            current = no.getId();
            model.addAttribute("current", current);
            model.addAttribute("title", no.getTitle());
            model.addAttribute("text", no.getText());

        }

        return "/news.html";
    }

    @GetMapping(path = "/news/{id}/image", produces = "image/gif")
    @ResponseBody
    public byte[] getOne(@PathVariable Long id) {
        NewsObject fo = newsObjectRepository.getOne(id);
        return fo.getContent();
    }

    
    
    @PostMapping("/news")
    public String postContent(@RequestParam("file") MultipartFile file, @RequestParam String title, @RequestParam String content) throws IOException {

        NewsObject no = new NewsObject();

        if (file.getContentType().equals("image/gif") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
            no.setContent(file.getBytes());
        }
        
        if (title != null && title.length() > 0) {
            if (content != null && content.length() > 0) {
                no.setTitle(title);
                no.setText(content);

                no.setDate(System.nanoTime());

                Long a = System.nanoTime();

                newsObjectRepository.save(no);

            }
        }

        return "redirect:/news";
    }

}
