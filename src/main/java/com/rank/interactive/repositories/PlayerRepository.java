package com.rank.interactive.repositories;

import com.rank.interactive.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends CrudRepository<Player,Long> {
}
