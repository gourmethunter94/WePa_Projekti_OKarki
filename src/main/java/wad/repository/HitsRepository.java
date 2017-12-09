/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.repository;

import java.util.List;
import wad.domain.NewsObject;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Hits;

/**
 *
 * @author ollik
 */
public interface HitsRepository extends JpaRepository<Hits, Long>{
    List<Hits> findAllByNewsObject(NewsObject newsObject);
}
