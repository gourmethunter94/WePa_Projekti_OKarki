/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import java.io.IOException;
import java.util.List;
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
    
    @GetMapping("/news")
    public String home(Model model) {
        
        List<NewsObject> nol = newsObjectRepository.findAll();
        
        model.addAttribute("news", nol);
        
        return "/home";
    }
    
    @GetMapping("/news/{id}")
    public String getPage(Model model, @PathVariable Long id) {
        
        NewsObject no = newsObjectRepository.getOne(id);
        Long current = null;
        
        if(no != null){
            
            current = no.getId();
            model.addAttribute("current", current);
            model.addAttribute("title", no.getTitle());
            model.addAttribute("text", no.getText());
        
        }
        
        return "/news";
    }
    
    
    @GetMapping(path = "/news/{id}/image", produces = "image/gif")
    @ResponseBody
    public byte[] getOne(@PathVariable Long id) {
        NewsObject fo = newsObjectRepository.getOne(id);
        return fo.getContent();
    }
    
    @PostMapping("/news")
    public String postContent(@RequestParam("file") MultipartFile file, @RequestParam String title, @RequestParam String content) throws IOException {
        
        if(file.getContentType().equals("image/gif")){
            if(title != null){
                if(content != null){
            
                    NewsObject no = new NewsObject();
                    
                    no.setContent(file.getBytes());
                    no.setTitle(title);
                    no.setText(content);
                    
                    newsObjectRepository.save(no);
                    
                }
            }
        }
        
        return "redirect:/news";
    }
}
