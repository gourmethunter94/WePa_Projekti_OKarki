/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author ollik
 */

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewsObject extends AbstractPersistable<Long> {
    
    @Lob
    private byte[] content;
    
    @Column(columnDefinition = "TEXT")
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String text;
    
    private Date nodate;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<Hits> hits;
    
    private boolean hasImage;
    
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String author;
    
    private String small;
    
}
