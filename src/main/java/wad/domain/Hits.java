/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Hits extends AbstractPersistable<Long> {
    
    private Date date;
    
    @ManyToOne
    @JoinColumn
    private NewsObject newsObject;
    
}
