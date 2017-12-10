/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
import wad.domain.Hits;
import wad.domain.NewsObject;
import wad.repository.HitsRepository;
import wad.repository.NewsObjectRepository;

/**
 *
 * @author ollik
 */
@Controller
public class NewsController {
    
    @Autowired
    NewsObjectRepository newsObjectRepository;
    
    @Autowired
    HitsRepository hitsRepository;
    
    private List<String> categories;
    
    @PostConstruct
    public void init() {
        NewsObject ne1 = new NewsObject();
        ne1.setTitle("User and Password");
        ne1.setText("Username: jour | Password: nalist | can write and post news. // Admin user: ad | password: min | can also remove posted news.");
        ne1.setSmall(ne1.getText().substring(0,64)+"...");
        ne1.setNodate(Calendar.getInstance().getTime());
        ne1.setHits(new ArrayList<Hits>());
        ne1.setHasImage(false);
        ne1.setCategory("Breaking News");
        ne1.setAuthor("System Management");
        newsObjectRepository.save(ne1);
        
        NewsObject ne2 = new NewsObject();
        ne2.setTitle("Long text test");
        ne2.setText("Random text, used to test long posts, it might take a while to write enough to see how well changing row works. I do not have any proper idea on what to write here so I'll just ramble aimlessly for several dozen more words. Did you know that producing random filler text can be quite tedious, if you didn't, well now you do. I am sorry if you read of all this, well not really. It's your own faulth if you did, after all I did point out that this was just a filler text. Or maybe I didn't, you should have noticed that on your own.");
        ne2.setSmall(ne2.getText().substring(0,64)+"...");
        ne2.setNodate(Calendar.getInstance().getTime());
        ne2.setHits(new ArrayList<Hits>());
        ne2.setHasImage(false);
        ne2.setCategory("Breaking News");
        ne2.setAuthor("System Management");
        newsObjectRepository.save(ne2);
        
        categories = new ArrayList<>();
        categories.add("Sports");
        categories.add("Politics");
        categories.add("Enviroment");
        categories.add("Music");
        categories.add("Technology");
        categories.add("Free Time");
        categories.add("Breaking News");
        
    }

    @GetMapping("/news")
    public String home(Model model) {

        List<NewsObject> nol = newsObjectRepository.findAll();
        
        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((NewsObject) o2).getNodate().compareTo(((NewsObject) o1).getNodate());
            }
        });
        
        model.addAttribute("title", "Latest");

        model.addAttribute("news", nol.subList(0, Math.min(5, nol.size())));
        model.addAttribute("categories", categories);
        
        
        return "/home.html";
    }

    @GetMapping("/news/hottest")
    public String hottest(Model model) {

        List<NewsObject> nol = newsObjectRepository.findAll();

        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                
                Date days = cal.getTime();
                
                List <Date> o1dates = hitsRepository.findAllByNewsObject((NewsObject)o1).stream().map(k -> k.getDate()).collect(Collectors.toList()).stream().filter(d -> d.compareTo(days) >= 0).collect(Collectors.toList());
                List <Date> o2dates = hitsRepository.findAllByNewsObject((NewsObject)o2).stream().map(k -> k.getDate()).collect(Collectors.toList()).stream().filter(d -> d.compareTo(days) >= 0).collect(Collectors.toList());
                
                
                return ((Integer)o2dates.size()).compareTo(o1dates.size());
            }
        });
        
        model.addAttribute("title", "Hottest");
        model.addAttribute("news", nol.subList(0, Math.min(15, nol.size())));
        model.addAttribute("categories", categories);
        
        
        return "/home.html";
    }

    @GetMapping("/news/all")
    public String allNews(Model model) {

        List<NewsObject> nol = newsObjectRepository.findAll();
        
        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((NewsObject) o2).getNodate().compareTo(((NewsObject) o1).getNodate());
            }
        });
        
        model.addAttribute("title", "All");
        model.addAttribute("news", nol);
        model.addAttribute("categories", categories);
        
        
        return "/home.html";
    }

    @GetMapping("/news/mostread")
    public String mostRead(Model model) {

        List<NewsObject> nol = newsObjectRepository.findAll();

        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                
                List <Date> o1dates = hitsRepository.findAllByNewsObject((NewsObject)o1).stream().map(k -> k.getDate()).collect(Collectors.toList());
                List <Date> o2dates = hitsRepository.findAllByNewsObject((NewsObject)o2).stream().map(k -> k.getDate()).collect(Collectors.toList());
                
                
                return ((Integer)o2dates.size()).compareTo(o1dates.size());
            }
        });
        
        model.addAttribute("title", "Most Read");
        model.addAttribute("news", nol);
        model.addAttribute("categories", categories);
        
        
        return "/home.html";
    }
    
    @GetMapping("/news/category")
    public String byCategory(@RequestParam String category, Model model) {

        List<NewsObject> nol = newsObjectRepository.findAllByCategory(category);

        Collections.sort(nol, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((NewsObject) o2).getNodate().compareTo(((NewsObject) o1).getNodate());
            }
        });
        
        model.addAttribute("title", "Category: " + category);
        model.addAttribute("news", nol);
        model.addAttribute("categories", categories);
        
        
        return "/home.html";
    }
    
    @GetMapping("/write")
    public String getWrite(Model model) {
        model.addAttribute("categories", categories);
        return "/write.html";
    }

    @GetMapping("/news/{id}")
    public String getPage(Model model, @PathVariable Long id) {

        NewsObject no = newsObjectRepository.getOne(id);
        
        Date date = Calendar.getInstance().getTime();
        
        Hits nh = new Hits();
        nh.setNewsObject(no);
        nh.setDate(date);
        
        no.getHits().add(nh);
        
        hitsRepository.save(nh);
        newsObjectRepository.save(no);
        
        Long current = null;

        if (no != null) {

            if(no.isHasImage()){
                current = no.getId();
            }
            
            int hits = 0;
                
            hits = hitsRepository.findAllByNewsObject(no).size();
            
            model.addAttribute("hits", hits);
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
    public String postContent(@RequestParam("file") MultipartFile file, @RequestParam String title, @RequestParam String content, @RequestParam String category, @RequestParam String author) throws IOException {

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
                no.setCategory(category);
                no.setAuthor(author);
                
                if(content.length() <= 64){
                    no.setSmall(content);
                } else {
                    no.setSmall(content.substring(0, 64)+"...");
                }
                
                no.setNodate(Calendar.getInstance().getTime());

                no.setHits(new ArrayList<>());
                
                newsObjectRepository.save(no);

            }
        }

        return "redirect:/news";
    }

}
