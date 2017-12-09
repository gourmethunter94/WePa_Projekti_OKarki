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
        ne1.setText("Username: jour | Password: nalist | can write and post news. // Admin user: ad | password: min | can also remove posted news.");
        ne1.setDate(System.nanoTime());
        ne1.setHits((long)0);
        ne1.setHasImage(false);
        newsObjectRepository.save(ne1);
        
        NewsObject ne2 = new NewsObject();
        ne2.setTitle("Long text test");
        ne2.setText("Random text, used to test long posts, it might take a while to write enough to see how well changing row works. I do not have any proper idea on what to write here so I'll just ramble aimlessly for several dozen more words. Did you know that producing random filler text can be quite tedious, if you didn't, well now you do. I am sorry if you read of all this, well not really. It's your own faulth if you did, after all I did point out that this was just a filler text. Or maybe I didn't, you should have noticed that on your own.");
        ne2.setDate(System.nanoTime());
        ne2.setHits((long)0);
        ne1.setHasImage(false);
        newsObjectRepository.save(ne2);
    }

    @GetMapping("/news")
    public String home(Model model) {

        List<NewsObject> nol = newsObjectRepository.findAll();

        List<NewsObject> hot = newsObjectRepository.findAll();
        
        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((NewsObject) o2).getDate().compareTo(((NewsObject) o1).getDate());
            }
        });

        Collections.sort(hot, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((NewsObject) o2).getHits().compareTo(((NewsObject) o1).getHits());
            }
        });
        
        model.addAttribute("news", nol);
        model.addAttribute("hot", hot.subList(0, Math.min(5, hot.size())));
        
        return "/home.html";
    }

    @GetMapping("/write")
    public String getWrite() {
        return "/write.html";
    }

    @GetMapping("/news/{id}")
    public String getPage(Model model, @PathVariable Long id) {

        NewsObject no = newsObjectRepository.getOne(id);
        
        no.setHits(no.getHits()+1);
        
        newsObjectRepository.save(no);
        
        Long current = null;

        if (no != null) {

            if(no.isHasImage()){
                current = no.getId();
            }
            
            model.addAttribute("current", current);
            model.addAttribute("news", no);

        }

        return "/news.html";
    }

    @GetMapping(path = "/news/{id}/image", produces = "image/gif")
    @ResponseBody
    public byte[] getOne(@PathVariable Long id) {
        NewsObject fo = newsObjectRepository.getOne(id);
        return fo.getContent();
    }

    @PostMapping("/news/{id}/remove")
    public String removeNews(@PathVariable Long id){
        NewsObject no = newsObjectRepository.findById(id).get();
        newsObjectRepository.delete(no);
        return "redirect:/news";
    }
    
    @PostMapping("/news")
    public String postContent(@RequestParam("file") MultipartFile file, @RequestParam String title, @RequestParam String content) throws IOException {

        NewsObject no = new NewsObject();
        
        no.setHasImage(false);

        if (file.getContentType().equals("image/gif") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
            no.setContent(file.getBytes());
            no.setHasImage(true);
        }
        
        if (title != null && title.length() > 0) {
            if (content != null && content.length() > 0) {
                no.setTitle(title);
                no.setText(content);

                no.setDate(System.nanoTime());

                no.setHits((long)0);
                
                newsObjectRepository.save(no);

            }
        }

        return "redirect:/news";
    }

}
